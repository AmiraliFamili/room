package com.example.room;


/**
 * @see music_ActionPlaying
 *
 *      - Class music_ActionPlaying is an interface and an helper class for main activity that is
 *      responsible for playing songs, this class enables the methods below to be shared between classes.
 * @author Amirali Famili
 */
public interface music_ActionPlaying {

    void playAndPauseClicked();
    void nextSongClicked();
    void previousSongClicked();
}
