package com.harleyoconnor.treasurehunt.treasure;

import com.harleyoconnor.javagrids.utils.IntegerUtils;
import com.harleyoconnor.javautilities.FileUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class TreasureItems {

    public static final List<ITreasureItem> TREASURE_ITEMS = new ArrayList<>();

    public static void register () {
        final JSONParser jsonParser = new JSONParser(); // Create JSON object.
        final File directory = FileUtils.getFile("treasure"); // Get treasure directory.

        // Loop through treasure jsons and register each item.
        for (final File file : Objects.requireNonNull(FileUtils.getDirChildren(directory, ".json")))
            register (jsonParser, file);
    }

    private static void register (final JSONParser jsonParser, final File file) {
        try {
            final JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader(file));
            TREASURE_ITEMS.add(new TreasureItem((String) jsonObject.get("name"), (long) jsonObject.get("value")));
        } catch (IOException | ParseException e) {
            System.err.println("Fatal error occurred whilst registering a treasure item. It is likely that the treasure JSONs have been formatted incorrectly.");
            e.printStackTrace();
        }
    }

    public static ITreasureItem getRandom () {
        return TREASURE_ITEMS.get(IntegerUtils.getRandomIntBetween(0, TREASURE_ITEMS.size() - 1));
    }

}
