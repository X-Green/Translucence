package dev.eeasee.translucence.render;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import dev.eeasee.translucence.util.ChunkSectionPos;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.block.BlockState;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

public class OutlineRenderer {
    private final Map<BlockState, Iterable<BlockPos>> blockPosToBeOutlined = Maps.newHashMap();

    private final Int2ObjectMap<Int2ObjectMap<Int2ObjectMap<Map<BlockState, Iterable<BlockPos>>>>> blocksToBeOutlined = new Int2ObjectOpenHashMap<>();

    private final BlockingQueue<Pair<ChunkSectionPos, Consumer<Map<BlockState, Iterable<BlockPos>>>>> taskQueue = new LinkedBlockingQueue<>();

    public OutlineRenderer() {

    }

    public void addOutlineBlock(BlockState blockState, BlockPos pos) {
        if (!blockPosToBeOutlined.containsKey(blockState)) {
            blockPosToBeOutlined.put(blockState, Sets.newHashSet(pos));
        } else {
            ((Set<BlockPos>) blockPosToBeOutlined.get(blockState)).add(pos);
        }
    }

    public void clearSection(ChunkSectionPos sectionPos) {
        try {
            taskQueue.put(new Pair<>(sectionPos, Map::clear));
        } catch (InterruptedException ignored) {
        }
    }

    public void addToSection(BlockPos blockPos, BlockState blockState) {
        try {
            taskQueue.put(new Pair<>(ChunkSectionPos.from(blockPos), blockStateSetMap -> {
                if (blockStateSetMap.containsKey(blockState)) {
                    ((List<BlockPos>) blockStateSetMap.get(blockState)).add(blockPos);
                } else {
                    blockStateSetMap.put(blockState, Lists.newArrayList(blockPos));
                }
            }));
        } catch (InterruptedException ignored) {
        }
    }

    private void updateOutlineBlockPosMap() {
        for (Pair<ChunkSectionPos, Consumer<Map<BlockState, Iterable<BlockPos>>>> taskPair : this.taskQueue) {
            ChunkSectionPos sectionPos = taskPair.getLeft();
            Consumer consumer = taskPair.getRight();
            // if ()
        }
    }

    public void renderAllOutlines() {
        this.updateOutlineBlockPosMap();
    }
}
