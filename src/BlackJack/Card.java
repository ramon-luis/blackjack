package BlackJack;

/**
 * creates a playing Card object with suit and face
 * code is based on lab provided in class
 */

public class Card {

    //  enum for Suit
    public enum Suit {
        // enum values
        CLUBS('C'), DIAMONDS('D'), HEARTS('H'), SPADES('S');

        // instance variable
        private char value;

        //  private constructor
        Suit(char value) {
            this.value = value;
        }

        // get value of the enum
        public char getValue() {
            return value;
        }

    }

    // array of valid face chars 
    public static final char[] VALID_FACES = {'2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K', 'A'};

    //  instance members
    private char mFace;
    private Suit mSuit;

    //  constructor
    public Card(char face, Suit suit) {
        if (!validFace(face))
            throw new IllegalArgumentException("invalid face");
        if (suit == null)
            throw new IllegalArgumentException("suit cannot be null");

        mFace = face;
        mSuit = suit;
    }

    // get the point value for a card
    public int getPointValue() {
        // get the java defined numeric value
        int iValue = Character.getNumericValue(mFace);

        // update the numeric value of the face is a letter (T, J, Q, K, A)
        if (mFace == 'A')
            iValue = 1;  // default value for ace is 1 point -> game play will increase to 10 when possible
        else if (!Character.isDigit(mFace))
            iValue = 10;  // all other face cards (T, J, Q, K, A) are worth 10 points
        return iValue;
    }

    // check if a card is an ace -> used in game play to determine if card should count for 11 instead of 1
    public boolean isAce() {
        return mFace == 'A';
    }

    // toString
    @Override
    public String toString() {
        return String.valueOf(mFace) + mSuit.getValue();
    }

    // private helper to validate face char
    private boolean validFace(char face) {
        // loop through valid chars, return true if there is a match
        for (char c : VALID_FACES)
            if (c == face)
                return true;
        return false;
    }

}