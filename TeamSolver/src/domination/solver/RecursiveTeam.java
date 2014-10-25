package domination.solver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import domination.common.Player;

/**
 * A team that is recursively defined from a sub-team. Uses the {@link BaseTeam}
 * as the base case for the recursion. Takes a previously existing sub-team and
 * augments it by one player, caching the sub-team values for performance.
 */
public final class RecursiveTeam implements Team {
	private final double score;
	private final double cost;
	private final Player player;
	private final Team subTeam;

	private transient List<Player> cachedPlayers;
	private transient String cachedRoster;

	public RecursiveTeam(final Player player, final Team subTeam) {
		this.score = player.getScore() + subTeam.getScore();
		this.cost = player.getCost() + subTeam.getCost();
		this.player = player;
		this.subTeam = subTeam;
		this.cachedPlayers = null;
		this.cachedRoster = null;
	}

	@Override
	public double getScore() {
		return this.score;
	}

	@Override
	public double getCost() {
		return this.cost;
	}

	@Override
	public List<Player> getPlayers() {
		if (this.cachedPlayers == null) {
			final ArrayList<Player> playerList = new ArrayList<Player>();
			playerList.addAll(this.subTeam.getPlayers());
			playerList.add(this.player);
			this.cachedPlayers = Collections.unmodifiableList(playerList);
		}
		return this.cachedPlayers;
	}

	@Override
	public boolean containsPlayer(final Player player) {
		if (this.player.equals(player))
			return true;
		else if (this.subTeam.containsPlayer(player))
			return true;

		return false;
	}

	@Override
	public String getRoster() {
		if (this.cachedRoster == null) {
			final String subRoster = this.subTeam.getRoster();
			if (!subRoster.isEmpty()) {
				this.cachedRoster = String.format("%s, %s (%.2f:$%.0f)", subRoster, this.player.getName(), this.player.getScore(), this.player.getCost());
			}
			else {
				this.cachedRoster = String.format("%s (%.2f:$%.0f)", this.player.getName(), this.player.getScore(), this.player.getCost());
			}
		}

		return this.cachedRoster;
	}

	@Override
	public int getRosterCode() {
		return this.player.getName().hashCode() + this.subTeam.getRosterCode();
	}

}
