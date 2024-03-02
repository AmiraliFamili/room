package com.example.room.pass;

/**
 * @see lowerCase
 *
 *      - Class lowerCase generates an Lower Case character between 'a' and 'z'.
 *
 * @Note This class is using helper class to choose a random character
 *
 * @author Amirali Famili
 */
public class lowerCase extends passwordGenerator {
    private  static final char CHAR_A = 'a';
    private  static final char CHAR_Z = 'z';


    /**
     *      - getChar method generates a random character and returns it.
     *
     * @return a character between 'a' and 'z'
     */
    @Override
    public String getChar(){
        return String.valueOf((char) (helper.randomChar(CHAR_A, CHAR_Z)));
    }
}
