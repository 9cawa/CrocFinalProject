package ru.croccode.hypernull.geometry;

import java.util.Objects;

public class Offset {

	private final int dx;
	private final int dy;

	public Offset(int dx, int dy) {
		this.dx = dx;
		this.dy = dy;
	}

	public int dx() {
		return dx;
	}

	public int dy() {
		return dy;
	}

	public int length2() {
		return dx * dx + dy * dy;
	}

	public double length() {
		return Math.sqrt(length2());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Offset)) return false;
		Offset offset = (Offset) o;
		return dx == offset.dx && dy == offset.dy;
	}

	@Override
	public int hashCode() {
		return Objects.hash(dx, dy);
	}

	@Override
	public String toString() {
		return "(" + dx + ", " + dy + ")";
	}
}
