package dev.eeasee.translucence;

import net.fabricmc.api.ModInitializer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.Set;

public class TranslucenceMod implements ModInitializer {


    public static final Logger LOGGER = LogManager.getLogger();

    public static final Set<Block> MODIFIED_BLOCKS = new HashSet<>();

    @Override
    public void onInitialize() {

    }


    public TranslucenceMod() {

    }

}
