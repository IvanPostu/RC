package com.ivan.rc;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MainTest {

    static class Pair {
        int a, b;
        int indexA, indexB;
    };

    static Pair findMinPair(List<Integer> l) {
        Pair p = new Pair();
        p.a = Integer.MAX_VALUE;
        p.b = 0;

        for (int i = 0; i < l.size() - 1; i++) {
            int sum = l.get(i) + l.get(i + 1);
            if (sum < p.a + p.b) {
                p.a = l.get(i);
                p.b = l.get(i + 1);
                p.indexA = i;
                p.indexB = i + 1;
            }
        }

        return p;
    }

    static int solve(int[] arr) {
        List<Integer> l = new ArrayList<>(arr.length);
        for (int a : arr) {
            l.add(a);
        }

        int sum = 0;

        while (l.size() != 1) {
            Pair p = findMinPair(l);
            l.remove(p.indexA);
            l.remove(p.indexA);

            sum += p.a + p.b;
            l.add(p.indexA, p.a + p.b);

        }

        return sum;
    }

    @Test
    public void testCase() {

        Assertions.assertEquals(37, solve(new int[] { 4, 3, 2, 6, 1 }));
    }

}
