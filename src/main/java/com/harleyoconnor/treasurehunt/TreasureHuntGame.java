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

    private void populateTreasureMap () {
        for (int i = 0; i < IntegerUtils.getRandomIntBetween(this.gridSize, this.gridSize * 2); i++) {
            Pair<Integer, Integer> position;

            do {
                position = new Pair<>(IntegerUtils.getRandomIntBetween(0, this.gridSize - 1), IntegerUtils.getRandomIntBetween(0, this.gridSize - 1));
            } while(this.treasureMap.containsKey(position));

            this.treasureMap.put(position, TreasureItems.getRandom());
        }
    }

    public void gameLoop () {
        System.out.println("\nWelcome to the treasure hunting game. You have " + this.guesses + " guesses to find as much treasure as possible.");

        for (int i = 0; i < this.guesses; i++) {
            this.treasureGrid.printGrid();

            String guessPositionString;
            Pair<Integer, Integer> guessPosition;

            do {
                guessPositionString = InputUtils.getInput("\nGuess a position (for example, A1 would be the first position). ", true).toUpperCase();
                guessPosition = this.treasureGrid.getElementPosition(guessPositionString);
            } while (guessPosition == null);

            if (!this.treasureMap.containsKey(guessPosition)) {
                System.out.println("No treasure at this position.");
                this.treasureGrid.getGrid().get(guessPosition.getKey()).set(guessPosition.getValue(), "[O]");
                continue;
            }

            final ITreasureItem treasureItem = this.treasureMap.get(guessPosition);
            this.treasureGrid.getGrid().get(guessPosition.getKey()).set(guessPosition.getValue(), "[X]");
            this.foundTreasure.add(treasureItem);

            System.out.println("You found a " + treasureItem.getName() + " worth " + treasureItem.getValue() + " gold coins.");
        }

        this.printResults();
    }

    private void printResults() {
        for (int i = 0; i < this.treasureGrid.getGrid().size(); i++) {
            for (int j = 0; j < this.treasureGrid.getGrid().get(i).size(); j++) {
                this.treasureGrid.getGrid().get(i).set(j, "[" + (this.treasureMap.containsKey(new Pair<>(i, j)) ? 'X' : 'O') + "]");
            }
        }

        this.treasureGrid.printGrid();

        int goldCoinValue = 0;
        final Map<ITreasureItem, Long> treasureCounts = new HashMap<>();

        for (ITreasureItem treasureItem : this.foundTreasure)
            goldCoinValue += treasureItem.getValue();

        for (final ITreasureItem treasureItem : TreasureItems.TREASURE_ITEMS) {
            final long count = this.foundTreasure.stream().filter(treasureItem::equals).count();
            if (count > 0) treasureCounts.put(treasureItem, count);
        }

        System.out.println("\nOverall, you got " + goldCoinValue + " gold coins worth of treasure. " + (foundTreasure.size() > 0 ? "You found the following items:" : ""));

        treasureCounts.forEach((treasureItem, count) -> System.out.println("- " + count + " " + treasureItem.getName() + (count > 1 ? "s" : "") + " worth " + treasureItem.getValue() + " gold coins each, and " + (treasureItem.getValue() * count) + " in total."));
    }

}
