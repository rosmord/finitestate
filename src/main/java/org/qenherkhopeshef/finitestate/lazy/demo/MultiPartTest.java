/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.qenherkhopeshef.finitestate.lazy.demo;

import java.util.List;
import org.qenherkhopeshef.finitestate.lazy.CharHelper;
import org.qenherkhopeshef.finitestate.lazy.MatchResult;
import org.qenherkhopeshef.finitestate.lazy.RegularExtractor;
import static org.qenherkhopeshef.finitestate.lazy.RegularLanguageFactory.*;
/**
 *
 * @author rosmord
 */
public class MultiPartTest {
    public static void main(String[] args) {
        RegularExtractor<Character> reg = RegularExtractor
                .<Character>getBuilder()
                .part(seq(exact('a'), star(exact('b'))))
                .part(star(exact('b')))
                .part(seq(star(exact('b')), exact('c')))
                .build();
        List<MatchResult> res = reg.search(CharHelper.fromString("xxabbbbbbbbcd"));
        System.out.println(res);
    }
   
}
