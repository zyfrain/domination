package domination.solver;

import java.util.Collections;
import java.util.List;

import domination.common.Player;

/**
 * Empty team used as the base case for the recursive team definition
 */
public final class BaseTeam implements Team {

	@Override
	public double getScore() {
		return 0;
	}

	@Override
	public double getCost() {
		return 0;
	}

	@Override
	public List<Player> getPlayers() {
		return Collections.emptyList();
	}

	@Override
	public boolean containsPlayer(final Player player) {
		return false;
	}

	@Override
	public String getRoster() {
		return "";
	}

	@Override
	public int getRosterCode() {
		return 0;
	}

}
