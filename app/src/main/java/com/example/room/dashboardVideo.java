package com.example.room;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

public class dashboardVideo extends AppCompatActivity {

    VideoView video;
    MediaController media;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_video);

        video = findViewById(R.id.videoViewBackground);
        media = new MediaController(this);
        String startingVideoPath = "android.resource://" + getPackageName() + "/" + R.raw.music;
        Uri startingVideoUri = Uri.parse(startingVideoPath);
        video.setVideoURI(startingVideoUri);
        media.setAnchorView(video);
        video.start();
        video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true); // Set looping true to loop the video
            }
        });
        // rest of the dashboard activity






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