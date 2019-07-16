package org.qenherkhopeshef.finiteState.lazy.lexiconBased;

import org.qenherkhopeshef.finiteState.lazy.RegularExtractor;
import org.qenherkhopeshef.finiteState.lazy.RegularLanguageFactory;

import java.util.Arrays;
import java.util.List;

/**
 * A simple example of what can be done with our generic classes.
 */
public class Main {
	public static void main(String[] args) {
		RegularExtractor<DemoWord> subjectVerbeExtractor =
				RegularExtractor.<DemoWord>getBuilder()
						.part(RegularLanguageFactory.skip())
						.part(DemoWordLabelHelper.posLabel("NN"))
						.part(RegularLanguageFactory.skip())
						.part(RegularLanguageFactory.seq(
								DemoWordLabelHelper.posLabel("VB"),
								RegularLanguageFactory.opt(DemoWordLabelHelper.posLabel("ADV"))))
						.build();
		List<List<Integer>> l = subjectVerbeExtractor.search(Arrays.asList(
				new DemoWord("the", "ART"),   //0
				new DemoWord("big", "ADJ"), // 1
				new DemoWord("cat", "NN"), // 2
				new DemoWord("did", "AUX"), // 3
				new DemoWord("not", "NOT"), // 4
				new DemoWord("see", "VB"), // 5
				new DemoWord("the", "ART"), // 6
				new DemoWord("pretty", "ADJ"), // 7
				new DemoWord("little", "ADJ"), // 8
				new DemoWord("blue", "ADJ"), // 9
				new DemoWord("bird", "NN"), // 10
				new DemoWord("which", "PP"), // 11
				new DemoWord("sang", "VB"), // 12
				new DemoWord("loudly", "ADV") // 13
		));

		System.err.println(l);

	}
}
