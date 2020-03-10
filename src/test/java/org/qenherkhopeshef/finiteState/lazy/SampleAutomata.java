package org.qenherkhopeshef.finiteState.lazy;

import static org.qenherkhopeshef.finiteState.lazy.character.CharacterLanguageFactory.*;
import static org.qenherkhopeshef.finiteState.lazy.RegularLanguageFactory.*;

@SuppressWarnings("unchecked")
public class SampleAutomata {

	public static final RegularLanguageIF<Character> A = c('a');
	public static final RegularLanguageIF<Character> B = c('b');
	public static final RegularLanguageIF<Character> C = c('c');

	public static final EmptyStringLanguage<Character> EMPTY_STRING_LANGUAGE = new EmptyStringLanguage<>();
	
	public static final RegularLanguageIF<Character> SKIP = skip();

	public static final RegularLanguageIF<Character> ABC = seq(A,B,C);

	public static final RegularLanguageIF<Character> A_EMPTY_A_B = seq(A, EMPTY_STRING_LANGUAGE, A, B);

	public static final RegularLanguageIF<Character> A_EMPTY_B_EMPTY = seq(A, EMPTY_STRING_LANGUAGE, B,
			EMPTY_STRING_LANGUAGE);

	public static final RegularLanguageIF<Character> A_STAR = star(A);

	/**
	 * Regexp : (ab)*ac
	 */
	public static final RegularLanguageIF<Character> AB_STAR_AC =
			seq(star(seq(A, B)), A, C);

	/**
	 * Regexp : ab | ac
	 */
	public static final RegularLanguageIF<Character> AB_OR_AC = union(seq(A,B), seq(A,C));


	public static final RegularLanguageIF<Character> AB_OR_AC_BB = seq(AB_OR_AC, B, B);

	public static final RegularLanguageIF<Character> A_STAR_BC = seq(star(A), B, C);

	public static final RegularLanguageIF<Character> A_BC_STAR = seq(A, star(seq(B, C)));

	public static final RegularLanguageIF<Character> A_STAR_BC_INTER_A_BC_STAR =
			inter(A_STAR_BC, A_BC_STAR);

	public static final RegularLanguageIF<Character> AA_THEN_A_STAR_BC_INTER_A_BC_STAR_THEN_CC =
			seq(A, A, A_STAR_BC_INTER_A_BC_STAR, C, C);

	
	public static final RegularLanguageIF<Character> NOT_AB_STAR_AC_ENDNOT = complement(AB_STAR_AC);

	public static final RegularLanguageIF<Character> NOT_A_EMPTYAB_ENDNOT = complement(A_EMPTY_A_B);

	public static final RegularLanguageIF<Character> NOT_EMPTY = complement(EMPTY_STRING_LANGUAGE);

	public static final RegularLanguageIF<Character> NOT_ONE = complement(any());

	public static final RegularLanguageIF<Character> NOT_TWO = complement(seq(any(), any()));

	public static final RegularLanguageIF<Character> DOTPLUS_COMPLEMENT = complement(seq(SKIP, any()));

	public static final RegularLanguageIF<Character> NOT_AB_STAR_AC_ENDNOT_ABAB = seq(NOT_AB_STAR_AC_ENDNOT, A, B, A, B);
	
	/**
	 * A tricky one : matches "B" HTML tag without any "B" tag in them.
	 */
	
	public static final RegularLanguageIF<Character> NO_B_TAG =
			complement(seq(SKIP, c('<'), c('b'), c('>'), star(any())));
	
	public static final RegularLanguageIF<Character> B_TAG_NO_BTAG = seq(c('<'), c('b'), c('>'), NO_B_TAG, c('<'),
			c('/'), c('b'), c('>'), c('!') );

}
