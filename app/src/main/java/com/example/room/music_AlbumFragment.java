package com.example.room;

import static com.example.room.music_main.albums;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * @see music_AlbumFragment
 *
 *      - Class music_AlbumFragment is main class that provides the backend simple operations on album fragment inside the music_main.
 *
 * @author Amirali Famili
 */
public class music_AlbumFragment extends Fragment {

    private RecyclerView recyclerView;

    private music_AlbumAdapter musicAlbumAdapter;

    /**
     * @see music_AlbumFragment
     *
     *      - music_AlbumFragment default Constructor for ease of access.
     *
     */
    public music_AlbumFragment(){

    }

    /**
     *      - onCreateView the main method which is called when the fragment is shown.
     *
     *
     * @param inflater : inflater is for setting the correct layout
     * @param container : the container that is holding the fragment
     * @param savedInstanceState : for saving the state of the fragment
     */
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