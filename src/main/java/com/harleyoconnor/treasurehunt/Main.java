package com.harleyoconnor.treasurehunt;

import com.harleyoconnor.javautilities.InputUtils;
import com.harleyoconnor.treasurehunt.treasure.TreasureItems;

import java.util.ArrayList;
import java.util.List;

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

            final List<String> playerNames = new ArrayList<>();
            boolean playerNameExisted;

            do {
                playerNameExisted = false;
                final String playerName = InputUtils.getInput("\nEnter the next player's name, or 'start' to begin the game.");

                if (playerNames.contains(playerName)) {
                    System.out.println("Could not add this player as there is already a player with that name.");
                    playerNameExisted = true;
                    continue;
                }

                playerNames.add(playerName);
            } while (!playerNames.get(playerNames.size() - 1).equalsIgnoreCase("start") || playerNameExisted);

            playerNames.removeIf("start"::equalsIgnoreCase); // Remove start from player name list.

            if (playerNames.size() == 0) playerNames.add("player"); // Add default name 'player' if user did not enter any player names.

            // Create new treasure hunt game.
            final TreasureHuntGame treasureHuntGame = new TreasureHuntGame(boardSize, guesses, playerNames);
            treasureHuntGame.gameLoop(); // Begin treasure hunt game loop.

            // Ask user if they want to go again (set to true if they input 'y').
            playAgain = InputUtils.getInput("\nWould you like to play again? (y/n) ").equalsIgnoreCase("y");
        } while (playAgain);
    }

}
