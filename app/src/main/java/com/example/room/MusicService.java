package com.example.room;

        import static com.example.room.ApplicationClass.ACTION_NEXT;
        import static com.example.room.ApplicationClass.ACTION_PLAY;
        import static com.example.room.ApplicationClass.ACTION_PREVIOUS;
        import static com.example.room.ApplicationClass.CHANNEL_ID_1;
        import static com.example.room.ApplicationClass.CHANNEL_ID_2;
        import static com.example.room.PlayingSongs.songList;

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
        import android.widget.Toast;

        import androidx.annotation.Nullable;
        import androidx.core.app.NotificationCompat;
        import androidx.palette.graphics.Palette;

        import java.io.IOException;
        import java.util.ArrayList;

public class MusicService extends Service implements MediaPlayer.OnCompletionListener {


    IBinder binders = new binder();
    MediaPlayer mediaPlayer;
    ArrayList<MusicFiles> musicFiles = new ArrayList<>();
    Uri uri;
    int position = -1;

    ActionPlaying actionPlaying;
    MediaSessionCompat mediaSessionCompat;

    @Override
    public void onCreate() {
        super.onCreate();
        mediaSessionCompat = new MediaSessionCompat(getBaseContext(), "My Audio");

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e("Bind", "Method");
        return binders;
    }




    public class binder extends Binder {
        MusicService getService(){
            return MusicService.this;
        }
    }
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

    void start(){
        mediaPlayer.start();
    }
    boolean isPlaying(){
        return mediaPlayer.isPlaying();
    }

    void stop(){
        mediaPlayer.stop();
    }
    void release(){
        mediaPlayer.release();
    }
    int getDuration () {
        return mediaPlayer.getDuration();
    }
    void seekTo(int position){
        mediaPlayer.seekTo(position);
    }

    int getCurrentPosition(){
        return mediaPlayer.getCurrentPosition();
    }

    void createMediaPlayer(int positionInner){
        position = positionInner;
        uri = Uri.parse(musicFiles.get(position).getPath());
        mediaPlayer = MediaPlayer.create(getBaseContext(), uri);
    }
    void pause(){
        mediaPlayer.pause();
    }
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

    void setCallBack(ActionPlaying actionPlaying) {
        this.actionPlaying = actionPlaying;
    }

    private byte[] getAlbumart(String uri) throws IOException {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] art = retriever.getEmbeddedPicture();
        retriever.release();
        return art;
    }

    public boolean isSongPlaying() {
        return mediaPlayer != null && mediaPlayer.isPlaying();
    }

    void showNotification(int playPauseButton) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel1 = new NotificationChannel(CHANNEL_ID_1, "Channel(1)", NotificationManager.IMPORTANCE_HIGH);
            channel1.setDescription("Channel 1 Description ... ");
            NotificationChannel channel2 = new NotificationChannel(CHANNEL_ID_2, "Channel(2)", NotificationManager.IMPORTANCE_HIGH);
            channel1.setDescription("Channel 2 Description ... ");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel1);
            notificationManager.createNotificationChannel(channel2);
        }

        Intent intent = new Intent(this, PlayingSongs.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Intent prevIntent = new Intent(this, NotificationReceiver.class).setAction(ACTION_PREVIOUS);
        PendingIntent prevPending = PendingIntent.getBroadcast(this, 0, prevIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent pauseIntent = new Intent(this, NotificationReceiver.class).setAction(ACTION_PLAY);
        PendingIntent pausePending = PendingIntent.getBroadcast(this, 0, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT); // Use unique requestCode

        Intent nextIntent = new Intent(this, NotificationReceiver.class).setAction(ACTION_NEXT);
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

