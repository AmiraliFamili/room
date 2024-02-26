package com.example.room;

import static java.nio.file.Files.delete;

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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.VideoHolder> {

    private Context context;
    static ArrayList<MusicFiles> music;


    MusicAdapter(Context musicContext, ArrayList<MusicFiles> musicFiles) {
        this.music = musicFiles;
        this.context = musicContext;
    }

    @NonNull
    @Override
    public VideoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.music_items, parent, false);
        return new VideoHolder(view);
    }

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
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PlayingSongs.class);
                intent.putExtra("position", position);
                context.startActivity(intent);
            }
        });

        holder.menuMore.setOnClickListener(new View.OnClickListener() {
            @Override
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

    @Override
    public int getItemCount() {
        return music.size();
    }

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

    private byte[] getAlbumart(String uri) throws IOException {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] art = retriever.getEmbeddedPicture();
        retriever.release();
        return art;
    }
    void updateList(ArrayList<MusicFiles> musicFiles){
        music = new ArrayList<>();
        music.addAll(musicFiles);
        notifyDataSetChanged();
    }
}
