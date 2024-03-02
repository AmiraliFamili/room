package com.example.room.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.room.Models.Notes;

/**
 * @see RoomDB
 *
 *      - Class RoomDB acts as a database for the Notes activity with the help of an interface called
 *      MainDataAccessObject, it initialises the database, it's name and instance.
 *
 * @author Amirali Famili
 */
@Database(entities = Notes.class, version = 1, exportSchema = false)
public abstract class RoomDB extends RoomDatabase {
    private static RoomDB database;
    private static String DATABASE_NAME = "Notes";

    public synchronized static RoomDB getInstance(Context context){
        if (database == null) {
            database = Room.databaseBuilder(context.getApplicationContext(), RoomDB.class, DATABASE_NAME).allowMainThreadQueries().fallbackToDestructiveMigration().build();
        }
        return database;
    }
    public abstract MainDataAccessObject mainDAO();
}
