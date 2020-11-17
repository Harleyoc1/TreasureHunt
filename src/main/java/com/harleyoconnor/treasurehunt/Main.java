package com.harleyoconnor.treasurehunt;

import com.harleyoconnor.javagrids.utils.InputUtils;
import com.harleyoconnor.treasurehunt.treasure.TreasureItems;

public final class Main {

    private Main() {}

    public static void main (final String[] args) {
        TreasureItems.register();

        boolean playAgain;

        do {
            final int boardSize = InputUtils.getIntInput("\nWhat board size would you like? ");
            final int guesses = InputUtils.getIntInput("\nHow many guesses would you like? ");

            final TreasureHuntGame treasureHuntGame = new TreasureHuntGame(boardSize, guesses);
            treasureHuntGame.gameLoop();

            playAgain = InputUtils.getInput("\nWould you like to play again? (y/n) ").equalsIgnoreCase("y");
        } while (playAgain);
    }

}
