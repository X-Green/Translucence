package dev.eeasee.translucence.util;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class InChunkBlockPosSet implements Set<BlockPos> {
    private static int getInChunkHash(BlockPos pos) {
        return ((pos.getX() & 0b1111) << 4) | ((pos.getY() & 0b11111111) << 8) | (pos.getZ() & 0b1111);
    }

    private final List<BlockPos> values = Lists.newArrayList();

    private final IntList hashes = new IntArrayList();

    public InChunkBlockPosSet() {
    }

    @Override
    public int size() {
        return this.hashes.size();
    }

    @Override
    public boolean isEmpty() {
        return this.hashes.size() == 0;
    }

    @Override
    public boolean contains(Object o) {
        return indexedBinarySearch(this.hashes, getInChunkHash((BlockPos) o)) >= 0;
    }

    @NotNull
    @Override
    public Iterator<BlockPos> iterator() {
        return this.values.iterator();
    }

    @NotNull
    @Override
    public Object[] toArray() {
        return this.values.toArray();
    }

    @NotNull
    @Override
    public <T> T[] toArray(@NotNull T[] a) {
        return this.values.toArray(a);
    }

    @Override
    public boolean add(BlockPos blockPos) {
        int hash = getInChunkHash(blockPos);
        int index = indexedBinarySearch(this.hashes, hash);
        if (index >= 0) {
            return false;
        } else {
            this.hashes.add(-index - 1, hash);
            this.values.add(-index - 1, blockPos);
            return true;
        }
    }

    @Override
    public boolean remove(Object o) {
        BlockPos blockPos = (BlockPos) o;
        int hash = getInChunkHash(blockPos);
        int index = indexedBinarySearch(this.hashes, hash);
        if (index < 0) {
            return false;
        } else {
            this.hashes.removeInt(index);
            this.values.remove(index);
            return true;
        }
    }

    public int getHash(int index) {
        return this.hashes.getInt(index);
    }

    public boolean containHash(int hash) {
        return this.hashes.contains(hash);
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        for (Object o : c) {
            if (!this.contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends BlockPos> c) {
        boolean flag = false;
        for (BlockPos blockPos : c) {
            flag |= this.add(blockPos);
        }
        return flag;
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        InChunkBlockPosSet specifiedSet = new InChunkBlockPosSet();
        specifiedSet.addAll((Collection<? extends BlockPos>) c);
        IntList markRemove = new IntArrayList();
        for (int i = 0; i < this.hashes.size(); i++) {
            int targetHash = this.hashes.getInt(i);
            if (!specifiedSet.containHash(targetHash)) {
                markRemove.add(i);
            }
        }
        for (int i : markRemove) {
            this.hashes.removeInt(i);
            this.values.remove(i);
        }
        return !markRemove.isEmpty();
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        boolean flag = false;
        for (Object o : c) {
            BlockPos blockPos = (BlockPos) o;
            flag |= this.remove(blockPos);
        }
        return flag;
    }

    @Override
    public void clear() {
        this.hashes.clear();
        this.values.clear();
    }

    public static int indexedBinarySearch(IntList list, int key) {
        int low = 0;
        int high = list.size() - 1;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            int midVal = list.getInt(mid);
            int cmp = midVal - key;

            if (cmp < 0)
                low = mid + 1;
            else if (cmp > 0)
                high = mid - 1;
            else
                return mid; // key found
        }
        return -(low + 1);  // key not found
    }
}
