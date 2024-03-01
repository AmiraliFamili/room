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

public class music_AlbumDetails extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageView albumCover;
    String albumName;
    ArrayList<music_Files> albumSongs = new ArrayList<>();
    music_AlbumDetailsAdapter musicAlbumDetailsAdapter;

    @Override
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

    @Override
    protected void onResume() {
        super.onResume();
        if (!(albumSongs.size() < 1)){
            musicAlbumDetailsAdapter = new music_AlbumDetailsAdapter(this, albumSongs);
            recyclerView.setAdapter(musicAlbumDetailsAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        }
    }

    private byte[] getAlbumart(String uri) throws IOException {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] art = retriever.getEmbeddedPicture();
        retriever.release();
        return art;
    }
}