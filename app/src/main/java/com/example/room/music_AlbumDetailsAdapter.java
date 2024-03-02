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

/**
 * @see music_AlbumDetailsAdapter
 *
 *      - Class music_AlbumDetailsAdapter is the Adapter class for album details class. it is helping the album details class by playing the song when clicked by sending intents.
 *
 * @author Amirali Famili
 */
public class music_AlbumDetailsAdapter extends RecyclerView.Adapter<music_AlbumDetailsAdapter.VHolder> {

    private Context context;
    static ArrayList<music_Files> albumFiles;
    private View view;

    /**
     * @see music_AlbumDetailsAdapter
     *
     *      - music_AlbumDetailsAdapter Constructor just for assigning values when an intent is made to this class.
     *
     * @param context : the context of the album files
     * @param albumFiles : all the album files
     */
    public music_AlbumDetailsAdapter(Context context, ArrayList<music_Files> albumFiles) {
        this.context = context;
        this.albumFiles = albumFiles;
    }

    /**
     *
     *      - Initialise a new view to View Holder class
     */
    @NonNull
    @Override
    public VHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.music_items, parent, false);
        return new VHolder(view);
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
                Intent intent = new Intent(context, music_PlayingSongs.class);
                intent.putExtra("sender", "albumDetails");
                intent.putExtra("position", position);
                context.startActivity(intent);
            }
        });
    }

    /**
     *      - getItemCount method is returning the size of the list containing all songs
     *
     *
     * @return total number of songs in the app
     *
     * @author Amirali Famili
     */
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
}
