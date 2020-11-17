package com.harleyoconnor.treasurehunt;

import com.harleyoconnor.javagrids.grids.Grid;
import com.harleyoconnor.javagrids.utils.InputUtils;
import com.harleyoconnor.javagrids.utils.IntegerUtils;
import com.harleyoconnor.treasurehunt.treasure.ITreasureItem;
import com.harleyoconnor.treasurehunt.treasure.TreasureItems;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Treasure Hunt Game Object. Handles the game and holds all relevant data.
 * Create a new object for each game.
 *
 * @author Harley O'Connor
 */
public final class TreasureHuntGame {

    private final Grid treasureGrid;
    private final int gridSize;
    private final int guesses;
    private final Map<Pair<Integer, Integer>, ITreasureItem> treasureMap = new HashMap<>();
    private final List<ITreasureItem> foundTreasure = new ArrayList<>();

    public TreasureHuntGame (final int gridSize, final int guesses) {
        this.treasureGrid = new Grid (gridSize, gridSize, " ", "[", "]");
        this.gridSize = gridSize;
        this.guesses = guesses;

        this.populateTreasureMap();
    }

    /**
     * Populates the treasure map with a random number of random treasure at random positions.
     */
    private void populateTreasureMap () {
        // Loops for a random number of times.
        for (int i = 0; i < IntegerUtils.getRandomIntBetween(this.gridSize, this.gridSize * 2); i++) {
            Pair<Integer, Integer> position;

            do {
                // Gets a random position in the grid.
                position = new Pair<>(IntegerUtils.getRandomIntBetween(0, this.gridSize - 1), IntegerUtils.getRandomIntBetween(0, this.gridSize - 1));
            } while(this.treasureMap.containsKey(position));

            // Puts a random treasure item at the position.
            this.treasureMap.put(position, TreasureItems.getRandom());
        }
    }

    /**
     * Main game loop.
     */
    public void gameLoop () {
        System.out.println("\nWelcome to the treasure hunting game. You have " + this.guesses + " guesses to find as much treasure as possible.");

        // Loop for the number of guesses the user gets.
        for (int i = 0; i < this.guesses; i++) takeGuess();

        this.printResults(); // Print the results of the game.
    }

    private void takeGuess() {
        this.treasureGrid.printGrid();

        String guessPositionString;
        Pair<Integer, Integer> guessPosition;

        do {
            guessPositionString = InputUtils.getInput("\nGuess a position (for example, A1 would be the first position). ", true).toUpperCase();
            guessPosition = this.treasureGrid.getElementPosition(guessPositionString);
        } while (guessPosition == null);

        if (!this.treasureMap.containsKey(guessPosition)) {
            System.out.println("No treasure at this position.");

            // Sets the guesses position to [O] to show they have already searched there.
            this.treasureGrid.getGrid().get(guessPosition.getKey()).set(guessPosition.getValue(), "[O]");

            return;
        }

        // Gets the treasure item and the position.
        final ITreasureItem treasureItem = this.treasureMap.get(guessPosition);
        // Sets the guess position to [X] to show that they found treasure there.
        this.treasureGrid.getGrid().get(guessPosition.getKey()).set(guessPosition.getValue(), "[X]");
        // Add the treasure item to the foundTreasure list.
        this.foundTreasure.add(treasureItem);

        System.out.println("You found a " + treasureItem.getName() + " worth " + treasureItem.getValue() + " gold coins.");
    }

    /**
     * Prints the results of the game.
     */
    private void printResults() {
        fillOutGrid(); // Fills grid in with treasure information.

        this.treasureGrid.printGrid(); // Prints the filled out grid.

        int goldCoinValue = 0;
        final Map<ITreasureItem, Long> treasureCounts = new HashMap<>();

        // Calculates how much value they got from all their treasure.
        for (ITreasureItem treasureItem : this.foundTreasure)
            goldCoinValue += treasureItem.getValue();

        // Puts their found treasure into a map of each item and how many they got.
        for (final ITreasureItem treasureItem : TreasureItems.TREASURE_ITEMS) {
            final long count = this.foundTreasure.stream().filter(treasureItem::equals).count();
            if (count > 0) treasureCounts.put(treasureItem, count);
        }

        System.out.println("\nOverall, you got " + goldCoinValue + " gold coins worth of treasure. " + (foundTreasure.size() > 0 ? "You found the following items:" : ""));

        // Displays each treasure item they found, how many they got, and what it's worth.
        treasureCounts.forEach((treasureItem, count) -> System.out.println("- " + count + " " + treasureItem.getName() + (count > 1 ? "s" : "") + " worth " + treasureItem.getValue() + " gold coins each, and " + (treasureItem.getValue() * count) + " in total."));
    }

    /**
     * Fills grid with which spaces had treasure and were empty didn't. Executed at the end of the game.
     */
    private void fillOutGrid() {
        for (int i = 0; i < this.treasureGrid.getGrid().size(); i++)
            for (int j = 0; j < this.treasureGrid.getGrid().get(i).size(); j++)
                this.treasureGrid.getGrid().get(i).set(j, "[" + (this.treasureMap.containsKey(new Pair<>(i, j)) ? 'X' : 'O') + "]");
    }

}
