package com.harleyoconnor.treasurehunt.grid;

import com.harleyoconnor.javagrids.grids.GridElement;
import com.harleyoconnor.treasurehunt.living.Monster;
import com.harleyoconnor.treasurehunt.treasure.ITreasureItem;

import javax.annotation.Nullable;

/**
 * @author Harley O'Connor
 */
public final class TreasureGridElement extends GridElement {

    private ITreasureItem treasureItem;
    private boolean treasureItemTaken = true;
    private Monster monster;

    public TreasureGridElement (final String displayText) {
        super(displayText);
    }

    @Nullable
    public Monster getMonster() {
        return monster;
    }

    public void setMonster(Monster monster) {
        this.monster = monster;
    }

    public void clearMonster () {
        this.monster = null;
    }

    public ITreasureItem getTreasureItem () {
        return this.treasureItem;
    }

    /**
     * Sets the treasure item to parameter, and treasureItemTaken to false.
     *
     * @param treasureItem The treasure item to set.
     */
    public void setTreasureItem (final ITreasureItem treasureItem) {
        this.treasureItem = treasureItem;
        this.treasureItemTaken = false;
    }

    public boolean isTreasureItemTaken () {
        return this.treasureItemTaken;
    }

    public void setTreasureItemTaken(boolean treasureItemTaken) {
        this.treasureItemTaken = treasureItemTaken;
    }

}
