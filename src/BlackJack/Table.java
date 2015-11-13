package BlackJack;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * table object to display blackjack game
 * uses Game class to help handle workflow
 */


// TO DO LIST:
    
    // check for winner at end of dealers turn
    // update info when dealers turn
    // add timer delay for dealer cards
    // notify and update cards when cut card reached
    // disable buttons instead of hiding them
    
    // refactor code -> clean up and simplify
    
    // draw each card one-at-a-time - DONE
    // draw dealer cards - DONE
    // show current user points - DONE
    // update new game button - DONE
    // paint table background green - DONE
    // add border to main panel - DONE


public class Table extends JFrame {

    // enum to determine which info message to display
    public enum Outcome {CLEAR, MIN_BET, FUNDS, SURRENDER, PUSH, WIN_WITH_BJ, LOSE_TO_BJ, WIN, LOSE, BUST}
    
    // constant for margin padding
    private static final int PADDING = 5;
    
    // private member: Game object
    private Game mGame;  // game object
    
    // private members: labels
    private JLabel mUserMoneyLabel;  // display user money available to bet
    private JLabel mUserBetLabel;  // display current user bet amount
    private JLabel mMinBetLabel;  // display minimum bet
    private JLabel mUserPointsLabel;  // display current user points for hand
    private JLabel mInfoLabel;  // display useful info to user
    
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
    private JPanel mActionPanel;  // panel for intragame action buttons (hit, stay, etc.)
    private JPanel mCardPanel;  // panel to display cards
    private JPanel mMainPanel;  // main panel -> holds other panels

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
        mMainPanel = new JPanel();
        addPadding(mMainPanel);
        mMainPanel.setLayout(new BorderLayout());
        
        // create sub panels
        createCardPanel();
        createBettingPanel();
        createLowerPanel();
        createActionPanel();

        // add sub panels to main panel
        mMainPanel.add(mLowerPanel,BorderLayout.PAGE_END);
        mMainPanel.add(mActionPanel, BorderLayout.LINE_END);
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
        
        // set the color for all card panels to be green
        mCardPanel.setBackground(Color.green);
        mUserCardDisplay.setBackground(Color.green);
        mDealerCardDisplay.setBackground(Color.green);
        
        // set the layout and add the panels for user and dealer cards
        mCardPanel.setLayout(new GridLayout(2,1));
        mCardPanel.add(mUserCardDisplay);
        mCardPanel.add(mDealerCardDisplay);
    }
    
    // create panel for betting buttons
    private void createBettingPanel() {
        // create panel, add padding, set layout
        mBettingPanel = new JPanel();
        addPadding(mBettingPanel);
        mBettingPanel.setLayout(new GridLayout(1,5));
        
        // add betting buttons to panel
        mBettingPanel.add(mBetFiveButton);
        mBettingPanel.add(mBetTenButton);
        mBettingPanel.add(mBetTwentyFiveButton);
        mBettingPanel.add(mBetOneHundredButton);
        mBettingPanel.add(mBetFiveHundredButton);
    }

    // create panel for labels
    private void createLowerPanel() {
        // create panel, add padding, set layout
        mLowerPanel = new JPanel();
        addPadding(mLowerPanel);
        mLowerPanel.setLayout(new GridLayout(6,1));
        
        // add labels to panel
        mLowerPanel.add(mUserPointsLabel);
        mLowerPanel.add(mUserMoneyLabel);
        mLowerPanel.add(mMinBetLabel);
        mLowerPanel.add(mUserBetLabel);
        mLowerPanel.add(mBettingPanel);
        mLowerPanel.add(mInfoLabel);
    }

    // create panel for intragame user actions
    private void createActionPanel() {
        // create panel, add padding, set layout
        mActionPanel = new JPanel();
        addPadding(mActionPanel);
        mActionPanel.setLayout(new GridLayout(8,1));
        
        // add buttons for intragame user actions to panel
        mActionPanel.add(mHitButton);
        mActionPanel.add(mStayButton);
        mActionPanel.add(mDoubleButton);
        mActionPanel.add(mSurrenderButton);
        mActionPanel.add(mNextHandButton);
        mActionPanel.add(mDealButton);
        mActionPanel.add(mResetBetButton);
        mActionPanel.add(mNewGameButton);
    }

    // create labels
    private void createLabels() {
        // create min bet label -> fixed label that does not update
        String sMinBet = String.format("Minimum Bet: $%,d", Game.MIN_BET / 100);
        mMinBetLabel = new JLabel(sMinBet);
        
        // create dynamic labels (points, money, bet, info)
        mUserPointsLabel = new JLabel();
        mUserMoneyLabel = new JLabel();
        mUserBetLabel = new JLabel();
        mInfoLabel = new JLabel();
        
        // update the display for dynamic labels
        updateDisplayLabelsLabels();
    }

    // create all buttons
    private void createButtons() {

        // text for betting buttons
        String sResetBetButtonText = "Reset Bet";
        String sFiveButtonText = "5";
        String sTenButtonText = "10";
        String sTwentyFiveButtonText = "25";
        String sOneHundredButtonText = "100";
        String sFiveHundredButtonText = "500";

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
            
            // user first card  
            mGame.dealCard(Game.Player.USER);
            drawLastDealtCard(Game.Player.USER);
            
            // dealer first card  
            mGame.dealCard(Game.Player.DEALER);
            drawLastDealtCard(Game.Player.DEALER);
            
            // user second card
            mGame.dealCard(Game.Player.USER);
            drawLastDealtCard(Game.Player.USER);
            
            // dealer second card  
            mGame.dealCard(Game.Player.USER);
            drawLastDealtCard(Game.Player.USER);
    
            // check for blackjack
            if (mGame.playerOnlyBlackJack()) {
                updateInfoLabel(Outcome.WIN_WITH_BJ);
                hideIntraGameActions();
                showNextHand();
            } else if (mGame.dealerOnlyBlackJack()) {
                updateInfoLabel(Outcome.LOSE_TO_BJ);
                hideIntraGameActions();
                showNextHand();
            } else if (mGame.bothPlayersBlackJack()) {
                updateInfoLabel(Outcome.PUSH);
                hideIntraGameActions();
                showNextHand();
            }
        }
    }
    
    // hit - user takes a card
    private void hit() {
        // deal card
        mGame.dealCard(Game.Player.USER);
        drawLastDealtCard(Game.Player.USER);
        
        // check for bust
        if (mGame.playerHasBust(Game.Player.USER)) {
            updateInfoLabel(Outcome.BUST);
            hideIntraGameActions();
            showNextHand();
        }
    }
    
    // stay -> user takes no more cards, dealer turn
    private void stay() {
        hideIntraGameActions();
        dealerTurn();
        // NEED TO CHECK FOR GAME OUTCOME
        showNextHand();
    }
    
    // deal cards to dealer until at point limit (hard 17+)
    private void dealerTurn() {
        while (!mGame.dealerAtPointLimit()) {
            mGame.dealCard(Player.DEALER);
            drawLastDealtCard(Player.D)
        }
    }
    
    // double down -> user doubles bet and only takes 1 more card, then stay (dealers turn)
    private void doubleDown() {
        iBetAmount = mGame.getCurrentUserBet();
        if (!mGame.isValidBet(iBetAmount) {
            updateInfoLabel(Outcome.FUNDS);
        } else {
            increaseBet(iBetAmount);
            mGame.dealCard(Player.USER);
            drawLastDealtCard(Player.USER);
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
        clearCardImages(mCardPanel.getGraphics());
        hideNextHand();
        showBettingActions();
    }
    // start a new game
    private void newGame() {
        // assign new Game to intance variable
        mGame = new Game();
        
        // clear out info label, card images, and reset displayed buttons
        updateInfoLabel(Outcome.CLEAR);
        clearCardImages(mCardPanel.getGraphics());
        hideNextHand();
        showBettingActions();
    }
    
    // remove all card images
    private void clearCardImages() {
        super.paint(mUserCardDisplay.getGraphics);
        super.paint(mDealerCardDisplay.getGraphics);
    }

    // draw image of playing card on table, source: lab code in class (updated) 
    private void drawLastDealtCard(Player player) {
        if (player == null)
            throw new IllegalArgumentException("g, player, and card cannot be null");
            
        // super.paint(g);  -> will repaint graphic & remove other cards?
        
        // create graphics object based on player (user vs. dealer)
        Graphics g = (player == Game.Player.USER) ? mUserCardDisplay.getGraphics() : mDealerCardDisplay.getGraphics();
        
        // create image object for card
        Card card = mGame.getLastDealtCard();
        BufferedImage bufferedImage = genBuffImage("/src/BlackJack/cards/" + card + ".png");
        
        // scale image to fit
        int nWidth = 139;
        int nHeight = 202;
        bufferedImage = scaleImage(bufferedImage, nWidth, nHeight);
        
        // determine position of card
        int iXAdjustmentForOtherCards = 25 * mGame.countOfCardsInHand(player);
        int iXPosition = 10 + iXAdjustmentForOtherCards;
        int iYPosition

        // draw the card
        g.drawImage(bufferedImage, iXPosition, iYPosition, null);
        }
    }




    // NEED TO FINISH THIS METHOD
    private void delayedCard(Player player) {
        
        Timer timer = new Timer();
        timer.schedule(new DelayedTask(), DELAY);
        
        
        private class DelayedTask extends TimerTask {
            
            public void run() {
                timer.cancel();
                
                // deal card, draw card, etc.
                
                throw new RuntimeException("ToDo");
            }
        }
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
            throw new IllegalArgumentException("panel cannot be null);
        panel.setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));
    }
    
    // update the info message displayed to the user (info label)
    private void updateInfoLabel(Outcome outcome) {
        if (outcome == null)
            throw new IllegalArgumentException("outcome cannot be null");
        
        // assign appropiate string value based on outcome
        // NOTE: Outcome.CLEAR is not assigned a new String, therefore assigns empty String
        String sLabel = "";
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
        String sLabel = "Current Points: " + mGame.getPoints(Player.USER);
        mUserPointsLabel.setText(sLabel);
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
    }
    
    // show buttons for betting (bet & deal)
    private void showBettingActions() {
        mBettingPanel.setVisible(true);
        mDealButton.setVisible(true);
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
        mNextHandButton.setVisible(true);
    }

}