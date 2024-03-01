package com.example.room;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.room.Models.Notes;

import java.util.Date;


/**
 * @see NotesTaker
 *
 *      - NotesTaker class is responsible for taking the raw text from user notes and correct it's format, validate it and add
 *      date and time to it, then it deals with updating or inserting the new note by sending the result to the main Notes activity.
 *
 * @author Amirali Famili
 */
public class NotesTaker extends AppCompatActivity {

    EditText textEditor_title, textEditor_notes;
    ImageView note_image_view;
    Notes notes;
    boolean oldNote = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_taker);

        note_image_view = findViewById(R.id.note_image_view);
        textEditor_title = findViewById(R.id.textEditor_title);
        textEditor_notes = findViewById(R.id.textEditor_notes);

        notes = new Notes();

        // check if the user is updating their note
        try {
            notes = (Notes) getIntent().getSerializableExtra("old_note");
            textEditor_title.setText(notes.getTitle());
            textEditor_notes.setText(notes.getNotes());
            oldNote = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        note_image_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = textEditor_title.getText().toString();
                String description = textEditor_notes.getText().toString();

                if (description.isEmpty()){
                    Toast.makeText(NotesTaker.this, "Ur Note Is Empty!", Toast.LENGTH_SHORT).show();
                    return;
                }

                SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm a");
                Date date = new Date();

                if (!oldNote) {
                    notes = new Notes();
                }

                notes.setTitle(title);
                notes.setNotes(description);
                notes.setDate(dateFormat.format(date));

                Intent intent = new Intent();
                intent.putExtra("note", notes);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }
}