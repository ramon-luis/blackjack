package BlackJack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

/**
 * creates a shoe object that is filled with playing card decks
 * shoe object can be used in blackjack game to acccess cards
 */

public class Shoe {

    // constants for number of decks in shoe and relative position of cut card
    public static final int MAX_CARD_DECKS_IN_SHOE = 6;
    public static final int MIN_PERCENT_FOR_CUT_CARD = 90;
    public static final int MAX_PERCENT_FOR_CUT_CARD = 95;

    // private instance variables
    private ArrayList<Card> mCardsInShoe;
    private int mCutCardPosition;

    // constructor
    public Shoe() {
        // ORDER MATTERS!
        mCardsInShoe = new ArrayList<>();  // create an empty list to store the cards in the shoe
        fillShoe();  // add decks of cards to the shoe
        shuffle();  // shuffle the shoe;
        mCutCardPosition = getCutCardPosition();  // place cut card in shuffled shoe
    }

    // place the cards in the shoe in random order
    public void shuffle() {
        Collections.shuffle(mCardsInShoe);
    }

    // plays the next ("top") card in the shoe
    public Card playCard() {
        Card topCard = getTopCard();  // store the top card before it's removed from the deck
        removeTopCard();  // remove the top card from the deck
        return topCard;  // return the card that was previously the top card in the deck
    }

    // check if the cut card has been reached in the shoe (if more cards than cut position, then card has not been reached yet)
    public boolean cutCardReached() {
        return countOfRemainingCards() < mCutCardPosition;
    }

    // check if all cards gone from shoe
    public boolean isEmpty() {
        return countOfRemainingCards() == 0;
    }

    // toString
    @Override
    public String toString() {
        String sCards = "Cards in shoe:\n";

        // loop through all cards in deck and add string of each card
        for (Card c : mCardsInShoe)
            sCards += c.toString() + "\n";

        return sCards;
    }

    
    //**********************
    // HELPER METHODS BELOW
    //**********************

    // fill the shoe with cards 
    private void fillShoe() {
        // create a new card deck
        CardDeck deck = new CardDeck();

        // loop for max number of decks in shoe: add each card in deck to shoe
        for (int i = 0; i < MAX_CARD_DECKS_IN_SHOE; i++)
            for (Card c : deck.getCards())
                mCardsInShoe.add(c);
    }

    // returns the top (first) card in the shoe
    private Card getTopCard() {
        return mCardsInShoe.get(0);
    }

    // removes the top (first) from the shoe
    private void removeTopCard() {
        mCardsInShoe.remove(0);
    }

    // get the number of cards remaining in shoe
    private int countOfRemainingCards() {
        return mCardsInShoe.size();
    }

    // generate the cut card (random mark in deck to determine when shoe is replaced)
    private int getCutCardPosition() {
        // http://stackoverflow.com/questions/363681/generating-random-integers-in-a-range-with-java
        int iMinPosition = countOfRemainingCards() * MIN_PERCENT_FOR_CUT_CARD / 100;  // min position is max percent in shoe
        int iMaxPosition = countOfRemainingCards() * MAX_PERCENT_FOR_CUT_CARD / 100;  // max position is min percent in shoe
        return ThreadLocalRandom.current().nextInt(iMinPosition, iMaxPosition + 1);  // int is measured from end of deck
    }

}