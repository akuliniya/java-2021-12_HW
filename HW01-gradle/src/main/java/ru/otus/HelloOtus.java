package ru.otus;

import com.google.common.collect.Range;

public class HelloOtus {
    public static void main(String[] args) {
        Range<Integer> range = Range.open(1, 5);
        System.out.println(range.contains(1));
        System.out.println(range.contains(2));
        System.out.println(range.contains(5));
    }
}
