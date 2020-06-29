/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.qenherkhopeshef.finitestate.lazy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import org.qenherkhopeshef.finitestate.lazy.RegularExtractor;
import static org.qenherkhopeshef.finitestate.lazy.RegularLanguageFactory.*;

/**
 * Checking a number of assumptions about my algorithms.
 * <b> Note : this file should move elsewhere (modern Gradle doesn't allow it to be in tests).
 * <p>Results: 
 * <ul>
 * <li> The time is nicely linear with entry length. Good !
 * <li> However, the complexity of the underlying automaton has a <strong>HUGE</strong> impact.
 * </ul>
 * 
 * To be more precise :
 * <ul>
 * <li> if time(a.*b) == 1</li>
 * <li> time( a.*b - .*cd.*) == 8</li>
 * <li> time( limit4) == 100</li>
 * <li> time( limit10) == 600</li>
 * <li> time( limit20) == 2300</li>
 * </ul>
 * @author rosmord
 */
public class SpeedTest {

    private final static int MAX_SIZE = 1000;
    private final RegularLanguageIF<Character> a_star_b;
    private final RegularLanguageIF<Character> a_star_b_minus_cd;
    private final RegularLanguageIF<Character> cd;
    private final RegularExtractor<Character> a_star_b_recognizer;
    private final RegularExtractor<Character> a_star_b_minus_cd_recognizer;
    private static final String WITH_CD = "withCD";
    private static final String ONLY_A = "onlyA";
    private static final String MATCH = "match";

    private final HashMap<String, List<Character>> testMap = new HashMap<>();
    private final RegularLanguageIF<Character> a_star_b_4;
    private final RegularLanguageIF<Character> a_star_b_10;
    private final RegularLanguageIF<Character> a_star_b_20;
    private final RegularExtractor<Character> abcd4rec;
    private final RegularExtractor<Character> abcd10rec;
    private final RegularExtractor<Character> abcd20rec;

    public SpeedTest() {
        a_star_b = seq(
                exact('a'),
                skip(),
                exact('b')
        );
        cd = seq(skip(), exact('c'), exact('d'), skip());
        a_star_b_minus_cd = inter(a_star_b, complement(cd));
        a_star_b_recognizer = RegularExtractor.getBuilder(a_star_b).build();
        a_star_b_minus_cd_recognizer = RegularExtractor.getBuilder(a_star_b_minus_cd).build();
        a_star_b_4 = maxLength(a_star_b_minus_cd, 4);
        a_star_b_10 = maxLength(a_star_b_minus_cd, 10);
        a_star_b_20 = maxLength(a_star_b_minus_cd, 20);

        abcd4rec = RegularExtractor.getBuilder(a_star_b_4).build();
        abcd10rec = RegularExtractor.getBuilder(a_star_b_10).build();
        abcd20rec = RegularExtractor.getBuilder(a_star_b_20).build();

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

    public void timeSearch(String title, RegularExtractor<Character> recognizer) {
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
            recognizer.search(testMap.get(MATCH + size));
            recognizer.search(testMap.get(ONLY_A + size));
            recognizer.search(testMap.get(WITH_CD + size));
            long endTime = System.nanoTime();
            double duration = ((endTime - startTime) * 1e-9) / numberOfRuns;
            System.out.println(size+ "\t" + duration);
            startTime = System.nanoTime();
            size *= 10;
        }      
        System.out.println("");
    }

    public void run() {
        timeSearch("a.*b", a_star_b_recognizer);
        //timeSearch("a.*b moins .*cd.*", a_star_b_minus_cd_recognizer);
        //timeSearch("limit 4", abcd4rec);
        //timeSearch("limit 10", abcd10rec);
        //timeSearch("limit 20", abcd20rec);
        
        for (int i = 2; i < 100; i++) {
            RegularLanguageIF<Character> lang = maxLength(a_star_b_minus_cd, i);
            RegularExtractor<Character> rec = RegularExtractor.getBuilder(lang).build();
            timeSearch("limit "+i, rec);
        }
    }

    public static void main(String[] args) {
        new SpeedTest().run();
    }

}
