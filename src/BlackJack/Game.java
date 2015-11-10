package BlackJack;

import java.util.ArrayList;

public class Game() {
    
    // constants
    public static final int BLACKJACK = 21;
    public static final int USER_STARTING_MONEY = 1000_00;
    public static final int MIN_BET = 10_00;
    public static final int DEALER_POINT_LIMIT = 17;
    
    // private instance members
    private Shoe mShoe;
    private int mUserPoints;
    private int mDealerPoints;
    
    private ArrayList<Card> mDealerCards;
    private ArrayList<Card> mUserCards;
    
    private int mUserMoney;
    private int mUserBet;
    
    // constructor
    public Game() {
        mShoe = new Shoe();
        mUserPoints = 0;
        mDealerPoints = 0;
        mUserCards = new ArrayList<>();
        mDealerCards = new ArrayList<>();
        mUserMoney = USER_STARTING_MONEY;
        mUserBet = 0;
    }
    
    // get a new shoe of playing cards 
    public void replaceShoe() {
        mShoe = new Shoe();
    }
    
    // set the initial user bet
    public void createInitialUserBet() {
        mUserMoney -= MIN_BET;
        mUserBet = MIN_BET;
    }
    
    // deal initial cards 
    public void dealCards() {
        int iInitialCardsPerHand = 2;
        if (!shoe.cutCardReached() && !shoe.isEmpty())  // add check for cards remaining (small prob)
            for (int i = 0; i < iInitialCardsPerHand; i++) {
                dealUserCard();
                dealDealerCard();
            }
    }
    
    // double the current user bet
    public void doubleUserBet() {
        mUserMoney -= mUserBet;
        mUserBet += mUserBet;
    }
    
    // deal user card
    public void dealUserCard() {
        Card nextCard = shoe.playCard();
        mUserCards.add(nextCard);
        updatePoints();
    }
    
    // deal dealer card
    public void dealDealerCard() {
        Card nextCard = shoe.playCard();
        mDealerCards.add(shoe.playCard());
        updatePoints();
    }
    
    // check if dealer needs another card 
    public boolean dealerAtPointLimit() {
        boolean bOverLimit = mDealerPoints > DEALER_POINT_LIMIT
        boolean bHardSeventeen = (mDealerPoints == DEALER_POINT_LIMIT && !hasSoftSeventeen(mDealerCards)
        
        return bOverLimit || bHardSeventeen;
    }
    
    // NEEDS TO DIFFERENTIATE BETWEEN BLACKJACK AND 21 -> based on count of cards in hand
    // check for blackjack
    public boolean isBlackjack(int currentPoints) {
        if (currentPoints < 0)
            throw new IllegalArgumentException("currentPoints cannot be less than zero");
        return currentPoints == BLACKJACK;
    }
    
    // check for bust
    public boolean isBust(int currentPoints) {
        if (currentPoints < 0)
            throw new IllegalArgumentException("currentPoints cannot be less than zero");
        return currentPoints > BLACKJACK;
    }
    
    // check if dealer has more points
    public boolean dealerHasMorePoints() {
        return mDealerPoints >mUserPoints;
    }
    
    // check for tie 
    public boolean isPush() {
        return mDealerPoints == mUserPoints;
    }
    
    // pay user (for winning)
    public void payUser(boolean isBlackJackWinner) {
        int iUserPayment = mUserBet
        int iBJNum = 3;
        int iBJDen = 2;
        if (isBlackjack(mUserPoints))
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
    }
    
    // return the users bet
    public void returnBet() {
        mUserMoney+= mUserBet;
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
    public void updatePoints() {
        int iCardsInUserHand = mUserCards.size()
        int iUserAceCount = getUserAceCount();
        int iUserPoints = 0;
        
        for (int i = 0; i < iCardsInUserHand; i++)
            iUserPoints += mUserCards.get(i).getPointValue();
    
        for (int i = 0; i < iUserAceCount; i++)
            if (iUserPoints + 10 <= 21)
                iUserPoints += 10;
        
        mUserPoints = iUserPoints;
        
        int iCardsInDealerHand = mDealerCards.size()
        int iDealerAceCount = getDealerAceCount();
        int iDealerPoints = 0;
        
        for (int i = 0; i < iCardsInDealerHand; i++)
            iDealerPoints += mDealerCards.get(i).getPointValue();
    
        for (int i = 0; i < iDealerAceCount; i++)
            if (iDealerPoints + 10 <= 21)
                iDealerPoints += 10;
        
        mDealerPoints = iDealerPoints;
    }
    
    
    private int getUserAceCount() {
        int iCount = 0;
        for (Card c: mUserCards)
            if (c.isAce())
                iCount++;
        return iCount;
    }
    
    private int getDealerAceCount() {
        int iCount = 0;
        for (Card c: mDealerCards)
            if (c.isAce())
                iCount++;
        return iCount;
    }
    
    // get user points
    public int getUserPoints() {
        return mUserPoints;
    }

    // get dealer points
    public int getDealerPoints() {
        return mDealerPoints;
    }
    
    // check if soft seventeen (17 where Ace is cointing for 11 points)
    private boolean hasSoftSeventeen(ArrayList<Card> cards) {
        int iCardsInDealerHand = cards.size()
        int iDealerAceCount = getDealerAceCount();
        int iDealerBasePoints = 0;
        
        for (int i = 0; i < iCardsInDealerHand; i++)
            iDealerPoints += cards.get(i).getPointValue();
    
        return (mDealerPoints == 17) && (iDealerPoints < mDealerPoints);
    }

}