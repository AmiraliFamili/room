package com.example.room;

import static com.example.room.music_ApplicationClass.ACTION_NEXT;
import static com.example.room.music_ApplicationClass.ACTION_PLAY;
import static com.example.room.music_ApplicationClass.ACTION_PREVIOUS;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


/**
 * @see music_NotificationReceiver
 *
 *      - Class music_NotificationReceiver is a helper class for responding to the options selected on the
 *      notification bar, actions like pause or play songs and skiping them
 *
 * @author Amirali Famili
 */
public class music_NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        String actionName = intent.getAction();
        Intent serviceIntent = new Intent(context, MusicService.class);
        if(actionName != null){
            switch (actionName) {
                case ACTION_PLAY:
                    serviceIntent.putExtra("ActionName", "playPause");
                    context.startService(serviceIntent);
                    break;
                case ACTION_NEXT:
                    serviceIntent.putExtra("ActionName", "next");
                    context.startService(serviceIntent);
                    break;
                case ACTION_PREVIOUS:
                    serviceIntent.putExtra("ActionName", "previous");
                    context.startService(serviceIntent);
                    break;
            }
        }
    }
}
