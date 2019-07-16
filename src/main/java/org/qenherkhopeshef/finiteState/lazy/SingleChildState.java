package org.qenherkhopeshef.finiteState.lazy;


/**
 * Base implementation for states with only one child.
 * 
 * @author Serge Rosmorduc (serge.rosmorduc@qenherkhopeshef.org)
 * 
 */
abstract class SingleChildState<T> extends AbstractMultiState<T> {

	protected MultiStateIF<T> childState;

	public SingleChildState(MultiStateIF<T> childState,boolean terminal) {
		super(terminal);
		this.childState= childState;
	}

	@Override
       
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj.getClass() != this.getClass()) {
			return false;
		} else {
			SingleChildState<?> other = (SingleChildState<?>) obj;
			return this.childState.equals(other.childState)
					&& this.terminal == other.terminal
					&& this.getParent().equals(other.getParent());
		}
	}

	@Override
	public int hashCode() {
		return this.childState.hashCode() + 31*this.getParent().hashCode();
	}
	
	

}
