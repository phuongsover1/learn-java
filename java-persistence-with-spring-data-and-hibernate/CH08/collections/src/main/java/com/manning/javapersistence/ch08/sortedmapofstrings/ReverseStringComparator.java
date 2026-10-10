package com.manning.javapersistence.ch08.sortedmapofstrings;

import java.util.Comparator;

public class ReverseStringComparator implements Comparator<String> {

    @Override
    public int compare(String o1, String o2) {
        return -o1.compareTo(o2);
    }
}
