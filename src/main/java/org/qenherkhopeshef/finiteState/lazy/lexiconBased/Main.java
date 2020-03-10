package org.qenherkhopeshef.finiteState.lazy.lexiconBased;

import org.qenherkhopeshef.finiteState.lazy.RegularExtractor;
import org.qenherkhopeshef.finiteState.lazy.RegularLanguageFactory;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A simple example of what can be done with our generic classes.
 *
 * We build an automaton which recognizes "Noun .* VERB ADV". And we extract those
 * sequences.
 */
public class Main {

    public static void main(String[] args) {
        RegularExtractor<DemoWord> subjectVerbeExtractor
                = RegularExtractor.<DemoWord>getBuilder()
                        .part(DemoWordLabelHelper.posLabel("NN"))
                        .part(RegularLanguageFactory.skip())
                        .part(RegularLanguageFactory.seq(
                                DemoWordLabelHelper.posLabel("VB"),
                                RegularLanguageFactory.opt(DemoWordLabelHelper.posLabel("ADV"))))
                        .build();
        final List<DemoWord> text = Arrays.asList(
                new DemoWord("the", "ART"), //0
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
        );
        List<List<Integer>> l = subjectVerbeExtractor.search(text);
        l.stream().map(m -> m.stream().map(pos -> text.get(pos).getWord()).collect(Collectors.toList())).forEach(res -> System.out.println(res));
    }
}
