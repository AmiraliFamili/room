package com.example.room;

import static com.example.room.music_ApplicationClass.ACTION_NEXT;
        import static com.example.room.music_ApplicationClass.ACTION_PLAY;
        import static com.example.room.music_ApplicationClass.ACTION_PREVIOUS;
        import static com.example.room.music_ApplicationClass.CHANNEL_ID_1;
        import static com.example.room.music_ApplicationClass.CHANNEL_ID_2;
        import static com.example.room.music_PlayingSongs.songList;

        import android.app.Notification;
        import android.app.NotificationChannel;
        import android.app.NotificationManager;
        import android.app.PendingIntent;
        import android.app.Service;
        import android.content.Intent;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.graphics.Color;
        import android.media.MediaMetadataRetriever;
        import android.media.MediaPlayer;
        import android.net.Uri;
        import android.os.Binder;
        import android.os.Build;
        import android.os.IBinder;
        import android.support.v4.media.session.MediaSessionCompat;
        import android.util.Log;
        import android.widget.RemoteViews;

        import androidx.annotation.Nullable;
        import androidx.core.app.NotificationCompat;
        import androidx.palette.graphics.Palette;

        import java.io.IOException;
        import java.util.ArrayList;

/**
 * @see MusicService
 *
 *      - Class MusicService is main class for the service that starts when user plays a song it's responsible for communications between the main activity
 *      , communications like when user pauses the song through a notification, the music_main activity is called
 *
 * @author Amirali Famili
 */
public class MusicService extends Service implements MediaPlayer.OnCompletionListener {


    private IBinder binders = new binder();
    private MediaPlayer mediaPlayer;
    private ArrayList<music_Files> musicFiles = new ArrayList<>();
    private Uri uri;
    private int position = -1;

    private music_ActionPlaying actionPlaying;
    private MediaSessionCompat mediaSessionCompat;

    @Override // get the base context when the class is called
    public void onCreate() {
        super.onCreate();
        mediaSessionCompat = new MediaSessionCompat(getBaseContext(), "My Audio");

    }

    /**
     *      - onBind method returns a new binder object.
     *
     * @param intent
     * return a new binder object
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binders;
    }

    /**
     * @see binder
     *
     *      - Class binder returns an music service instance.
     * @author Amirali Famili
     */
    public class binder extends Binder {
        MusicService getService(){
            return MusicService.this;
        }
    }

    /**
     *      - onStartCommand method reacts to the commands from the notification.
     *
     * @param intent : intent sent for this class
     * @param flags : flags value
     * @param startId : startID for action
     *
     * @return START_STICKY : Constant to return from onStartCommand
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int mPosition = intent.getIntExtra("servicePosition", -1);
        String actionName = intent.getStringExtra("ActionName");
        if (mPosition != -1){
            playMedia(mPosition);
        }
        if (actionName != null){
            switch (actionName){
                case "playPause" :
                    //Toast.makeText(this, "Play Pause", Toast.LENGTH_SHORT).show();
                    if (actionPlaying != null) {
                        actionPlaying.playAndPauseClicked();
                    }
                    break;
                case "next" :
                    //Toast.makeText(this, "Next", Toast.LENGTH_SHORT).show();
                    if (actionPlaying != null) {
                        actionPlaying.nextSongClicked();
                    }
                    break;
                case "previous" :
                    //Toast.makeText(this, "Previous", Toast.LENGTH_SHORT).show();
                    if (actionPlaying != null) {
                        actionPlaying.previousSongClicked();
                    }
                    break;
            }
        }
        return START_STICKY;
    }

    /**
     *      - playMedia method updates the music files list and starts the song at the index given
     *      as an argument.
     *
     * @param position1 : intent sent for this class
     */
    private void playMedia(int position1) {
        musicFiles = songList;
        position = position1;
        if (mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
            if (musicFiles != null){
                createMediaPlayer(position);
                mediaPlayer.start();
            }
        } else {
            createMediaPlayer(position);
            mediaPlayer.start();
        }
    }

    /**
     *      - start method starts the mediaPlayer instance.
     */
    void start(){
        mediaPlayer.start();
    }

    /**
     *      - isPlaying method checks if the song is currently being played.
     *
     * @return true if the song isPlaying and false if it's not.
     */
    boolean isPlaying(){
        return mediaPlayer.isPlaying();
    }

    /**
     *      - stop method stops the mediaPlayer instance.
     */
    void stop(){
        mediaPlayer.stop();
    }

    /**
     *      - release method releases the mediaPlayer instance.
     */
    void release(){
        mediaPlayer.release();
    }

    /**
     *      - getDuration method is a getter method for obtaining the total duration of the song selected.
     *
     * @return total duration of the selected song
     */
    int getDuration() {
        return mediaPlayer.getDuration();
    }

    /**
     *      - seekTo method changes the seekbar and thus changes the part of the song being played,
     *      according to the position inside the seekbar, this position is given as an argument and the method simply makes
     *      the song to jump forward or backward depending on the position.
     *
     *
     * @param position : the position that the song should jump to.
     */
    void seekTo(int position){
        mediaPlayer.seekTo(position);
    }

    /**
     *      - getCurrentPosition method is a getter method for obtaining the current position of the song inside the seekbar.
     *
     * @return the position of the song in the seekbar
     */
    int getCurrentPosition(){
        return mediaPlayer.getCurrentPosition();
    }


    /**
     *      - createMediaPlayer method is used for creating a new media player.
     */
    void createMediaPlayer(int positionInner){
        position = positionInner;
        uri = Uri.parse(musicFiles.get(position).getPath());
        mediaPlayer = MediaPlayer.create(getBaseContext(), uri);
    }

    /**
     *      - pause method pauses the song.
     */
    void pause(){
        mediaPlayer.pause();
    }

    /**
     *      - onCompleted method listens for when the song is completed, then plays the next song.
     */
    void onCompleted(){
        mediaPlayer.setOnCompletionListener(this);
    }
    @Override
    public void onCompletion(MediaPlayer mp) {
        if (actionPlaying != null){
            actionPlaying.nextSongClicked();
            if (mediaPlayer != null){
                createMediaPlayer(position);
                mediaPlayer.start();
                onCompleted();
            }
        }

    }

    /**
     *      - setCallBack method is a setter method for action Playing .
     *
     * @param actionPlaying : music_ActionPlaying instance
     */
    void setCallBack(music_ActionPlaying actionPlaying) {
        this.actionPlaying = actionPlaying;
    }

    /**
     *      - getAlbumart method is responsible for extracting the cover of music files.
     *
     * @param uri : the path for the music file
     *
     *
     * @return art : the cover for the song that has the path "uri"
     */
    private byte[] getAlbumart(String uri) throws IOException {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] art = retriever.getEmbeddedPicture();
        retriever.release();
        return art;
    }

    /**
     *      - showNotification method is a single method responsible for the notification created for when user,
     *      clicks on a song, the notification should show at the top of the screen
     *
     * @param playPauseButton : the id of the current play and pause method (is it play or pause ?)
     */
    void showNotification(int playPauseButton) {

        Intent intent = new Intent(this, music_PlayingSongs.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Intent prevIntent = new Intent(this, music_NotificationReceiver.class).setAction(ACTION_PREVIOUS);
        PendingIntent prevPending = PendingIntent.getBroadcast(this, 0, prevIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent pauseIntent = new Intent(this, music_NotificationReceiver.class).setAction(ACTION_PLAY);
        PendingIntent pausePending = PendingIntent.getBroadcast(this, 0, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT); // Use unique requestCode

        Intent nextIntent = new Intent(this, music_NotificationReceiver.class).setAction(ACTION_NEXT);
        PendingIntent nextPending = PendingIntent.getBroadcast(this, 0, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT); // Use unique requestCode

        Bitmap thumb = null;
        byte[] picture = null;
        try {
            picture = getAlbumart(musicFiles.get(position).getPath());
        } catch (IOException ignored) {
        }
        if (picture != null) {
            thumb = BitmapFactory.decodeByteArray(picture, 0, picture.length);
        } else {
            thumb = BitmapFactory.decodeResource(getResources(), R.drawable.music);
        }

        RemoteViews customNotificationView = new RemoteViews(getPackageName(), R.layout.music_notification);
        customNotificationView.setImageViewBitmap(R.id.music_image, thumb);
        customNotificationView.setTextViewText(R.id.songName, musicFiles.get(position).getTitle());
        customNotificationView.setTextViewText(R.id.artistName, musicFiles.get(position).getArtist());
        customNotificationView.setImageViewResource(R.id.playPause, playPauseButton);
        // Set click listeners for playback controls
        customNotificationView.setOnClickPendingIntent(R.id.previousSong, prevPending);
        customNotificationView.setOnClickPendingIntent(R.id.playPause, pausePending);
        customNotificationView.setOnClickPendingIntent(R.id.nextSong, nextPending);


        Palette.from(thumb).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(@Nullable Palette palette) {
                int backgroundColor;
                if (palette != null) {
                    backgroundColor = palette.getDominantColor(0x000000); // Default to black if color extraction fails
                } else {
                    backgroundColor = Color.DKGRAY;
                }

                // Adjust the transparency by 60%
                int originalAlpha = Color.alpha(backgroundColor);
                int newAlpha = (int) (originalAlpha * 0.5);
                newAlpha = Math.max(0, Math.min(255, newAlpha));
                backgroundColor = Color.argb(newAlpha, Color.red(backgroundColor), Color.green(backgroundColor), Color.blue(backgroundColor));

                // Set the background color of the notification layout
                customNotificationView.setInt(R.id.holderForBackground, "setBackgroundColor", backgroundColor);

                // Build the notification
                Notification notification = new NotificationCompat.Builder(MusicService.this, CHANNEL_ID_2)
                        .setSmallIcon(playPauseButton)
                        .setContentIntent(contentIntent)
                        .setCustomContentView(customNotificationView) // Set custom layout
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setOnlyAlertOnce(true)
                        .build();

                startForeground(1, notification);
            }
        });
    }

}

