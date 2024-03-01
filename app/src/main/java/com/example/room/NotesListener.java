package com.example.room;

import androidx.cardview.widget.CardView;

import com.example.room.Models.Notes;


/**
 * @see NotesListener
 *
 *      - NotesListener is an interface which works correspondingly with Notes class
 *
 * @author Amirali Famili
 */
public interface NotesListener {

    void onClick(Notes notes);
    void onHold(Notes notes, CardView cardView);

}
