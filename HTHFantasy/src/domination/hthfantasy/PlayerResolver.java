package domination.hthfantasy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import domination.common.Player;

/**
 * Combines lists of players into one list
 */
public final class PlayerResolver implements Runnable {

	private static final boolean DEBUG_ENABLED = false;
	
	private final Map<String, Player> resolvedPlayers;
	private final List<Pair<Player, String>> unresolvedPlayers;
	
	private final List<Player> costedPlayers;
	private final List<Player> pointedPlayers;
	
	/**
	 * Constructor
	 * @param costedPlayers players that have been assigned valid costs
	 * @param pointedPlayers players that have been assigned valid points
	 */
	public PlayerResolver(final List<Player> costedPlayers, final List<Player> pointedPlayers) {
		this.costedPlayers = Objects.requireNonNull(costedPlayers);
		this.pointedPlayers = Objects.requireNonNull(pointedPlayers);
		
		this.resolvedPlayers = new HashMap<String, Player>();
		this.unresolvedPlayers = new ArrayList<Pair<Player, String>>();
	}
	
	@Override
	public synchronized void run() {
		final Map<String, Player> keyedCostedPlayers = buildMap(costedPlayers);
		final Map<String, Player> keyedPointedPlayers = buildMap(pointedPlayers);
		
		resolvedPlayers.clear();
		unresolvedPlayers.clear();
		
		for (final Entry<String, Player> pp : keyedPointedPlayers.entrySet()) {
			if (keyedCostedPlayers.containsKey(pp.getKey())) {
				final Player costedPlayer = keyedCostedPlayers.get(pp.getKey());
				resolvedPlayers.put(pp.getKey(), new Player(pp.getKey(), costedPlayer.getName(), costedPlayer.getPosition(), 
						costedPlayer.getCost(), pp.getValue().getScore()));
				if (DEBUG_ENABLED) {
					System.out.println(String.format("Matching %s with scores %.2f and %.2f", pp.getKey(), pp.getValue().getScore(), costedPlayer.getScore()));
				}
			}
			else {
				unresolvedPlayers.add(new ImmutablePair<Player, String>(pp.getValue(), "Pointed"));

				if (DEBUG_ENABLED) {
					System.out.println(String.format("Failed Pointed: %s", pp.getKey()));
				}
			}
		}
		
		for (Entry<String, Player> rp : keyedCostedPlayers.entrySet()) {
			if (!resolvedPlayers.containsKey(rp.getKey())) {
				if (DEBUG_ENABLED) {
					System.out.println(String.format("Failed Costed: %s", rp.getKey()));
				}
				unresolvedPlayers.add(new ImmutablePair<Player, String>(rp.getValue(), "Costed"));
			}
		}
	}
	
	/**
	 * Builds a map between the player and the player key
	 * @param players the players
	 * @return the map
	 */
	private Map<String, Player> buildMap(List<Player> players) {
		final Map<String, Player> map = new HashMap<String, Player>();
		for (final Player player : players) {
			map.put(player.getKey(), player);
		}
		return map;
	}

	public List<Player> getResolvedPlayers() {
		return new ArrayList<Player> (resolvedPlayers.values());
	}
	
	public List<Pair<Player, String>> getUnresolvedPlayers() {
		return unresolvedPlayers;
	}
}
