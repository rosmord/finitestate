package org.qenherkhopeshef.finitestate.demo;

import static org.qenherkhopeshef.finitestate.lazy.RegularLanguageFactory.*;

import java.util.TreeMap;

import org.qenherkhopeshef.finitestate.lazy.CharHelper;
import org.qenherkhopeshef.finitestate.lazy.RegularLanguageIF;

/**
 * Tests a pattern which leads to exponentially bad results on backtracking
 * libraries...
 */
public class WorstCaseTest {

    public static RegularLanguageIF<Character> buildLanguage() {
        RegularLanguageIF<Character> language = seq(exact('a'), star(union(exact('b'), plus(exact('c')))), exact('d'),
        exact('e'));
        return language;
    }


    /**
     * A much simpler language...
     * @return
     */
    public static RegularLanguageIF<Character> buildSimpleLanguage() {
        RegularLanguageIF<Character> language = seq(exact('a'), star(exact('c')), exact('d'),
        exact('e'));
        return language;
    }

    public static long getUsedMemory() {
        return Runtime.getRuntime().maxMemory() - Runtime.getRuntime().freeMemory();
    }
    

    public static long testN(int n, RegularLanguageIF<Character> language) {
        StringBuilder builder = new StringBuilder("a");
        for (int i=0; i < n; i++)
            builder.append("c");
        long t0 = System.nanoTime();
        System.gc();
        long memBefore = Runtime.getRuntime().maxMemory() - Runtime.getRuntime().freeMemory();
        boolean check = language.recognize(CharHelper.fromString(builder.toString()));
        long memAfter = Runtime.getRuntime().maxMemory() - Runtime.getRuntime().freeMemory();
        long t1 = System.nanoTime();
        System.out.println();        
        System.out.println();
        System.out.println("********************************************");
        System.out.println();
        System.out.println("Length "+ n);
        System.out.println("Result "+ check);
        System.out.println("elapsed time in ms "+ (t1-t0)/1e6);
        System.out.println("Memory before "+memBefore);
        System.out.println("Memory after "+memAfter);
        System.out.println("Delta memory "+(memAfter - memBefore));
        System.out.println();        
        System.out.println("********************************************");
        System.out.println();
        System.out.println();
        return (t1-t0);
    }

    public static void main(String[] args) {
        TreeMap<Integer,Long> timeMap = new TreeMap<>();
        RegularLanguageIF<Character> lang = buildLanguage();
        lang = buildSimpleLanguage();
        for (int i = 1; i <= 100000; i = i+5000) {
            long deltaT = testN(i, lang);
            timeMap.put(i, deltaT);
        }
        for (int length: timeMap.keySet()) {
            System.out.println("length "+ length + "\tratio "+ (timeMap.get(length)/Double.valueOf(length)));
        }
    }
}
