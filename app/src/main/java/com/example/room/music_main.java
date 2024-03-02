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

import com.example.room.Gallery.Gallery;
import com.example.room.pass.passwordGN;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

/**
 * @see music_main
 *
 *      - Class music_main is the main class for the music activity, it holds all the songs on one side and
 *      all the albums on the other side in ther own fragment, it acts as the main page and an access point for all the classes in this project that have a
 *      music_ at the beggining of their name.
 *
 * @author Amirali Famili
 */
public class music_main extends AppCompatActivity implements SearchView.OnQueryTextListener, NavigationView.OnNavigationItemSelectedListener {

    public static final int REQUEST_CODE = 1;
    static ArrayList<music_Files> musicFiles;
    private DrawerLayout drawerLayout;

    static boolean shuffleBool = false, repeatBool = false;
    static ArrayList<music_Files> albums = new ArrayList<>();

    private String SORT_THE_PREF = "Sort Order";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_player);

        // set the drawer layout's navigation menu and sync it
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
        // -------------------------------------------

        // Request permission on activity creation
        requestPermission();
        //loadMusicFiles(); this method loads all the music files in the device
    }

    /**
     *      - requestPermission is a method called in the onCreate method for this activity, is asking for user's
     *      permission to have access to their files (that they have on their phone).
     *      If the permission is granted then the music files are loaded
     */
    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(music_main.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
        } else {
            // If permission is granted, proceed with loading music files and initializing ViewPager
            loadMusicFiles();
        }
    }

    /**
     *      - onRequestPermissionsResult handling the response of the user to the permission question
     *
     * @param requestCode : should be 1 for all permissions
     * @param permissions : permission type
     * @param grantResults : should be 0 if the answer is yes
     */
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

    /**
     *      - loadMusicFiles loads all the music files into the app using the getAllAudio method by sending the instance of this class to it.
     */
    private void loadMusicFiles() {
        // Load music files from storage
        musicFiles = getAllAudio(this);

        // Initialize ViewPager
        initViewPager();
    }

    /**
     *      - initViewPager uses a helper class to load the music and album fragments
     */
    private void initViewPager(){
        ViewPager viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tableLayout);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragments(new music_SongsFragment(), "Songs");
        viewPagerAdapter.addFragments(new music_AlbumFragment(), "Albums");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    /**
     * @see ViewPagerAdapter
     *
     *      - Class ViewPagerAdapter is conserned with the only two fragments this activity has (album and song),
     *      it adds, sets and sizes the fragments. (Fragment Maintainance)
     *
     * @author Amirali Famili
     */
    private static class ViewPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        /**
         *      - ViewPagerAdapter constructor for this class, it assigns the fragments and titles,
         */
        public ViewPagerAdapter(@NonNull FragmentManager fm){
            super(fm);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();
        }

        /**
         *      - addFragments method adds new fragments and titles to the arrays specified above.
         */
        void addFragments(Fragment fragment, String title){
            fragments.add(fragment);
            titles.add(title);
        }

        /**
         *      - getItem method takes a position (index) and returns the fragment on that position
         *
         * @param position : index of the chosen element
         * @return Fragment element : it returns a fragment at the specified position
         */
        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        /**
         *      - getCount method is a getter method for the size of the fragment (total fragments).
         *
         * @return fragments array's size (total fragments)
         */
        @Override
        public int getCount() {
            return fragments.size();
        }

        /**
         *      - getPageTitle method is a getter method for returning a char sequence of the title
         *      at the position passed as the argument.
         *
         * @param position : index of the chosen title
         * @return CharSequence title : it returns a title at the specified position from the main titles array.
         */
        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }

    /**
     *      - getAllAudio method is a method used to gather all music files in the user's device into a single array list using their path, name , album and ...
     *
     * @param context : instance of this class
     * @return all the music files found as a arraylist which has musicFiles Object as each element
     */
    public ArrayList<music_Files> getAllAudio(Context context){
        SharedPreferences preferences = getSharedPreferences(SORT_THE_PREF, MODE_PRIVATE);
        String sortOrder = preferences.getString("sorting", "sortByName");
        ArrayList<music_Files> tempAudioList = new ArrayList<>();
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
                music_Files musicFiles = new music_Files(path, title, artist, album, duration, id);
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

    /**
     *      - onCreateOptionsMenu method is used for assigning the popup menu responsible for deleting songs and searching for songs.
     *
     * @param menu : menu used for this page
     * @return true or false
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_for_songs, menu);
        MenuItem menuItem = menu.findItem(R.id.search_option);
        SearchView searchView = (SearchView) menuItem.getActionView();
        assert searchView != null;
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     *          - Unimplemented method
     */
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    /**
     *      - onQueryTextChange method is used when searching for songs, each time user enters a new character,
     *      this method is called to update the user's query
     *
     * @param newText : new text input from search bar
     * @return true or false
     */
    @Override
    public boolean onQueryTextChange(String newText) {
        String userInput = newText.toLowerCase();
        ArrayList<music_Files> mfiles = new ArrayList<>();
        for (music_Files song : musicFiles){
            if (song.getTitle().toLowerCase().contains(userInput)){
                mfiles.add(song);
            }
        }
        music_SongsFragment.musicAdapter.updateList(mfiles);
        return true;
    }

    /**
     *      - onOptionsItemSelected method is called when user decides to click on the menu on the top right corner to change the order that
     *     the music files are displayed.
     *
     * @param item : selected item from menu
     * @return true or false
     */
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


    /**
     *      - This method is responsible for redirecting user to the correct activity when they click on the items listed on the navigation menu.
     *
     * @param item : id of the item user clicked on it
     *
     * @return true if item exists, false if it doesn't
     */
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
            Intent intent = new Intent(this, music_main.class);
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
