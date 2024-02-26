package com.example.room.pass;

public class numericCase extends  passwordGenerator {
    private  static final char CHAR_0 = '0';
    private  static final char CHAR_9 = '0';


    @Override
    public String getChar(){
        return String.valueOf((char) (helper.randomChar(CHAR_0, CHAR_9)));
    }
}
