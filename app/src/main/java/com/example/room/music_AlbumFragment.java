package com.example.room;

import static com.example.room.music_main.albums;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class music_AlbumFragment extends Fragment {

    RecyclerView recyclerView;

    music_AlbumAdapter musicAlbumAdapter;
    public music_AlbumFragment(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_album, container, false);
        recyclerView = view.findViewById(R.id.songFragmentRecycler);
        recyclerView.setHasFixedSize(true);
        if (!(albums.size() < 1)) {
            musicAlbumAdapter = new music_AlbumAdapter(getContext(), albums);
            recyclerView.setAdapter(musicAlbumAdapter);
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        }
        return view;
    }
}