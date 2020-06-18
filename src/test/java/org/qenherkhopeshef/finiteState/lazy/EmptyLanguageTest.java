package org.qenherkhopeshef.finitestate.lazy;

import org.qenherkhopeshef.finitestate.lazy.RegularLanguageIF;
import org.qenherkhopeshef.finitestate.lazy.RegularLanguageFactory;
import static org.junit.Assert.*;

import org.junit.Test;
import org.qenherkhopeshef.finitestate.lazy.character.StringToListHelper;

public class EmptyLanguageTest {

	@Test
	public void test() {
		RegularLanguageIF<Character> rec = rec= RegularLanguageFactory.emptyLanguage();
		assertFalse("should not recognize emptySequence string", rec.recognize(StringToListHelper.fromString("")));
		assertFalse("", rec.recognize(StringToListHelper.fromString("ABC")));
	}

}
