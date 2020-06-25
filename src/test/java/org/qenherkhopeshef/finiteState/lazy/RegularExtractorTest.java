package org.qenherkhopeshef.finitestate.lazy;

import org.qenherkhopeshef.finitestate.lazy.RegularExtractor;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

import static org.qenherkhopeshef.finitestate.lazy.RegularLanguageFactory.*;

public class RegularExtractorTest {

	@Test
	/**
	 * Note : as the system extracts the <em>shortest</em> possible solutions, each
	 * individual digit will be found...
	 */
	public void integerExtractTest() {
		RegularExtractor<Character> extractor = RegularExtractor.<Character>getBuilder()
				.part(plus(range('0', '9')))
				.build();
		String text = "il y a 100 manières de travailler 2 à 4.";
		List<List<Integer>> res = extractor.search(CharHelper.fromString(text));
		List<String> extracted = res.stream().map(r -> text.substring(r.get(0), r.get(1))).collect(Collectors.toList());
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
		List<List<Integer>> res = extractor.search(CharHelper.fromString(text));
		List<String> extracted = res.stream().map(r -> text.substring(r.get(0), r.get(1))).collect(Collectors.toList());
		assertEquals(Arrays.asList("100", "2", "4"), extracted);
	}

}