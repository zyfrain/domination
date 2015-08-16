package domination.common;

/**
 * Enumeration describing player positions
 */
public enum PlayerPosition {
	QB, RB, WR, TE, K, DEF, RBWR, // Football
	CB, DE, S, DT, LB,            // Football - Defensive
	PG, SG, SF, PF, C;            // Basketball

	/**
	 * Helps resolve RBWR type positions where RB or WR would satisfy the slot
	 * @param position
	 * @return
	 */
	public boolean satisfies(final PlayerPosition position) {
		if (position == this)
			return true;
		else if (position == RBWR && (this == WR || this == RB))
			return true;

		return false;
	}
}
