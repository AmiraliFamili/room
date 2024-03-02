package com.example.room.pass;


/**
 * @see helper
 *
 *      - Class helper is a helper class which is used to create a static refrence to randomVal and randomChar methods
 *      to generate a random character for a strong password.
 *
 * @author Amirali Famili
 */
public class helper {

    /**
     *      - randomVal method generates a random numerical value with a given size and returns it.
     *
     * @param size : size of the final random value
     * @return a random numerical value
     */
    public static int randomVal(int size){
        double rand = Math.random();
        return (int) (rand * size);
    }

    /**
     *      - randomChar method generates a random character value with a given min size and  max size.
     *
     * @param min : min size
     * @param max : max size
     * @return a random character value
     */
    public static int randomChar(int min, int max){
        return randomVal(max-min + 1) + min;
    }
}
