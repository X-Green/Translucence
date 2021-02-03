package dev.eeasee.test;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;

import static dev.eeasee.translucence.util.InChunkBlockPosSet.indexedBinarySearch;

public class Test {
    public void foo() {
        System.out.println("Hey there!");
    }

    public static void main(String[] args) {
        IntList l = new IntArrayList();
        l.add(0);
        l.add(2);
        l.add(4);
        l.add(8);
        l.add(9);
        l.add(11);
        int a = 4;
        l.removeInt(indexedBinarySearch(l, a));
        System.out.println(l);
    }
}
