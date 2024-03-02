package com.example.room.pass;


/**
 * @see numericCase
 *
 *      - Class numericCase generates an Numerical character between '0' and '9'.
 *
 * @Note This class is using helper class to choose a random character
 *
 * @author Amirali Famili
 */
public class numericCase extends  passwordGenerator {
    private  static final char CHAR_0 = '0';
    private  static final char CHAR_9 = '9';


    /**
     *      - getChar method generates a random character and returns it.
     *
     * @return a character between '0' and '9'
     */
    @Override
    public String getChar(){
        return String.valueOf((char) (helper.randomChar(CHAR_0, CHAR_9)));
    }
}
