package com.example.room;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.widget.SearchView;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class SongPlayer extends AppCompatActivity implements SearchView.OnQueryTextListener, NavigationView.OnNavigationItemSelectedListener {

    public static final int REQUEST_CODE = 1;
    static ArrayList<MusicFiles> musicFiles;
    DrawerLayout drawerLayout;

    static boolean shuffleBool = false, repeatBool = false;
    static ArrayList<MusicFiles> albums = new ArrayList<>();

    private String SORT_THE_PREF = "Sort Order";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_player);


        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        drawerLayout = findViewById(R.id.songs_drawer_layout);
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.song_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Request permission on activity creation
        requestPermission();
    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SongPlayer.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
        } else {
            // If permission is granted, proceed with loading music files and initializing ViewPager
            loadMusicFiles();
        }
    }

    // Handle permission request result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, load music files and initialize ViewPager
                loadMusicFiles();
            } else {
                // Permission denied, handle accordingly
                Log.e("Permission", "permission denied.");
            }
        }
    }

    private void loadMusicFiles() {
        // Load music files from storage
        musicFiles = getAllAudio(this);

        // Initialize ViewPager
        initViewPager();
    }

    private void initViewPager(){
        ViewPager viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tableLayout);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragments(new SongsFragment(), "Songs");
        viewPagerAdapter.addFragments(new AlbumFragment(), "Albums");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private static class ViewPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        public ViewPagerAdapter(@NonNull FragmentManager fm){
            super(fm);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();
        }

        void addFragments(Fragment fragment, String title){
            fragments.add(fragment);
            titles.add(title);
        }
        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }

    public ArrayList<MusicFiles> getAllAudio(Context context){
        SharedPreferences preferences = getSharedPreferences(SORT_THE_PREF, MODE_PRIVATE);
        String sortOrder = preferences.getString("sorting", "sortByName");
        ArrayList<MusicFiles> tempAudioList = new ArrayList<>();
        ArrayList<String> duplicates = new ArrayList<>();
        String order = null;
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        if (sortOrder.equals("sortByName")){
            order = MediaStore.MediaColumns.DISPLAY_NAME + " ASC";
        } else if (sortOrder.equals("sortByDate")){
            order = MediaStore.MediaColumns.DATE_ADDED + " ASC";
        } else if (sortOrder.equals("sortBySize")){
            order = MediaStore.MediaColumns.SIZE + " DESC";
        }
        String[] projection = {
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media._ID
        };

        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, order);
        if (cursor != null){
            albums.clear();
            while (cursor.moveToNext()){
                String album = cursor.getString(0);
                String title = cursor.getString(1);
                String duration = cursor.getString(2);
                String path = cursor.getString(4);
                String artist = cursor.getString(3);
                String id = cursor.getString(5);
                MusicFiles musicFiles = new MusicFiles(path, title, artist, album, duration, id);
                tempAudioList.add(musicFiles);
                if (!duplicates.contains(album)){
                    albums.add(musicFiles);
                    duplicates.add(album);
                }
            }
            cursor.close();
        }
        return tempAudioList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_for_songs, menu);
        MenuItem menuItem = menu.findItem(R.id.search_option);
        SearchView searchView = (SearchView) menuItem.getActionView();
        assert searchView != null;
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String userInput = newText.toLowerCase();
        ArrayList<MusicFiles> mfiles = new ArrayList<>();
        for (MusicFiles song : musicFiles){
            if (song.getTitle().toLowerCase().contains(userInput)){
                mfiles.add(song);
            }
        }
        SongsFragment.musicAdapter.updateList(mfiles);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        SharedPreferences.Editor editor = getSharedPreferences(SORT_THE_PREF, MODE_PRIVATE).edit();
        if (item.getItemId() == R.id.sortByName){
            editor.putString("sorting", "sortByName");
            editor.apply();
            this.recreate();
        } else if (item.getItemId() == R.id.sortByDate){
            editor.putString("sorting", "sortByDate");
            editor.apply();
            this.recreate();
        } else if (item.getItemId() == R.id.sortBySize) {
            editor.putString("sorting", "sortBySize");
            editor.apply();
            this.recreate();
        }
        return super.onOptionsItemSelected(item);
    }


    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation item clicks here
        int id = item.getItemId();
        if (id == R.id.homeInNav){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.noteInNav){
            Intent intent = new Intent(this, Notes.class);
            startActivity(intent);
            return true;
        }else if (id == R.id.musicInNav){
            Intent intent = new Intent(this, SongPlayer.class);
            startActivity(intent);
            return true;
        }else if (id == R.id.passwordInNav){
            Intent intent = new Intent(this, passwordGN.class);
            startActivity(intent);
            return true;
        }else if (id == R.id.galleryInNav){
            Intent intent = new Intent(this, Gallery.class);
            startActivity(intent);
            return true;
        }else if (id == R.id.calculatorInNav){
            Intent intent = new Intent(this, calculator.class);
            startActivity(intent);
            return true;
        }else if (id == R.id.aboutInNav){
            Intent intent = new Intent(this, aboutUs.class);
            startActivity(intent);
            return true;
        }else if (id == R.id.shareInNav){
            Intent intent = new Intent(this, shareUs.class);
            startActivity(intent);
            return true;
        }else if (id == R.id.rateInNav){
            Intent intent = new Intent(this, rateUs.class);
            startActivity(intent);
            return true;
        } else {
            return false;
        }
    }

}
