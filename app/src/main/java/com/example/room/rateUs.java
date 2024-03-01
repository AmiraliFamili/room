package com.example.room;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.room.Gallery.Gallery;
import com.example.room.pass.passwordGN;
import com.google.android.material.navigation.NavigationView;


/**
 * @see rateUs
 *
 *      - rateUs is a simple activity containing just a star bar for capturing the user's general review,
 *      it does not yet contain a method for storing that review.
 *
 * @Notes This class is just a demo class and does not provide an important service to users.
 *
 * @author Amirali Famili
 */

public class rateUs extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    RatingBar ratingStar;

    DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_us);


        // set the nav view and it's button
        NavigationView navigationView = findViewById(R.id.notes_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        drawerLayout = findViewById(R.id.notes_main_page);
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        ratingStar = findViewById(R.id.ratingBar);


        // display a text corresponding to the user's experience with the app
            ratingStar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    int rate = (int) rating;

                    String message = null;

                    switch (rate){
                        case 1:
                            message = "Sorry to hear that :(";
                            break;
                        case 2 :
                            message = "it's ok we make it better together";
                            break;
                        case 3:
                            message = "that's very kind of you";
                            break;
                        case 4:
                            message = "the real beauty is from your eyes";
                            break;
                        case 5:
                            message = " :) ";
                            break;
                        default:
                            message = "Rate Us Please :)";
                            break;
                    }


                    Toast.makeText(rateUs.this, message, Toast.LENGTH_SHORT).show();
                }
            });

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