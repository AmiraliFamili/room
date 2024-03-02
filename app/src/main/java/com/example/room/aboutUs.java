package com.example.room;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.navigation.NavigationView;

import java.util.Calendar;
import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;


/**
 * @see aboutUs
 *
 *      - Class aboutUs is responsible for creating a about us page using the
 *      mehdi.sakout library, this library is specifically designed to create about us pages.
 *
 * @author Amirali Famili
 */
public class aboutUs extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_about_us);




        // 0 is for day, 1 is for night mode
        simulateDayNight(1);


        Element adsElement = new Element();
        adsElement.setTitle("Advertise with us");

        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setImage(R.drawable.nav_head_4)
                .setDescription("Learn About Us")
                .addEmail("a.famili81@gmail.com")
                .addWebsite("https://room.example.com/")
                .addFacebook("AmiraliFamili")
                .addTwitter("AmiraliFamili")
                .addYoutube("No Youtube Channel Yet")
                .addPlayStore("com.example.room  :  to be published")
                .addInstagram("awmiir_fml")
                .addGitHub("AmiraliFamili")
                .addItem(getCopyRightsElement())
                .create();

        setContentView(aboutPage);
    }

    /**
     *      - This method will take an integer as input and sets the theme of the about us page,
     *      0 is white (day mode) and 1 is black (dark theme)
     *
     * @param setting : an integer for determining the theme of the page
     */
    private void simulateDayNight(int setting) {

        int DAY = 0;
        int NIGHT = 1;
        int FOLLOW_SYSTEM=3;
        int currentSetting = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        if (setting == DAY && currentSetting!=Configuration.UI_MODE_NIGHT_NO){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else if (setting == NIGHT && currentSetting!=Configuration.UI_MODE_NIGHT_YES){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else if (setting == FOLLOW_SYSTEM){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }
    }

    /**
     *      - This method is responsible for customizing the Rights part of the about us page.
     *
     * @return a customised copy rights element to be used in the about us page
     */
    private Element getCopyRightsElement() {
        Element copyRightsElement = new Element();
        @SuppressLint({"StringFormatInvalid", "LocalSuppress"}) final String copyrights = String.format(getString(R.string.copy_write), Calendar.getInstance().get(Calendar.YEAR));
        copyRightsElement.setTitle(copyrights);
        copyRightsElement.setIconDrawable(R.drawable.baseline_lightbulb_24);
        copyRightsElement.setAutoApplyIconTint(true);
        copyRightsElement.setIconTint(mehdi.sakout.aboutpage.R.color.about_item_icon_color);
        copyRightsElement.setIconNightTint(android.R.color.white);
        copyRightsElement.setGravity(Gravity.CENTER);
        copyRightsElement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(aboutUs.this, copyrights, Toast.LENGTH_SHORT).show();
            }
        });
        return copyRightsElement;
    }
}