package BlackJack;

/**
 * creates a playing Card object with suit and face 
 * code is based on lab provided in class
 */
 
public class Card {

    //  enum for Suit
    public static enum Suit {
        CLUBS('C'), DIAMONDS('D'), HEARTS('H'), SPADES('S');
        private char value;

        //  private constructor
        private Suit(char value) {
            this.value = value;
        }
        
        // get value of the enum
        public char getValue(){
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
    
    // public accessor -> face
    public char getFace() {
        return mFace;
    }

    // public accessor -> suit
    public Suit getSuit() {
        return mSuit;
    }
    
    public int getPointValue() {
        int iValue = Character.getNumericValue(mFace);
        if (mFace == 'A')
            iValue = 1;
        else if (!Character.isNumeric(mFace))
            iValue = 10;
        return iValue;
    }
    
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