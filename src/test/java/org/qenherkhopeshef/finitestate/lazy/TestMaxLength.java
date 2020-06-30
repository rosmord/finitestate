/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.qenherkhopeshef.finitestate.lazy;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import static org.qenherkhopeshef.finitestate.lazy.RegularLanguageFactory.*;
import static org.junit.Assert.*;

/**
 * Test that the "maxlength" system for searches works correctly.
 *
 * @author rosmord
 */
public class TestMaxLength {

    private RegularExtractor<Character> astarbEx;

    @Before
    public void init() {
        astarbEx = RegularExtractor.<Character>getBuilder()
                .part(seq(exact('a'), skip(), exact('b')))
                .build();
    }

    @Test
    public void simpleMaxLengthOk() {
        List<MatchResult> l1 = astarbEx.search(CharHelper.fromString("xxxa1bhjh"), 4);
        assertEquals(1, l1.size());
        assertEquals(new MatchResult(3, 6), l1.get(0));
    }

    @Test
    public void simpleMaxLengthOk1() {
        List<MatchResult> l1 = astarbEx.search(CharHelper.fromString("xxxabhjh"), 4);
        assertEquals(1, l1.size());
        assertEquals(new MatchResult(3, 5), l1.get(0));
    }

    @Test
    public void simpleMaxLengthOkLimit() {
        List<MatchResult> l1 = astarbEx.search(CharHelper.fromString("xxxa11bhjh"), 4);
        assertEquals(1, l1.size());
        assertEquals(new MatchResult(3, 7), l1.get(0));
    }

    @Test
    public void lengthOverLimit() {
        List<MatchResult> l1 = astarbEx.search(CharHelper.fromString("xxxa111bhjh"), 4);
        assertEquals(0, l1.size());
    }

    @Test
    public void twoMatches() {
        List<MatchResult> l1 = astarbEx.search(CharHelper.fromString("xxxa11bhjhaxb"), 4);
        assertEquals(2, l1.size());
    }

    @Test
    public void oneTooLongOtherOk() {
        String input = "xxxa111bhjhaxbx";
        List<MatchResult> l1 = astarbEx.search(CharHelper.fromString(input), 4);
        assertEquals(1, l1.size());
        assertEquals("axb", l1.get(0).extractFullMatch(input));
    }
}
