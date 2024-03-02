package com.example.room;

import static com.example.room.music_AlbumDetailsAdapter.albumFiles;
import static com.example.room.music_Adapter.music;
import static com.example.room.music_main.repeatBool;
import static com.example.room.music_main.shuffleBool;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;


/**
 * @see music_PlayingSongs
 *
 *      - Class music_PlayingSongs is the activity called when user clicks on a song to be played, it handls all the
 *      actions made by user on songs, like playing them, skipping them and so on ...
 *
 * @author Amirali Famili
 */
public class music_PlayingSongs extends AppCompatActivity implements music_ActionPlaying, ServiceConnection {


    private TextView SongName, artistName, durationPlayed, durationTotal, nowPlaying;

    private Button playPause;
    private ImageView back_button, cover_art, shuffle_button, previousSong, nextSong, repeatSong;
    private FloatingActionButton playAndPause;
    private SeekBar seekBarSongs;
    private int position = 1;
    public static ArrayList<music_Files> songList = new ArrayList<>();
    public static Uri uri;
    //public static MediaPlayer mediaPlayer;
    private Handler handler = new Handler();
    private Thread playThread, prevThread, nextThread;
    MusicService musicService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullScreen();
        setContentView(R.layout.activity_playing_songs);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }


        initialise();
        getIntentMethods();
        seekBarSongs.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override  // when the seekbar's progress is changed this method is called
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (musicService != null && fromUser){
                    musicService.seekTo(progress * 1000);
                    Log.d(null, "The media player is NOT null inside the onProgress Changed");
                } else {
                    Log.d(null, "The media player is null inside the onProgress Changed");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Unimplemented method
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Unimplemented method
            }
        });
        updateSeekBar(); // update seekbar to show the correct remaining time


        back_button.setOnClickListener(new View.OnClickListener() {
            @Override // simple back button, redirects users to the previous page
            public void onClick(View v) {
                Intent intent = new Intent(music_PlayingSongs.this, music_main.class);
                startActivity(intent);
            }
        });

    }

    /**
     *      - updateSeekBar method is used for updating the seekbar by calculating the remaining time from when
     *      the user started the song and updating the seekbar text values correspondingly.
     */
    private void updateSeekBar() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (musicService != null){
                    int mCurrentPosition = musicService.getCurrentPosition() / 1000;
                    seekBarSongs.setProgress(mCurrentPosition);
                    durationPlayed.setText(formattedTime(mCurrentPosition));
                }
                handler.postDelayed(this, 1000);
            }
        }, 1000);
        shuffle_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shuffleBool){
                    shuffleBool = false;
                    shuffle_button.setImageResource(R.drawable.song_shuffle_off);
                }else {
                    shuffleBool = true;
                    shuffle_button.setImageResource(R.drawable.song_shuffle_on);

                }
            }
        });
        repeatSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (repeatBool){
                    repeatBool = false;
                    repeatSong.setImageResource(R.drawable.song_repeat);
                } else {
                    repeatBool = true;
                    repeatSong.setImageResource(R.drawable.song_repeat_on);
                }
            }
        });
    }

    /**
     *      - fullScreen method is called for making the page to appear as a full screen page.
     */
    private void fullScreen(){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     *      - onResume method for this activity, binding the service and starting the play, next and previous threads.
     */
    @Override
    protected void onResume() {
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, this, BIND_AUTO_CREATE);
        playThread();
        nextThread();
        prevThread();
        super.onResume();
    }

    /**
     *      - onPause method for this activity, un binding the service.
     */
    @Override
    protected void onPause() {
        super.onPause();
        unbindService(this);
    }

    /**
     *      - playThread method listens for clicks on the play and pause button and acts accordingly.
     */
    private void playThread(){
        playThread = new Thread(){
            @Override
            public void run() {
                super.run();
                playAndPause.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playAndPauseClicked();
                    }
                });
            }
        };
        playThread.start();
    }


    /**
     *      - playAndPauseClicked method is called when user clicks on the play and pause button, this method is
     *      updating both the notification's play and pause button and the layout's play and pause button.
     */
    public void playAndPauseClicked(){
        if (musicService.isPlaying()){
            playAndPause.setImageResource(R.drawable.song_play_arrow);
            musicService.showNotification(R.drawable.song_colored_play_arrow);
            musicService.pause();
            seekBarSongs.setMax(musicService.getDuration() / 1000);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (musicService != null) {
                        int mCurrentPosition = musicService.getCurrentPosition() / 1000;
                        seekBarSongs.setProgress(mCurrentPosition);
                        durationPlayed.setText(formattedTime(mCurrentPosition));
                    }
                    handler.postDelayed(this, 1000);
                }
            }, 1000);
        } else {
            playAndPause.setImageResource(R.drawable.song_pause);
            musicService.showNotification(R.drawable.song_colored_pause);
            musicService.start();
            seekBarSongs.setMax(musicService.getDuration() / 1000);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (musicService != null){
                        int mCurrentPosition = musicService.getCurrentPosition() / 1000;
                        seekBarSongs.setProgress(mCurrentPosition);
                        durationPlayed.setText(formattedTime(mCurrentPosition));
                    }
                    // Run this thread after 1 second
                    handler.postDelayed(this, 1000);
                }
            }, 1000); // Start after 1 second
        }
    }

    /**
     *      - nextThread method listens for clicks on the next song button and acts accordingly.
     */
    private  void  nextThread(){
        nextThread = new Thread(){
            @Override
            public void run() {
                super.run();
                nextSong.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nextSongClicked();
                    }
                });
            }
        };
        nextThread.start();
    }

    /**
     *      - nextSongClicked method is called when user clicks on the next song button, this method is
     *      updating both the notification's next song button and the layout's next song button.
     */
    public void nextSongClicked(){
        if (musicService.isPlaying()){
            musicService.stop();
            musicService.release();
            if (shuffleBool && !repeatBool){
                position = getRandom(songList.size() - 1);
            } else if (!shuffleBool && !repeatBool){
                position = ((position + 1) % songList.size());
            }
            uri = Uri.parse(songList.get(position).getPath());
            musicService.createMediaPlayer(position);
            metaData(uri);
            SongName.setText(songList.get(position).getTitle());
            artistName.setText(songList.get(position).getArtist());
            nowPlaying.setText(songList.get(position).getAlbum());
            seekBarSongs.setMax(musicService.getDuration() / 1000);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (musicService != null){
                        int mCurrentPosition = musicService.getCurrentPosition() / 1000;
                        seekBarSongs.setProgress(mCurrentPosition);
                        durationPlayed.setText(formattedTime(mCurrentPosition));
                    }
                    handler.postDelayed(this, 1000);
                }
            }, 1000);
            musicService.onCompleted();
            playAndPause.setBackgroundResource(R.drawable.song_pause);
            musicService.showNotification(R.drawable.song_colored_pause);
            musicService.start();
        } else {
            musicService.stop();
            musicService.release();
            if (shuffleBool && !repeatBool){
                position = getRandom(songList.size() - 1);
            } else if (!shuffleBool && !repeatBool){
                position = ((position + 1) % songList.size());
            }
            uri = Uri.parse(songList.get(position).getPath());
            musicService.createMediaPlayer(position);
            metaData(uri);
            SongName.setText(songList.get(position).getTitle());
            artistName.setText(songList.get(position).getArtist());
            nowPlaying.setText(songList.get(position).getAlbum());
            seekBarSongs.setMax(musicService.getDuration() / 1000);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (musicService != null){
                        int mCurrentPosition = musicService.getCurrentPosition() / 1000;
                        seekBarSongs.setProgress(mCurrentPosition);
                        durationPlayed.setText(formattedTime(mCurrentPosition));
                    }
                    handler.postDelayed(this, 1000);
                }
            }, 1000);
            musicService.onCompleted();
            playAndPause.setBackgroundResource(R.drawable.song_play_arrow);
            musicService.showNotification(R.drawable.song_colored_play_arrow);
        }
    }

    private int getRandom(int i) {
        Random random = new Random();
        return  random.nextInt(i + 1);
    }

    /**
     *      - prevThread method listens for clicks on the previous song button and acts accordingly.
     */
    private void prevThread(){
        prevThread = new Thread(){
            @Override
            public void run() {
                super.run();
                previousSong.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        previousSongClicked();
                    }
                });
            }
        };
        prevThread.start();
    }

    /**
     *      - previousSongClicked method is called when user clicks on the previous song button, this method is
     *      updating both the notification's previous song button and the layout's previous song button.
     */
    public void  previousSongClicked(){
        if (musicService.isPlaying()){
            musicService.stop();
            musicService.release();
            if (shuffleBool && !repeatBool){
                position = getRandom(songList.size() - 1);
            } else if (!shuffleBool && !repeatBool){
                position = ((position - 1) < 0 ? (songList.size() - 1) : (position - 1));
            }
            uri = Uri.parse(songList.get(position).getPath());
            musicService.createMediaPlayer(position);
            metaData(uri);
            SongName.setText(songList.get(position).getTitle());
            artistName.setText(songList.get(position).getArtist());
            nowPlaying.setText(songList.get(position).getAlbum());
            seekBarSongs.setMax(musicService.getDuration() / 1000);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (musicService != null){
                        int mCurrentPosition = musicService.getCurrentPosition() / 1000;
                        seekBarSongs.setProgress(mCurrentPosition);
                        durationPlayed.setText(formattedTime(mCurrentPosition));
                    }
                    handler.postDelayed(this, 1000);
                }
            }, 1000);
            musicService.onCompleted();
            playAndPause.setBackgroundResource(R.drawable.song_pause);
            musicService.showNotification(R.drawable.song_colored_pause);
            musicService.start();
        } else {
            musicService.stop();
            musicService.release();
            if (shuffleBool && !repeatBool){
                position = getRandom(songList.size() - 1);
            } else if (!shuffleBool && !repeatBool){
                position = ((position - 1) < 0 ? (songList.size() - 1) : (position - 1));
            }
            uri = Uri.parse(songList.get(position).getPath());
            musicService.createMediaPlayer(position);
            metaData(uri);
            SongName.setText(songList.get(position).getTitle());
            artistName.setText(songList.get(position).getArtist());
            nowPlaying.setText(songList.get(position).getAlbum());
            seekBarSongs.setMax(musicService.getDuration() / 1000);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (musicService != null){
                        int mCurrentPosition = musicService.getCurrentPosition() / 1000;
                        seekBarSongs.setProgress(mCurrentPosition);
                        durationPlayed.setText(formattedTime(mCurrentPosition));
                    }
                    handler.postDelayed(this, 1000);
                }
            }, 1000);
            musicService.onCompleted();
            playAndPause.setBackgroundResource(R.drawable.song_play_arrow);
            musicService.showNotification(R.drawable.song_colored_play_arrow);
        }
    }


    /**
     *      - formattedTime method is used for reformatting the time and returning the correct format
     *      by extracting the minutes and seconds of the total duration and setting the string for seekbar.
     *
     * @param mCurrentPosition : total duration of a song
     * @return standard total duration with minutes and seconds
     */
    private String formattedTime(int mCurrentPosition){
        String totalOut = "";
        String totalNew = "";
        String seconds = String.valueOf(mCurrentPosition % 60);
        String minutes = String.valueOf(mCurrentPosition / 60);
        totalOut = minutes + ":" + seconds;
        totalNew = minutes + ":" + "0" + seconds;

        if (seconds.length() == 1){
            return  totalNew;
        } else {
            return totalOut;
        }
    }

    /**
     *      - getIntentMethods method is used for receiving the extra text from the intents sent for this activity and
     *      act accordingly in response to them.
     */
    private void getIntentMethods(){
        position = getIntent().getIntExtra("position", -1);
        String sender = getIntent().getStringExtra("sender");
        if (sender != null && sender.equals("albumDetails")){
            songList = albumFiles;
        } else {
            songList = music;
        }

        if (songList != null && position >= 0 && position < songList.size()){
            playAndPause.setImageResource(R.drawable.song_pause);
            String filePath = songList.get(position).getPath();
            File file = new File(filePath);
            if(file.exists()) {
                uri = Uri.parse(filePath);
            } else {
                uri = null;
            }
            Intent intent = new Intent(this, MusicService.class);
            intent.putExtra("servicePosition", position);
            startService(intent);
        }

    }

    /**
     *      - initialise method initialises all the elements inside this activity
     */
    private void initialise(){
        SongName = findViewById(R.id.SongName);
        nowPlaying = findViewById(R.id.nowPlaying);
        artistName = findViewById(R.id.artistName);
        durationPlayed = findViewById(R.id.durationPlayed);
        durationTotal = findViewById(R.id.durationTotal);
        back_button = findViewById(R.id.back_button);
        cover_art = findViewById(R.id.cover_art);
        shuffle_button = findViewById(R.id.shuffle_button);
        previousSong = findViewById(R.id.previousSong);
        nextSong = findViewById(R.id.nextSong);
        repeatSong = findViewById(R.id.repeatSong);
        playAndPause = findViewById(R.id.playAndPause);
        seekBarSongs = findViewById(R.id.seekBarSongs);
    }


    /**
     *      - metaData method uses the music file's meta data to set it's cover image, title and so on .. .
     *
     * @param uri : path of the music file
     */
    private void metaData(Uri uri){
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri.toString());
        int duration_total = Integer.parseInt(songList.get(position).getDuration()) / 1000;
        durationTotal.setText(formattedTime(duration_total));
        byte[] art = retriever.getEmbeddedPicture();
        Bitmap bitmap;
        if (art != null){
            bitmap = BitmapFactory.decodeByteArray(art, 0, art.length);
            ImageAnimation(this, cover_art, bitmap);
            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(@Nullable Palette palette) {
                    Palette.Swatch swatch = palette.getDominantSwatch();
                    if (swatch != null){
                        ImageView gradient = findViewById(R.id.ImageViewGradient);
                        RelativeLayout mContainer = findViewById(R.id.songContainerPlaying);
                        gradient.setBackgroundResource(R.drawable.song_image_gradient);
                        mContainer.setBackgroundResource(R.drawable.background_songs);
                        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{swatch.getRgb(), 0x00000000});
                        //gradient.setBackground(gradientDrawable);
                        GradientDrawable gradientDrawableBG = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new int[]{swatch.getRgb(), swatch.getRgb()});
                        mContainer.setBackground(gradientDrawableBG);

                        SongName.setTextColor(swatch.getTitleTextColor());
                        artistName.setTextColor(swatch.getBodyTextColor());
                    } else {
                        ImageView gradient = findViewById(R.id.ImageViewGradient);
                        RelativeLayout mContainer = findViewById(R.id.songContainerPlaying);
                        gradient.setBackgroundResource(R.drawable.song_image_gradient);
                        mContainer.setBackgroundResource(R.drawable.background_songs);
                        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new int[]{0xff000000, 0x00000000});
                        //gradient.setBackground(gradientDrawable);
                        GradientDrawable gradientDrawableBG = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new int[]{0xff000000, 0xff000000});
                        mContainer.setBackground(gradientDrawableBG);
                        SongName.setTextColor(Color.BLACK);
                        artistName.setTextColor(Color.DKGRAY);
                    }
                }
            });
        } else {
            Glide.with(this).asBitmap().load(R.drawable.music).into(cover_art);
            ImageView gradient = findViewById(R.id.ImageViewGradient);
            RelativeLayout mContainer = findViewById(R.id.songContainerPlaying);
            gradient.setBackgroundResource(R.drawable.song_image_gradient);
            mContainer.setBackgroundResource(R.drawable.background_songs);
            SongName.setTextColor(Color.BLACK);
            artistName.setTextColor(Color.DKGRAY);
        }
    }


    /**
     *      - metaData method set's the music's cover as well as a small fade-in, fade-out animation when the song is skipped.
     *
     * @param context : Context instance of this class
     * @param imageView : temporary image for music files
     * @param bitmap : actual cover of the song
     */
    public void ImageAnimation(Context context, ImageView imageView, Bitmap bitmap){
        Animation animOut = AnimationUtils.loadAnimation(context, android.R.anim.fade_out);
        Animation animIn = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
        animOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                    // Unimplemented method
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Glide.with(context).load(bitmap).into(imageView);
                animIn.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        // Unimplemented method
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        // Unimplemented method
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                        // Unimplemented method
                    }
                });
                imageView.startAnimation(animIn);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // Unimplemented method
            }
        });
        imageView.startAnimation(animOut);
    }

    /**
     *      - onServiceConnected method is called when user clicks on a song, it starts the service as well as
     *      starting a notification channel for ease of the user.
     *
     * @param name : name of the communication channel
     * @param service : music service that plays songs in the background
     */
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        MusicService.binder mbinder = (MusicService.binder) service;
        musicService = mbinder.getService();
        musicService.setCallBack(this);
        if (musicService != null) {
            //Toast.makeText(this, "Connected to MusicService", Toast.LENGTH_SHORT).show();
            seekBarSongs.setMax(musicService.getDuration() / 1000);
            metaData(uri);
            SongName.setText(songList.get(position).getTitle());
            artistName.setText(songList.get(position).getArtist());
            nowPlaying.setText(songList.get(position).getAlbum());
            musicService.onCompleted();
            musicService.showNotification(R.drawable.song_colored_pause);
        }
    }


    /**
     *      - onServiceDisconnected method stops the service when the service is disconnected by assigning it to a null value.
     *
     * @param name : name of the communication channel
     */
    @Override
    public void onServiceDisconnected(ComponentName name) {
        musicService = null;
    }

}
