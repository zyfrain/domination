package domination.solver;

public enum PlayerPosition {
	QB, RB, WR, TE, K, DEF, RBWR;

	public boolean satisfies(final PlayerPosition position) {
		if (position == this)
			return true;
		else if (position == RBWR && (this == WR || this == RB))
			return true;

		return false;
	}
}
