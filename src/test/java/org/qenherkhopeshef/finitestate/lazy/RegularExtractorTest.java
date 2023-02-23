package org.qenherkhopeshef.finitestate.lazy;

import org.qenherkhopeshef.finitestate.lazy.RegularExtractor;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

import static org.qenherkhopeshef.finitestate.lazy.RegularLanguageFactory.*;

public class RegularExtractorTest {

    /**
     * Tests that a search with an empty language performs correctly.
     */
    @Test
    public void testEmptySearch() {
        RegularExtractor<Character> extractor = RegularExtractor.<Character>getBuilder().build();
        String text = "abc";
        List<MatchResult> res = extractor.search(CharHelper.fromString(text));
        List<MatchResult> expected = Arrays.asList(
                new MatchResult(0, 0),
                new MatchResult(1, 1),
                new MatchResult(2, 2),
                new MatchResult(3, 3)
        );
        assertEquals(expected, res);
    }

    @Test
    /**
     * Tests that a search with a language which match the empty string performs
     * correctly.
     */
    public void testEmptyStringSearch() {
        RegularExtractor<Character> extractor
                = RegularExtractor.<Character>getBuilder()
                        .part(opt(any()))
                        .build();
        String text = "abc";
        List<MatchResult> res = extractor.search(CharHelper.fromString(text));
        List<MatchResult> expected = Arrays.asList(
                new MatchResult(0, 0),
                new MatchResult(1, 1),
                new MatchResult(2, 2),
                new MatchResult(3, 3)
        );
        assertEquals(expected, res);
    }

    /**
     * Tests that a search for a single char..
     */
    @Test
    public void testOnCharSearch() {
        RegularExtractor<Character> extractor = RegularExtractor.<Character>getBuilder()
                .part(exact('b'))
                .build();
        String text = "abcb";
        List<MatchResult> res = extractor.search(CharHelper.fromString(text));
        List<MatchResult> expected = Arrays.asList(
                new MatchResult(1, 2),
                new MatchResult(3, 4)
        );
        assertEquals(expected, res);
    }

    /**
     * Tests for a search with two parts.
     */
    @Test
    public void testTwoPartsSearch() {
        RegularExtractor<Character> extractor = RegularExtractor.<Character>getBuilder()
                .part(exact('b'))
                .part(exact('c'))
                .build();
        String text = "aaabcb";
        List<MatchResult> res = extractor.search(CharHelper.fromString(text));
        List<MatchResult> expected = Arrays.asList(
                new MatchResult(3,4,5)                                
        );
        assertEquals(expected, res);
    }

    @Test
    /**
     * Note : as the system extracts the <em>shortest</em> possible solutions,
     * each individual digit will be found...
     */
    public void integerExtractTest() {
        RegularExtractor<Character> extractor = RegularExtractor.<Character>getBuilder()
                .part(plus(range('0', '9')))
                .build();
        String text = "il y a 100 manières de travailler 2 à 4.";
        List<MatchResult> res = extractor.search(CharHelper.fromString(text));
        List<String> extracted = res.stream().map(r -> text.substring(r.getFirstPosition(), r.getLastPosition())).collect(Collectors.toList());
        assertEquals(Arrays.asList("1", "0", "0", "2", "4"), extracted);
    }

    @Test
    /**
     * In this test, we add a non-digit marker.
     */
    public void integerExtractTestFrontier() {
        RegularExtractor<Character> extractor = RegularExtractor.<Character>getBuilder()
                .part(plus(range('0', '9')))
                .part(outOfRange('0', '9'))
                .build();
        String text = "il y a 100 manières de travailler 2 à 4.";
        List<MatchResult> res = extractor.search(CharHelper.fromString(text));
        List<String> extracted = res.stream().map(
                r -> text.substring(r.getFirstPosition(), r.getStartOfPart(r.getNumberOfParts() - 1))).collect(Collectors.toList());
        assertEquals(Arrays.asList("100", "2", "4"), extracted);
    }
    
     @Test
    /**
     * In this test, we add a non-digit marker to force the match to contain the whole digit.
     */
    public void integerExtractTestFrontierWithPostContext() {
        RegularExtractor<Character> extractor = RegularExtractor.<Character>getBuilder()
                .part(plus(range('0', '9')))
                .startPostContext()
                .part(outOfRange('0', '9'))
                .build();
        String text = "il y a 100 manières de travailler 2 à 4.";
        List<MatchResult> res = extractor.search(CharHelper.fromString(text));
        List<String> extracted = res.stream().map(
                r -> text.substring(r.getFirstPosition(), r.getLastPosition())).collect(Collectors.toList());
        assertEquals(Arrays.asList("100", "2", "4"), extracted);
    }

}
