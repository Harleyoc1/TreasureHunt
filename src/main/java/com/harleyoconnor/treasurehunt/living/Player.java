package com.harleyoconnor.treasurehunt.living;

import com.harleyoconnor.treasurehunt.treasure.ITreasureItem;

import java.util.ArrayList;
import java.util.List;

/**
 * The player object. Stores their name and the treasure they have found.
 *
 * @author Harley O'Connor
 */
public final class Player {

    private final String name;
    private final List<ITreasureItem> foundTreasure = new ArrayList<>();

    public Player (final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<ITreasureItem> getFoundTreasure() {
        return foundTreasure;
    }

}
