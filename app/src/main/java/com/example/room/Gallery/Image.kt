package com.example.room.Gallery

/**
 * @see Image
 *
 *      - Class Image is a helper class that is holding all the image attributes like name and
 *      path and assigns them using it's constructor.
 *
 * @author Amirali Famili
 */
class Image {
    var imagePath:String?=null
    var imageName:String?=null

    /**
     * @see Image
     *
     *      - Image class's constructor for assigning name and path.
     *
     * @param imageName : name of the image
     * @param imagePath : path of the image
     */
    constructor(imagePath: String?, imageName: String?){
        this.imagePath = imagePath
        this.imageName = imageName
    }

    /**
     * @see Image
     *
     *      - Image class's default constructor for ease of access.
     */
    constructor(){}

}