package com.example.room;

import static com.example.room.music_main.musicFiles;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.ArrayList;
/**
 * @see music_AlbumDetails
 *
 *      - Class music_AlbumDetails is the main class that sets the album details inside the album fragment.
 *
 * @author Amirali Famili
 */
public class music_AlbumDetails extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ImageView albumCover;
    private String albumName;
    private ArrayList<music_Files> albumSongs = new ArrayList<>();
    private music_AlbumDetailsAdapter musicAlbumDetailsAdapter;

    @Override // the main activity starts
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_details);

        recyclerView = findViewById(R.id.albumRecyclerView);
        albumCover = findViewById(R.id.albumPhoto);
        albumName = getIntent().getStringExtra("albumName");
        albumSongs.clear();
        int j = 0;
        for (int i = 0 ; i < musicFiles.size() ; i++){
            if (albumName.equals(musicFiles.get(i).getAlbum())){
                albumSongs.add(j, musicFiles.get(i));
                j++;
            }
        }
        byte[] image = new byte[0];
        try {
            image = getAlbumart(albumSongs.get(0).getPath());
            if (image != null){
                Glide.with(this).load(image).into(albumCover);
            } else {
                Glide.with(this).load(R.drawable.music).into(albumCover);
            }
        } catch (IOException e) {
            Glide.with(this).load(R.drawable.music).into(albumCover);
        }

    }

    /**
     *      - onResume method is responsible for updating the recycler view when the user is doing operations on the main page but is not leaving the page either :/ .
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (!(albumSongs.size() < 1)){
            musicAlbumDetailsAdapter = new music_AlbumDetailsAdapter(this, albumSongs);
            recyclerView.setAdapter(musicAlbumDetailsAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        }
    }

    /**
     *      - getAlbumart method is responsible for extracting the cover of music files.
     *
     * @param uri : the path for the music file
     *
     *
     * @return art : the cover for the song that has the path "uri"
     *
     */
    private byte[] getAlbumart(String uri) throws IOException {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] art = retriever.getEmbeddedPicture();
        retriever.release();
        return art;
    }
}