package org.qenherkhopeshef.finitestate.lazy;

/**
 * A label which recognizes any token.
 * @author Serge Rosmorduc (serge.rosmorduc@qenherkhopeshef.org)
 *
 * @param <T> token type.
 */
class AnyTokenLabel<T> implements  LazyLabelIF<T> {

	public AnyTokenLabel() {
		super();
	}

        @Override
	public boolean matches(T token) {
		return true;
	}

	@Override
	public String toString() {
		return "[ .]";
	}

}
