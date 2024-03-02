package com.example.room;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.room.Adapters.NotesListAdapter;
import com.example.room.Database.RoomDB;
import com.example.room.Gallery.Gallery;
import com.example.room.pass.passwordGN;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;


/**
 * @see Notes
 *
 *      - Notes class is responsible for main operations and interactions of user with their notes, is linked with 4 different packages
 *      and with the help of the packages can take user's notes and store them in an database called Room which is imported to the project from androidx.
 *
 * @package Database, with classes : RoomDB, MainDataAccessObject
 * @package Models, with class : Notes (different file from this one)
 * @package Adapters, with classes : NotesListAdapter, NotesViewHolder
 * @package room , which is the main package that the project is implemented in
 *
 * @Extra This class uses the packages above to communicate with the Room database and operate different operations on notes and their containers, like changing their color to a random value.
 *
 * @author Amirali Famili
 */
public class Notes extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener, NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recyclerView;
    private NotesListAdapter notesListAdapter;
    private List<com.example.room.Models.Notes> notes = new ArrayList<>();
    private RoomDB database;
    private FloatingActionButton addNewNote;
    private SearchView searchNotes;
    private com.example.room.Models.Notes pinned;

    private DrawerLayout drawerLayout;
    @Override // this class is called
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        recyclerView = findViewById(R.id.Recyclerview_main);
        addNewNote = findViewById(R.id.new_Note);
        searchNotes = findViewById(R.id.search_notes);
        database = RoomDB.getInstance(this);
        notes = database.mainDAO().getAll();


        // set the Nav View
        NavigationView navigationView = findViewById(R.id.notes_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        updateRecycler(notes);

        addNewNote.setOnClickListener(new View.OnClickListener() {
            @Override // when a new note is required by user
            public void onClick(View v) {
                Intent intent = new Intent(Notes.this, NotesTaker.class);
                startActivityForResult(intent, 200);
            }
        });

        searchNotes.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override // Unimplemented method
            public boolean onQueryTextSubmit(String query) {
                return false;
            }


            /**
             *      - onQueryTextChange method is called when user is trying to change the search bar.
             *
             * @param newText : the new text entered at the search bar
             */
            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });


        // Set Nav Menu and it's button
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.notes_main_page);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

    }



    /**
     *      - filter method is called everytime user enters a character in the search bar within the Notes Activity,
     *      and finds the relevant search result in both note's title and body.
     */
    private void filter(String newText){
        List<com.example.room.Models.Notes> filtered = new ArrayList<>();
        for (com.example.room.Models.Notes note : notes) {
            if (note.getTitle().toLowerCase().contains(newText.toLowerCase()) || note.getNotes().toLowerCase().contains(newText.toLowerCase())){
                filtered.add(note);
            }
        }

        notesListAdapter.filter(filtered);
    }


    /**
     *
     *      - this method is responsible for updating and inserting notes to the database.
     *
     */
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

    /**
     *      - update the recycler view
     */
    private void updateRecycler(List<com.example.room.Models.Notes> notes) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        notesListAdapter = new NotesListAdapter(Notes.this, notes, notesListener);
        recyclerView.setAdapter(notesListAdapter);
    }

    /**
     *      - notesListener is an anonymous class responsible for pining a note on hold and letting the note taker class know
     *      that their dealing with an old note.
     */
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

    /**
     *      - showPopUpDialog method shows a popup menu for delete and pin operations
     *
     * @param cardView : view for the menu
     */
    private void showPopUpDialog(CardView cardView) {
        PopupMenu popup = new PopupMenu(this, cardView);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.pop_up_menu);
        popup.show();
    }


    /**
     *      - onMenuItemClick method is called when user clicks on an item on pop up menu
     *
     * @param item : a menu item
     */
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

    /**
     *      - onCreateOptionsMenu method is called when the activity is started and creates the
     *      menu view.
     *
     * @param menu : navigation menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) { // create menu
        getMenuInflater().inflate(R.menu.navigation_menu, menu);
        return true;
    }

    /**
     *      - onOptionsItemSelected method is called when an item is selected from the menu.
     *
     * @param item : menu item
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    /**
     *      - This method is responsible for redirecting user to the correct activity when they click on the items listed on the navigation menu.
     *
     * @param item : id of the item user clicked on it
     *
     * @return true if item exists, false if it doesn't
     */
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation item clicks here
        int id = item.getItemId();
        if (id == R.id.homeInNav){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.noteInNav){
            Intent intent = new Intent(this, Notes.class);
            startActivity(intent);
            return true;
        }else if (id == R.id.musicInNav){
            Intent intent = new Intent(this, music_main.class);
            startActivity(intent);
            return true;
        }else if (id == R.id.passwordInNav){
            Intent intent = new Intent(this, passwordGN.class);
            startActivity(intent);
            return true;
        }else if (id == R.id.galleryInNav){
            Intent intent = new Intent(this, Gallery.class);
            startActivity(intent);
            return true;
        }else if (id == R.id.calculatorInNav){
            Intent intent = new Intent(this, calculator.class);
            startActivity(intent);
            return true;
        }else if (id == R.id.aboutInNav){
            Intent intent = new Intent(this, aboutUs.class);
            startActivity(intent);
            return true;
        }else if (id == R.id.shareInNav){
            Intent intent = new Intent(this, shareUs.class);
            startActivity(intent);
            return true;
        }else if (id == R.id.rateInNav){
            Intent intent = new Intent(this, rateUs.class);
            startActivity(intent);
            return true;
        } else {
            return false;
        }
    }

}
