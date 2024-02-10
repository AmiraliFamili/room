package com.example.room;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.room.Room;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.room.Adapters.NotesListAdapter;
import com.example.room.Database.RoomDB;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class Notes extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    RecyclerView recyclerView;
    NotesListAdapter notesListAdapter;
    List<com.example.room.Models.Notes> notes = new ArrayList<>();
    RoomDB database;
    FloatingActionButton addNewNote;
    SearchView searchNotes;
    com.example.room.Models.Notes pinned;

    private DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        recyclerView = findViewById(R.id.Recyclerview_main);
        addNewNote = findViewById(R.id.new_Note);
        searchNotes = findViewById(R.id.search_notes);
        database = RoomDB.getInstance(this);
        notes = database.mainDAO().getAll();

        updateRecycler(notes);

        addNewNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Notes.this, NotesTaker.class);
                startActivityForResult(intent, 200);
            }
        });

        searchNotes.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);

        drawerLayout = findViewById(R.id.notes_main_page);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

    }



    private void filter(String newText){
        List<com.example.room.Models.Notes> filtered = new ArrayList<>();
        for (com.example.room.Models.Notes note : notes) {
            if (note.getTitle().toLowerCase().contains(newText.toLowerCase()) || note.getNotes().toLowerCase().contains(newText.toLowerCase())){
                filtered.add(note);
            }
        }

        notesListAdapter.filter(filtered);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 200){
            if (resultCode == Activity.RESULT_OK){
                com.example.room.Models.Notes newNote = (com.example.room.Models.Notes) data.getSerializableExtra("note");
                database.mainDAO().insert(newNote);
                notes.clear();
                notes.addAll(database.mainDAO().getAll());
                if(notesListAdapter != null) {
                    notesListAdapter.notifyDataSetChanged();
                }
            }
        }
        if (requestCode == 500){
            if (resultCode == Activity.RESULT_OK){
                com.example.room.Models.Notes newNote = (com.example.room.Models.Notes) data.getSerializableExtra("note");
                database.mainDAO().update(newNote.getID(), newNote.getTitle(), newNote.getNotes());
                notes.clear();
                notes.addAll(database.mainDAO().getAll());
                if(notesListAdapter != null) {
                    notesListAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    private void updateRecycler(List<com.example.room.Models.Notes> notes) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        notesListAdapter = new NotesListAdapter(Notes.this, notes, notesListener);
        recyclerView.setAdapter(notesListAdapter);
    }

    private final NotesListener notesListener = new NotesListener() {
        @Override
        public void onClick(com.example.room.Models.Notes notes) {
            Intent intent = new Intent(Notes.this, NotesTaker.class);
            intent.putExtra("old_note", notes);
            startActivityForResult(intent, 500);
        }

        @Override
        public void onHold(com.example.room.Models.Notes notes, CardView cardView) {
            pinned = new com.example.room.Models.Notes();
            pinned = notes;
            showPopUpDialog(cardView);
        }
    };

    private void showPopUpDialog(CardView cardView) {
        PopupMenu popup = new PopupMenu(this, cardView);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.pop_up_menu);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.pin) {
            if (pinned.isPinned()) {
                database.mainDAO().pin(pinned.getID(), false);
                Toast.makeText(Notes.this, "Unpinned!", Toast.LENGTH_SHORT).show();
            } else {
                database.mainDAO().pin(pinned.getID(), true);
                Toast.makeText(Notes.this, "Pinned!", Toast.LENGTH_SHORT).show();
            }

            notes.clear();
            notes.addAll(database.mainDAO().getAll());
            notesListAdapter.notifyDataSetChanged();
            return true;
        } else if (R.id.delete == item.getItemId()) {
            database.mainDAO().delete(pinned);
            notes.remove(pinned);
            notesListAdapter.notifyDataSetChanged();
            Toast.makeText(Notes.this, "Note Deleted...", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_open_navigation) {
            // Open the navigation drawer here
            DrawerLayout drawer = findViewById(R.id.notes_main_page);
            drawer.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
