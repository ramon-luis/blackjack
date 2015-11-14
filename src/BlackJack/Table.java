package BlackJack;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import BlackJack.Game.Player;
import java.util.TimerTask;
import java.util.Timer;

/**
 * table object to display blackjack game
 * uses Game class to help handle workflow
 */


// TO DO LIST:
    
    // refactor code -> clean up and simplify
    // disable buttons instead of hiding them - MAYBE?

public class Table extends JFrame {

    // enum to determine which info message to display
    public enum Outcome {CLEAR, MIN_BET, FUNDS, SURRENDER, WIN_WITH_BJ, LOSE_TO_BJ, WIN, LOSE, PUSH, BUST, GAME_OVER}
    
    // constants
    public static final int PADDING = 5;  // margin padding
    public static final int DELAY = 250;  // delay for dealer cards in milliseconds
    
    // private member: Game object
    private Game mGame;  // game object
    
    // private members: labels
    private JLabel mUserMoneyLabel;  // display user money available to bet
    private JLabel mUserBetLabel;  // display current user bet amount
    private JLabel mMinBetLabel;  // display minimum bet
    private JLabel mInfoLabel;  // display useful info to user
    private JLabel mShoeLabel; // display updates on shoe, if necessary
    private JLabel mDealerCardsLabel;
    private JLabel mUserCardsLabel;
    
    // private members: buttons
    private JButton mResetBetButton;  // reset the current user bet
    private JButton mBetFiveButton;  // increase bet $5
    private JButton mBetTenButton;  // increase bet $10
    private JButton mBetTwentyFiveButton;  // increase bet $25
    private JButton mBetOneHundredButton;  // increase bet $100
    private JButton mBetFiveHundredButton;  // increase bet $500
    private JButton mDealButton;  // deal initial cards
    private JButton mStayButton;  // stay
    private JButton mHitButton;  // hit
    private JButton mDoubleButton;  // double down
    private JButton mSurrenderButton;  // surrender
    private JButton mNextHandButton;  // continue play -> play next hand
    private JButton mNewGameButton;  // new game -> start over
    
    // private members: panels
    private JPanel mBettingPanel;  // panel with betting buttons
    private JPanel mLowerPanel;  // panel with labels
    private JPanel mPlayPanel;  // panel for intragame action buttons (hit, stay, etc.)
    private JPanel mLabelPanel;
    private JPanel mButtonPanel;
    private JPanel mActionPanel;
    private JPanel mUserCardDisplay;  // panel for user cards
    private JPanel mDealerCardDisplay; // panel for dealer cards
    private JPanel mCardPanel;  // panel to display cards

    // constructor
    public Table() {
        mGame = new Game();  // new game object
        createLabels();  // create labels
        createButtons();  // create buttons
        createMainPanel();  // create the main panel
    }

    // create the main panel for the table
    private void createMainPanel() {
        // create panel, add padding, and set layout
        JPanel mMainPanel = new JPanel();
        addPadding(mMainPanel);
        mMainPanel.setLayout(new BorderLayout());
        
        // create sub panels
        createCardPanel();
        createLowerPanel();

        // add sub panels to main panel
        mMainPanel.add(mShoeLabel, BorderLayout.PAGE_START);
        mMainPanel.add(mLowerPanel,BorderLayout.PAGE_END);
        mMainPanel.add(mCardPanel, BorderLayout.CENTER);

        // update what items should be active
        hideIntraGameActions();
        hideNextHand();
        
        // add the main panel to the frame (Table)
        this.add(mMainPanel);
    }
    
    // create panel to display cards
    private void createCardPanel() {
        // create panels for user cards, dealer cards, and a panel to hold both
        mCardPanel = new JPanel();
        addPadding(mCardPanel);
        mDealerCardDisplay = new JPanel();
        mUserCardDisplay = new JPanel();
        
        // set the color for card table: brown outline, green surface
        mCardPanel.setBackground(new Color(65, 45, 28));
        mUserCardDisplay.setBackground(new Color(21, 93, 43));
        mDealerCardDisplay.setBackground(new Color(21, 93, 43));

        // add padding to individual card displays
        addPadding(mUserCardDisplay);
        addPadding(mDealerCardDisplay);



        mDealerCardDisplay.setLayout(new BorderLayout());
        mDealerCardDisplay.add(mDealerCardsLabel, BorderLayout.PAGE_START);
        mUserCardDisplay.setLayout(new BorderLayout());
        mUserCardDisplay.add(mUserCardsLabel, BorderLayout.PAGE_START);

        // set the layout and add the panels for user and dealer cards
        mCardPanel.setLayout(new GridLayout(2,1));
        mCardPanel.add(mDealerCardDisplay);
        mCardPanel.add(mUserCardDisplay);

    }

    // create panel for labels and buttons
    private void createLowerPanel() {
        // create panel, add padding, set layout
        mLowerPanel = new JPanel();
        addPadding(mLowerPanel);
        mLowerPanel.setLayout(new BorderLayout());

        // create sub panels
        createLabelPanel();
        createButtonPanel();

        // add components to panel
        mLowerPanel.add(mInfoLabel, BorderLayout.PAGE_START);
        mLowerPanel.add(mLabelPanel, BorderLayout.LINE_START);
        mLowerPanel.add(mButtonPanel, BorderLayout.CENTER);
    }

    private void createButtonPanel() {
        mButtonPanel = new JPanel();
        mButtonPanel.setLayout(new GridLayout(3,1));

        // create sub-panels
        createPlayPanel();
        createBettingPanel();
        createActionPanel();

        // add sub-panels
        mButtonPanel.add(mBettingPanel);
        mButtonPanel.add(mPlayPanel);
        mButtonPanel.add(mActionPanel);
    }

    // create panel for betting buttons
    private void createBettingPanel() {
        // create panel, add padding, set layout
        mBettingPanel = new JPanel();
        addPadding(mBettingPanel);
        mBettingPanel.setLayout(new GridLayout(1,6));

        // add betting buttons to panel
        mBettingPanel.add(mResetBetButton);
        mBettingPanel.add(mBetFiveButton);
        mBettingPanel.add(mBetTenButton);
        mBettingPanel.add(mBetTwentyFiveButton);
        mBettingPanel.add(mBetOneHundredButton);
        mBettingPanel.add(mBetFiveHundredButton);
    }

    // create panel for intra-game play
    private void createPlayPanel() {
        // create panel, add padding, set layout
        mPlayPanel = new JPanel();
        addPadding(mPlayPanel);
        mPlayPanel.setLayout(new GridLayout(1,4));
        
        // add buttons for intra-game play
        mPlayPanel.add(mHitButton);
        mPlayPanel.add(mStayButton);
        mPlayPanel.add(mDoubleButton);
        mPlayPanel.add(mSurrenderButton);
    }

    private void createActionPanel() {
        // create panel, add padding, set layout
        mActionPanel = new JPanel();
        addPadding(mActionPanel);
        mActionPanel.setLayout(new GridLayout(1,3));

        // add buttons for additional actions
        mActionPanel.add(mDealButton);
        mActionPanel.add(mNextHandButton);
        mActionPanel.add(mNewGameButton);
    }

    private void createLabelPanel() {
        // create panel, add padding, set layout
        mLabelPanel = new JPanel();
        addPadding(mLabelPanel);
        mLabelPanel.setLayout(new GridLayout(3,1));

        // add labels to panel
        mLabelPanel.add(mUserBetLabel);
        mLabelPanel.add(mMinBetLabel);
        mLabelPanel.add(mUserMoneyLabel);
    }


    // create labels
    private void createLabels() {
        // create labels for the card panels, set color to white
        mDealerCardsLabel = new JLabel("Dealer Cards");
        mUserCardsLabel = new JLabel("Your Cards");
        mDealerCardsLabel.setForeground(new Color(255, 255, 255));
        mUserCardsLabel.setForeground(new Color(255, 255, 255));

        // create min bet label -> fixed label that does not update
        String sMinBet = String.format("Minimum Bet: $%,d", Game.MIN_BET / 100);
        mMinBetLabel = new JLabel(sMinBet);
        
        // create dynamic labels (points, money, bet, info)
        mShoeLabel = new JLabel(" ");
        mUserMoneyLabel = new JLabel();
        mUserBetLabel = new JLabel();
        mInfoLabel = new JLabel(" ");  // create with empty space so that it reserves space in JPanel
        mInfoLabel.setForeground(new Color(165, 11, 22));

        // update the display for dynamic labels
        updateDisplayLabels();
    }

    // create all buttons
    private void createButtons() {
        // text for betting buttons
        String sResetBetButtonText = "Reset Bet";
        String sFiveButtonText = "Bet $5";
        String sTenButtonText = "Bet $10";
        String sTwentyFiveButtonText = "Bet $25";
        String sOneHundredButtonText = "Bet $100";
        String sFiveHundredButtonText = "Bet $500";

        // create buttons for betting
        mResetBetButton = new JButton(sResetBetButtonText);
        mBetFiveButton = new JButton(sFiveButtonText);
        mBetTenButton = new JButton(sTenButtonText);
        mBetTwentyFiveButton = new JButton(sTwentyFiveButtonText);
        mBetOneHundredButton = new JButton(sOneHundredButtonText);
        mBetFiveHundredButton = new JButton(sFiveHundredButtonText);

        // text for player action buttons
        String sDealButtonText = "deal";
        String sStayButtonText = "stay";
        String sHitButtonText = "hit";
        String sDoubleButtonText = "double down";
        String sSurrenderButtonText = "surrender";

        // create buttons for player actions
        mDealButton = new JButton(sDealButtonText);
        mStayButton = new JButton(sStayButtonText);
        mHitButton = new JButton(sHitButtonText);
        mDoubleButton = new JButton(sDoubleButtonText);
        mSurrenderButton = new JButton(sSurrenderButtonText);

        // text for between hand action buttons
        String sNextHandButtonText = "start next hand";
        String sNewGameButtonText = "new game";

        // create buttons for between hand actions
        mNextHandButton = new JButton(sNextHandButtonText);
        mNewGameButton = new JButton(sNewGameButtonText);

        // create an action listener 
        ActionListener listener = new ButtonListener();
        
        // place all buttons in array -> loop and & add action listener to all buttons
        JButton[] allButtons = {mResetBetButton, mBetFiveButton, mBetTenButton, mBetTwentyFiveButton,
                mBetOneHundredButton, mBetFiveHundredButton, mDealButton, mStayButton, mHitButton,
                mDoubleButton, mSurrenderButton, mNextHandButton, mNewGameButton};
        for (JButton button : allButtons)
            button.addActionListener(listener);
    }

    // create action listener for buttons -> single listener with branched logic for action
    private class ButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // clear current info
            updateInfoLabel(Outcome.CLEAR);

            // get the source of the event (identify the button)
            Object source = e.getSource();

            // select action based on source button
            if (source == mResetBetButton) {
                resetBet();
            } else if (source == mBetFiveButton) {
                increaseBet(5_00);
            } else if (source == mBetTenButton) {
                increaseBet(10_00);
            } else if (source == mBetTwentyFiveButton) {
                increaseBet(25_00);
            } else if (source == mBetOneHundredButton) {
                increaseBet(100_00);
            } else if (source == mBetFiveHundredButton) {
                increaseBet(500_00);
            } else if (source == mDealButton) {
                deal();
            } else if (source == mStayButton) {
                stay();
            } else if (source == mHitButton) {
                hit();
            } else if (source == mDoubleButton) {
                doubleDown();
            } else if (source == mSurrenderButton) {
                surrender();
            } else if (source == mNextHandButton) {
                nextHand();
            } else if (source == mNewGameButton) {
                newGame();
            }

        }
    }


    // reset the user bet
    private void resetBet() {
        mGame.resetUserBet();
        updateDisplayLabels();
    }
   
    // increase user bet
    private void increaseBet(int amount) {
        if (amount < 0)
                throw new IllegalArgumentException("amount cannot be less than zero");
        
        // if player has enough money, then increase the bet
        // otherwise, display message not enough funds
        if (mGame.isValidBet(amount))
            mGame.increaseBet(amount);
        else
            updateInfoLabel(Outcome.FUNDS);
        
        // update the display labels
        updateDisplayLabels();
    }

    // deal initial cards
    public void deal() {
        if (!mGame.minBetReached())
            updateInfoLabel(Outcome.MIN_BET);
        else {
            // update the display to hide betting buttons and show intragame buttons
            hideBettingActions();
            showIntraGameActions();

            // replace the shoe if needed
            replaceShoeIfCutCard();

            // user first card
            mGame.dealCard(Player.USER);
            drawCard(Player.USER, mGame.getLastDealtCard(), mGame.countOfCardsInHand(Player.USER));
            checkForCutCard();
            updateUserPointsLabel();

            // dealer first card
            mGame.dealCard(Player.DEALER);
            drawCard(Player.DEALER, mGame.getLastDealtCard(), mGame.countOfCardsInHand(Player.DEALER));
            checkForCutCard();

            // user second card
            mGame.dealCard(Player.USER);
            drawCard(Player.USER, mGame.getLastDealtCard(), mGame.countOfCardsInHand(Player.USER));
            checkForCutCard();
            updateUserPointsLabel();

            // dealer second card
            mGame.dealCard(Player.DEALER);
            drawCard(Player.DEALER, mGame.getLastDealtCard(), mGame.countOfCardsInHand(Player.DEALER));
            checkForCutCard();

            // update display labels
            updateDisplayLabels();

            // check for blackjack
            if (mGame.playerOnlyBlackJack()) {
                updateInfoLabel(Outcome.WIN_WITH_BJ);
                hideIntraGameActions();
                updateDealerPointsLabel();
                showNextHand();
            } else if (mGame.dealerOnlyBlackJack()) {
                updateInfoLabel(Outcome.LOSE_TO_BJ);
                hideIntraGameActions();
                updateDealerPointsLabel();
                showNextHand();
            } else if (mGame.bothPlayersBlackJack()) {
                updateInfoLabel(Outcome.PUSH);
                hideIntraGameActions();
                updateDealerPointsLabel();
                showNextHand();
            }
        }
    }
    
    // hit - user takes a card
    private void hit() {
        // deal card
        mGame.dealCard(Player.USER);
        drawCard(Player.USER, mGame.getLastDealtCard(), mGame.countOfCardsInHand(Player.USER));
        updateUserPointsLabel();
        checkForCutCard();

        // update display labels, hide surrender & double buttons
        updateDisplayLabels();
        mSurrenderButton.setVisible(false);  // surrender only an option after initial deal
        mDoubleButton.setVisible(false);  // double only an option after initial deal
        // check for bust
        if (mGame.playerHasBust(Player.USER)) {
            updateInfoLabel(Outcome.BUST);
            hideIntraGameActions();
            updateDealerPointsLabel();
            showNextHand();
        }
    }

    private void checkForOutOfMoney() {
        if (mGame.userIsOutOfMoney()) {
            updateInfoLabel(Outcome.GAME_OVER);

            // hide all buttons except for new game
            hideBettingActions();
            hideIntraGameActions();
            hideNextHand();
        }
    }


    // stay -> user takes no more cards, dealer turn
    private void stay() {
        hideIntraGameActions();
        dealerTurn();
    }

    private void revealDealerCards() {
        drawCard(Player.DEALER, mGame.getDealerCards().get(0), 1);
        updateDealerPointsLabel();
    }


    private void displayHandOutcome() {
        if (mGame.dealerWins()) {
            updateInfoLabel(Outcome.LOSE);
        } else if (mGame.userWins()) {
            updateInfoLabel(Outcome.WIN);
        } else if (mGame.isPush()) {
            updateInfoLabel(Outcome.PUSH);
        }
    }


    // deal cards to dealer until at point limit (hard 17+)
    private void dealerTurn() {
        if (!mGame.dealerAtPointLimit()) {
            delayedCard(Player.DEALER);
        } else {
            displayHandOutcome();
            revealDealerCards();
            updateDealerPointsLabel();
            showNextHand();
        }



    }
    
    // double down -> user doubles bet and only takes 1 more card, then stay (dealers turn)
    private void doubleDown() {
        int iBetAmount = mGame.getCurrentUserBet();
        if (!mGame.isValidBet(iBetAmount)) {
            updateInfoLabel(Outcome.FUNDS);
        } else {
            increaseBet(iBetAmount);
            mGame.dealCard(Player.USER);
            drawCard(Player.USER, mGame.getLastDealtCard(), mGame.countOfCardsInHand(Player.USER));
            updateUserPointsLabel();
            checkForCutCard();
            updateDisplayLabels();
            stay();
        }
    }
    
    // surrender -> user gives up 1/2 bet, no more cards for user
    private void surrender() {
        mGame.surrender();
        updateInfoLabel(Outcome.SURRENDER);
        hideIntraGameActions();
        showNextHand();
    }
    
    // next hand -> end this hand and go to betting screen
    private void nextHand() {
        mGame.endHand();  // settle money, clear current bet & cards
        updateInfoLabel(Outcome.CLEAR);
        updateDisplayLabels();
        clearCardImages();
        clearUserPointsLabel();
        hideNextHand();
        showBettingActions();

        mShoeLabel.setText(" ");
    }
    // start a new game
    private void newGame() {
        // assign new Game to intance variable
        mGame = new Game();
        
        // clear out info label, card images, and reset displayed buttons
        updateInfoLabel(Outcome.CLEAR);
        updateDisplayLabels();
        clearCardImages();
        clearUserPointsLabel();
        hideNextHand();
        showBettingActions();
        this.repaint();
    }

    private void checkForCutCard() {
        if (mGame.cutCardReached())
            mShoeLabel.setText("CUT CARD REACHED: card shoe will be replaced after this hand");

    }

    private void replaceShoeIfCutCard() {
        if (mGame.cutCardReached()) {
            mShoeLabel.setText("NEW CARDS: card shoe has been replaced");
            mGame.replaceShoe();
        }
    }

    // remove all card images
    private void clearCardImages() {
        mUserCardDisplay.repaint();
        mDealerCardDisplay.repaint();
    }

    // draw image of playing card on table, source: lab code in class (updated) 
    private void drawCard(Player player, Card card, int cardNumber) {
        if (player == null || card == null)
            throw new IllegalArgumentException("player and card cannot be null");
        if (cardNumber < 1)
            throw new IllegalArgumentException("cardNumber cannot be less than zero");
        
        // create graphics object based on player (user vs. dealer)
        Graphics g = (player == Player.USER) ? mUserCardDisplay.getGraphics() : mDealerCardDisplay.getGraphics();

        // create image object for card -> use back of card if it is dealers first card
        boolean bDealerFirstCard = (player == Player.DEALER && mGame.countOfCardsInHand(Player.DEALER) == 1);
        String sCard = bDealerFirstCard ? "back" : card.toString();
        BufferedImage bufferedImage = generateBufferedImage("/src/BlackJack/cards/" + sCard + ".png");
        
        // scale image to fit
        int nWidth = 104;
        int nHeight = 152;
        bufferedImage = scaleImage(bufferedImage, nWidth, nHeight);

        // determine position of card
        int iXAdjustmentForOtherCards = 125 * (cardNumber - 1) + 5;
        int iXPosition = 5 + iXAdjustmentForOtherCards;
        // int iXPosition = (player == Player.DEALER && cardNumber > 1) ? 100 + iXAdjustmentForOtherCards : 5 + iXAdjustmentForOtherCards;
        int iYPosition = 25;

        // draw the card
        g.drawImage(bufferedImage, iXPosition, iYPosition, null);
    }

    // deal card on delay
    private void delayedCard(Player player) {

        // create timer object and schedule new anonymous TimerTask class
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            public void run() {
                timer.cancel();  // cancel timer -> one-time event
                mGame.dealCard(player);  // deal card to player
                drawCard(player, mGame.getLastDealtCard(), mGame.countOfCardsInHand(player));  // draw card on table
                updateUserPointsLabel();
                checkForCutCard();
                if (player == Player.DEALER) {
                    if (!mGame.dealerAtPointLimit())
                        dealerTurn();
                    else {
                        displayHandOutcome();
                        revealDealerCards();
                        updateDealerPointsLabel();
                        showNextHand();
                    }
                }
            }
        },DELAY);  // delay TimerTask before running
    }


    //***********************
    // HELPER METHODS BELOW
    //***********************

    // generate an image from a local file, source: lab code in class (updated) 
    private static BufferedImage generateBufferedImage(String relativeFilePath){
        if (relativeFilePath == null)
            throw new IllegalArgumentException("relativeFilePath cannot be null");
        
        // create file object to obtain image using path of user directory
        String sFilePathForImage= System.getProperty("user.dir")+ relativeFilePath;
        File imageFile = new File(sFilePathForImage);

        // store image in variable, set image to null & try to update image from file
        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // return the image
        return bufferedImage;
    }

    // scale an image to specific width & height, source: (updated) lab code in class
    private static BufferedImage scaleImage(BufferedImage image, int width, int height) {
        if (image == null)
            throw new IllegalArgumentException("image cannot be null");
        if (width < 0 || height < 0)
            throw new IllegalArgumentException("width and height cannot be less than zero");

        // create a new BufferedImage object and Graphics2D object
        BufferedImage newImage = new BufferedImage(width, height,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = newImage.createGraphics();
        
        // try to render the graphics object using width, height, and image supplied
        try {
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g.setBackground(new Color(0,0,0));
            g.clearRect(0, 0, width, height);
            g.drawImage(image, 0, 0, width, height, null);
        } finally {
            g.dispose();
        }
        
        // return the new (scaled) image
        return newImage;
    }
    
    // add padding to panels
    private void addPadding(JPanel panel) {
        if (panel == null)
            throw new IllegalArgumentException("panel cannot be null");
        panel.setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));
    }
    
    // update the info message displayed to the user (info label)
    private void updateInfoLabel(Outcome outcome) {
        if (outcome == null)
            throw new IllegalArgumentException("outcome cannot be null");
        
        // assign appropriate string value based on outcome
        // NOTE: Outcome.CLEAR is not assigned a new String, therefore assigns empty String
        String sLabel = " ";
        if (outcome == Outcome.FUNDS)
            sLabel = "INVALID BET: insufficient funds";
        else if (outcome == Outcome.MIN_BET)
            sLabel = "MINIMUM BET THRESHOLD: need to increase bet";
        else if (outcome == Outcome.SURRENDER)
            sLabel = "SURRENDER: you chose to surrender";
        else if (outcome == Outcome.PUSH)
            sLabel = "PUSH: you tied the dealer";
        else if (outcome == Outcome.WIN_WITH_BJ)
            sLabel = "BLACKJACK: you beat the dealer";
        else if (outcome == Outcome.LOSE_TO_BJ)
            sLabel = "LOSE: dealer has blackjack";
        else if (outcome == Outcome.LOSE)
            sLabel = "LOSE: you lost to the dealer";
        else if (outcome == Outcome.BUST)
            sLabel = "BUST: you lost to the dealer";
        else if (outcome == Outcome.WIN)
            sLabel = "WIN: you beat the dealer";
        else if (outcome == Outcome.GAME_OVER)
            sLabel = "GAME OVER: you are out of money";

        // update the label text
        mInfoLabel.setText(sLabel);
    }
    
    // update dynamics labels for user bet, money, and points
    private void updateDisplayLabels() {
        updateUserBetLabel();  // update the display for user bet
        updateUserMoneyLabel();  // update the display for user money
        updateUserPointsLabel();  // update the display for user points
    }
    
    // update the display for user points 
    private void updateUserPointsLabel() {
//        String sLabel = "Current Points: " + mGame.getPoints(Player.USER);
//        mUserPointsLabel.setText(sLabel);

        String sUserLabel = "Your Cards (" + mGame.getPoints(Player.USER) + ")";
        mUserCardsLabel.setText(sUserLabel);
    }

    private void updateDealerPointsLabel() {
        String sDealerLabel = "Dealer Cards (" + mGame.getPoints(Player.DEALER) + ")";
        mDealerCardsLabel.setText(sDealerLabel);
    }


    // clear user points after a turn
    private void clearUserPointsLabel() {
//        String sLabel = "Current Points: ";
//        mUserPointsLabel.setText(sLabel);

        String sDealerLabel = "Dealer Cards";
        mDealerCardsLabel.setText(sDealerLabel);

        String sUserLabel = "Your Cards";
        mUserCardsLabel.setText(sUserLabel);
    }

    // update the display for user money
    private void updateUserMoneyLabel() {
        String sLabel = String.format("Money Available: $%,d", mGame.getCurrentUserMoney() / 100);
        mUserMoneyLabel.setText(sLabel);
    }
    
    // update the display for user bet
    private void updateUserBetLabel() {
        String sLabel = String.format("Current Bet: $%,d", mGame.getCurrentUserBet() / 100);
        mUserBetLabel.setText(sLabel);
    }
    
    // hide buttons for betting (bet & deal)
    private void hideBettingActions() {
        mBettingPanel.setVisible(false);
        mDealButton.setVisible(false);
        mResetBetButton.setVisible(false);
    }
    
    // show buttons for betting (bet & deal)
    private void showBettingActions() {
        mBettingPanel.setVisible(true);
        mDealButton.setVisible(true);
        mResetBetButton.setVisible(true);
    }

    // hide buttons for intragame actions (stay, hit, double, surrender)
    private void hideIntraGameActions() {
        mStayButton.setVisible(false);
        mHitButton.setVisible(false);
        mDoubleButton.setVisible(false);
        mSurrenderButton.setVisible(false);
    }

    // show buttons for intragame actions (stay, hit, double, surrender)
    private void showIntraGameActions() {
        mStayButton.setVisible(true);
        mHitButton.setVisible(true);
        mDoubleButton.setVisible(true);
        mSurrenderButton.setVisible(true);
    }

    // hide button to play next hand
    private void hideNextHand() {
        mNextHandButton.setVisible(false);
    }

    // show button to play next hand
    private void showNextHand() {
        revealDealerCards();
        mNextHandButton.setVisible(true);
        checkForOutOfMoney();  // make sure game is not over
    }

}