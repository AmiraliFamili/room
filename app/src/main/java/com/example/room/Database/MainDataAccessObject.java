package com.example.room.Database;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;

import com.example.room.Models.Notes;

import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

/**
 * @see MainDataAccessObject
 *
 *      - Class MainDataAccessObject is responsible to act upon the queries made by the other classes
 *      managing the notes, it acts as a interface by just defining the methods and uses Annotations to guid the methods
 *      off what they are supposed to do.
 *
 * @author Amirali Famili
 */
@Dao
public interface MainDataAccessObject {

    /**
     *      - inserts a note inside the database (a row)
     *
     * @param notes : the note object to be added to the database
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert (Notes notes);

    /**
     *      - getAll method retrieves all the notes in a descending order
     */
    @Query("SELECT * From notes ORDER BY id DESC")
    List<Notes> getAll();

    /**
     *      - update method simply updates all the attributes of the notes object inside
     *      the database such as their id, title and note object it self.
     *
     * @param id : id of the note object
     * @param title : title of the note object
     * @param notes : note object
     */
    @Query("UPDATE notes SET title = :title, notes= :notes WHERE ID IS :id")
    void update(int id, String title, String notes);

    /**
     *      - delete method deletes a row from the database containing the note object given as the
     *      argument.
     *
     * @param notes : note object
     */
    @Delete
    void delete(Notes notes);

    /**
     *      - pin method updates the pin status of the note object with the id given as argument.
     *
     * @param id : id of the note object
     * @param pin : pin status of the note object (true for pinned and false for not pinned)
     */
    @Query("UPDATE notes SET pinned = :pin WHERE ID = :id")
    void pin(int id, boolean pin);
}
