package com.harleyoconnor.treasurehunt;

import com.harleyoconnor.javagrids.utils.InputUtils;
import com.harleyoconnor.treasurehunt.treasure.TreasureItems;

/**
 * @author Harley O'Connor
 */
public final class Main {

    private Main() {}

    public static void main (final String[] args) {
        TreasureItems.register(); // Register the treasure items.

        boolean playAgain;

        do {
            // Gets board size and guess amounts from user input.
            final int boardSize = InputUtils.getIntInput("\nWhat board size would you like? ");
            final int guesses = InputUtils.getIntInput("\nHow many guesses would you like? ");

            // Create new treasure hunt game.
            final TreasureHuntGame treasureHuntGame = new TreasureHuntGame(boardSize, guesses);
            treasureHuntGame.gameLoop(); // Begin treasure hunt game loop.

            // Ask user if they want to go again (set to true if they input 'y').
            playAgain = InputUtils.getInput("\nWould you like to play again? (y/n) ").equalsIgnoreCase("y");
        } while (playAgain);
    }

}
