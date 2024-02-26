package com.example.room;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.room.pass.lowerCase;
import com.example.room.pass.numericCase;
import com.example.room.pass.passwordGenerator;
import com.example.room.pass.specialCase;
import com.example.room.pass.upperCase;
import com.google.android.material.navigation.NavigationView;

public class passwordGN extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private EditText passwordSize;
    private TextView passGenerated, textErrorMessage;
    private CheckBox checkBoxLowercase, checkBoxUppercase, checkBoxNumeric, checkBoxSpecialChars;
    private Button copy;

    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe);

        NavigationView navigationView = findViewById(R.id.safe_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        drawerLayout = findViewById(R.id.safe_main_page);
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.safe_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();



        passwordSize = findViewById(R.id.password_length);
        passGenerated = findViewById(R.id.ShowPassword);
        checkBoxLowercase = findViewById(R.id.checkBoxLowercase);
        checkBoxUppercase = findViewById(R.id.checkBoxUppercase);
        checkBoxNumeric = findViewById(R.id.checkBoxNumeric);
        checkBoxSpecialChars = findViewById(R.id.checkBoxSpecialChars);
        copy = findViewById(R.id.copyPassword);
        textErrorMessage = findViewById(R.id.textErrorMessage);

        Button generate = findViewById(R.id.GenerateButton);

        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    if (passwordSize.getText().toString().isEmpty()){
                        textErrorMessage.setText("You have to have a length for your password, it adds an extra dimention to it :)");
                        return;
                    }
                    int passwordLength = Integer.parseInt(passwordSize.getText().toString());

                    if (passwordLength < 0) {
                        textErrorMessage.setText("good size changes everything :)");
                        return;
                    }

                    textErrorMessage.setText("");

                    passwordGenerator.clearPass();

                    if (checkBoxLowercase.isChecked()){
                        passwordGenerator.add(new lowerCase());
                    }
                    if (checkBoxUppercase.isChecked()){
                        passwordGenerator.add(new upperCase());
                    }
                    if (checkBoxNumeric.isChecked()){
                        passwordGenerator.add(new numericCase());
                    }
                    if (checkBoxSpecialChars.isChecked()){
                        passwordGenerator.add(new specialCase());
                    }


                    if (passwordLength > 100){
                        textErrorMessage.setText(passwordLength + "! Seriously ?  Try under 100 :)");
                        return;
                    }

                    if (passwordGenerator.isEmpty()){
                        textErrorMessage.setText("Check one 0f the Boxes :)");
                        return;
                    }



                    String pwd = passwordGenerator.generateApassword(passwordLength);
                    passGenerated.setText(pwd);

                } catch (Exception e){
                    textErrorMessage.setText("Try Numbers :)");
                }
            }
        });


        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager manager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                manager.setPrimaryClip(ClipData.newPlainText("password", passGenerated.getText().toString()));
                Toast.makeText(passwordGN.this, "password copied !", Toast.LENGTH_SHORT).show();
            }
        });

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