package com.example.room.pass;

/**
 * @see specialCase
 *
 *      - Class specialCase generates an Special character.
 *
 * @Note This class is using helper class to choose a random character
 *
 * @author Amirali Famili
 */
public class specialCase extends passwordGenerator {
    private static final char[] SPECIAL_CHARS = "?.'/|!@£$%^&*()-_=+}{;:~`¡€#¢∞§¶•ªº–≠[]><≠–“‘«æ…¬˚∆æ≥≤œ∑´®†¥¨^øπåß∂ƒ©˙∆˚¬`Ω≈ç√∫~µ".toCharArray();

    /**
     *      - getChar method generates a random character and returns it.
     *
     * @return a random special character
     */
    @Override
    public String getChar(){
        return String.valueOf(SPECIAL_CHARS[helper.randomChar(0, SPECIAL_CHARS.length -1)]);
    }
}
