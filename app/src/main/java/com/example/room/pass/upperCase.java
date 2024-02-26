package com.example.room.pass;

public class upperCase extends passwordGenerator {
    private  static final char CHAR_A = 'A';
    private  static final char CHAR_Z = 'Z';


    @Override
    public String getChar(){
        return String.valueOf((char) (helper.randomChar(CHAR_A, CHAR_Z)));
    }
}
