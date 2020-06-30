/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.qenherkhopeshef.finitestate.lazy;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author rosmord
 */
public class MatchResultTest {

    private MatchResult simple;
    private MatchResult empty;
    private MatchResult twoParts;

    @Before
    public void prepare() {
        simple = new MatchResult(3, 5);
        empty = new MatchResult(4, 4);
        twoParts = new MatchResult(10, 15, 20);
    }

    @Test
    public void testSizeEmpty() {
        assertEquals(0, empty.getMatchLength());
    }

    @Test
    public void testMatchLength1() {
        assertEquals(2, simple.getMatchLength());
    }

    @Test
    public void testMatchLength2() {
        assertEquals(10, twoParts.getMatchLength());
    }

    @Test
    public void testFirst() {
        assertEquals(3, simple.getFirstPosition());
        assertEquals(4, empty.getFirstPosition());
        assertEquals(10, twoParts.getFirstPosition());
    }

    @Test
    public void testLast() {
        assertEquals(5, simple.getLastPosition());
        assertEquals(4, empty.getLastPosition());
        assertEquals(20, twoParts.getLastPosition());
    }

}
