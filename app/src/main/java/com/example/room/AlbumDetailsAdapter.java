package com.example.room;

import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.ArrayList;

public class AlbumDetailsAdapter extends RecyclerView.Adapter<AlbumDetailsAdapter.VHolder> {

    private Context context;
    static ArrayList<MusicFiles> albumFiles;
    View view;

    public AlbumDetailsAdapter(Context context, ArrayList<MusicFiles> albumFiles) {
        this.context = context;
        this.albumFiles = albumFiles;
    }

    @NonNull
    @Override
    public VHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.music_items, parent, false);
        return new VHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VHolder holder, int position) {

        holder.album_name.setText(albumFiles.get(position).getTitle());
        try {
            byte[] image = getAlbumart(albumFiles.get(position).getPath());
            if (image != null) {
                Glide.with(context).asBitmap().load(image).into(holder.album_cover);
            }else {
                holder.album_cover.setImageResource(R.drawable.music);
            }
        } catch (IOException e) {
            Glide.with(context).load(R.drawable.music).into(holder.album_cover);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PlayingSongs.class);
                intent.putExtra("sender", "albumDetails");
                intent.putExtra("position", position);
                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return albumFiles.size();
    }

    public class VHolder extends  RecyclerView.ViewHolder{
        ImageView album_cover;
        TextView album_name;
        public VHolder(@NonNull View itemView) {
            super(itemView);
            album_cover = itemView.findViewById(R.id.music_image);
            album_name = itemView.findViewById(R.id.musicFileName);
        }
    }
    private byte[] getAlbumart(String uri) throws IOException {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] art = retriever.getEmbeddedPicture();
        retriever.release();
        return art;
    }
}
