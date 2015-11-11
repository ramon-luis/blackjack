package yoda;

import java.util.Scanner;
import java.util.ArrayList;

/**
 * class to reverse the order of words in a sentence
 * console driven program -> user enters a sentence, program returns reversed words using iteration & recursion
 */


public class YodaSpeak {

    public static void main(String[] args) {

        // create scanner object to obtain user input from console & define variable to store user input
        Scanner scan = new Scanner(System.in);
        String sUserInput;

        // loop for user input
        while (true) {
            // print user prompt & store user input in variable
            System.out.print("Please enter a sentence (or E to exit):");
            sUserInput = scan.nextLine();

            // exit on E, otherwise print yoda speak from iterative and recursive methods
            if (sUserInput.equalsIgnoreCase("E"))
                break;
            else {
                System.out.println("yoda speak iterative: " + yodaIterative(sUserInput));
                System.out.println("yoda speak recursive: " + yodaRecursive(sUserInput));
                System.out.println();
            }
        }
    }

    // static method to reverse words in a sentence using iteration
    public static String yodaIterative(String sentence) {
        // translate the sentence string into a list of words
        ArrayList<String> sWords = getWordList(sentence);

        // store the size of the word list & create variable to store reversed sentence
        int iWordCount = sWords.size();
        String sReverse = "";

        // loop through word list, adding words in reverse order
        for (int i = 0; i < iWordCount; i++) {
            sReverse += sWords.get(iWordCount - i - 1) + " ";
        }

        // return reversed sentence with any extra whitespace removed
        return sReverse.trim();
    }

    // static method to reverse words in a sentence using recursion
    public static String yodaRecursive(String sentence) {
        // translate the sentence string into a list of words
        ArrayList<String> sWordList = getWordList(sentence);

        // store the size of the word list
        int iWordCount = sWordList.size();

        // create string from last word in current word list, then remove that word from the list
        String sReverse = sWordList.get(iWordCount - 1);
        sWordList.remove(iWordCount - 1);

        // call method recursively if there was more than one word in the list
        if (iWordCount > 1)
            sReverse = sReverse + " " + yodaRecursive(getSentence(sWordList));

        // otherwise return reversed sentence with any extra whitespace removed
        return sReverse.trim();
    }

    // helper method to create a list of words from a sentence as a single string
    private static ArrayList<String> getWordList(String sentence) {
        // create empty list and variable to store each word
        ArrayList<String> sWordList = new ArrayList<>();
        String sWord = "";

        // put all chars in sentence string into an array
        char[] letters = sentence.trim().toCharArray();

        // loop through char array
        // if char is not a space, then add the char to current word
        // otherwise, add the current word to the list & reset the current word to be empty
        for (char c : letters)
            if (c != ' ')
                sWord = sWord + c;
            else {
                sWordList.add(sWord);
                sWord = "";
            }

        // add the final word (no whitespace at end of sentence to trigger add at end of loop)
        sWordList.add(sWord);

        // return the list
        return sWordList;
    }

    // helper method to create a single string (sentence) from list of words
    private static String getSentence(ArrayList<String> wordList) {
        // define variable for sentence
        String sSentence = "";

        // loop through list and add each word with a space to the sentence string
        for (String word : wordList)
            sSentence += word + " ";

        // return the sentence with any extra whitespace removed
        return sSentence.trim();
    }

}