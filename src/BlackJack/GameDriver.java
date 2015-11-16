package BlackJack;

import javax.swing.*;

/**
 * contains main method to run a BlackJack game object
 */

public class GameDriver {

    public static void main(String[] args) {

        // define dimensions of game frame as constants
        final int GAME_FRAME_WIDTH = 800;
        final int GAME_FRAME_HEIGHT = 600;

        // create game frame object, set dimensions, default close & make visible
        JFrame frame = new Table();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(GAME_FRAME_WIDTH, GAME_FRAME_HEIGHT);
        frame.setVisible(true);
    }
}