package dev.eeasee.test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Test {
    public void foo() {
        System.out.println("Hey there!");
    }

    public static void main(String[] args) {
        int i = 255 + 256;
        System.out.println(i & 0b11111111);
    }
}
