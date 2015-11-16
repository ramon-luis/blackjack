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


/**
 * Table object to display game data
 * Game object handles information flow between user interaction and Game object
 */

public class Table extends JFrame {

    // enum to determine which info message to display
    public enum Outcome {
        CLEAR, MIN_BET, FUNDS, SURRENDER, WIN_WITH_BJ, LOSE_TO_BJ, WIN, LOSE, PUSH, BUST, GAME_OVER
    }

    // constants
    public static final int PADDING = 5;  // margin padding
    public static final int DELAY = 250;  // delay for showing dealer cards in milliseconds

    // private member: Game object
    private Game mGame;  // game object

    // private members: labels
    private JLabel mUserMoneyLabel;  // display user money available to bet
    private JLabel mUserBetLabel;  // display current user bet amount
    private JLabel mMinBetLabel;  // display minimum bet
    private JLabel mInfoLabel;  // display useful info to user
    private JLabel mShoeLabel; // display updates on shoe, if necessary
    private JLabel mDealerCardsLabel;  // display label and points for dealer cards
    private JLabel mUserCardsLabel;  // display label and points for user cards

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
    private JPanel mLowerPanel;  // panel that holds all info other than card display
    private JPanel mPlayPanel;  // panel for intragame action buttons (hit, stay, etc.)
    private JPanel mLabelPanel;  // panel for most labels
    private JPanel mButtonPanel;  // panel for most buttons
    private JPanel mActionPanel;  // panel for action buttons
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

    //****************************
    // METHODS BELOW CREATE GUI
    //****************************

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
        updateUserLabels();
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
        String sNextHandButtonText = "play next hand";
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
            // clear current info when a button is pressed
            updateInfoLabel(Outcome.CLEAR);

            // get the source of the event (identify the button)
            Object source = e.getSource();

            // select action based on source button
            if (source == mResetBetButton)
                resetBet();
            else if (source == mBetFiveButton)
                increaseBet(5_00);
            else if (source == mBetTenButton)
                increaseBet(10_00);
            else if (source == mBetTwentyFiveButton)
                increaseBet(25_00);
            else if (source == mBetOneHundredButton)
                increaseBet(100_00);
            else if (source == mBetFiveHundredButton)
                increaseBet(500_00);
            else if (source == mDealButton)
                deal();
            else if (source == mStayButton)
                stay();
            else if (source == mHitButton)
                hit();
            else if (source == mDoubleButton)
                doubleDown();
            else if (source == mSurrenderButton)
                surrender();
            else if (source == mNextHandButton)
                nextHand();
            else if (source == mNewGameButton)
                newGame();
        }
    }

    // create the main panel for the table
    private void createMainPanel() {
        // create panel, add padding, and set layout
        JPanel mMainPanel = new JPanel();
        addPadding(mMainPanel);
        mMainPanel.setLayout(new BorderLayout());

        // create sub panels
        createCardPanel();  // shows cards
        createLowerPanel();  // shows buttons and info

        // add sub panels to main panel
        mMainPanel.add(mShoeLabel, BorderLayout.PAGE_START);  // tells user about shoe information
        mMainPanel.add(mLowerPanel, BorderLayout.PAGE_END);  // all buttons and other info
        mMainPanel.add(mCardPanel, BorderLayout.CENTER);  // displays cards

        // update what items should be active
        hideIntraGameActionButtons();
        hideNextHandButton();

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

        // set layout and add labels to individual card displays
        mDealerCardDisplay.setLayout(new BorderLayout());
        mDealerCardDisplay.add(mDealerCardsLabel, BorderLayout.PAGE_START);
        mUserCardDisplay.setLayout(new BorderLayout());
        mUserCardDisplay.add(mUserCardsLabel, BorderLayout.PAGE_START);

        // set the layout and add the sub-panels for user and dealer cards
        mCardPanel.setLayout(new GridLayout(2, 1));
        mCardPanel.add(mDealerCardDisplay);
        mCardPanel.add(mUserCardDisplay);
    }

    // create lower panel for labels and buttons
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

    // create panel to hold all the buttons
    private void createButtonPanel() {
        // create panel and layout
        mButtonPanel = new JPanel();
        mButtonPanel.setLayout(new GridLayout(3, 1));

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
        mBettingPanel.setLayout(new GridLayout(1, 6));

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
        mPlayPanel.setLayout(new GridLayout(1, 4));

        // add buttons for intra-game play
        mPlayPanel.add(mHitButton);
        mPlayPanel.add(mStayButton);
        mPlayPanel.add(mDoubleButton);
        mPlayPanel.add(mSurrenderButton);
    }

    // create panel for action buttons
    private void createActionPanel() {
        // create panel, add padding, set layout
        mActionPanel = new JPanel();
        addPadding(mActionPanel);
        mActionPanel.setLayout(new GridLayout(1, 3));

        // add buttons for additional actions
        mActionPanel.add(mDealButton);
        mActionPanel.add(mNextHandButton);
        mActionPanel.add(mNewGameButton);
    }

    // create panel for labels
    private void createLabelPanel() {
        // create panel, add padding, set layout
        mLabelPanel = new JPanel();
        addPadding(mLabelPanel);
        mLabelPanel.setLayout(new GridLayout(3, 1));

        // add labels to panel
        mLabelPanel.add(mUserBetLabel);
        mLabelPanel.add(mMinBetLabel);
        mLabelPanel.add(mUserMoneyLabel);
    }


    //*************************************************
    // BUTTON LISTENER METHODS BELOW - DRIVE GAME PLAY
    //*************************************************

    // reset the user bet
    private void resetBet() {
        mGame.resetUserBet();  // set user bet to $0
        updateUserLabels();  // update labels for bet, money, and points
    }

    // increase user bet
    private void increaseBet(int amount) {
        if (amount < 0)
            throw new IllegalArgumentException("amount cannot be less than zero");

        // increase bet if user has enough money
        if (mGame.isValidBet(amount))
            mGame.increaseBet(amount);
        else
            updateInfoLabel(Outcome.FUNDS);  // tell user bad funds if not enough money

        // update labels for bet, money, and points
        updateUserLabels();
    }

    // deal initial cards
    public void deal() {
        if (!mGame.minBetReached())
            updateInfoLabel(Outcome.MIN_BET);  // tell user if min bet not reached
        else {
            // hide betting buttons and show intra-game buttons
            hideBettingButtons();
            showIntraGameActionButtons();

            // replace the shoe if needed
            replaceShoeIfCutCardReached();

            // deal two cards to each player
            dealCard(Player.USER);
            dealCard(Player.DEALER, DELAY);
            dealCard(Player.USER, DELAY);
            dealCard(Player.DEALER, DELAY);

            // check for blackjack
            checkForBlackJack();
        }
    }

    // hit - user takes a card
    private void hit() {
        dealCard(Player.USER);  // deal card to user
        checkForBust(Player.USER);  // check for bust
        hideSurrenderAndDoubleButtons();  // hide surrender & double buttons
    }

    // stay -> user takes no more cards, dealer turn
    private void stay() {
        hideIntraGameActionButtons();  // hide buttons for hit, stay, etc.
        dealerTurn();  // dealers turn to draw cards
    }

    // double down -> user doubles bet and only takes 1 more card, then stay (dealers turn)
    private void doubleDown() {
        int iBetAmount = mGame.getCurrentUserBet();
        if (!mGame.isValidBet(iBetAmount)) {
            updateInfoLabel(Outcome.FUNDS);  // tell user if not enough funds
        } else {
            increaseBet(iBetAmount);  // increase the bet
            dealCard(Player.USER);  // deal card to user
            stay();  // user only gets one card, then has to stay
        }
    }

    // surrender -> user gives up 1/2 bet, no more cards for user
    private void surrender() {
        mGame.surrender();  // set boolean to true in game object
        updateDisplayForHandOver();
    }

    // next hand -> end this hand and go to new betting screen
    private void nextHand() {
        mGame.endHand();  // settle money, clear current bet & cards
        displayBettingScreen();  // clear images & labels, show only betting buttons
    }

    // start a new game
    private void newGame() {
        mGame = new Game();  // assign new Game to instance variable
        displayBettingScreen();  // clear images & labels, show only betting buttons
    }


    //**************************************************
    // GAME PLAY HELPER METHODS BELOW - PROCESS ACTIONS
    //**************************************************

    // deal a card to a player - immediate, no lag
    private void dealCard(Player player) {
        if (player == null)
            throw new IllegalArgumentException("player cannot be null");

        // deal card
        mGame.dealCard(player);

        // get the card that was dealt & card number
        Card card = mGame.getLastDealtCard();
        int iCardNumber = mGame.getCountOfCardsInHand(player);

        // draw the card, update user points label, and check for cut card
        drawCard(player, card, iCardNumber);
        updateUserPointsLabel();
        checkForCutCard();
    }

    // deal card to player with slight lag (simulate actual game play)
    private void dealCard(Player player, int delay) {
        if (player == null)
            throw new IllegalArgumentException("player cannot be null");

        // delay game
        delayGame(delay);

        // deal card
        mGame.dealCard(player);

        // get the card that was dealt & card number
        Card card = mGame.getLastDealtCard();
        int iCardNumber = mGame.getCountOfCardsInHand(player);

        // draw the card, update user points label, and check for cut card
        drawCard(player, card, iCardNumber);
        updateUserPointsLabel();
        checkForCutCard();
    }

    // slow game for short period of time
    // http://stackoverflow.com/questions/3342651/how-can-i-delay-a-java-program-for-a-few-seconds
    private void delayGame(int delay) {
        try {
            Thread.sleep(delay);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    // deal cards to dealer until at point limit (hard 17+)
    private void dealerTurn() {
        if (!mGame.dealerAtPointLimit()) {
            dealCard(Player.DEALER, DELAY);
            dealerTurn();
        } else
            updateDisplayForHandOver();
    }

    // check if player has more than 21 points
    private void checkForBust(Player player) {
        if (player == null)
            throw new IllegalArgumentException("player cannot be null");
        if (mGame.playerHasBust(player))
            updateDisplayForHandOver();
    }

    // check if either player has blackjack -> end current hand if a player does
    private void checkForBlackJack() {
        if (mGame.playerHasBlackjack(Player.DEALER) || mGame.playerHasBlackjack(Player.USER)) {
            updateDisplayForHandOver();
        }
    }

    // check if user is out of money -> GAME OVER!!
    private void checkForOutOfMoney() {
        if (mGame.userIsOutOfMoney()) {
            updateInfoLabel(Outcome.GAME_OVER);
            showNewGameButtonOnly();
        }
    }
    // tell user if cut card reached
    private void checkForCutCard() {
        if (mGame.cutCardReached())
            mShoeLabel.setText("CUT CARD REACHED: card shoe will be replaced after this hand");
    }

    // replace the shoe if cut card has been reached
    private void replaceShoeIfCutCardReached() {
        if (mGame.cutCardReached()) {
            mShoeLabel.setText("NEW CARDS: card shoe has been replaced");
            mGame.replaceShoe();
        }
    }



    //*******************************************
    // GUI HELPER METHODS BELOW - UPDATE DISPLAY
    //*******************************************

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

    // draw image of playing card on table, source: lab code in class (updated)
    private void drawCard(Player player, Card card, int cardNumber) {
        if (player == null || card == null)
            throw new IllegalArgumentException("player and card cannot be null");
        if (cardNumber < 1)
            throw new IllegalArgumentException("cardNumber cannot be less than zero");

        // create graphics object based on player (user vs. dealer)
        Graphics g = (player == Player.USER) ? mUserCardDisplay.getGraphics() : mDealerCardDisplay.getGraphics();

        // create image object for card -> use back of card if it is dealers first card
        boolean bDealerFirstCard = (player == Player.DEALER && mGame.getCountOfCardsInHand(Player.DEALER) == 1);
        String sCard = bDealerFirstCard ? "back" : card.toString();
        BufferedImage bufferedImage = generateBufferedImage("/src/BlackJack/cards/" + sCard + ".png");

        // scale image to fit
        int nWidth = 104;  // based on original png file size
        int nHeight = 152;  // based on original png file size
        bufferedImage = scaleImage(bufferedImage, nWidth, nHeight);

        // determine position of card
        int iXAdjustmentForOtherCards = 125 * (cardNumber - 1) + 5;  // draw each card to the right of the next card
        int iXPosition = 5 + iXAdjustmentForOtherCards;
        int iYPosition = 25;

        // draw the card
        g.drawImage(bufferedImage, iXPosition, iYPosition, null);
    }

    // generate an image from a local file, source: lab code in class (updated) 
    private static BufferedImage generateBufferedImage(String relativeFilePath) {
        if (relativeFilePath == null)
            throw new IllegalArgumentException("relativeFilePath cannot be null");

        // create file object to obtain image using path of user directory
        String sFilePathForImage = System.getProperty("user.dir") + relativeFilePath;
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
        BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = newImage.createGraphics();

        // try to render the graphics object using width, height, and image supplied
        try {
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g.setBackground(new Color(0, 0, 0));
            g.clearRect(0, 0, width, height);
            g.drawImage(image, 0, 0, width, height, null);
        } finally {
            g.dispose();
        }

        // return the new (scaled) image
        return newImage;
    }

    // update display information if current hand is over
    private void updateDisplayForHandOver() {
        displayHandOutcomeToInfoLabel(); // update info label on hand outcome
        revealDealerCards();  // show the dealer cards
        updateDealerPointsLabel();  // show the dealer points
        showNextHandButton();  // show next hand button
        hideIntraGameActionButtons();  // hide intra-game action buttons
    }

    // update info display to show outcome of the hand (win, lose, tie)
    private void displayHandOutcomeToInfoLabel() {
        if (mGame.playerHasSurrendered())
            updateInfoLabel(Outcome.SURRENDER);
        else if (mGame.isPush())
            updateInfoLabel(Outcome.PUSH);
        else if (mGame.playerHasBust(Player.USER))
            updateInfoLabel(Outcome.BUST);
        else if (mGame.playerOnlyBlackJack())
            updateInfoLabel(Outcome.WIN_WITH_BJ);
        else if (mGame.dealerOnlyBlackJack())
            updateInfoLabel(Outcome.LOSE_TO_BJ);
        else if (mGame.dealerWins())
            updateInfoLabel(Outcome.LOSE);
        else if (mGame.userWins())
            updateInfoLabel(Outcome.WIN);
    }

    // reveal the dealer cards and show the dealer points
    private void revealDealerCards() {
        delayGame(DELAY);
        drawCard(Player.DEALER, mGame.getDealerCards().get(0), 1);
        updateDealerPointsLabel();
    }

    // remove all card images
    private void clearCardImages() {
        mUserCardDisplay.repaint();
        mDealerCardDisplay.repaint();
    }

    // update dynamics labels for user bet, money, and points
    private void updateUserLabels() {
        updateUserBetLabel();  // update the display for user bet
        updateUserMoneyLabel();  // update the display for user money
        updateUserPointsLabel();  // update the display for user points
    }

    // update the display for user bet
    private void updateUserBetLabel() {
        String sLabel = String.format("Current Bet: $%,d", mGame.getCurrentUserBet() / 100);
        mUserBetLabel.setText(sLabel);
    }
    // update the display for user money
    private void updateUserMoneyLabel() {
        String sLabel = String.format("Money Available: $%,d", mGame.getCurrentUserMoney() / 100);
        mUserMoneyLabel.setText(sLabel);
    }

    // update the display for user points 
    private void updateUserPointsLabel() {
        String sUserLabel = "Your Cards (" + mGame.getPoints(Player.USER) + ")";
        mUserCardsLabel.setText(sUserLabel);
    }

    // update the display for dealer points
    private void updateDealerPointsLabel() {
        String sDealerLabel = "Dealer Cards (" + mGame.getPoints(Player.DEALER) + ")";
        mDealerCardsLabel.setText(sDealerLabel);
    }

    // clear user points after a turn
    private void clearUserPointsLabel() {
        String sDealerLabel = "Dealer Cards";
        mDealerCardsLabel.setText(sDealerLabel);

        String sUserLabel = "Your Cards";
        mUserCardsLabel.setText(sUserLabel);
    }

    // hide buttons for betting (bet & deal)
    private void hideBettingButtons() {
        mResetBetButton.setEnabled(false);
        mBetFiveButton.setEnabled(false);
        mBetTenButton.setEnabled(false);
        mBetTwentyFiveButton.setEnabled(false);
        mBetOneHundredButton.setEnabled(false);
        mBetFiveHundredButton.setEnabled(false);
        mDealButton.setEnabled(false);  // deal button only available when betting
    }

    // show buttons for betting (bet & deal)
    private void showBettingButtons() {
        mResetBetButton.setEnabled(true);
        mBetFiveButton.setEnabled(true);
        mBetTenButton.setEnabled(true);
        mBetTwentyFiveButton.setEnabled(true);
        mBetOneHundredButton.setEnabled(true);
        mBetFiveHundredButton.setEnabled(true);
        mDealButton.setEnabled(true);  // deal button only available when betting
    }

    // hide buttons for intra-game actions (stay, hit, double, surrender)
    private void hideIntraGameActionButtons() {
        mStayButton.setEnabled(false);
        mHitButton.setEnabled(false);
        mDoubleButton.setEnabled(false);
        mSurrenderButton.setEnabled(false);
    }

    // show buttons for intra-game actions (stay, hit, double, surrender)
    private void showIntraGameActionButtons() {
        mStayButton.setEnabled(true);
        mHitButton.setEnabled(true);
        mDoubleButton.setEnabled(true);
        mSurrenderButton.setEnabled(true);
    }

    // hide button to play next hand
    private void hideNextHandButton() {
        mNextHandButton.setEnabled(false);
    }

    // show button to play next hand
    private void showNextHandButton() {
        revealDealerCards();
        mNextHandButton.setEnabled(true);
        checkForOutOfMoney();  // make sure game is not over
    }

    // hide all buttons except for new game
    private void showNewGameButtonOnly() {
        hideBettingButtons();
        hideIntraGameActionButtons();
        hideNextHandButton();
    }

    // clear images & labels, show only betting buttons
    private void displayBettingScreen() {
        updateInfoLabel(Outcome.CLEAR);
        updateUserLabels();
        clearCardImages();
        clearUserPointsLabel();
        hideNextHandButton();
        hideIntraGameActionButtons();
        showBettingButtons();
        mShoeLabel.setText(" ");
    }

    private void hideSurrenderAndDoubleButtons() {
        mSurrenderButton.setEnabled(false);  // surrender only an option after initial deal
        mDoubleButton.setEnabled(false);  // double only an option after initial deal
    }

    // add padding to panels
    private void addPadding(JPanel panel) {
        if (panel == null)
            throw new IllegalArgumentException("panel cannot be null");
        panel.setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));
    }
}