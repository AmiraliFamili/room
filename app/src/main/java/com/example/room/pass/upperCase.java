package com.example.room.pass;

/**
 * @see upperCase
 *
 *      - Class upperCase generates an Uppercase character between 'A' and 'Z'.
 *
 * @Note This class is using helper class to choose a random character
 *
 * @author Amirali Famili
 */
public class upperCase extends passwordGenerator {
    private  static final char CHAR_A = 'A';
    private  static final char CHAR_Z = 'Z';


    /**
     *      - getChar method generates a random character and returns it.
     *
     * @return a character between 'A' and 'Z'
     */
    @Override
    public String getChar(){
        return String.valueOf((char) (helper.randomChar(CHAR_A, CHAR_Z)));
    }
}
