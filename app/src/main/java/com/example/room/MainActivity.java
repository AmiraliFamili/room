package com.example.room;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;
import android.widget.LinearLayout;
import android.view.MenuItem;
import android.widget.PopupMenu;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private VideoView videoView;
    Button play ;
    MediaController media;
    VideoView video;
    VideoView dashboardVideo;


    VideoView Category1Video;
    VideoView Category2Video;
    VideoView Category3Video;
    VideoView Category4Video;
    VideoView Category5Video;
    VideoView Category6Video;


    LinearLayout dashboardLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        video = findViewById(R.id.myVideo);
        media = new MediaController(this);
        String startingVideoPath = "android.resource://" + getPackageName() + "/" + R.raw.music;
        Uri startingVideoUri = Uri.parse(startingVideoPath);
        video.setVideoURI(startingVideoUri);
        media.setAnchorView(video);
        video.start();

        video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(false); // Set looping true to loop the video
            }
        });
        video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                video.setVisibility(View.GONE);
                showDashboard();
            }
        });

        dashboardLayout = findViewById(R.id.linearLayout);




        dashboardVideo = findViewById(R.id.DashboardVideo);
        media = new MediaController(this);
        String dashboardVideoPath = "android.resource://" + getPackageName() + "/" + R.raw.dash;
        Uri dashboardVideoUri = Uri.parse(dashboardVideoPath);
        dashboardVideo.setVideoURI(dashboardVideoUri);
        media.setAnchorView(dashboardVideo);
        dashboardVideo.start();

        dashboardVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true); // Set looping true to loop the video
            }
        });

        dashboardVideo.setOnClickListener(view -> showPopupMenu(view));




        // User Clicks on the Cards :

        LinearLayout category1CardView = findViewById(R.id.Category1);
        category1CardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start a new activity when the CardView is clicked
                Intent intent = new Intent(MainActivity.this, Notes.class);
                startActivity(intent);
            }
        });






    }
    private void showDashboard() {
        dashboardLayout.setVisibility(View.VISIBLE);
        dashboardVideo.setVisibility(View.VISIBLE);
    }



    void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.dropdown_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_music) {
                    // Handle music toggle
                    return true;
                } else if (item.getItemId() == R.id.action_dark_mode) {
                    // Handle dark mode toggle
                    return true;
                } else if (item.getItemId() == R.id.nav_about) {
                    // Handle help action
                    return true;
                } else {
                    return false;
                }
            }
        });

        popupMenu.show();
    }


}

