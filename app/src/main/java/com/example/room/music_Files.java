package com.example.room;

/**
 * @see music_Files
 *
 *      - Class music_Files is a simple class for assigning and accessing the details inside each music file
 *      like path, title and album the song is from.
 *
 * @author Amirali Famili
 */
public class music_Files {
    private String path;
    private String title;
    private String artist;
    private String album;
    private String duration;
    private String id;

    /**
     * @see music_Files
     *
     *      - music_Files Constructor is for assigning the values for setter and getter methods.
     *
     * @param path : path of the music file
     * @param title : title of the song
     * @param artist : name of the artist
     * @param album : album name
     * @param duration : duration of the song
     * @param id : default id given when extracting from device
     */
    public music_Files(String path, String title, String artist, String album, String duration, String id) {
        this.path = path;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.duration = duration;
        this.id = id;
    }

    /**
     * @see music_Files
     *
     *      - music_Files default Constructor for ease of access.
     *
     */
    public music_Files(){

    }


    /**
     *      - getPath method a getter method for obtaining the path of the file.
     *
     * @return path of the current music file instance
     */
    public String getPath() {
        return path;
    }

    /**
     *      - setPath method a setter method for assigning the path of the file.
     *
     * @param path : path of the music file
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     *      - getTitle method a getter method for obtaining the title of the song.
     *
     * @return name of the current music file instance
     */
    public String getTitle() {
        return title;
    }

    /**
     *      - setTitle method a setter method for assigning the title of the file.
     *
     * @param title : name of the music file
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     *      - getArtist method a getter method for obtaining the artist name for the song.
     *
     * @return name of the artist for the current music file instance
     */
    public String getArtist() {
        return artist;
    }

    /**
     *      - setArtist method a setter method for assigning the artist name for the file.
     *
     * @param artist : name of the artist for the music file
     */
    public void setArtist(String artist) {
        this.artist = artist;
    }

    /**
     *      - getAlbum method a getter method for obtaining the name of the album the song is from.
     *
     * @return name of the album for current music file instance
     */
    public String getAlbum() {
        return album;
    }

    /**
     *      - setAlbum method a setter method for assigning the album name for the song.
     *
     * @param album : name of the album for the music file
     */
    public void setAlbum(String album) {
        this.album = album;
    }

    /**
     *      - getDuration method a getter method for obtaining the duration of the music.
     *
     * @return total length of the current music file instance
     */
    public String getDuration() {
        return duration;
    }

    /**
     *      - setDuration method a setter method for assigning the duration of the song.
     *
     * @param duration : total duration of the music file
     */
    public void setDuration(String duration) {
        this.duration = duration;
    }

    /**
     *      - getId method a getter method for obtaining the id of the music.
     *
     * @return id of the current music file instance
     */
    public String getId() {
        return id;
    }

    /**
     *      - setId method a setter method for assigning the id of the song.
     *
     * @param id :id of the music file
     */
    public void setId(String id) {
        this.id = id;
    }
}
