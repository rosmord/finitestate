package org.qenherkhopeshef.finitestate.lazy;

import org.qenherkhopeshef.finitestate.lazy.RegularLanguageIF;
import org.qenherkhopeshef.finitestate.lazy.RegularLanguageFactory;
import static org.junit.Assert.*;

import org.junit.Test;

public class EmptyLanguageTest {

	@Test
	public void test() {
		RegularLanguageIF<Character> rec =  RegularLanguageFactory.emptyLanguage();
		assertFalse("should not recognize emptySequence string", rec.recognize(CharHelper.fromString("")));
		assertFalse("", rec.recognize(CharHelper.fromString("ABC")));
	}

}
