package com.example.room.Gallery

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.room.MainActivity
import com.example.room.music_main
import com.example.room.music_main.REQUEST_CODE
import com.example.room.Notes
import com.example.room.R
import com.example.room.aboutUs
import com.example.room.calculator
import com.example.room.pass.helper
import com.example.room.pass.passwordGN
import com.example.room.rateUs
import com.example.room.shareUs
import com.google.android.material.navigation.NavigationView

/**
 * @see Gallery
 *
 *      - Class Gallery is the main class responsible for displaying all images inside a recycler view,
 *      and handles permissions and file loading.
 *
 * @author Amirali Famili
 */
class Gallery : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var imageRecycler: RecyclerView? = null
    private var progressBar: ProgressBar? = null
    private var allPictures: ArrayList<Image>? = null
    private lateinit var drawerLayout: DrawerLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallary)

        val navigationView: NavigationView = findViewById(R.id.navigation_view)
        navigationView.setNavigationItemSelectedListener(this)

        drawerLayout = findViewById(R.id.songs_drawer_layout)
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.song_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        imageRecycler = findViewById(R.id.gallaryRecycler)
        progressBar = findViewById(R.id.recycler_progressor)

        imageRecycler?.layoutManager = GridLayoutManager(this, 3)
        imageRecycler?.setHasFixedSize(true)

        if (ContextCompat.checkSelfPermission(
                this@Gallery,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@Gallery,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_CODE
            )
        } else {
            loadImages()
        }
    }

    /**
     *      - loadImages method loads all the images available in users device to the app.
     */
    private fun loadImages() {
        progressBar?.visibility = View.VISIBLE
        allPictures = getAllImages()
        imageRecycler?.adapter = ImageAdapter(this, allPictures!!)
        progressBar?.visibility = View.GONE
    }

    /**
     *      - getAllAudio method is a method used to gather all image files in the user's device into a single array list using their path and name
     *
     * @param context : instance of this class (an image)
     * @return a list containing all images including their path and name
     */
    private fun getAllImages(): ArrayList<Image>? {
        val images = ArrayList<Image>()
        val allImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.Images.ImageColumns.DATA, MediaStore.Images.Media.DISPLAY_NAME)
        val cursor = contentResolver.query(allImageUri, projection, null, null, null)

        cursor?.use {
            while (it.moveToNext()) {
                val image = Image()
                image.imagePath = it.getString(it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
                image.imageName = it.getString(it.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME))
                images.add(image)
            }
        }

        return images
    }

    /**
     *      - onRequestPermissionsResult handling the response of the user to the permission question ...
     *
     * @param requestCode : should be 1 for all permissions
     * @param permissions : permission type
     * @param grantResults : should be 0 if the answer is yes
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            loadImages()
        }
    }

    /**
     *      - This method is responsible for redirecting user to the correct activity when they click on the items listed on the navigation menu.
     *
     * @param item : id of the item user clicked on it
     *
     * @return true if item exists, false if it doesn't
     */
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation item clicks here
        val id = item.itemId
        when (id) {
            R.id.homeInNav -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.noteInNav -> {
                val intent = Intent(this, Notes::class.java)
                startActivity(intent)
                return true
            }
            R.id.musicInNav -> {
                val intent = Intent(this, music_main::class.java)
                startActivity(intent)
                return true
            }
            R.id.passwordInNav -> {
                val intent = Intent(this, passwordGN::class.java)
                startActivity(intent)
                return true
            }
            R.id.galleryInNav -> {
                val intent = Intent(this, Gallery::class.java)
                startActivity(intent)
                return true
            }
            R.id.calculatorInNav -> {
                val intent = Intent(this, calculator::class.java)
                startActivity(intent)
                return true
            }
            R.id.aboutInNav -> {
                val intent = Intent(this, aboutUs::class.java)
                startActivity(intent)
                return true
            }
            R.id.shareInNav -> {
                val intent = Intent(this, shareUs::class.java)
                startActivity(intent)
                return true
            }
            R.id.rateInNav -> {
                val intent = Intent(this, rateUs::class.java)
                startActivity(intent)
                return true
            }
            else -> return false
        }
    }


}
