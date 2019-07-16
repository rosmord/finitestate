package org.qenherkhopeshef.finiteState.lazy;

/**
 * Possible base class for specific multistates.
 * 
 * @author Serge Rosmorduc (serge.rosmorduc@qenherkhopeshef.org)
 * @param <T> base type for the alphabet of our language.
 */
abstract class AbstractMultiState<T> implements MultiStateIF<T> {
	protected boolean terminal = false;

	public AbstractMultiState(boolean terminal) {
		super();
		this.terminal= terminal;
	}

	public boolean isTerminal() {
		return terminal;
	}
}
