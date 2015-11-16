package BlackJack;

import java.util.ArrayList;

/**
 * creates a Game object that is filled with a shoe of playing cards
 * Game object holds data for game play
 */

public class Game {

    // enum for players
    public enum Player {
        USER, DEALER
    }

    // constants
    public static final int TWENTY_ONE = 21;
    public static final int USER_STARTING_MONEY = 1000_00;  // stored as cents
    public static final int MIN_BET = 10_00;  // stored as cents
    public static final int DEALER_POINT_LIMIT = 17;  // dealer must have hard 17 in order to stop taking cards

    // private instance members
    private Shoe mShoe;  // shoe of multiple card decks -> contains the playing cards for game
    private ArrayList<Card> mDealerCards;  // list of dealer cards for a specific hand
    private int mDealerPoints;  // dealer points for a specific hand
    private ArrayList<Card> mUserCards;  // list of user cards for a specific hand
    private int mUserPoints;  // user points for a specific hand
    private int mUserMoney;  // bank roll for user stored as cents
    private int mUserBet;  // current bet for hand stored as cents
    private boolean mSurrender;  // boolean if user surrenders for current hand
    private Card mDealtCard; // stores last card that was dealt

    // constructor
    public Game() {
        mShoe = new Shoe();  // create new shoe of cards
        mDealerCards = new ArrayList<>();  // create empty list to store dealer cards for each hand
        mDealerPoints = 0;  // dealer starts with 0 points
        mUserCards = new ArrayList<>();  // create empty list to store player cards for each hand
        mUserPoints = 0;  // user starts with 0 points
        mUserMoney = USER_STARTING_MONEY;  // set starting money for user
        mUserBet = 0;  // user bet starts at 0
        mSurrender = false;  // user does not surrender unless active choice on a specific hand
        mDealtCard = null; // dealt card does not exit to start
    }


    //********************************************
    // MUTATOR METHODS BELOW - USED FOR GAME PLAY
    //********************************************

    // increase the amount of the current user bet
    public void increaseBet(int amount) {
        if (!isValidBet(amount))
            throw new IllegalArgumentException("invalid amount to bet");

        mUserMoney -= amount;  // subtract amount from user's money
        mUserBet += amount;  // add money to user's current bet
    }

    // reset the current user bet to 0 -> put the money back in their bankroll
    public void resetUserBet() {
        mUserMoney += mUserBet;
        mUserBet = 0;
    }

    // deal card from deck to a player
    public void dealCard(Player player) {
        if (player == null)
            throw new IllegalArgumentException("player cannot be null");

        // remove card from shoe, add card to players hand
        mDealtCard = mShoe.playCard();
        if (player == Player.USER)
            mUserCards.add(mDealtCard);
        else
            mDealerCards.add(mDealtCard);

        // update the player points
        updatePlayerPoints(player);
    }

    // switch boolean for user surrender to true
    public void surrender() {
        mSurrender = true;
    }

    // end current hand
    public void endHand() {
        // settle money based on outcome
        if (mSurrender)
            surrenderBet();  // return half user bet
        else if (isPush())
            returnBet();  // return the user bet
        else if (userWins())
            payUser();  // pay user

        // clear out hand variables
        clearUserBet(); // clear the user bet
        clearCurrentCards();  // clear user and dealer cards
        clearPlayerPoints();  // clear user and dealer points
    }

    // get a new shoe of playing cards
    public void replaceShoe() {
        mShoe = new Shoe();
    }


    //************************
    // ACCESSOR METHODS BELOW
    //************************

    // return the last card tht was dealt -> used by game to draw card
    public Card getLastDealtCard() {
        return mDealtCard;
    }

    // get amount of current user bet
    public int getCurrentUserBet() {
        return mUserBet;
    }

    // get amount of current user money (bankroll)
    public int getCurrentUserMoney() {
        return mUserMoney;
    }

    // get cards in dealer hand -> used in game play to turn over 1st dealer card at end of hand
    public ArrayList<Card> getDealerCards() {
        return mDealerCards;
    }

    // get points for a player
    public int getPoints(Player player) {
        if (player == null)
            throw new IllegalArgumentException("player cannot be null");
        return (player == Player.USER) ? mUserPoints : mDealerPoints;
    }

    // get number of cards in a players hand
    public int getCountOfCardsInHand(Player player) {
        if (player == null)
            throw new IllegalArgumentException("player cannot be null");
        return (player == Player.USER) ? mUserCards.size() : mDealerCards.size();
    }


    //*************************************************************
    // BOOLEAN METHODS BELOW - USED TO DRIVE DECISION IN GAME PLAY
    //*************************************************************

    // check if cut card has been reached
    public boolean cutCardReached() {
        return mShoe.cutCardReached();
    }

    // check if bet amount is valid -> needs to be more than zero and less than users available money
    public boolean isValidBet(int amount) {
        return amount > 0 && amount <= mUserMoney;
    }

    // check if bet amount exceeds table minimum
    public boolean minBetReached() {
        return mUserBet >= MIN_BET;
    }

    // check if user is out of money -> used by game to end current game
    public boolean userIsOutOfMoney() {
        return mUserMoney <= 0;
    }

    // check if dealer needs another card 
    public boolean dealerAtPointLimit() {
        boolean bOverLimit = mDealerPoints > DEALER_POINT_LIMIT;
        boolean bHardSeventeen = (mDealerPoints == DEALER_POINT_LIMIT && !dealerHasSofSeventeen());

        // dealer is must have score of hard 17 or greater
        return bHardSeventeen || bOverLimit;
    }

    // check if player surrendered
    public boolean playerHasSurrendered() {
        return mSurrender;
    }

    // check if player has blackjack and dealer does not -> used after initial deal
    public boolean playerOnlyBlackJack() {
        return playerHasBlackjack(Player.USER) && !playerHasBlackjack(Player.DEALER);
    }

    // check if dealer has blackjack and user does not -> used after initial deal
    public boolean dealerOnlyBlackJack() {
        return playerHasBlackjack(Player.DEALER) && !playerHasBlackjack(Player.USER);
    }

    // check for blackjack -> player has 21 points and only 2 cards
    public boolean playerHasBlackjack(Player player) {
        if (player == null)
            throw new IllegalArgumentException("player cannot be null");
        return playerHasTwentyOne(player) && (getCountOfCardsInHand(player) == 2);
    }

    // check for bust -> player has over 21 points
    public boolean playerHasBust(Player player) {
        if (player == null)
            throw new IllegalArgumentException("player cannot be null");
        return (player == Player.USER) ? mUserPoints > TWENTY_ONE : mDealerPoints > TWENTY_ONE;
    }

    // check if dealer wins -> user busts and dealer does not or dealer has more points
    public boolean dealerWins() {
        boolean bDealerUnderTwentyOneAndUserBust = !playerHasBust(Player.DEALER) && playerHasBust(Player.USER);
        boolean bDealerHasMorePoints = !playerHasBust(Player.DEALER) && !playerHasBust(Player.USER) && mDealerPoints > mUserPoints;
        return bDealerUnderTwentyOneAndUserBust || bDealerHasMorePoints;
    }

    // check if user wins -> dealer busts and user does not or user has more points
    public boolean userWins() {
        boolean bUserUnderTwentyOneAndDealerBust = !playerHasBust(Player.USER) && playerHasBust(Player.DEALER);
        boolean bUserHasMorePoints = !playerHasBust(Player.USER) && !playerHasBust(Player.DEALER) && mUserPoints > mDealerPoints;
        return bUserUnderTwentyOneAndDealerBust || bUserHasMorePoints;
    }

    // check for tie -> both players under 21 with same score or both players bust
    public boolean isPush() {
        boolean bUnderTwentyOne = (!playerHasBust(Player.DEALER) && !playerHasBust(Player.USER));
        boolean bSamePoints = (mDealerPoints == mUserPoints);
        boolean bDealerAndUserBust = (playerHasBust(Player.DEALER) && playerHasBust(Player.USER));
        return (bUnderTwentyOne && bSamePoints) || bDealerAndUserBust;
    }


    //******************************
    // PRIVATE HELPER METHODS BELOW
    //******************************

    // clear the current cards for user and dealer
    private void clearCurrentCards() {
        mUserCards.clear();
        mDealerCards.clear();
    }

    // reset player points to 0
    private void clearPlayerPoints() {
        mDealerPoints = 0;
        mUserPoints = 0;
    }

    // pay user (for winning)
    private void payUser() {
        int iUserPayment = mUserBet;
        int iBJNum = 3;
        int iBJDen = 2;
        if (playerHasBlackjack(Player.USER))
            iUserPayment = iUserPayment * iBJNum / iBJDen;  // pay 1.5x on blackjack
        mUserMoney += mUserBet + iUserPayment;
    }

    // split the users bet between user and dealer (surrender)
    private void surrenderBet() {
        int iSplitDivisor = 2;
        mUserMoney = mUserMoney + mUserBet / iSplitDivisor;  // user gets 1/2 their money back
        mSurrender = false;  // reset the boolean variable after taking money
    }

    // return the users bet
    private void returnBet() {
        mUserMoney += mUserBet;
    }

    // clear the current user bet
    private void clearUserBet() {
        mUserBet = 0;
    }

    // update points -> call for each card dealt (ace value can change based on new cards)
    private void updatePlayerPoints(Player player) {
        if (player == null)
            throw new IllegalArgumentException("player cannot be null");

        // determine which cards to use based on player
        ArrayList<Card> playerCards = (player == Player.USER) ? mUserCards : mDealerCards;

        // store number of cards & number of aces in hand
        int iAceCount = getAceCount(player);

        // start point value at zero & define adjustment to make to ace values
        int iPoints = 0;
        int iIncreaseToValue = 10;  // default ace = 1, need to add 10 if want to use 11 for ace value

        // add points for all cards in hand (default point value for Ace is 1)
        for (Card c : playerCards)
            iPoints += c.getPointValue();

        // for all aces in hand, try to add 10 more points (i.e. ace value worth 11 if it won't break 21 for total)
        for (int i = 0; i < iAceCount; i++)
            if (iPoints + iIncreaseToValue <= TWENTY_ONE)
                iPoints += iIncreaseToValue;

        // update the points for appropriate player
        if (player == Player.USER)
            mUserPoints = iPoints;
        else
            mDealerPoints = iPoints;
    }

    // check if a player has scored 21 points
    private boolean playerHasTwentyOne(Player player) {
        if (player == null)
            throw new IllegalArgumentException("player cannot be null");
        return (player == Player.USER) ? mUserPoints == TWENTY_ONE : mDealerPoints == TWENTY_ONE;
    }

    // get the number of aces in a players hand -> used to determine value of aces in a hand
    private int getAceCount(Player player) {
        if (player == null)
            throw new IllegalArgumentException("player cannot be null");

        // determine which cards to use based on player
        ArrayList<Card> playerCards = (player == Player.USER) ? mUserCards : mDealerCards;

        // assume zero aces to start
        int iCount = 0;

        // loop through cards and increase count if ace
        for (Card c : playerCards)
            if (c.isAce())
                iCount++;

        return iCount;
    }

    // check if soft seventeen (point total 17 with at least one Ace counting for 11 points)
    private boolean dealerHasSofSeventeen() {
        // return false if dealer does not have 17 points
        if (mDealerPoints != DEALER_POINT_LIMIT)
            return false;
        else {
            // loop through cards in dealer hand and recalculate dealer points using only 1 for aces
            int iDealerPoints = 0;
            for (Card c : mDealerCards)
                iDealerPoints += c.getPointValue();

            // return true if sum of points using only 1 for aces does not match current dealer point total
            // any difference implies that dealer points is applying 11 for an ace, which means soft 17
            return iDealerPoints < mDealerPoints;
        }
    }

}