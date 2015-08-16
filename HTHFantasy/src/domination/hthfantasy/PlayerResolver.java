package domination.hthfantasy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import domination.common.Player;

/**
 * Combines lists of players into one list and applies any overrides
 */
public final class PlayerResolver implements Runnable {

	private static final boolean DEBUG_ENABLED = false;
	
	private Collection<Player> resolvedPlayers;
	private final List<Pair<Player, String>> unresolvedPlayers;
	
	private final Collection<Player> costedPlayers;
	private final Collection<Player> pointedPlayers;
	private final Collection<String> playerOverrides;
	
	/**
	 * Constructor
	 * @param costedPlayers players that have been assigned valid costs
	 * @param pointedPlayers players that have been assigned valid points
	 */
	public PlayerResolver(final Collection<Player> costedPlayers, final Collection<Player> pointedPlayers,
			final Collection<String> overrides) {
		this.costedPlayers = Objects.requireNonNull(costedPlayers);
		this.pointedPlayers = Objects.requireNonNull(pointedPlayers);
		
		this.resolvedPlayers = new ArrayList<>(); 
		this.unresolvedPlayers = new ArrayList<Pair<Player, String>>();
		this.playerOverrides = new ArrayList<String>(overrides);
	}
	
	@Override
	public synchronized void run() {
		final Map<String, Player> keyedCostedPlayers = buildMap(costedPlayers);
		final Map<String, Player> keyedPointedPlayers = buildMap(pointedPlayers);
		
		resolvedPlayers.clear();
		unresolvedPlayers.clear();
		
		Map<String, Player> playerMap = new HashMap<>();
		
		for (final Entry<String, Player> pp : keyedPointedPlayers.entrySet()) {
			if (keyedCostedPlayers.containsKey(pp.getKey())) {
				final Player costedPlayer = keyedCostedPlayers.get(pp.getKey());
				playerMap.put(pp.getKey(), new Player(pp.getKey(), costedPlayer.getName(), costedPlayer.getPosition(), 
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
			if (!playerMap.containsKey(rp.getKey())) {
				if (DEBUG_ENABLED) {
					System.out.println(String.format("Failed Costed: %s", rp.getKey()));
				}
				unresolvedPlayers.add(new ImmutablePair<Player, String>(rp.getValue(), "Costed"));
			}
		}
		
		resolvedPlayers = processOverrides(playerMap.values());
	}
	
	/**
	 * If there are overriden players, find them in the list and replace them with an overriden copy
	 * @param players the list of players
	 * @return the list of players with overrides applied
	 */
	private Collection<Player> processOverrides(final Collection<Player> players) {
		List<Player> overridenPlayers = new ArrayList<Player>();
		
		for (Player player : players) {
			if (playerOverrides.contains(player.getKey())) {
				overridenPlayers.add(Player.override(player, -100.0));
			} else {
				overridenPlayers.add(player);
			}
		}
		
		return overridenPlayers;
	}
	
	/**
	 * Builds a map between the player and the player key
	 * @param players the players
	 * @return the map
	 */
	private Map<String, Player> buildMap(Collection<Player> players) {
		final Map<String, Player> map = new HashMap<String, Player>();
		for (final Player player : players) {
			map.put(player.getKey(), player);
		}
		return map;
	}

	public Collection<Player> getResolvedPlayers() {
		return resolvedPlayers;
	}
	
	public List<Pair<Player, String>> getUnresolvedPlayers() {
		return unresolvedPlayers;
	}
}
