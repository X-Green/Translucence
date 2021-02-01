package dev.eeasee.translucence.config;


import com.google.common.collect.ImmutableSet;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Collections;
import java.util.Set;

public enum Configs {
    INSTANCE;

    public final Set<Identifier> AFFECTED_BLOCK_ID_SET;

    Configs() {
        AFFECTED_BLOCK_ID_SET = getAffectedBlockIDs();
    }

    private static Set<Identifier> getAffectedBlockIDs() {
        Set<String> affectedBlockStrings;
        File configJSONFile = new File(FabricLoader.getInstance().getConfigDir().toFile(), "block_translucence/affected_blocks.json");
        try {
            JsonArray translucentBlockIDsJsonArray = new JsonParser().parse(new FileReader(configJSONFile)).getAsJsonObject().getAsJsonArray("affected_blocks");
            affectedBlockStrings = new Gson().fromJson(translucentBlockIDsJsonArray, new TypeToken<Set<String>>() {
            }.getType());
        } catch (FileNotFoundException e) {
            org.apache.logging.log4j.LogManager.getLogger().info("No translucent config found.");
            affectedBlockStrings = Collections.emptySet();
        }
        ImmutableSet.Builder<Identifier> builder = ImmutableSet.builder();
        for (String s : affectedBlockStrings) {
            builder.add(new Identifier(s));
        }
        return builder.build();
    }

}
