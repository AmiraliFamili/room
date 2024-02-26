package com.example.room.pass;

public class lowerCase extends passwordGenerator {
    private  static final char CHAR_A = 'a';
    private  static final char CHAR_Z = 'z';


    @Override
    public String getChar(){
        return String.valueOf((char) (helper.randomChar(CHAR_A, CHAR_Z)));
    }
}
