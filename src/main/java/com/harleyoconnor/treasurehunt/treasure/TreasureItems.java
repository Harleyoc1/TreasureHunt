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
import java.util.List;
import java.util.Objects;

/**
 * Class to handle treasure item registry.
 *
 * @author Harley O'Connor
 */
public class TreasureItems {

    /**
     * Registry list of treasure items. Populated in the register() procedure in this class.
     */
    public static final List<ITreasureItem> TREASURE_ITEMS = new ArrayList<>();

    /**
     * Registers treasure items from assets/treasure directory.
     */
    public static void register () {
        final JSONParser jsonParser = new JSONParser(); // Create JSON object.
        final File directory = FileUtils.getFile("treasure"); // Get treasure directory.

        // Loop through treasure jsons and register each item.
        for (final File file : Objects.requireNonNull(FileUtils.getDirChildren(directory, ".json")))
            register (jsonParser, file);
    }

    /**
     * Registers a treasure item from a JSON file.
     *
     * @param jsonParser The relevant JSON parser.
     * @param file The JSON file to read from.
     */
    public static void register (final JSONParser jsonParser, final File file) {
        try {
            final JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader(file));
            TREASURE_ITEMS.add(new TreasureItem((String) jsonObject.get("name"), (long) jsonObject.get("value")));
        } catch (IOException | ParseException e) {
            System.err.println("Fatal error occurred whilst registering a treasure item. It is likely that the treasure JSONs have been formatted incorrectly.");
            e.printStackTrace();
        }
    }

    /**
     * Gets a random treasure item from the registry list.
     *
     * @return Random treasure item.
     */
    public static ITreasureItem getRandom () {
        return TREASURE_ITEMS.get(IntegerUtils.getRandomIntBetween(0, TREASURE_ITEMS.size() - 1));
    }

}
