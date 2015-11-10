package yoda;

import java.util.ArrayList;

public class YodaSpeak {


    public static void main(String[] args) {
        String test1 = "the force is strong with you";
        String test2 = "the cubs lost to the mets";


        System.out.println(test1);
        System.out.println("iter: " + yodaIterative(test1));
        System.out.println("recur: " + yodaRecursive(test1));

        System.out.println();
        System.out.println(test2);
        System.out.println("iter: " + yodaIterative(test2));
        System.out.println("recur: " + yodaRecursive(test2));

    }


    public static String yodaIterative(String sentence) {
        ArrayList<String> sWords = getWordList(sentence);
        int iWordCount = sWords.size();
        String sReverse = "";

        for (int i = 0; i < iWordCount; i++) {
            sReverse += sWords.get(iWordCount - i - 1) + " ";
        }
        return sReverse.trim();
    }

    public static String yodaRecursive(String sentence) {
        ArrayList<String> sWordList = getWordList(sentence);
        int iWordCount = sWordList.size();
        String sReverse = "";

        sReverse = sReverse + sWordList.get(iWordCount - 1);

        if (iWordCount == 1)
            return sReverse;
        else {
            sWordList.remove(iWordCount - 1);
            sReverse = sReverse + " " + yodaRecursive(getSentence(sWordList));
        }
        return sReverse.trim();
    }

    private static ArrayList<String> getWordList(String sentence) {
        ArrayList<String> sWordList = new ArrayList<>();
        String sWord = "";

        char[] letters = sentence.trim().toCharArray();

        for (char c : letters)
            if (c != ' ')
                sWord = sWord + c;
            else {
                sWordList.add(sWord);
                sWord = "";
            }
        sWordList.add(sWord);
        return sWordList;
    }

    private static String getSentence(ArrayList<String> wordList) {
        String sSentence = "";
        for (String word : wordList)
            sSentence += word + " ";
        return sSentence.trim();
    }


}