package com.example.room;

import androidx.cardview.widget.CardView;

import com.example.room.Models.Notes;

public interface NotesListener {

    void onClick(Notes notes);
    void onHold(Notes notes, CardView cardView);

}
