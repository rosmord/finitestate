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
 * Lazy regular language recognition.
 * 
 * <h3>Short guide to classes and use</h3>
 * 
 * The language described and recognised below are sequences of objects of an arbitrary class
 * T. Hence most classes here will take this base class as a parameter. For instance, for 
 * plain text, the class might be {@link java.lang.Character}.
 * 
 * <ul>
 *   <li> build parts of the search expression 
 *  using {@link org.qenherkhopeshef.finitestate.lazy.RegularLanguageFactory}</li>
 *   <li> direct match might be done using 
 *  {@link org.qenherkhopeshef.finitestate.lazy.RegularLanguageIF#recognize}</li>
 *   <li> in general, however, one will use {@link org.qenherkhopeshef.finitestate.lazy.RegularExtractor}</li>
 *   <li> Customisation of the language will usually involve implementing {@link LazyLabelIF}
 * </ul>
 * the language is built with static methods from 
 * {@link org.qenherkhopeshef.finitestate.lazy.RegularLanguageIF} .
 *
 * <h3>Introduction and implementation notes</h3>
 * <p>This library allows one to build <em>efficient</em>
 *    recognisers on complex regular languages. Execution time 
 * is linear in terms of the entry's length</p>
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
 * The library uses <em>lazy determinization.</em>
 * Most algorithms for doing this would work on automata, 
 * but the automaton for complement needs an explicitly determinized automaton.
 * <p> From an object oriented point of view, we found that the best
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
 * a particular search, not the language itself.</p>
 * <h3>First example : character-based</h3>
 * <p>An automaton to extract numbers from a text.</p>
 * <pre>
 *          RegularExtractor<Character> rec1 = 
 *               RegularExtractor.<Character>getBuilder()
 *                       .part(star(any()))
 *                       .part(plus(range('0', '9')))
 *                       .part(outOfRange('0','9')))
 *                       .build();
 *       String s = "dhfjhdfsh0023.dfsd35fds";
 *       toMatch = StringToListHelper.fromString(s);
 *       rec.recognizesBeginning(toMatch).ifPresent((m)-&gt; {System.out.println(m);});  
 * </pre>
 * <h3>Second example : generic search with custom labels</h3>
 * <p>We want to match tagged text. Each word in the entry will be tagged with its part of speach.
 * 
 * Hence, we create a class <code>DemoWord</code> which is basically a couple (spelling,pos).
 * The class has the following skeleton : 
 * <pre>
 * public class DemoWord {
 *	
 *	public String getPartOfSpeech() {...}
 *
 *	public String getWord() {...}
 * }
 * </pre>
 * 
 * Then, we need two classes which implements 
 * {@link org.qenherkhopeshef.finitestate.lazy.LazyLabelIF}. 
 * One which will match parts of speeches,
 * the other which will match words.
 * 
 * <p> In this example, we use lambda notation to provide simple implementations :
 * <pre>
 * public final class DemoWordLabelHelper  {
 *
 *
 *	public static RegularLanguageIF<DemoWord> textLabel(String text) {
 *		return RegularLanguageFactory.label(demoWord -&gt; demoWord.getWord().equals(text));
 *	}
 *
 *	public static RegularLanguageIF<DemoWord> posLabel(String partOfSpeech) {
 *			return RegularLanguageFactory.label(demoWord -&gt; demoWord.getPartOfSpeech().equals(partOfSpeech));
 *	}
 * }
 * </pre>
 * 
 * Now, we can build regular languages on DemoWords :
 * <pre>
 * RegularExtractor&lt;DemoWord&gt; subjectVerbeExtractor =
 *				RegularExtractor.&lt;DemoWord&gt;getBuilder()
 *						.part(DemoWordLabelHelper.posLabel("NN"))
 *						.part(RegularLanguageFactory.skip())
 *						.part(RegularLanguageFactory.seq(
 *								DemoWordLabelHelper.posLabel("VB"),
 *								RegularLanguageFactory.opt(DemoWordLabelHelper.posLabel("ADV"))))
 *						.build();
 * List&lt;List&lt;Integer&gt;&gt; l = subjectVerbeExtractor.search(Arrays.asList(
 *				new DemoWord("the", "ART"),   //0
 * 				new DemoWord("big", "ADJ"), // 1
 *				new DemoWord("cat", "NN"), // 2
 *				new DemoWord("did", "AUX"), // 3
 *				new DemoWord("not", "NOT"), // 4
 *				new DemoWord("see", "VB"), // 5
 *				new DemoWord("the", "ART"), // 6
 *				new DemoWord("pretty", "ADJ"), // 7
 *				new DemoWord("little", "ADJ"), // 8
 *				new DemoWord("blue", "ADJ"), // 9
 *				new DemoWord("bird", "NN"), // 10
 *				new DemoWord("which", "PP"), // 11
 *				new DemoWord("sang", "VB"), // 12
 *				new DemoWord("loudly", "ADV") // 13
 *		));
 * </pre>
 */
package org.qenherkhopeshef.finitestate.lazy;
