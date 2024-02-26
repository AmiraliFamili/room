package com.example.room;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

public class dashboardVideo extends AppCompatActivity {

    CardView homeDash, aboutDash, shareDash;
    VideoView video;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_video);

        setListeners();

        video = findViewById(R.id.videoViewBackground);

        // Set video path
        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.dashvid;
        Uri videoUri = Uri.parse(videoPath);
        video.setVideoURI(videoUri);

        // Start video
        video.start();

        // Set looping true to loop the video
        video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
    }

    private void setListeners() {
        homeDash = findViewById(R.id.homeDash);
        aboutDash = findViewById(R.id.aboutDash);
        shareDash = findViewById(R.id.shareDash);

        homeDash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(dashboardVideo.this, MainActivity.class);
                startActivity(intent);
            }
        });

        aboutDash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(dashboardVideo.this, aboutUs.class);
                startActivity(intent);
            }
        });
        shareDash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(dashboardVideo.this ,shareUs.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        video.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        video.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        video.stopPlayback();
    }
}
