# A library for searching using regular languages on complex alphabets

**Note** : the current `gradlew` file runs under java 8, and will have problems with recent versions of Java. 
On the other hand, the current `build.gradle` is not usable with recent versions of gradle.

I'll fix it one day. Meanwhile, adapting the `build.gradle` file is relatively easy. I don't do it yet because I would need to update the code which publishes the library to MavenCentral. **Why on earth does gradle lack the support of old gradle files? retrocompatibility is supposed to be a strong point of Java**.

Most regular expression libraries work on character strings, which is somehow a limitation in a number of cases.

For instance, in some cases, you want to search on a list of structured tokens. Some of your criteria
might deal with word spellings, others with some grammatical features (plural, part-of-speech, etc...)

It's generally possible to work around those problems by providing a specific textual representation of the document, 
but one might prefer a more direct approach.

This library allows you to search on almost arbitrary lists of tokens, *without backtracking*, and with the full power of
regular languages (including **complementation** and **intersection**).

## Introduction

This library allows you to search the occurrences of some patterns on lists of tokens of an arbitrary type T. T can be as simple as `Character` or `String`, and as complex as you want.

The patterns are basically a combination of 

- labels, which recognize individual tokens depending on their features
    
    - the simpler label is of course an element of type T. For Characters, a label of 'a' would recognize the character 'a'.
    - more complex labels might recognize tokens depending on some of their characteristics. For instance, all characters whose code is larger than the code of 'a';
    - you can create your own labels - it's more or less necessary when you work with custom classes as tokens.
- the empty list
- concatenation of languages
- repetition of languages
- union of languages
- intersection of languages
- complementation of languages (i.e. all strings *not* recognised by the language)

Note that the system is close to the mathematical definition of regular languages. There is no definition of "greedy" or "lazy" star operator for instance.

All method will search the *shortest possible string*.

## Short primer (with characters)

To use this library to search in character lists :

1. import the static methods from `RegularLanguageFactory`
~~~~java
import static org.qenherkhopeshef.finitestate.lazy.RegularLanguageFactory.*;
~~~~
2. Use them to build a RegularExtractor
~~~~java
RegularExtractor<Character> rec = 
                RegularExtractor.<Character>getBuilder()
                        .part(plus(range('0', '9')))
                        .part(outOfRange('0','9')))
                        .build();
~~~~
This language will recognize strings of digits, ended by something which is not a digit.

Note that "parts" are elements which the system will be able to extract. More precisely, the `search` function will 
give us the position of each part. See below.


3. search:
~~~java
 String s = "dhfjhdfsh0023.dfsd35fds";
 List<Character> toMatch = StringToListHelper.fromString(s);
 List<MatchResult> result = rec.search(toMatch);
~~~

The result of the search will be a list of MatchResult. **Each match result indicates the positions of the matched text in the searched list**. A `MatchResult` has methods to access the 
positions of the whole matched text, as well as the positions of the text matched by individual `parts`.


Thus, the text between the first position and the last position is the whole match. We could write :
~~~java
for (MatchResult m: result) {
	System.out.println(
		s.substring(m.getFirstPosition(),
		            m.getLastPosition()));
}
~~~
to list them.

As we are often interested to know the matched text, the `MatchResult` class has a method called `extractFullMatch` 
to extract it.

## The elements of `RegularLanguageFactory`

`RegularLanguageFactory`provides a large number of methods to describe languages. Each one returns a 
`RegularLanguageIF<T>`.

### One-token long languages

Those are the basic bricks to match tokens.

any()
: the language made of all lists of length 1 over the alphabet T.

exact(T token)
: the language consisting of a single list of length 1 containing the token `token`. For instance, `exact('a')` is the `RegularLanguageIF<Character>` which recognizes the List ['a'].

different(T notThisToken)
: the language consisting of a single list of length 1 containing a token which is *different from* `notThisToken`. For instance, `different('a')` will recognise ['b'] or ['v'], but not ['a'] nor ['b','c'].

inSet(tokenSet)
: the language consisting of a single list of length 1 containing a token in `tokenSet`.

notInSet(tokenSet)
: the language consisting of a single list of length 1 containing a token not in `tokenSet`.

range(T low, T high)
: the language consisting of a single list of length 1 containing a token between *low* and *high* (included)

outOfRange(T low, T high)
: the language consisting of a single list of length 1 containing a token not between *low* and *high*.

label(LazyLabelIF`<T>` label)
: advanced operation; you supply a LazyLabelIF which is basically a lambda expression over a label, telling if this label is accepted or not.

### Empty language and empty sequence

emptySequence()
: the empty sequence, of length 0.

emptyLanguage()
: the empty language, which recognises *nothing*. Not the same at all as the empty sequence. If you add the emptyLanguage() to a sequence, this sequence will recognise nothing. If you add the emptySequence(), it doesn't change the sequence.

In most cases, you want to use `emptySequence()`.

### Sequences and the like
seq(languages)
: a sequence of languages (elements are either a list, or given as separate arguments). Example : `seq(exact('a'), any(),exact('b'))` will recognise the same as pattern `/a.b/`

skip()
: an arbitrary sequence of tokens, including the empty one. Think of `.*` in regexps.

maxLength(int maxLength)
: recognises all lists of length ≤ `maxLength`.

exactLength(int length)
: recognises all lists of length `length`.

maxLength(RegularLanguageIF`<T>` l, int maxLength)
: recognises all lists **in language `l`** of length ≤ `maxLength`. Useful if you want to limit the length of a possible match. Note, however, that it's quite expensive in terms of performances. There is an alternative version of the `search` methods which is more efficient.


### Combinations of languages
opt(language)
: recognises `language` or the empty list, i.e. "optionally, language".

star(language)
: recognises zero or 𝑛 repetitions of `language`

plus(language)
: recognises one or 𝑛 repetitions of `language`

union(languages)
: an union of languages - a list is recognised iff it belongs to at least one of those languages.

inter(languages)
: an intersection of languages - a list is recognised iff it belongs to all of those languages.

complement(language)
: the complement of a language - a list is recognised iff it is *not* recognised by this language.

Note the `inter` and `complement` operator. They are not that usual in regexp libraries (although they belong to regular languages).

## An example where T is a custom type.
We want to match tagged text. Each word in the entry will be tagged with its part of speech. For instance, we would have
the text :

|Word |The |cat | eats|
|-----|----|----|-----|
|**Part of speech**| ART|NN  | VB  |

 Hence we create a class `DemoWord` which is basically a couple `(spelling,partOfSpeech)`.
 The class has the following skeleton : 
 ~~~java
 public class DemoWord {
 	public String getPartOfSpeech() {...}
 	public String getWord() {...}
 }
 ~~~~

 Then, we need two classes which implements `LazyLabelIF`.
 - one which will match parts of speech,
 - the other which will match words.
 
As `LazyLabelIF` is a functional interface, we can use lambda notation to provide simple implementations :
 ~~~java
public final class DemoWordLabelHelper  {
  /**
   * A label which matches a specific word
   */
  public static RegularLanguageIF<DemoWord> textLabel(String text) {
 		return RegularLanguageFactory.label(demoWord -> demoWord.getWord().equals(text));
   }
 
 /**
   * A label which matches a specific part of speech
   */
   public static RegularLanguageIF<DemoWord> posLabel(String partOfSpeech) {
 			return RegularLanguageFactory.label(demoWord -> demoWord.getPartOfSpeech().equals(partOfSpeech));
    }
 }
 ~~~
 Alternatively, we could use standard java classes, for instance :
 ~~~java
 public class PartOfSpeechLabel implements LazyLabelIF<DemoWord> {
	 private String partOfSpeechToMatch;
	 public PartOfSpeechLabel(String s) {
		 partOfSpeechToMatch = s;
	 }

	 @Override
	 public boolean matches(DemoWord token) {
		 return token.getPartOfSpeech()
		 	.equals(partOfSpeechToMatch);
	 }
 }
 ~~~
 
Now, we can build regular languages on DemoWords. This one will recognize texts made of Noun, anything, Verb, and optionaly adverb (note that the adverb will never be recognized, as the system favours the shortest match).
~~~java
 RegularExtractor<DemoWord> subjectVerbeExtractor =
 	RegularExtractor.<DemoWord>getBuilder()
 			.part(DemoWordLabelHelper.posLabel("NN"))
 			.part(RegularLanguageFactory.skip())
 			.part(RegularLanguageFactory.seq(
 				DemoWordLabelHelper.posLabel("VB"),
 				RegularLanguageFactory.opt(DemoWordLabelHelper.posLabel("ADV"))))
 		.build();
  List<MatchResult> l = subjectVerbeExtractor.search(Arrays.asList(
 		new DemoWord("the", "ART"),   //0
  		new DemoWord("big", "ADJ"), // 1
 		new DemoWord("cat", "NN"), // 2
 		new DemoWord("did", "AUX"), // 3
 		new DemoWord("not", "NOT"), // 4
 		new DemoWord("see", "VB"), // 5
 		new DemoWord("the", "ART"), // 6
 		new DemoWord("little", "ADJ"), // 8
 		new DemoWord("blue", "ADJ"), // 9
 		new DemoWord("bird", "NN"), // 10
 		new DemoWord("which", "PP"), // 11
 		new DemoWord("sang", "VB"), // 12
 		new DemoWord("loudly", "ADV") // 13
 		));
~~~


## Interesting patterns

To limit the size of a matched sequence to 𝑛, the system builds the union of the empty sequence, the sequence with one token, the sequence with two tokens... the sequence with 𝑛 tokens.

We then intersect this language with the language whose length we want to limit. 

This automaton is a bit expensive, however. As the rest of
the library, the search complexity is O(n), where *n* is the size of the input. The problem is that the overhead due to the automaton itself is very large.  We provide a more efficient way
to do this with a revised version of the `search` method.

## Differences with "usual" regular expressions

The definition of languages used in this library is mathematically sound. But some features are not that intuitive. In particular, 
in most regexp libraries, complementation is available only at the token level. Here, it can be done also at the *language* level. And it's quite different.

For instance, `complement(exact('a'))` is the complement of the Character-based language 'a'. That is, it's all strings, save the one made of only ONE 'a'. It includes such strings as "aa", "aaa", and the empty string.

We had originally named the operation `not`, instead of `complement`, but it was very ambiguous and not readable at all.

If you want the equivalent of regexp `/[^a]/`, you would use `different('a')`.

## Performances
I can't vouch for the performances of the library in terms of *speed* on the *actual patterns* you will use, but 
both theory and practice indicate that it performs in *linear time*, even for patterns for which usual libraries 
will suffer from exponentially bad results.

For instance, the demo `WorstCaseTest` shows that with the expression `a(b|c+)*de`, and tests on strings of the form "ac*",
the time to parse the string is linear regarding the length of the string. Basically, the ratio $\frac{time}{number of chars}$ is more or less $5.10^{-6}s$ for a char.

The speed depends heavily on the pattern, though. For instance, replacing `a(b|c+)*de` with `a(c*)de` gives a speed of 
$1.10^{-6}s$

Memory tests should be done too ; I suspect in some cases, the library may be very memory-hungry, as :
- the size of a deterministic automaton may be exponentially larger than the size of the non-deterministic equivalent ;
- the library allows one to create seemingly simple automaton (for instance `any()`) which would contain hundreds of 
  transitions if fully written.


## Licence

This library is distributed under the [CeCILL-C](https://cecill.info/licences.fr.html) Licence (see...) which is compatible with the LGPL.
