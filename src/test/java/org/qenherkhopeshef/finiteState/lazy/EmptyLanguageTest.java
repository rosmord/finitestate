package org.qenherkhopeshef.finiteState.lazy;

import static org.junit.Assert.*;

import org.junit.Test;
import org.qenherkhopeshef.finiteState.lazy.character.StringToListHelper;

public class EmptyLanguageTest {

	@Test
	public void test() {
		RegularLanguageIF<Character> rec = rec= RegularLanguageFactory.empty();
		assertFalse("should not recognize emptySequence string", rec.recognize(StringToListHelper.fromString("")));
		assertFalse("", rec.recognize(StringToListHelper.fromString("ABC")));
	}

}
