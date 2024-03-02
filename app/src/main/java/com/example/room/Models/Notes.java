package com.example.room.Models;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.room.pass.helper;

import java.io.Serializable;


/**
 * @see Notes
 *
 *      - Class Notes is an access point for all classes related to Notes, it holds all the setter and getter methods for details
 *      of a note, as well as their value, it also acts as a table for the Room database.
 *
 * @author Amirali Famili
 */
@Entity(tableName = "notes")
public class Notes implements Serializable {

    @PrimaryKey(autoGenerate = true)
    int ID = 0;

    @ColumnInfo(name = "title")
    String title = "";

    @ColumnInfo(name = "notes")
    String notes = "";

    @ColumnInfo(name = "date")
    String date = "";

    @ColumnInfo(name = "pinned")
    boolean pinned = false;

    /**
     *      - getID is a getter method user for retrieving the Unique ID of the created note.
     *
     * @return id of the note object
     */
    public int getID() {
        return ID;
    }

    /**
     *      - setID is a setter method user for assigning the id of a note.
     *
     * @param ID : Unique id of the note Object
     */
    public void setID(int ID) {
        this.ID = ID;
    }

    /**
     *      - getTitle is a getter method user for retrieving the title of the note object.
     *
     * @return title of the note object
     */
    public String getTitle() {
        return title;
    }

    /**
     *      - setTitle is a setter method user for assigning the title of the note object.
     *
     * @param title : title of the note Object
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     *      - getNotes is a getter method user for retrieving the note object.
     *
     * @return the note object
     */
    public String getNotes() {
        return notes;
    }

    /**
     *      - setNotes is a setter method user for assigning the note object.
     *
     * @param notes : the note Object
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     *      - getDate is a getter method user for retrieving the date that the note object is created/edited.
     *
     * @return date that the note object is created/edited
     */
    public String getDate() {
        return date;
    }

    /**
     *      - setDate is a setter method user for assigning the date that the note object is created/edited.
     *
     * @param date : date that the note object is created/edited
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     *      - isPinned method determines if the note is pinned or not
     *
     * @return true if the note isPinned and false if it is not
     */
    public boolean isPinned() {
        return pinned;
    }

    /**
     *      - setPinned method is a setter method, it sets the pinned status of the notes object to true or false.
     *
     * @param pinned : a boolean determining the pinned status of a note object.
     */
    public void setPinned(boolean pinned) {
        this.pinned = pinned;
    }
}
