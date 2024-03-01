package com.example.room;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;
import android.widget.LinearLayout;

import com.example.room.Gallery.Gallery;
import com.example.room.pass.passwordGN;

/**
 * @see MainActivity
 *
 *      - MainActivity Class is the first activity the user will see when the application is up and running,
 *      This class holder several clickable cards and a video, and holdes all the main services this app delivers and can be considered a holder for these services.
 *
 * @author Amirali Famili
 */
public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_HOME = 1;
    MediaController media;
    VideoView video;
    VideoView dashboardVideo;

    LinearLayout dashboardLayout;

    boolean startingVideoPlayed = false;

    boolean dashboardVideoPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if(!startingVideoPlayed){ // play the initial video
            video = findViewById(R.id.myVideo);
            media = new MediaController(this);
            String startingVideoPath = "android.resource://" + getPackageName() + "/" + R.raw.music;
            Uri startingVideoUri = Uri.parse(startingVideoPath);
            video.setVideoURI(startingVideoUri);
            media.setAnchorView(video);
            video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setLooping(false);
                    video.start();
                }
            });
            video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    video.setVisibility(View.GONE);
                    showDashboard();
                    startingVideoPlayed = true;
                }
            });
        }

        // play the dashboard video after the initial video
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
                if (!dashboardVideoPlaying) {
                    dashboardVideo.start(); // Start the video playback if it's not already playing
                    dashboardVideoPlaying = true;
                }
            }
        });

        dashboardVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, dashboardVideo.class);
                startActivity(intent);
            }
        });

        // User Clicks on the Cards ------------------------------------


        // -------------    Notes Activity    ------------- //

        LinearLayout category1CardView = findViewById(R.id.Category1);
        category1CardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Notes.class);
                startActivity(intent);
            }
        });


        // -------------    Password Generator Activity    ------------- //

        LinearLayout category2CardView = findViewById(R.id.Category2);
        category2CardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, passwordGN.class);
                startActivity(intent);
            }
        });


        // -------------    Music Activity    ------------- //

        LinearLayout category3CardView = findViewById(R.id.Category3);
        category3CardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, music_main.class);
                startActivity(intent);
            }
        });


        // -------------    Gallery Activity    ------------- //

        LinearLayout category4CardView = findViewById(R.id.Category4);
        category4CardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Gallery.class);
                startActivity(intent);
            }
        });


        // -------------    Calculator Activity    ------------- //

        LinearLayout category5CardView = findViewById(R.id.Category5);
        category5CardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, calculator.class);
                startActivity(intent);
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_HOME && resultCode == RESULT_OK) {
            // User returned from the home intent
            startingVideoPlayed = true;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (video.isPlaying()) {
            video.pause();
        }
        if (dashboardVideo.isPlaying()) {
            dashboardVideo.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!dashboardVideo.isPlaying() && dashboardVideoPlaying) {
            dashboardVideo.start();
        }
    }

    private void showDashboard() { // setting all the elements in main activity to visible after the video is played
        dashboardLayout.setVisibility(View.VISIBLE);
        dashboardVideo.setVisibility(View.VISIBLE);
    }
}
