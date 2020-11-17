package com.harleyoconnor.treasurehunt.treasure;

public class TreasureItem implements ITreasureItem {

    private final String name;
    private final long value;

    public TreasureItem(final String name, final long value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public long getValue() {
        return this.value;
    }

}
