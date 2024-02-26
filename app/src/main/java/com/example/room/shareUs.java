package com.example.room;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import com.google.android.material.navigation.NavigationView;

public class shareUs extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    VideoView video;
    MediaController media;
    boolean isVideoPaused = false;

    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_us);


        NavigationView navigationView = findViewById(R.id.notes_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        drawerLayout = findViewById(R.id.notes_main_page);
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        video = findViewById(R.id.aboutUsVid);
        media = new MediaController(this);
        String startingVideoPath = "android.resource://" + getPackageName() + "/" + R.raw.sharevideo;
        Uri startingVideoUri = Uri.parse(startingVideoPath);
        video.setVideoURI(startingVideoUri);
        media.setAnchorView(video);
        video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
                video.start();
            }
        });

        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (video.isPlaying()) {
                    video.pause();
                    isVideoPaused = true;
                }

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String body = "Check Out This Cool App";
                String sub = "http://com.example.room";
                intent.putExtra(Intent.EXTRA_TEXT, body);
                intent.putExtra(Intent.EXTRA_TEXT, sub);
                startActivity(Intent.createChooser(intent, "Share Via"));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isVideoPaused) {
            video.start();
            isVideoPaused = false;
        }
    }


    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation item clicks here
        int id = item.getItemId();
        if (id == R.id.homeInNav){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.noteInNav){
            Intent intent = new Intent(this, Notes.class);
            startActivity(intent);
            return true;
        }else if (id == R.id.musicInNav){
            Intent intent = new Intent(this, SongPlayer.class);
            startActivity(intent);
            return true;
        }else if (id == R.id.passwordInNav){
            Intent intent = new Intent(this, passwordGN.class);
            startActivity(intent);
            return true;
        }else if (id == R.id.galleryInNav){
            Intent intent = new Intent(this, Gallery.class);
            startActivity(intent);
            return true;
        }else if (id == R.id.calculatorInNav){
            Intent intent = new Intent(this, calculator.class);
            startActivity(intent);
            return true;
        }else if (id == R.id.aboutInNav){
            Intent intent = new Intent(this, aboutUs.class);
            startActivity(intent);
            return true;
        }else if (id == R.id.shareInNav){
            Intent intent = new Intent(this, shareUs.class);
            startActivity(intent);
            return true;
        }else if (id == R.id.rateInNav){
            Intent intent = new Intent(this, rateUs.class);
            startActivity(intent);
            return true;
        } else {
            return false;
        }
    }
}