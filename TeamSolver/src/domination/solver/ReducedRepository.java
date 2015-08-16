package domination.solver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import domination.common.Player;
import domination.common.PlayerPosition;

/**
 * Organizes and reduces the list of players to increase algorithmic efficiency.
 * Splits the players into a position keyed map, which will reduce the amount of
 * filtering necessary when moving from one position to the next.
 */
public class ReducedRepository {
	private final Collection<PlayerPosition> positions;
	private final Map<PlayerPosition, List<Player>> playersByPosition;
	private boolean logFlag;

	/**
	 * Constructor.
	 * 
	 * @param positions the list of positions
	 */
	public ReducedRepository(final Collection<PlayerPosition> positions, final boolean flag) {
		this.positions = positions;
		this.playersByPosition = new HashMap<PlayerPosition, List<Player>>();
		this.logFlag = flag;
		
		for (final PlayerPosition position : positions) {
			if (!this.playersByPosition.containsKey(position)) {
				this.playersByPosition.put(position, new ArrayList<Player>());
			}
		}
	}

	/**
	 * Constructor.
	 * 
	 * @param positions the positions
	 * @param players the players
	 */
	public ReducedRepository(final Collection<PlayerPosition> positions, final Collection<Player> players, final boolean flag) {
		this(positions, flag);
		this.add(players);
	}

	/**
	 * Remove a player from the repository
	 * @param player the player
	 * @return true if the player is removed, false if not
	 */
	public boolean removePlayer(final Player player) {
		boolean value = false;
		List<Player> players = this.playersByPosition.get(player.getPosition());
		if (players != null) {
			value = players.remove(player);
		}
		
		return value;
	}
	
	/**
	 * Iterates of the list of players and adds them to each position that they
	 * {@link PlayerPosition#satisfies(PlayerPosition)}. 
	 * 
	 * @param players the list of players
	 */
	private void add(final Collection<Player> players) {
		for (final PlayerPosition position : this.playersByPosition.keySet()) {
			for (final Player player : players) {
				if (player.getPosition().satisfies(position)) {
					this.playersByPosition.get(position).add(player);
				}
			}

		}
		
		logPositionCounts();
	}

	/**
	 * Returns and unmodifiable list of players that play a given position.
	 * 
	 * @param position the position
	 * @return the unmodifiable list of players
	 */
	public List<Player> getPlayers(final PlayerPosition position) {
		if (this.playersByPosition.containsKey(position))
			return Collections.unmodifiableList(this.playersByPosition.get(position));
		return Collections.emptyList();
	}

	/**
	 * @return the positions
	 */
	public Collection<PlayerPosition> getPositions() {
		return this.positions;
	}

	/**
	 * Sets the log flag
	 * @param value ?
	 */
	public void setLogFlag(final boolean value) {
		this.logFlag = value;
	}
	
	/**
	 * Logs the position counts
	 */
	private void logPositionCounts() {
		if (logFlag) {
			StringBuilder positionCounts = new StringBuilder();
			positionCounts.append("Reduced to player counts: " + System.getProperty("line.separator"));
			for (Entry<PlayerPosition, List<Player>> entry : playersByPosition.entrySet()) {
				positionCounts.append(String.format("  %s: %d\n", entry.getKey().toString(), entry.getValue().size()));
			}
			System.out.println(positionCounts.toString());
		}
	}

}
