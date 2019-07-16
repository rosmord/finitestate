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

/**
 * Lazy regular language recognition; the language is built with static methods from 
 * {@link org.qenherkhopeshef.finiteState.lazy.RegularLanguageIF} .
 *
 * <p>This library allows one to build efficient automata on complex languages.</p>
 * 
 * <p>
 * The classes in this system allows one to use the full power of Regular
 * Languages, including intersection and complementation, which are not
 * available easily through standard regular expression.</p>
 *
 * <p>
 * As we use "lazy" techniques, the basic alphabet for the languages doesn't
 * need to be defined extensively, which can be interesting in some cases. For
 * instance, in the Ramses projects, the search is done on complex entries with
 * many associated attributes. The standard way to process this with DFA
 * libraries would be to serialize the properties as text. With the present
 * library, we only need to define a Subclass of LazyToken.
 * <p>
 * The library is still somehow DFA based, but it uses lazy determinization.
 * Most algorithms for doing this work on automata, but the automaton for
 * complement needs an explicitly determinized automaton.
 * <p> </p>From an object oriented point of view, we found that the best
 * way to express our problem was in fact to work in terms of <em>language,</em>
 * not of <em>automata</em>. The exact details (and in particular <em>what</em>
 * the accept() method is supposed to send back) are a bit subtle.
 * </p>
 * <p>
 * More precisely :</p>
 * <ul>
 * <li>A sequence is a list (A1,A2...An) of languages, which recognizes L(A) .
 * L(A2) .... L(An)</li>
 * <li> (explanations to be continued)</li>
 * </ul>
 *
 * <h3>Uses</h3>
 * <p>
 * To use this part of the library, you need to choose a <em>token base
 * class,</em>
 * which might be a simple class like Character, or an abstract class/an
 * interface when tokens may have various classes.</p>
 * <p>
 * Then, you need at least one label classes. A label represent a class of
 * tokens. Example labels for character may be : a label which recognizes one
 * specific character, a label which recognizes a range of characters, etc... As
 * an example, and for testing purposes, a number of classes for building
 * character-oriented automata are provided. </p>
 * <p>
 * In order to base our language on characters, we start by defining a class for
 * character tokens. We call it
 * <code>CharacterToken</code> and it must implement LazyToken (think of
 * removing this restriction, as LazyToken does not define anything interesting
 * ????).</p>
 * <p>
 * Then we define a number of Label classes based on our type. A Label is the
 * base element for our system. It's able to recognize <em>one</em> "letter" of
 * our "alphabet". The only necessary one is <code>CharacterLabel</code>, which
 * is able to recognize one particular character. Other possible (and provided)
 * labels might be able to match any character in a range, or a class of unicode
 * characters (punctuation, etc...).</p>
 *
 * <h3>Other design goals</h3>
 * <p> When working, for instance, on text databases, we wish to be able to 
 * parallelize searches. So the same language should be usable to search multiple 
 * texts at once. Hence, the search result should be stored in objects which represent
 * a particular search, not the language itself. The current version uses a simple list 
 * of languages to represent a complex request, but this is not very nice. We might 
 * make the SequenceLanguage public, but it would'nt be logical if we make it public, but 
 * hide the others.</p>
 * <h3>Example</h3>
 * <p>Let's build an automaton to extract numbers from a text.</p>
 */
package org.qenherkhopeshef.finiteState.lazy;
