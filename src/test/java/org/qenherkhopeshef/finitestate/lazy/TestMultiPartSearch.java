/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.qenherkhopeshef.finitestate.lazy;

import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

import static org.qenherkhopeshef.finitestate.lazy.RegularLanguageFactory.*;

/**
 * A test which demonstrates how the matches will tend to be managed in cases of ambiguities.
 * <ul>
 * <li> Search will find the result as early as possible</li>
 * <li> each part will end as early as possible - even if it makes the next part longer
 * </ul>
 * 
 * The following test uses the language (ab*)(b*)(b*c).
 * In this case, all "b" will be assigned to the <em>last</em> element.
 * 
 * <p> In a more formal way, if we compare matchResults in lexicographic order,
 * the first result found will be as small as possible.
 * 
 * <p> Hence, for the string "xxabbbbbbbbcd", the (ab*)(b*)(b*c)
 * language <em>could</em> 
 * result in match results such as :
 * <ul>
 * <li> (2,3,3,12)
 * <li> (2,3,6,12)
 * <li> (2,7,9,12)
 * <li> ...
 * </ul>
 * And the match result returned will be the smallest one, i.e. (2,3,3,12).
 * 
 * @author rosmord
 */
public class TestMultiPartSearch {
    @Test
    public void multiPartTest() {
         RegularExtractor<Character> reg = RegularExtractor
                .<Character>getBuilder()
                .part(seq(exact('a'), star(exact('b'))))
                .part(star(exact('b')))
                .part(seq(star(exact('b')), exact('c')))                 
                .build();
        List<MatchResult> res = reg.search(CharHelper.fromString("xxabbbbbbbbcd"));
        assertEquals(new MatchResult(2,3,3,12), res.get(0));
    }
}
