package BlackJack;

import java.util.ArrayList;

public class Game {

    // enum for player options
    public enum Player {
        USER, DEALER
    }


    // constants
    public static final int TWENTY_ONE = 21;
    public static final int USER_STARTING_MONEY = 1000_00;  // stored as cents
    public static final int MIN_BET = 10_00;  // stored as cents
    public static final int DEALER_POINT_LIMIT = 17;

    // private instance members
    private Shoe mShoe;  // shoe of multiple card decks -> contains the playing cards for game
    private int mUserPoints;  // user points for a specific hand
    private int mDealerPoints;  // dealer points for a specific hand
    private ArrayList<Card> mDealerCards;  // list of dealer cards for a specific hand
    private ArrayList<Card> mUserCards;  // list of user cards for a specific hand
    private int mUserMoney;  // stored as cents
    private int mUserBet;  // stored as cents
    private boolean mSurrender;
    private Card mDealtCard; // stores last card that was dealt


    // constructor
    public Game() {
        mShoe = new Shoe();  // create new shoe of cards
        mUserPoints = 0;  // user starts with 0 points
        mDealerPoints = 0;  // dealer starts with 0 points
        mUserCards = new ArrayList<>();  // create empty list to store player cards for each hand
        mDealerCards = new ArrayList<>();  // create empty list to store dealer cards for each hand
        mUserMoney = USER_STARTING_MONEY;  // set starting money for user
        mUserBet = 0;  // user bet starts at 0
        mSurrender = false;
        mDealtCard = null;
    }

    // get a new shoe of playing cards 
    public void replaceShoe() {
        mShoe = new Shoe();
    }

    // check if bet amount is valid -> needs to be more than min bet and less than users available money
    public boolean isValidBet(int amount) {
        return amount > 0 && amount < mUserMoney;

    }

    public boolean minBetReached() {
        return mUserBet >= MIN_BET;
    }

    // set the initial user bet
    public void increaseBet(int amount) {
        if (!isValidBet(amount))
            throw new IllegalArgumentException("invalid amount to bet");

        mUserMoney -= amount;  // subtract amount from user's money
        mUserBet += amount;  // add mount to user's current bet
    }

    public void resetUserBet() {
        mUserMoney += mUserBet;
        mUserBet = 0;
    }

    // deal initial cards
    public void deal() {
        int iInitialCardsPerPlayer= 2;
        if (mShoe.cutCardReached() || mShoe.isEmpty()) {
            replaceShoe();
            deal();
        } else {
            for (int i = 0; i < iInitialCardsPerPlayer; i++) {
                dealCard(Player.USER);
                dealCard(Player.DEALER);
            }
        }
    }

    public void dealerTurn() {
        while (!dealerAtPointLimit()) {
            dealCard(Player.DEALER);  // move to table for delayed action?
        }
    }

    public void doubleDown() {
        if (isValidBet(mUserBet)) {
            increaseBet(mUserBet);
            dealCard(Player.USER);
            dealerTurn();
        }
    }

    public void endHand() {
        if (mSurrender)
            surrenderBet();
        else if (isPush())
            returnBet();
        else if (dealerWins())
            collectBet();
        else if (userWins())
            payUser();

        clearUserBet();
        clearCurrentCards();
    }

    public void surrender() {
        mSurrender = true;
    }

    public Card getLastDealtCard() {
        return mDealtCard;
    }

    public void dealCard(Player player) {
        if (player == null)
            throw new IllegalArgumentException("player cannot be null");

        mDealtCard = mShoe.playCard();
        if (player == Player.USER)
            mUserCards.add(mDealtCard);
        else
            mDealerCards.add(mDealtCard);
        updatePlayerPoints(player);
    }

    // check if dealer needs another card 
    public boolean dealerAtPointLimit() {
        boolean bOverLimit = mDealerPoints > DEALER_POINT_LIMIT;
        boolean bHardSeventeen = (mDealerPoints == DEALER_POINT_LIMIT && !dealerHasSofSeventeen());

        // dealer is must have score of hard 17 or greater
        return bHardSeventeen || bOverLimit;
    }

    public boolean playerOnlyBlackJack() {
        return playerHasBlackjack(Player.USER) && !playerHasBlackjack(Player.DEALER);
    }

    public boolean dealerOnlyBlackJack() {
        return playerHasBlackjack(Player.DEALER) && !playerHasBlackjack(Player.USER);
    }

    public boolean bothPlayersBlackJack() {
        return playerHasBlackjack(Player.USER) && playerHasBlackjack(Player.DEALER);
    }

    // check for blackjack -> player has 21 points and only 2 cards
    public boolean playerHasBlackjack(Player player) {
        if (player == null)
            throw new IllegalArgumentException("player cannot be null");
        return playerHasTwentyOne(player) && (countOfCardsInHand(player) == 2);
    }


    // check if a player has scored 21
    public boolean playerHasTwentyOne(Player player) {
        if (player == null)
            throw new IllegalArgumentException("player cannot be null");
        return (player == Player.USER) ? mUserPoints == TWENTY_ONE : mDealerPoints == TWENTY_ONE;
    }

    // check for bust
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

    // pay user (for winning)
    public void payUser() {
        int iUserPayment = mUserBet;
        int iBJNum = 3;
        int iBJDen = 2;
        if (playerHasBlackjack(Player.USER))
            iUserPayment = iUserPayment * iBJNum / iBJDen;
        mUserMoney += iUserPayment;
    }

    // take users bet (for losing)
    public void collectBet() {
        mUserMoney = mUserMoney - mUserBet;
    }

    // split the users bet between user and dealer (surrender)
    public void surrenderBet() {
        int iSplitDivisor = 2;
        mUserMoney = mUserMoney - mUserBet / iSplitDivisor;
        mSurrender = false;
    }

    // return the users bet
    public void returnBet() {
        mUserMoney += mUserBet;
    }

    // clear the current cards for user and dealer
    public void clearCurrentCards() {
        mUserCards.clear();
        mDealerCards.clear();
    }

    // clear the current user bet
    public void clearUserBet() {
        mUserBet = 0;
    }

    // update points -> call for each card dealt (ace value can change based on new cards)
    public void updatePlayerPoints(Player player) {
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

        // for all aces in hand, try to add 10 more points (try to use ace value 11 if it won't break 21 for total)
        for (int i = 0; i < iAceCount; i++)
            if (iPoints + iIncreaseToValue <= TWENTY_ONE)
                iPoints += iIncreaseToValue;

        // update the points for appropriate player
        if (player == Player.USER)
            mUserPoints = iPoints;
        else
            mDealerPoints = iPoints;
    }

    public int getCurrentUserBet() {
        return mUserBet;
    }

    public int getCurrentUserMoney() {
        return mUserMoney;
    }

    public ArrayList<Card> getUserCards() {
        return mUserCards;
    }

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
    public int countOfCardsInHand(Player player) {
        if (player == null)
            throw new IllegalArgumentException("player cannot be null");
        return (player == Player.USER) ? mUserCards.size() : mDealerCards.size();
    }

    // get the number of aces in a players hand
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