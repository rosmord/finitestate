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
import java.util.Collection;
import java.util.List;

/**
 *
 * @author rosmord
 */
public final class RegularLanguageFactory {
    private RegularLanguageFactory() {}
    
    
    /**
     * Builds a sequence of languages.
     *
     * @param <T> the alphabet (tokens) for the language
     * @param elts a number of languages l1,l2.. based on T.
     * @return a language which is the sequence of l1, l2...
     */
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
    public static <T> RegularLanguageIF<T> or(
            RegularLanguageIF<T>... elts) {
        return or(Arrays.asList(elts));
    }

    /**
     * Builds a union of languages.
     *
     * @param <T> the token type.
     * @param elts
     * @return a language which is a union of languages.
     */
    public static <T> RegularLanguageIF<T> or(
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

    public static <T> RegularLanguageIF<T> not(
            RegularLanguageIF<T> l) {
        return new ComplementLanguage<>(l);
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
    public static <T> RegularLanguageIF<T> inter(
            RegularLanguageIF<T>... l) {
        return new IntersectionLanguage<>(Arrays.asList(l));
    }

    /**
     * Build the complement of a language.
     *
     * @param <T>
     * @param lang
     * @return
     */
    public static <T> RegularLanguageIF<T> comp(
            RegularLanguageIF<T> lang) {
        return new ComplementLanguage<>(lang);
    }

    /**
     * Build the emptyLanguage language.
     * A languages which recognises no string at all.
     * @param <T>
     * @return
     */
    public static <T> RegularLanguageIF<T> empty() {
        return new EmptyLanguage<>();
    }


    /**
         * Build the emptySequence sequence language.
         * A languages which recognises the emptySequence sequence.
         * @param <T>
         * @return
         */
        public static <T> RegularLanguageIF<T> emptySequence() {
            return new EmptyStringLanguage<>();
        }

    /**
     * Build the language which recognizes any one token.
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
        return new LabelLanguage<T>(new SimpleLabel<T>(token));
    }

    /**
     * Builds a language which recognizes a token in a range.
     * For instance,
     * 
     * @param low the lower bound (included)
     * @param high the upper bound (included).
     * @param <T> a Comparable token type.
     * @return
     */
    public static <T extends Comparable<T>> RegularLanguageIF<T> range(T low, T high) {
        return new LabelLanguage<T>(new RangeLabel<T>(low, high));
    }

    /**
        * Builds a language which recognizes a token out of a range.
        * For instance,
        *
        * @param low the lower bound (included)
        * @param high the upper bound (included).
        * @param <T> a Comparable token type.
        * @return a language matching any token out of range low,high.
        */
       public static <T extends Comparable<T>> RegularLanguageIF<T> outOfRange(T low, T high) {
           return new LabelLanguage<T>(new OutOfRangeLabel<T>(low, high));
       }

    public static <T> RegularLanguageIF<T> skip() {
        return new KleeneStarLanguage<>(any());
    }

    public static <T> RegularLanguageIF<T> opt(RegularLanguageIF<T> l) {
        return or(emptySequence(), l);
    }

}
