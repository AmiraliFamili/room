package com.example.room.Database;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;

import com.example.room.Models.Notes;

import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MainDataAccessObject {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert (Notes notes);

    @Query("SELECT * From notes ORDER BY id DESC")
    List<Notes> getAll();

    @Query("UPDATE notes SET title = :title, notes= :notes WHERE ID IS :id")
    void update(int id, String title, String notes);

    @Delete
    void delete(Notes notes);

    @Query("UPDATE notes SET pinned = :pin WHERE ID = :id")
    void pin(int id, boolean pin);
}
