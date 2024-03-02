package com.example.room;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


/**
 * @see music_Adapter
 *
 *      - Class music_Adapter is an Adapter class used for simple operations on music files, like getting their position in the list or removing them from the uset's device.
 *
 * @author Amirali Famili
 */
public class music_Adapter extends RecyclerView.Adapter<music_Adapter.VideoHolder> {

    private Context context;
    static ArrayList<music_Files> music;


    /**
     * @see music_AlbumAdapter
     *
     *      - music_AlbumAdapter Constructor just for assigning values when an intent is made to this class.
     *
     * @param musicContext : the context of the music files
     * @param musicFiles : all the music files
     */
    music_Adapter(Context musicContext, ArrayList<music_Files> musicFiles) {
        this.music = musicFiles;
        this.context = musicContext;
    }

    /**
     *
     *      - Initialise a new view to View Holder class
     */
    @NonNull
    @Override
    public VideoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.music_items, parent, false);
        return new VideoHolder(view);
    }

    /**
     *
     *      - onBindViewHolder method is a method which determines what is happening inside the recycler view.
     *      It's an mandatory method when extending the recycler view class.
     *
     *
     * @param holder : information about the album
     * @param position : the position of the music file inside the main array
     *
     */
    @Override
    public void onBindViewHolder(@NonNull VideoHolder holder, int position) {
        holder.fileName.setText(music.get(position).getTitle());
        try {
            byte[] image = getAlbumart(music.get(position).getPath());
            if (image != null) {
                Glide.with(context).asBitmap().load(image).into(holder.albumImage);
            }else {
                holder.albumImage.setImageResource(R.drawable.music);
            }
        } catch (IOException e) {
            Glide.with(context).load(R.drawable.music).into(holder.albumImage);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override // when user clicks the item holder
            public void onClick(View v) {
                Intent intent = new Intent(context, music_PlayingSongs.class);
                intent.putExtra("position", position);
                context.startActivity(intent);
            }
        });

        holder.menuMore.setOnClickListener(new View.OnClickListener() {
            @Override// when user clicks on the holder for menu
            public void onClick(final View v) {
                PopupMenu popupMenu = new PopupMenu(context, v);
                popupMenu.getMenuInflater().inflate(R.menu.individual_songs_menu, popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener((item) -> {
                    if (item.getItemId() == R.id.delete) {
                        Toast.makeText(context, "Song Deleted !", Toast.LENGTH_SHORT).show();
                        deleteFile(position, v);
                    }
                    return true;
                });
            }
        });

    }

    /**
     *      - deleteFile method is a private helper method which removes the music file from the app with a position given as input.
     *
     * @param position : an integer that determines the music file's location inside the main files list
     * @param v : the View for the song
     */
    private void deleteFile(int position, View v) {
        Uri contentUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, Long.parseLong(music.get(position).getId()));
        File file = new File(music.get(position).getPath());
        boolean deleted = file.delete();
        if (deleted) {
            int rowsDeleted = context.getContentResolver().delete(contentUri, null, null);
            if (rowsDeleted > 0) {
                music.remove(position);
                notifyItemRemoved(position);
                notifyItemChanged(position, music.size());
                Snackbar.make(v, "Song Deleted!", Snackbar.LENGTH_LONG).show();
            } else {
                Snackbar.make(v, "Failed to delete song from media store", Snackbar.LENGTH_LONG).show();
            }
        } else {
            Snackbar.make(v, "Failed to delete file from storage", Snackbar.LENGTH_LONG).show();
        }
    }

    /**
     *      - getItemCount method is returning the size of the list containing all songs
     *
     *
     * @return total number of songs in the app
     */
    @Override
    public int getItemCount() {
        return music.size();
    }

    /**
     * @see VideoHolder
     *
     *      - Class VideoHolder is a helper class which assigns the music's image, menu and name and makes them available for the main class to use.
     *
     * @author Amirali Famili
     */
    public class VideoHolder extends RecyclerView.ViewHolder {

        TextView fileName;
        ImageView albumImage, menuMore;

        public VideoHolder(@NonNull View itemView) {
            super(itemView);
            fileName = itemView.findViewById(R.id.musicFileName);
            albumImage = itemView.findViewById(R.id.music_image);
            menuMore = itemView.findViewById(R.id.menuMore);
        }
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
     *      - updateList method is updating the music arrayList simply by assigning a new one given as a parameter.
     *
     * @param musicFiles : all the songs in the app
     */
    void updateList(ArrayList<music_Files> musicFiles){
        music = new ArrayList<>();
        music.addAll(musicFiles);
        notifyDataSetChanged();
    }
}
