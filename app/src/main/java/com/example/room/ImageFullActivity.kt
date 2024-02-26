package com.example.room

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

class ImageFullActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_full)

        val imagePath=intent.getStringExtra("path")
        val imageName=intent.getStringExtra("name")

        supportActionBar?.setTitle(imageName)
        val mainImage = findViewById<ImageView>(R.id.Main_Image_gallery)

        Glide.with(this)
            .asBitmap()
            .load(imagePath)
            .apply(RequestOptions().centerCrop())
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    mainImage.setImageBitmap(resource)
                    Palette.from(resource).generate { palette ->
                        val backgroundColor = palette?.dominantSwatch?.rgb ?: Color.DKGRAY
                        val originalAlpha = Color.alpha(backgroundColor)
                        val newAlpha = (originalAlpha * 0.9f).toInt()
                        val adjustedColor = Color.argb(newAlpha, Color.red(backgroundColor), Color.green(backgroundColor), Color.blue(backgroundColor))
                        mainImage.setBackgroundColor(adjustedColor)
                    }
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    // TODO :: ....
                }
            })
    }
}
