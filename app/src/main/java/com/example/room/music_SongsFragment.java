package com.example.room;

import static com.example.room.music_main.musicFiles;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * @see music_SongsFragment
 *
 *      - Class music_SongsFragment is main class that provides the backend simple operations on song fragment inside the music_main.
 *
 * @author Amirali Famili
 */
public class music_SongsFragment extends Fragment {

    private RecyclerView recyclerView;
    static music_Adapter musicAdapter;

    /**
     * @see music_SongsFragment
     *
     *      - music_SongsFragment default Constructor for ease of access.
     *
     */
    public music_SongsFragment() {
        // Required empty public constructor
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
        View view = inflater.inflate(R.layout.fragment_songs, container, false);
        recyclerView = view.findViewById(R.id.songFragmentRecycler);
        recyclerView.setHasFixedSize(true);
        if (!(musicFiles.size() < 1)) {
            musicAdapter = new music_Adapter(getContext(), musicFiles);
            recyclerView.setAdapter(musicAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        }
        return view;
    }
}