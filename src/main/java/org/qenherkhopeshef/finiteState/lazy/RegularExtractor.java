/*
 * Copyright ou © ou Copr. Serge Rosmorduc (2016) 
 * serge.rosmorduc@cnam.fr

 * Ce logiciel est un programme informatique servant à mettre en place 
 * une base de données linguistique pour l'égyptien ancien.

 * Ce logiciel est régi par la licence CeCILL soumise au droit français et
 * respectant les principes de diffusion des logiciels libres. Vous pouvez
 * utiliser, modifier et/ou redistribuer ce programme sous les conditions
 * de la licence [CeCILL|CeCILL-B|CeCILL-C] telle que diffusée par le CEA, le CNRS et l'INRIA 
 * sur le site "http://www.cecill.info".

 * En contrepartie de l'accessibilité au code source et des droits de copie,
 * de modification et de redistribution accordés par cette licence, il n'est
 * offert aux utilisateurs qu'une garantie limitée.  Pour les mêmes raisons,
 * seule une responsabilité restreinte pèse sur l'auteur du programme,  le
 * titulaire des droits patrimoniaux et les concédants successifs.

 * A cet égard  l'attention de l'utilisateur est attirée sur les risques
 * associés au chargement,  à l'utilisation,  à la modification et/ou au
 * développement et à la reproduction du logiciel par l'utilisateur étant 
 * donné sa spécificité de logiciel libre, qui peut le rendre complexe à 
 * manipuler et qui le réserve donc à des développeurs et des professionnels
 * avertis possédant  des  connaissances  informatiques approfondies.  Les
 * utilisateurs sont donc invités à charger  et  tester  l'adéquation  du
 * logiciel à leurs besoins dans des conditions permettant d'assurer la
 * sécurité de leurs systèmes et ou de leurs données et, plus généralement, 
 * à l'utiliser et l'exploiter dans les mêmes conditions de sécurité. 

 * Le fait que vous puissiez accéder à cet en-tête signifie que vous avez 
 * pris connaissance de la licence CeCILL, et que vous en avez accepté les
 * termes.
 */
package org.qenherkhopeshef.finiteState.lazy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * A language built as a sequence of smaller languages, suitable for data
 * extraction.
 * <p>
 * Basically, this reproduces the use of parenthesis group in regexp. The idea
 * is that the language is built of a sequence of smaller languages l1,
 * l2,...ln. A text is matched if it matches their concatenation
 * (l1)(l2)....(ln). In case of match, it'a possible to know exactly what part
 * of the text respectively matches l1, l2...ln.</p>
 * <p>
 * In other words, the main language is always a sequence of languages.
 * It means, for instance, that we want to recognize ([A-Z))([a-z]+), where we
 * have two sub-languages, <code>([A-Z))</code> and <code>([a-z]+)</code>.</p>
 * <p>
 * Note that, as we are working within the boundaries of language theory, we
 * don't use ideas like "greedy" operators and the like. </p>
 * <p>
 * the notion of sequence is important, because the recognizer will provide a
 * marker indicating <em>what</em> was matched.
 * <p>
 * The marker is a list <em>v</em> of integer positions. Each integer <em>v[k]</em> is
 * the
 * <b>position</b> where the matched part for language <em>k</em> <b>finishes</b>.
 * So, the first part is recognized between position 0 and v[0].
 *
 * <dl>
 * <dd>position</dd>
 * <dt>imagine you have a cursor between the letters of a text. Position 0 is
 * before the first letter, position 1 between the first and the second letter,
 * and so on.</dt>
 * </dl>
 *
 * <p>
 * In many cases, the extractor will simply be created by using its constructor.
 * However, you might use the builder from time to time. It's quite convenient
 * if you know at programming time what automaton you want to create. Exempli
 * gratia:
 * </p>
 * <pre>
 * // having imported the static factory methods DSL from RegularLanguageIF :
 * // import static org.qenherkhopeshef.finiteState.lazy.RegularLanguageIF.*;
 * RegularExtractor&lt;Character> rec =
 *               RegularExtractor.&lt;Character>getBuilder()
 *                       .part(star(any()))
 *                       .part(plus(label(new CharacterRangeLabel('0', '9'))))
 *                       .part(not(exact('.')))
 *                       .build();
 * </pre> Please note the use of
 * <code>RegularExtractor.&lt;Character>getBuilder()</code>. The type is needed,
 * else the inference system will fail.
 *
 * <p>Also note that if you need a broader "grain", you can use </p>
 *
 * <p>
 * This class is <strong>thread safe.</strong> It means that the same extractor
 * can be used for multiple texts in different threads if needed.
 * </p>
 *
 * @author rosmord
 * @param <T> the base type for tokens.
 */
public class RegularExtractor<T> {

    private final List<RegularLanguageIF<T>> language;
    private final int postContextSize;

    public RegularExtractor(List<RegularLanguageIF<T>> parts) {
        this.language = new ArrayList<>(parts);
        this.postContextSize = 0;
    }

    public RegularExtractor(RegularLanguageIF<T>... parts) {
        this.language = new ArrayList<>(Arrays.asList(parts));
        this.postContextSize = 0;
    }

    private RegularExtractor(List<RegularLanguageIF<T>> parts, int postContextSize) {
        this.language = new ArrayList<>(parts);
        this.postContextSize = postContextSize;
    }

    /**
     * Builds a language with a "post context".
     * In calling the search function (and currently only the search function), 
     * actual recognition will involve the sequence of both mainLanguage and postContext,
     * but the resulting match will consider only the part matched by the main language, 
     * and the search will continue immediatly after this part.
     * @param <T>
     * @param mainLanguage
     * @param postContext
     * @return a RegularExtractor.
     */
    public static<T> RegularExtractor<T> buildRegularExtractorWithPostContext(List<RegularLanguageIF<T>> mainLanguage, List<RegularLanguageIF<T>> postContext) {
        List<RegularLanguageIF<T>> allParts = new ArrayList<>(mainLanguage);
        allParts.addAll(postContext);
        return new RegularExtractor<>(allParts, postContext.size());
    }
    /**
     * Repeatedly search for matches of the regular language in the input.
     * <p>
     * Will return a list of list. Each sub list corresponds to a specific match
     * of the language. Elements of this list are the positions of the matched
     * language parts. The first entry in a sublist is the position of the match
     * ; the last entry is the position of the end of the match.
     *
     * @param input : the data to search in.
     * @return
     */
    public List<List<Integer>> search(List<T> input) {
        List<RegularLanguageIF<T>> toMatch= new ArrayList<>();
        toMatch.add(RegularLanguageFactory.skip());
        toMatch.addAll(language);
        ReportingLanguageRecognizer<T> aux = new ReportingLanguageRecognizer<>(toMatch);
        aux.setEarlyStop(true);
        ArrayList<List<Integer>> result= new ArrayList<>();
        boolean ok;
        // start of search position
        int pos= 0;
        do {
            ok= aux.recognize(pos, input);
            if (ok) {
                List<Integer> positions = aux.getMarkers();
                // Adds the result. Omit the post context, if any.
                result.add(positions.subList(0, positions.size() - postContextSize));
                // Next search position. Don't include post context in matched text.
                pos= positions.get(positions.size() - 1 - postContextSize);
            }
        } while(ok);
        return result;
    }

    /**
     * Tries to recognize the beginning of a list of tokens, and returns a list
     * of positions indicating the matched languages parts.
     * <p>
     * Formally, it will find the <em>shortest prefix</em> of tokens which is
     * recognized by the language (the algorithm stops as soon as it finds a
     * terminal state).</p>
     * <p>
     * The result can be </p>
     * <dl>
     * <dt>emptySequence</dt>
     * <dd>it means nothing was matched</dd>
     * <dt>a list of positions</dt>
     * <dd>A position is a cursor position in the token list. Position 0 is in
     * front of the first token, position 1 stands between the first token and
     * the second one, etc... If there are n tokens, the last position is n.
     * </dd>
     * <dd>
     * The list of position, for a language l0...ln, will return the respective
     * positions where the matches of l0,...ln end. So, if the match of l0
     * stands between position 0 and position 22, the first position in the
     * result will be 22. Note that the match always starts at position 0 (if
     * not, there is no match !).
     * </dd>
     * </dl>
     *
     * @param tokens the list of tokens to match.
     * @return an optional, which is emptySequence if no match occurred, or contains a
     * list of positions if a match took place.
     */
    public Optional<List<Integer>> recognizesBeginning(List<T> tokens) {
        if (postContextSize != 0)
            throw new IllegalStateException("method not supported with post context");
        ReportingLanguageRecognizer<T> aux = new ReportingLanguageRecognizer<>(language);
        aux.setEarlyStop(true);
        boolean ok = aux.recognize(tokens);
        if (ok) {
            return Optional.of(aux.getMarkers());
        } else {
            return Optional.empty();
        }
    }

    /**
     * Tries to recognize a list of tokens, and returns a list of positions
     * indicating the matched languages parts.
     * <p>
     * The whole list of tokens must be recognized as a word in the
     * language.</p>
     * <p>
     * The result can be </p>
     * <dl>
     * <dt>emptySequence</dt>
     * <dd>it means nothing was matched</dd>
     * <dt>a list of positions</dt>
     * <dd>A position is a cursor position in the token list. Position 0 is in
     * front of the first token, position 1 stands between the first token and
     * the second one, etc... If there are n tokens, the last position is n.
     * </dd>
     * <dd>
     * The list of position, for a language l0...ln, will return the respective
     * positions where the matches of l0,...ln end. So, if the match of l0
     * stands between position 0 and position 22, the first position in the
     * result will be 22. Note that the match always starts at position 0 (if
     * not, there is no match !). The last position in the list will always be
     * equal to the size of the imput.
     * </dd>
     * </dl>
     *
     * @param tokens the list of tokens to match.
     * @return an optional, which is emptySequence if no match occurred, or contains a
     * list of positions if a match took place.
     */
    public Optional<List<Integer>> recognizesAll(List<T> tokens) {
        if (postContextSize != 0)
            throw new IllegalStateException("method not supported with post context");
        
        ReportingLanguageRecognizer<T> aux = new ReportingLanguageRecognizer<>(language);
        aux.setEarlyStop(false);
        boolean ok = aux.recognize(tokens);
        if (ok) {
            return Optional.of(aux.getMarkers());
        } else {
            return Optional.empty();
        }
    }

    /**
     * Returns a builder which will allow you to create a Regular extractor in
     * an easy way.
     *
     * @param <T>
     * @param l0 the first element to match.
     * @return a builder for regular extractors.
     */
    public static <T> Builder<T> getBuilder(RegularLanguageIF<T> l0) {
        return new Builder<T>().part(l0);
    }

    /**
     * Returns a builder which will allow you to create a Regular extractor in
     * an easy way.
     *
     * <p>
     * The language is initially emptySequence.
     *
     * @param <T>
     * @return
     */
    public static <T> Builder<T> getBuilder() {
        return new Builder<>();
    }

    /**
     * A builder for {@link RegularExtractor}.
     * Don't create instances of this class with new, but with the static method
     * {@link RegularExtractor#getBuilder()} or {@link RegularExtractor#getBuilder(org.qenherkhopeshef.finiteState.lazy.RegularLanguageIF) }
     * @param <T> 
     */
    public static class Builder<T> {

        private final ArrayList<RegularLanguageIF<T>> languages = new ArrayList<>();

        Builder() {
        }

        /**
         * Appends a new sub language to the builder.
         *
         * @param l
         * @return
         */
        public Builder<T> part(RegularLanguageIF<T> l) {
            languages.add(l);
            return this;
        }

        /**
         * Actual creation of the extractor.
         *
         * @return
         */
        public RegularExtractor<T> build() {
            return new RegularExtractor<>(languages);
        }
    }
}
