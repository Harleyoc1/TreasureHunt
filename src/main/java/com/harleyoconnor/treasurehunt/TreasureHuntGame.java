package com.harleyoconnor.treasurehunt;

import com.harleyoconnor.javagrids.grids.Grid;
import com.harleyoconnor.javagrids.grids.GridElement;
import com.harleyoconnor.javautilities.InputUtils;
import com.harleyoconnor.javautilities.IntegerUtils;
import com.harleyoconnor.treasurehunt.grid.TreasureGridElement;
import com.harleyoconnor.treasurehunt.living.Monster;
import com.harleyoconnor.treasurehunt.living.Player;
import com.harleyoconnor.treasurehunt.treasure.ITreasureItem;
import com.harleyoconnor.treasurehunt.treasure.TreasureItems;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private final List<Player> players = new ArrayList<>();

    public TreasureHuntGame (final int gridSize, final int guesses, final List<String> playerNames) {
        this.treasureGrid = new Grid (gridSize, gridSize, new TreasureGridElement("[ ]"));
        this.gridSize = gridSize;
        this.guesses = guesses;

        playerNames.forEach(name -> this.players.add(new Player(name)));

        this.createTreasure();
        this.createMonsters();
    }

    /**
     * Populates the treasure map with a random number of random treasure at random positions.
     */
    private void createTreasure () {
        // Loops for a random number of times.
        for (int i = 0; i < IntegerUtils.getRandomIntBetween(this.gridSize * 2, this.gridSize * 5); i++) {
            // Stop creating treasure if no slots are available (this shouldn't happen if the game board is set to the recommended size).
            if (!treasureSlotAvailable()) break;

            Pair<Integer, Integer> position;
            TreasureGridElement gridElement;

            do {
                // Gets a random position in the grid.
                position = new Pair<>(IntegerUtils.getRandomIntBetween(0, this.gridSize - 1), IntegerUtils.getRandomIntBetween(0, this.gridSize - 1));
                gridElement = (TreasureGridElement) this.treasureGrid.getElementAt(position);
            } while (gridElement.getTreasureItem() != null);

            // Puts a random treasure item at the position.
            gridElement.setTreasureItem(TreasureItems.getRandom());
        }
    }

    /**
     * Creates a random amount of monsters at random positions within the grid.
     */
    private void createMonsters () {
        // Loops for a random amount of times.
        for (int i = 0; i < IntegerUtils.getRandomIntBetween(3, 5); i++) {
            // Stop creating monsters if no slots are available (this shouldn't happen if the game board is set to the recommended size).
            if (!monsterSlotAvailable()) break;

            Monster monster;
            TreasureGridElement gridElement;

            // Keep looping until an element which doesn't hold a monster is found.
            do {
                gridElement = ((TreasureGridElement) this.treasureGrid.getElementAt(this.getRandomPositionInGrid()));
                monster = gridElement.getMonster();
            } while (monster != null);

            // Assign the monster to the grid element.
            gridElement.setMonster(new Monster());
        }
    }

    /**
     * Loops through all elements in the grid to check at least one has no treasure.
     *
     * @return True if a treasure slot is available.
     */
    private boolean treasureSlotAvailable () {
        return this.treasureGrid.getGridElements().stream().anyMatch(gridElement -> ((TreasureGridElement) gridElement).getTreasureItem() == null);
    }

    /**
     * Loops through all elements in the grid to check at least one has no monster.
     *
     * @return True if a monster slot is available.
     */
    private boolean monsterSlotAvailable () {
        return this.treasureGrid.getGridElements().stream().anyMatch(gridElement -> ((TreasureGridElement) gridElement).getMonster() == null);
    }

    /**
     * Gets a random position within the bounds of the grid.
     *
     * @return The pair of integers making up the indexes of the random position.
     */
    private Pair<Integer, Integer> getRandomPositionInGrid () {
        return new Pair<>(IntegerUtils.getRandomIntBetween(0, this.gridSize - 1), IntegerUtils.getRandomIntBetween(0, this.gridSize - 1));
    }

    /**
     * Main game loop.
     */
    public void gameLoop () {
        System.out.println("\nWelcome to the treasure hunting game. You have " + this.guesses + " guesses to find as much treasure as possible.");

        // Loop for the number of guesses the user gets.
        for (int i = 0; i < this.guesses; i++) {
            this.players.forEach(this::takeGuess);
            System.out.println("\nThere are " + (this.guesses - i - 1) + " guesses left.");
            this.getMonsterSlots().forEach(this::moveMonster);
        }

        this.printResults(); // Print the results of the game.
    }

    /**
     * Randomly moves a monster at the given grid element.
     *
     * @param gridElement The grid element that currently holds the monster.
     */
    private void moveMonster (final GridElement gridElement) {
        final TreasureGridElement treasureGridElement = ((TreasureGridElement) gridElement);
        final Monster monster = treasureGridElement.getMonster();

        // If there aren't any slots available for monsters, leave them where they are.
        if (!monsterSlotAvailable()) return;

        Pair<Integer, Integer> newPosition;

        do {
            final int newX = gridElement.getPosition().getKey() + IntegerUtils.getRandomIntBetween(-monster.getStealth(), monster.getStealth());
            final int newY = gridElement.getPosition().getValue() + IntegerUtils.getRandomIntBetween(-monster.getStealth(), monster.getStealth());

            newPosition = new Pair<>(newX >= this.treasureGrid.getGrid().size() ? this.treasureGrid.getGrid().size() - 1 : Math.max(newX, 0), newY >= this.treasureGrid.getGrid().get(0).size() ? this.treasureGrid.getGrid().get(0).size() - 1 : Math.max(newY, 0));
        } while (((TreasureGridElement) this.treasureGrid.getElementAt(newPosition)).getMonster() != null);

        treasureGridElement.setTreasureItemTaken(true); // Monster eats treasure.
        treasureGridElement.clearMonster(); // Clear monster from old position.
        ((TreasureGridElement) this.treasureGrid.getElementAt(newPosition)).setMonster(monster); // Add monster to new position.
    }

    /**
     * @return A list of grid elements containing monsters.
     */
    private List<GridElement> getMonsterSlots () {
        return this.treasureGrid.getGridElements().stream().filter(gridElement -> ((TreasureGridElement) gridElement).getMonster() != null).collect(Collectors.toList());
    }

    private void takeGuess (final Player player) {
        // If on multiplayer, print who's turn it is.
        if (this.isMultiplayer()) System.out.println("\n" + player.getName() + "'s turn.");

        this.treasureGrid.printGrid(); // Print the grid.

        String guessPositionString;
        Pair<Integer, Integer> guessPosition;

        // Get guess position until it is a valid position on the grid.

        do {
            guessPositionString = InputUtils.getInput("\nGuess a position (for example, A1 would be the first position). ").toUpperCase();
            guessPosition = this.treasureGrid.getElementPosition(guessPositionString);
        } while (guessPosition == null);

        final TreasureGridElement gridElement = (TreasureGridElement) this.treasureGrid.getElementAt(guessPosition);
        final Monster monster = gridElement.getMonster();

        if (monster != null) {
            final Map<ITreasureItem, Integer> treasureItemsTaken = new HashMap<>();

            for (int i = 0; i < IntegerUtils.getRandomIntBetween(1, monster.getStealth()); i++) {
                if (player.getFoundTreasure().size() == 0) break;

                // Retrieve random piece of treasure.
                final ITreasureItem treasureItem = player.getFoundTreasure().get(IntegerUtils.getRandomIntBetween(0, player.getFoundTreasure().size() - 1));

                // Steal it from player and add it to treasure taken list.
                player.getFoundTreasure().remove(treasureItem);
                if (treasureItemsTaken.containsKey(treasureItem)) treasureItemsTaken.put(treasureItem, treasureItemsTaken.get(treasureItem) + 1);
                else treasureItemsTaken.put(treasureItem, 1);
            }

            System.out.println("\nYou landed on a monster! " + (treasureItemsTaken.size() > 0 ? "They took the following items:" : "They don't take anything from beggars."));
            treasureItemsTaken.forEach((treasureItem, count) -> System.out.println("- " + count + " " + treasureItem.getName() + (count > 1 ? "s" : "")));
        }

        if (gridElement.isTreasureItemTaken()) {
            System.out.println("\nNo treasure at this position.");

            // Sets the guesses position to [O] to show they have already searched there.
            this.treasureGrid.getGrid().get(guessPosition.getKey()).get(guessPosition.getValue()).setDisplayText("[O]");

            return;
        }

        gridElement.setTreasureItemTaken(true);

        final ITreasureItem treasureItem = gridElement.getTreasureItem();
        // Sets the guess position to [X] to show that they found treasure there.
        this.treasureGrid.getGrid().get(guessPosition.getKey()).get(guessPosition.getValue()).setDisplayText("[X]");
        // Add the treasure item to the foundTreasure list.
        player.getFoundTreasure().add(treasureItem);

        System.out.println("\nYou found a " + treasureItem.getName() + " worth " + treasureItem.getValue() + " gold coins.");
    }

    /**
     * Prints the results of the game.
     */
    private void printResults() {
        this.fillOutGrid(); // Fills grid in with treasure information.
        this.treasureGrid.printGrid(); // Prints the filled out grid.

        this.players.forEach(this::printPlayerResults); // Print player results for each player.
    }

    /**
     * Prints the results for a specific player.
     *
     * @param player The player object.
     */
    private void printPlayerResults(final Player player) {
        int goldCoinValue = 0;
        final Map<ITreasureItem, Long> treasureCounts = new HashMap<>();

        // Calculates how much value they got from all their treasure.
        for (ITreasureItem treasureItem : player.getFoundTreasure())
            goldCoinValue += treasureItem.getValue();

        // Puts their found treasure into a map of each item and how many they got.
        for (final ITreasureItem treasureItem : TreasureItems.TREASURE_ITEMS) {
            final long count = player.getFoundTreasure().stream().filter(treasureItem::equals).count();
            if (count > 0) treasureCounts.put(treasureItem, count);
        }

        System.out.println("\nOverall, " + (this.isMultiplayer() ? player.getName() : "you") + " got " + goldCoinValue + " gold coins worth of treasure. " + (player.getFoundTreasure().size() > 0 ? (this.isMultiplayer() ? "They" : "You") + " found the following items:" : ""));

        // Displays each treasure item they found, how many they got, and what it's worth.
        treasureCounts.forEach((treasureItem, count) -> System.out.println("- " + count + " " + treasureItem.getName() + (count > 1 ? "s" : "") + " worth " + treasureItem.getValue() + " gold coins each, and " + (treasureItem.getValue() * count) + " in total."));
    }

    /**
     * Fills grid with which spaces had treasure and were empty didn't. Executed at the end of the game.
     */
    private void fillOutGrid() {
        for (int i = 0; i < this.treasureGrid.getGrid().size(); i++)
            for (int j = 0; j < this.treasureGrid.getGrid().get(i).size(); j++) {
                final TreasureGridElement gridElement = (TreasureGridElement) this.treasureGrid.getGrid().get(i).get(j);
                this.treasureGrid.getGrid().get(i).get(j).setDisplayText("[" + (gridElement.getTreasureItem() != null ? 'X' : 'O') + "]");
            }
    }

    /**
     * Checks if the game is multiplayer by querying the player count.
     *
     * @return True if game is multiplayer.
     */
    private boolean isMultiplayer () {
        return this.players.size() > 1;
    }

}
