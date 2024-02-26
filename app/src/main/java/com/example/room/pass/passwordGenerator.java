package com.example.room.pass;

import java.util.ArrayList;

public abstract class passwordGenerator {

    private static ArrayList<passwordGenerator> generator;

    public static void clearPass(){
        if (generator != null) {
            generator.clear();
        } else {
            generator = new ArrayList<>();
        }
    }

    public static boolean isEmpty(){
        return  generator.isEmpty();
    }

    public static void add(passwordGenerator passwrd){
        generator.add(passwrd);
    }

    public abstract String getChar();

    public static passwordGenerator getRandomGen(){

        if (generator == null){
            generator = new ArrayList<>();
            add(new lowerCase());
        }

        if (generator.size() == 1) {
            return  generator.get(0);
        }
        int randomIndx = helper.randomVal(generator.size());
        return generator.get(randomIndx);
    }

    public static String generateApassword(int passSize){
        StringBuilder passwrd = new StringBuilder();

        while (passSize !=0){
            passwrd.append(getRandomGen().getChar());
            passSize--;
        }

        return passwrd.toString();
    }


}
