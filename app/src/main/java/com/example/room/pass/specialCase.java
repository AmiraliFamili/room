package com.example.room.pass;

public class specialCase extends passwordGenerator {
    private static final char[] SPECIAL_CHARS = "?.'/|!@£$%^&*()-_=+}{;:~`¡€#¢∞§¶•ªº–≠[]><≠–“‘«æ…¬˚∆æ≥≤œ∑´®†¥¨^øπåß∂ƒ©˙∆˚¬`Ω≈ç√∫~µ".toCharArray();

    @Override
    public String getChar(){
        return String.valueOf(SPECIAL_CHARS[helper.randomChar(0, SPECIAL_CHARS.length -1)]);
    }
}
