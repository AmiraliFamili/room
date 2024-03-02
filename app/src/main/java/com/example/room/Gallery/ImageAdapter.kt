package com.example.room.Gallery

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.room.R

/**
 * @see ImageAdapter
 *
 *      - Class ImageAdapter is a adapter class that is dealing with image view holder
 *      and other important methods that are called from this class.
 *
 * @author Amirali Famili
 */
class ImageAdapter(private var context: Context, private  var imagesList : ArrayList<Image>) :
        RecyclerView.Adapter<ImageAdapter.ImageViewHolder>(){

    /**
     * @see ImageViewHolder
     *
     *      - Class ImageViewHolder is a helper class which is responsible for image's view
     *      (it's container)
     *
     * @author Amirali Famili
     */
    class ImageViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView){
    var image : ImageView?=null

        init {
            image=itemView.findViewById(R.id.image_row)
        }
    }


    /**
     *      - onCreateViewHolder method is setting the view for the selected image
     *
     * @param parent : parent view
     * @param viewType : type of the current view
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.row_custom_recycler_view, parent, false)
        return ImageViewHolder(view)
    }

    /**
     *      - getItemCount is a getter method for the number of images in the app
     *
     * @return total number of images in the app
     */
    override fun getItemCount(): Int {
        return imagesList.size
    }

    /**
     *      - onBindViewHolder method is responsible for redirecting users to a fullscreen page when
     *      they click on an image.
     *
     * @param holder : holder view for the image (ImageViewHolder container)
     * @param position : index of the image in the main image list
     */
    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val currentImage = imagesList[position]
        Glide.with(context).load(currentImage.imagePath).apply(RequestOptions().centerCrop()).into(
            holder!!.image!!
        )

        holder.image?.setOnClickListener {
            val intent = Intent(context, ImageFullActivity::class.java)
            intent.putExtra("path", currentImage.imagePath)
            intent.putExtra("name", currentImage.imageName)
            context.startActivity(intent)
        }

    }
}