package dev.eeasee.translucence.render;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import dev.eeasee.translucence.util.ChunkSectionPos;
import dev.eeasee.translucence.util.InChunkBlockPosSet;
import dev.eeasee.translucence.util.MapUtil;
import dev.eeasee.translucence.util.RenderUtil;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.block.BlockState;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;

import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

public class OutlineRenderer {
    private final Int2ObjectMap<Int2ObjectMap<Int2ObjectMap<Map<BlockState, InChunkBlockPosSet>>>> blocksToBeOutlined = new Int2ObjectOpenHashMap<>();

    private final BlockingQueue<Pair<ChunkSectionPos, Consumer<Map<BlockState, InChunkBlockPosSet>>>> taskQueue = new LinkedBlockingQueue<>();

    public OutlineRenderer() {

    }

    public void tryClearSection(ChunkSectionPos sectionPos) {
        try {
            taskQueue.put(new Pair<>(sectionPos, Map::clear));
        } catch (InterruptedException ignored) {
        }
    }

    public void tryAddToSection(BlockPos blockPos, BlockState blockState) {
        try {
            taskQueue.put(new Pair<>(ChunkSectionPos.from(blockPos), blockStateSetMap -> {
                if (blockStateSetMap.containsKey(blockState)) {
                    blockStateSetMap.get(blockState).add(blockPos);
                } else {
                    blockStateSetMap.put(blockState, new InChunkBlockPosSet(blockPos));
                }
            }));
        } catch (InterruptedException ignored) {
        }
    }

    private void updateOutlineBlockPosMap() {
        for (Pair<ChunkSectionPos, Consumer<Map<BlockState, InChunkBlockPosSet>>> taskPair : this.taskQueue) {
            ChunkSectionPos sectionPos = taskPair.getLeft();
            Consumer<Map<BlockState, InChunkBlockPosSet>> consumer = taskPair.getRight();
            int secX = sectionPos.getSectionX();
            int secY = sectionPos.getSectionY();
            int secZ = sectionPos.getSectionZ();
            consumer.accept(MapUtil.getOrCreateInIntMap(
                    MapUtil.getOrCreateInIntMap(
                            MapUtil.getOrCreateInIntMap(
                                    this.blocksToBeOutlined, secX, Int2ObjectOpenHashMap::new
                            ), secY, Int2ObjectOpenHashMap::new
                    ), secZ, Maps::newHashMap
            ));
        }
    }

    private Map<BlockState, Iterable<BlockPos>> asBlockStateBlockPosSetMap() {
        Map<BlockState, List<BlockPos>> blockStateSetMap = Maps.newHashMap();
        for (Int2ObjectMap<Int2ObjectMap<Map<BlockState, InChunkBlockPosSet>>> my : this.blocksToBeOutlined.values()) {
            for (Int2ObjectMap<Map<BlockState, InChunkBlockPosSet>> mx : my.values()) {
                for (Map<BlockState, InChunkBlockPosSet> m0 : mx.values()) {
                    for (Map.Entry<BlockState, InChunkBlockPosSet> entry : m0.entrySet()) {
                        if (blockStateSetMap.containsKey(entry.getKey())) {
                            blockStateSetMap.get(entry.getKey()).addAll(entry.getValue());
                        } else {
                            List<BlockPos> list = Lists.newArrayList();
                            list.addAll(entry.getValue());
                            blockStateSetMap.put(entry.getKey(), list);
                        }
                    }
                }
            }
        }
        return (Map<BlockState, Iterable<BlockPos>>) (Object) blockStateSetMap;
    }

    public void renderAllOutlines() {
        this.updateOutlineBlockPosMap();
        RenderUtil.LineRenderUtil.renderBlocksOutlined(this.asBlockStateBlockPosSetMap());
    }
}
