package com.harleyoconnor.treasurehunt.living;

import com.harleyoconnor.javautilities.IntegerUtils;

/**
 * The monster object. Stores data about their stealth and their current position.
 *
 * @author Harley O'Connor
 */
public final class Monster {

    /**
     * Stealth is the monster's strength. The higher this value, the more items they have a chance of taking when landing on them, and the
     * more distance they can move in a single turn.
     */
    private final int stealth = IntegerUtils.getRandomIntBetween(1, 4);

    public int getStealth() {
        return this.stealth;
    }

}
