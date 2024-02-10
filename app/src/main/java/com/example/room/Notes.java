package com.example.room;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.room.Room;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.room.Adapters.NotesListAdapter;
import com.example.room.Database.RoomDB;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class Notes extends AppCompatActivity {

    RecyclerView recyclerView;
    NotesListAdapter notesListAdapter;
    List<com.example.room.Models.Notes> notes = new ArrayList<>();
    RoomDB database;
    FloatingActionButton addNewNote;
    SearchView searchNotes;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        recyclerView = findViewById(R.id.Recyclerview_main);
        addNewNote = findViewById(R.id.new_Note);
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

        }
    };
}