/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.qenherkhopeshef.finitestate.lazy.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.qenherkhopeshef.finitestate.lazy.RegularExtractor;
import org.qenherkhopeshef.finitestate.lazy.RegularLanguageIF;
import static org.qenherkhopeshef.finitestate.lazy.RegularLanguageFactory.*;

/**
 * Checking a number of assumptions about my algorithms. Tests the speed of the
 * new method for limited-length searches.
 *
 * @author rosmord
 */
public class SpeedTestWithLimit {

    private final static int MAX_SIZE = 100_000;
    private final RegularLanguageIF<Character> a_star_b;
    private final RegularLanguageIF<Character> a_star_b_minus_cd;
    private final RegularLanguageIF<Character> cd;
    private final RegularExtractor<Character> a_star_b_recognizer;
    private final RegularExtractor<Character> a_star_b_minus_cd_recognizer;
    private static final String WITH_CD = "withCD";
    private static final String ONLY_A = "onlyA";
    private static final String MATCH = "match";

    private final HashMap<String, List<Character>> testMap = new HashMap<>();

    public SpeedTestWithLimit() {
        a_star_b = seq(
                exact('a'),
                skip(),
                exact('b')
        );
        cd = seq(skip(), exact('c'), exact('d'), skip());
        a_star_b_minus_cd = inter(a_star_b, complement(cd));
        a_star_b_recognizer = RegularExtractor.getBuilder(a_star_b).build();
        a_star_b_minus_cd_recognizer = RegularExtractor.getBuilder(a_star_b_minus_cd).build();

        int size = 10;
        while (size <= MAX_SIZE) {
            testMap.put(MATCH + size, createMatch(size));
            testMap.put(ONLY_A + size, createOnlyA(size));
            testMap.put(WITH_CD + size, createWithCD(size));
            size *= 10;
        }

    }

    private void addToList(List<Character> l, char c, int n) {
        for (int i = 0; i < n; i++) {
            l.add(c);
        }
    }

    public List<Character> createMatch(int length) {
        ArrayList<Character> result = new ArrayList<>();
        int d1 = (int) (Math.random() * length);
        int d2 = (int) (Math.random() * (length - d1));
        int d3 = length - (d1 + d2);
        addToList(result, 'x', d1);
        result.add('a');
        addToList(result, 'x', d2);
        result.add('b');
        addToList(result, 'x', d3);
        return result;
    }

    public List<Character> createOnlyA(int length) {
        ArrayList<Character> result = new ArrayList<>();
        addToList(result, 'a', length);
        return result;
    }

    public List<Character> createWithCD(int length) {
        ArrayList<Character> result = new ArrayList<>();
        int d1 = (int) (Math.random() * length);
        int d2 = (int) (Math.random() * (length - (d1)));
        int d3 = (int) (Math.random() * (length - (d1 + d2)));
        int d4 = length - (d1 + d2 + d3);
        addToList(result, 'x', d1);
        result.add('a');
        addToList(result, 'x', d2);
        result.add('c');
        result.add('d');
        addToList(result, 'x', d3);
        result.add('b');
        addToList(result, 'x', d4);
        return result;
    }

    public void timeSearch(String title, RegularExtractor<Character> recognizer, int maxSize) {
        System.out.println();
        System.out.println("\tTest de " + title);
        int numberOfRuns = 1;
        // Bootstrap...
        for (int i = 0; i < 10; i++) {
            recognizer.search(testMap.get(MATCH + 10));
            recognizer.search(testMap.get(ONLY_A + 10));
            recognizer.search(testMap.get(WITH_CD + 10));
        }
        int size = 10;
        long startTime = System.nanoTime();
        while (size <= MAX_SIZE) {
            recognizer.search(testMap.get(MATCH + size), maxSize);
            recognizer.search(testMap.get(ONLY_A + size), maxSize);
            recognizer.search(testMap.get(WITH_CD + size), maxSize);
            long endTime = System.nanoTime();
            double duration = ((endTime - startTime) * 1e-9) / numberOfRuns;
            System.out.println(size + "\t" + duration);
            startTime = System.nanoTime();
            size *= 10;
        }
        System.out.println("");
    }

    public void run() {
        int m = 10;
        while (m <= 1000) {
            System.out.println("ici");
            RegularExtractor<Character> rec = RegularExtractor.getBuilder(a_star_b_minus_cd).build();
            timeSearch("limit " + m, rec, 0);
            m = m + 10;
        }
    }

    public static void main(String[] args) {
        new SpeedTestWithLimit().run();
    }

}
