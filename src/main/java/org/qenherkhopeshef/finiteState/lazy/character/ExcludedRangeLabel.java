package org.qenherkhopeshef.finiteState.lazy.character;

import org.qenherkhopeshef.finiteState.lazy.LazyLabelIF;

/**
 * Label which recognizes any character <em>outside of</em> a given range.
 * @author Serge Rosmorduc (serge.rosmorduc@qenherkhopeshef.org)
 */
public class ExcludedRangeLabel implements LazyLabelIF<Character>{

	private final char min, max;
		
	public ExcludedRangeLabel(char min, char max) {
		if (min > max) throw new IllegalArgumentException("bad range "+min+ " "+max);
		this.min = min;
		this.max = max;
	}

	@Override
	public boolean matches(Character token) {
		return token < min || token > max;
	}

	@Override
	public String toString() {
		return "[label NOT : "+min+ ", "+ max+"]";
	}
}
