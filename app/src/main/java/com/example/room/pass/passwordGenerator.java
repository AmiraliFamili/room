package com.example.room.pass;

import java.util.ArrayList;

/**
 * @see passwordGenerator
 *
 *      - Class passwordGenerator is used for creating a strong password with random characters
 *
 * @author Amirali Famili
 */
public abstract class passwordGenerator {

    private static ArrayList<passwordGenerator> generator;

    /**
     *      - clearPass method assigns the password to an empty one
     */
    public static void clearPass(){
        if (generator != null) {
            generator.clear();
        } else {
            generator = new ArrayList<>();
        }
    }

    /**
     *      - isEmpty method checks if the password is empty
     *
     * @return true if it is and false if it is not
     */
    public static boolean isEmpty(){
        return  generator.isEmpty();
    }

    /**
     *      - add method adds a character to the final password
     */
    public static void add(passwordGenerator passwrd){
        generator.add(passwrd);
    }

    /**
     *      - getChar method is an abstract method used for getting the character value implemented in
     *      passwordGN.
     *
     * @return a String as a password
     */
    public abstract String getChar();

    /**
     *      - getRandomGen method gets is used for returning an random index from a password.
     *
     * @return a character for the final password
     */
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

    /**
     *      - generateApassword method is generating the password using existing methods and a simple
     *      loop.
     * @return a string builder representing the final password
     */
    public static String generateApassword(int passSize) {
        StringBuilder passwrd = new StringBuilder();

        while (passSize != 0) {
            passwrd.append(getRandomGen().getChar());
            passSize--;
        }

        return passwrd.toString();
    }
}
