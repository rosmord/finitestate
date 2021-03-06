package org.qenherkhopeshef.finitestate.lazy;

class OutOfRangeLabel<T extends Comparable<T>> implements LazyLabelIF<T>{
	private T low, high;
	public OutOfRangeLabel(T low, T high) {
		this.low = low;
		this.high = high;
	}

	@Override
	public boolean matches(T token) {
		return low.compareTo(token) > 0 || token.compareTo(high) > 0;
	}
}
