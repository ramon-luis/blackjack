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
    // update info when dealers turn
    // draw dealer cards
    // paint table background green
    // show current user points
    // update new game button
    // add border to main panel
    // draw each card one-at-a-time
    // add timer delay for dealer cards
    // refactor code -> clean up and simplify



public class Table extends JFrame {

    public enum Outcome {CLEAR, MIN_BET, FUNDS, SURRENDER, PUSH, WIN_WITH_BJ, LOSE_TO_BJ, WIN, LOSE, BUST}


    Game mGame;

    private static final String MONEY_AVAIL_STRING = "Money Available: ";
    private static final String USER_BET_STRING = "Current bet: ";

    JLabel mUserMoneyLabel;
    JLabel mUserBetLabel;
    JLabel mMinBetLabel;
    JLabel mInfoLabel;

    JButton mResetBetButton;
    JButton mBetFiveButton;
    JButton mBetTenButton;
    JButton mBetTwentyFiveButton;
    JButton mBetOneHundredButton;
    JButton mBetFiveHundredButton;
    JButton mDealButton;
    JButton mStayButton;
    JButton mHitButton;
    JButton mDoubleButton;
    JButton mSurrenderButton;
    JButton mNextHandButton;
    JButton mNewGameButton;

    JPanel mBettingPanel;
    JPanel mLowerPanel;
    JPanel mActionPanel;
    JPanel mCardPanel;
    JPanel mMainPanel;

    public Table() {

        mGame = new Game();
        createLabels();
        createButtons();
        createMainPanel();

    }

    private void hideBettingActions() {
        mBettingPanel.setVisible(false);
        mDealButton.setVisible(false);
    }

    private void showBettingActions() {
        mBettingPanel.setVisible(true);
        mDealButton.setVisible(true);
    }

    private void hideIntraGameActions() {
        mStayButton.setVisible(false);
        mHitButton.setVisible(false);
        mDoubleButton.setVisible(false);
        mSurrenderButton.setVisible(false);
    }

    private void showIntraGameActions() {
        mStayButton.setVisible(true);
        mHitButton.setVisible(true);
        mDoubleButton.setVisible(true);
        mSurrenderButton.setVisible(true);
    }

    private void hideNextHand() {
        mNextHandButton.setVisible(false);
    }

    private void showNextHand() {
        mNextHandButton.setVisible(true);
    }

    private void createMainPanel() {
        mMainPanel = new JPanel();
        mMainPanel.setLayout(new BorderLayout());

        mCardPanel = new JPanel();

        createBettingPanel();
        createLowerPanel();
        createActionPanel();

        mMainPanel.add(mLowerPanel,BorderLayout.PAGE_END);
        mMainPanel.add(mActionPanel, BorderLayout.LINE_END);
        mMainPanel.add(mCardPanel, BorderLayout.CENTER);

        hideIntraGameActions();
        hideNextHand();

        this.add(mMainPanel);

    }

    private void createBettingPanel() {
        mBettingPanel = new JPanel();
        mBettingPanel.setLayout(new GridLayout(1,5));
        mBettingPanel.add(mBetFiveButton);
        mBettingPanel.add(mBetTenButton);
        mBettingPanel.add(mBetTwentyFiveButton);
        mBettingPanel.add(mBetOneHundredButton);
        mBettingPanel.add(mBetFiveHundredButton);
    }

    private void createLowerPanel() {
        mLowerPanel = new JPanel();
        mLowerPanel.setLayout(new GridLayout(5,1));
        mLowerPanel.add(mUserMoneyLabel);
        mLowerPanel.add(mMinBetLabel);
        mLowerPanel.add(mUserBetLabel);
        mLowerPanel.add(mBettingPanel);
        mLowerPanel.add(mInfoLabel);
    }

    private void createActionPanel() {
        mActionPanel = new JPanel();
        mActionPanel.setLayout(new GridLayout(8,1));
        mActionPanel.add(mHitButton);
        mActionPanel.add(mStayButton);
        mActionPanel.add(mDoubleButton);
        mActionPanel.add(mSurrenderButton);
        mActionPanel.add(mNextHandButton);
        mActionPanel.add(mDealButton);
        mActionPanel.add(mResetBetButton);
        mActionPanel.add(mNewGameButton);
    }


    private void createLabels() {

        String sMinBet = "Minimum Bet: " + String.format("$%,d", Game.MIN_BET / 100);
        mMinBetLabel = new JLabel(sMinBet);

        mUserMoneyLabel = new JLabel();
        mUserBetLabel = new JLabel();
        mInfoLabel = new JLabel();
        updateBetAndMoneyLabels();
    }

    private void updateBetAndMoneyLabels() {
        updateUserBetLabel();
        updateUserMoneyLabel();
    }

    private void updateUserMoneyLabel() {
        String sLabel = MONEY_AVAIL_STRING + String.format("$%,d", mGame.getCurrentUserMoney() / 100);
        mUserMoneyLabel.setText(sLabel);
    }

    private void updateUserBetLabel() {
        String sLabel = USER_BET_STRING + String.format("$%,d", mGame.getCurrentUserBet() / 100);
        mUserBetLabel.setText(sLabel);
    }

    private void updateInfoLabel(Outcome outcome) {
        if (outcome == null)
            throw new IllegalArgumentException("outcome cannot be null");
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

        mInfoLabel.setText(sLabel);
    }

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

        // create an action listener & add to all buttons
        ActionListener listener = new ButtonListener();
        JButton[] allButtons = {mResetBetButton, mBetFiveButton, mBetTenButton, mBetTwentyFiveButton,
                mBetOneHundredButton, mBetFiveHundredButton, mDealButton, mStayButton, mHitButton,
                mDoubleButton, mSurrenderButton, mNextHandButton, mNewGameButton};

        for (JButton button : allButtons)
            button.addActionListener(listener);
    }

    private void increaseBet(int amount) {
        if (amount < 0)
                throw new IllegalArgumentException("amount cannot be less than zero");
        if (mGame.isValidBet(amount))
            mGame.increaseBet(amount);
        else
            updateInfoLabel(Outcome.FUNDS);
        updateBetAndMoneyLabels();
    }

    private void clearCardImages(Graphics g) {
        super.paint(g);
    }


    private void drawCards(Graphics g) {

        super.paint(g);
        BufferedImage bufferedImage;
        //use this counter to position the cards horizontally
        int nXPos = 10;

        // factors to scale image
        int nWidth = 139;
        int nHeight = 202;

        for (Card card : mGame.getUserCards()) {
            bufferedImage = genBuffImage("/src/BlackJack/cards/" + card + ".png");
            bufferedImage = scaleImage(bufferedImage, nWidth, nHeight);
            //nc = x-pos, 100 = y-pos
            g.drawImage(bufferedImage, nXPos, 100, null);
            nXPos = nXPos + 25;
        }
    }

    private static BufferedImage genBuffImage(String strRelativeFilePath){
        String strPathImg= System.getProperty("user.dir")+ strRelativeFilePath;
        File filImg = new File(strPathImg);

        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(filImg);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return bufferedImage;

    }

    private static BufferedImage scaleImage(BufferedImage img, int nWidth, int nHeight) {

        BufferedImage newImage = new BufferedImage(nWidth, nHeight,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = newImage.createGraphics();
        try {
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g.setBackground(new Color(0,0,0));
            g.clearRect(0, 0, nWidth, nHeight);
            g.drawImage(img, 0, 0, nWidth, nHeight, null);
        } finally {
            g.dispose();
        }
        return newImage;
    }

    private class ButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();

            int iBetAmount;
            if (source == mResetBetButton) {
                mGame.resetUserBet();
                updateBetAndMoneyLabels();
            } else if (source == mBetFiveButton) {
                iBetAmount = 5_00;
                increaseBet(iBetAmount);
            } else if (source == mBetTenButton) {
                iBetAmount = 10_00;
                increaseBet(iBetAmount);
            } else if (source == mBetTwentyFiveButton) {
                iBetAmount = 25_00;
                increaseBet(iBetAmount);
            } else if (source == mBetOneHundredButton) {
                iBetAmount = 100_00;
                increaseBet(iBetAmount);
            } else if (source == mBetFiveHundredButton) {
                iBetAmount = 500_00;
                increaseBet(iBetAmount);


            } else if (source == mDealButton) {
                if (!mGame.minBetReached())
                    updateInfoLabel(Outcome.MIN_BET);
                else {

                    mGame.deal();
                    drawCards(mCardPanel.getGraphics());
                    hideBettingActions();
                    showIntraGameActions();

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
            } else if (source == mStayButton) {
                hideIntraGameActions();
                mGame.dealerTurn();
                drawCards(mCardPanel.getGraphics());
                // update info label based on outcome of hand


                showNextHand();
            } else if (source == mHitButton) {
                // deal card
                mGame.dealCard(Game.Player.USER);
                drawCards(mCardPanel.getGraphics());
                // check for bust
                if (mGame.playerHasBust(Game.Player.USER)) {
                    updateInfoLabel(Outcome.BUST);
                    hideIntraGameActions();
                    showNextHand();
                }

            } else if (source == mDoubleButton) {
                iBetAmount = mGame.getCurrentUserBet();
                if (mGame.isValidBet(iBetAmount)) {
                    mGame.doubleDown();  // doubles bet & calls dealer turn
                    hideIntraGameActions();
                    showNextHand();
                } else {
                    updateInfoLabel(Outcome.FUNDS);
                }

            } else if (source == mSurrenderButton) {
                mGame.surrender();
                updateInfoLabel(Outcome.SURRENDER);
                hideIntraGameActions();
                showNextHand();
            } else if (source == mNextHandButton) {
                mGame.endHand();  // settle money, clear current bet & cards
                updateInfoLabel(Outcome.CLEAR);
                clearCardImages(mCardPanel.getGraphics());
                hideNextHand();
                showBettingActions();
            } else if (source == mNewGameButton) {
                // call new Game
                // unload current frame
                throw new RuntimeException("ToDo");

            }

        }
    }



}