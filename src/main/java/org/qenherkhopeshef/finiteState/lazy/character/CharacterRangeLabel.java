package org.qenherkhopeshef.finitestate.lazy.character;

import org.qenherkhopeshef.finitestate.lazy.LazyLabelIF;

/**
 * Label which matches a range of chars.
 * first &lt; last
 * 
 * <p>Deprecated, because it can be done with the regular methods of RegularLanguageFactory.
 * @author Serge Rosmorduc (serge.rosmorduc@qenherkhopeshef.org)
 *
 */
@Deprecated()
public class CharacterRangeLabel implements LazyLabelIF<Character>{
	private char first, last;
	
	/**
	 * Creates a range from first to last.
	 * <p> Precondition first &lt; last.
	 * @param first
	 * @param last
	 */
	public CharacterRangeLabel(char first, char last) {
		super();
		if (first >= last)
			throw new IllegalArgumentException("first < last !! "+first+ " "+last);
		this.first = first;
		this.last = last;
	}

        @Override
	public boolean matches(Character token) {
		return first <= token && token <= last; 
	}
	
	@Override
	public String toString() {
		return "[label :"+ first+ "," + last+"]";
	}
	
}
