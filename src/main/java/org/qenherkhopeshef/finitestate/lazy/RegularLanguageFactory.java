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
package org.qenherkhopeshef.finitestate.lazy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * A number of convenient methods to create a Regular expression.
 *
 * <p>
 * The names of the methods are taken from languages theory, not usual
 * regexps.</p>
 *
 * @author rosmord
 */
public final class RegularLanguageFactory {

    /**
     * Don't create instances of this class !
     */
    private RegularLanguageFactory() {
    }

    /**
     * Builds a sequence of languages.
     *
     * @param <T> the alphabet (tokens) for the language
     * @param elts a number of languages l1,l2.. based on T.
     * @return a language which is the sequence of l1, l2...
     */
    @SafeVarargs
    public static <T> RegularLanguageIF<T> seq(
            RegularLanguageIF<T>... elts) {
        return seq(Arrays.asList(elts));
    }

    /**
     * Builds a sequence of languages.
     *
     * @param <T> the alphabet (tokens) for the language
     * @param elts a number of languages l1,l2.. based on T.
     * @return a language which is the sequence of l1, l2...
     */
    public static <T> RegularLanguageIF<T> seq(
            List<RegularLanguageIF<T>> elts) {
        return new SequenceLanguage<>(new ArrayList<>(elts));
    }

    /**
     * Builds a union of languages.
     *
     * @param <T> the token type.
     * @param elts
     * @return a language which is a union of languages.
     */
    @SafeVarargs
    public static <T> RegularLanguageIF<T> union(
            RegularLanguageIF<T>... elts) {
        return union(Arrays.asList(elts));
    }

    /**
     * Builds a union of languages.
     *
     * @param <T> the token type.
     * @param elts
     * @return a language which is a union of languages.
     */
    public static <T> RegularLanguageIF<T> union(
            Collection<RegularLanguageIF<T>> elts) {
        return new UnionLanguage<>(new ArrayList<>(elts));
    }

    /**
     * Builds an iteration (0 or more) of languages.
     *
     * @param l
     * @param <T> the token type.
     * @return a language representing (l)*
     */
    public static <T> RegularLanguageIF<T> star(
            RegularLanguageIF<T> l) {
        return new KleeneStarLanguage<>(l);
    }

    /**
     * Builds an iteration (1 or more) of languages.
     *
     * @param l a language.
     * @param <T> the token type.
     * @return a language representing (l)+
     */
    public static <T> RegularLanguageIF<T> plus(
            RegularLanguageIF<T> l) {
        return seq(l, star(l));
    }

    /**
     * Builds an intersection of languages.
     *
     * @param <T>
     * @param l : a number of languages.
     * @return a language representing ( &#2229; l)+
     */
    public static <T> RegularLanguageIF<T> inter(
            Collection<RegularLanguageIF<T>> l) {
        return new IntersectionLanguage<>(new ArrayList<>(l));
    }

    /**
     * Builds an intersection of languages.
     *
     * @param <T>
     * @param l : a number of languages.
     * @return a language representing ( &#2229; l)+
     */
    @SafeVarargs
    public static <T> RegularLanguageIF<T> inter(
            RegularLanguageIF<T>... l) {
        return new IntersectionLanguage<>(Arrays.asList(l));
    }

    /**
     * Build the complement of a language.
     * <p>
     * To be more precise : if l is a language on an alphabet A, then
     * complement(l) is the language which contains all elements of A* which are
     * not in l.</p>
     * <p>
     * This is <strong>completely different</strong> from <code>[^...]</code> in
     * regexps (you <em>will</em> make the mistake). For instance,
     * <code>complement(range('0','9'))</code> is a language which recognises
     * any String which is not made of a single digit. Hence, it will recognise
     * the empty string, and also strings made of two digits or more.
     * <p>
     * What you probably want in this case is
     * {@link #outOfRange(Comparable, Comparable)}</p>
     *
     * @param <T>
     * @param lang
     * @return
     */
    public static <T> RegularLanguageIF<T> complement(
            RegularLanguageIF<T> lang) {
        return new ComplementLanguage<>(lang);
    }

    /**
     * Build the emptyLanguage language. A languages which recognises no string
     * at all.
     * <p>
     * Completely different from {@link #emptySequence()}</p>
     *
     * @param <T>
     * @return a language which matches nothing.
     */
    public static <T> RegularLanguageIF<T> emptyLanguage() {
        return new EmptyLanguage<>();
    }

    /**
     * Build the emptySequence sequence language. A languages which recognises
     * the emptySequence sequence.
     * <p>
     * Completely different from {@link #emptyLanguage()}</p>
     *
     * @param <T>
     * @return
     */
    public static <T> RegularLanguageIF<T> emptySequence() {
        return new EmptyStringLanguage<>();
    }

    /**
     * Build the language which recognizes any <b>one</b> token.
     *
     * @param <T>
     * @return
     */
    public static <T> RegularLanguageIF<T> any() {
        return new LabelLanguage<>(new AnyTokenLabel<>());
    }

    /**
     * Build the language which recognizes one token which matches a specific
     * label.
     *
     * @param <T>
     * @param label
     * @return a language recognizing any single token which matches label.
     */
    public static <T> RegularLanguageIF<T> label(LazyLabelIF<T> label) {
        return new LabelLanguage<>(label);
    }

    /**
     * Build the language which recognizes a specific token, using equals.
     *
     * @param <T>
     * @param token the token to recognize
     * @return a language recognizing exactly the token.
     */
    public static <T> RegularLanguageIF<T> exact(T token) {
        return new LabelLanguage<>(new SimpleLabel<>(token));
    }

    /**
     * Build the language which recognizes any one token, save a specific one.
     *
     * @param <T>
     * @param notThisToken the token to recognize
     * @return a language recognizing exactly the token.
     */
    public static <T> RegularLanguageIF<T> different(T notThisToken) {
        return new LabelLanguage<>(
                new ComplementLabel<>(
                        new SimpleLabel<>(notThisToken)));
    }

    /**
     * A language which recognizes any one token in a given set.
     *
     * @param <T>
     * @param set
     * @return
     */
    public static <T> RegularLanguageIF<T> inSet(Set<T> set) {
        return new LabelLanguage<>(new SetLabel<>(set));
    }

    /**
     * A language which recognizes any one token not in a given set.
     *
     * @param <T>
     * @param set
     * @return
     */
    public static <T> RegularLanguageIF<T> notInSet(Set<T> set) {
        return new LabelLanguage<>(
                new ComplementLabel<>(
                        new SetLabel<>(set)));
    }

    /**
     * Builds a language which recognizes a token in a range.
     * <p>
     * For instance, range('0', '9').</p>
     * <p>
     * This is only possible if the base for the language (parameter class T) is
     * Comparable.</p>
     *
     * @param low the lower bound (included)
     * @param high the upper bound (included).
     * @param <T> a Comparable token type.
     * @return
     */
    public static <T extends Comparable<T>> RegularLanguageIF<T> range(T low, T high) {
        return new LabelLanguage<>(new RangeLabel<>(low, high));
    }

    /**
     * Builds a language which recognizes a token out of a range.
     * <p>
     * For instance, outOfRange('0','9') recognizes any character which is
     * <b>not</b> a digit.</p>
     * <p>
     * This is only possible if the base for the language (parameter class T) is
     * Comparable.</p>
     *
     * @param low the lower bound (included)
     * @param high the upper bound (included).
     * @param <T> a Comparable token type.
     * @return a language matching any token out of range low,high.
     */
    public static <T extends Comparable<T>> RegularLanguageIF<T> outOfRange(T low, T high) {
        return new LabelLanguage<>(new OutOfRangeLabel<>(low, high));
    }

    /**
     * A language which matches any sequence of elements of T.
     * <p>
     * Equivalent of <code>.*</code> in regexps.</p>
     *
     * @param <T> the alphabet for the language.
     * @return
     */
    public static <T> RegularLanguageIF<T> skip() {
        return new KleeneStarLanguage<>(any());
    }

    /**
     * A language which either matches the empty string, or a specific language.
     * <p>
     * The equivalent of "?" in regexps.</p>
     *
     * @param l what is optionally recognised.
     * @param <T>
     * @return
     */
    public static <T> RegularLanguageIF<T> opt(RegularLanguageIF<T> l) {
        return union(emptySequence(), l);
    }

    /**
     * Limits a language to a certain maximum length.
     *
     * @param <T>
     * @param l the language to limit
     * @param maxLength the maximum length. If 0, will correspond to "no limit".
     * @return the intersection of l and of the language of strings of maxLength
     * maxLength.
     */
    public static <T> RegularLanguageIF<T> maxLength(RegularLanguageIF<T> l, int maxLength) {
        if (maxLength == 0) {
            return l;
        } else {
            return inter(l, maxLength(maxLength));
        }
    }

    /**
     * The language made of arbitrary sequences of length from 0 to maxLength.
     *
     * @param <T>
     * @param maxLength
     * @return
     */
    public static <T> RegularLanguageIF<T> maxLength(int maxLength) {      
        if (maxLength == 0) {
            return emptySequence();
        } else {
            RegularLanguageIF<T> res = opt(any());
            for (int i = 1; i < maxLength; i++) {
                res = opt(seq(any(), res));
            }
            return res;
        }
    }

    /**
     * The language of arbitrary sequences of length length.
     *
     * @param <T>
     * @param length exact length of the sequence.
     * @return
     */
    public static <T> RegularLanguageIF<T> exactLength(int length) {
        if (length == 0) {
            return emptySequence();
        } else {
            List<RegularLanguageIF<T>> res = new ArrayList<>();
            for (int i = 0; i < length; i++) {
                res.add(any());
            }
            return seq(res);
        }
    }

    /**
     * Builds a sequence which matches an exact list of tokens, passed as
     * varargs.
     *
     * @param <T>
     * @param tokens
     * @return
     */
    @SafeVarargs
    public static <T> RegularLanguageIF<T> exactSequence(T... tokens) {
        List<RegularLanguageIF<T>> elts = new ArrayList<>();
        for (T tok : tokens) {
            elts.add(exact(tok));
        }
        return seq(elts);
    }
}
