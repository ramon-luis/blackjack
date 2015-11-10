package BlackJack;

import java.util.ArrayList;

/**
 *  creates a card deck object that is filled with playing cards 
 *  card deck object can be used by a shoe object to fill itself with decks of playing cards
 */
 
public class CardDeck {
    
    // private instance variable
    private ArrayList<Card> mCardsInDeck;
    
    // constructor
    public CardDeck() {
        mCardsInDeck = new ArrayList<>();  // create an empty list
        addAllValidCards();  // fill the list with cards
    }
    
    // get the cards in the deck as a list
    public ArrayList<Card> getCards() {
        return mCardsInDeck;
    }
    
    // toString
    @Override
    public String toString() {
        String sCards = "Cards in deck:\n";
        
        // loop through all cards in deck and add string of each card
        for (Card c : mCardsInDeck)
            sCards += c.toString() + "\n";
        
        return sCards;
    }
    
    // add each type of card to the deck
    private void addAllValidCards() {
        // define the card suits as an array
        Card.Suit[] validSuits = Card.Suit.values();
        
        // loop through suit and value, add card of each combination to deck
        for (Card.Suit s: validSuits)
            for (char c: Card.VALID_FACES)
                mCardsInDeck.add(new Card(c,s));
    }
    
}