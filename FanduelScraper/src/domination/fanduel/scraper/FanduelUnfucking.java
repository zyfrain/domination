package domination.fanduel.scraper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import domination.common.Player;
import domination.common.PlayerPosition;

/**
 * This class is for un-fucking the fanduel rankings - removing their intentional fucking with the fantasy predictions
 * NOTE: Not currently used, since their intentional fucking with the projections makes them inherently unreliable
 */
public final class FanduelUnfucking {
	/**
	 * Filter out players from the picker that have a higher predicted score than players sorted higher than them in their sorted list
	 * @param picker the picker
	 * @return the list of players
	 */
	public static List<Player> filterSuspiciousPlayers(final FanduelPlayerPicker picker) {
		List<Player> filteredPlayers = new ArrayList<Player>();
		
		Map<PlayerPosition, Pair<Integer, Player>> playerToPosition = determineHighestPlayer(picker);
		
		for (Entry<Integer, Player> player : picker.getPlayers().entrySet()) {
			if (!hasHigherScore(player, playerToPosition)) {
				filteredPlayers.add(player.getValue());
			}
		}
		
		return filteredPlayers;
	}

	private static boolean hasHigherScore(Entry<Integer, Player> player, 
										  Map<PlayerPosition, Pair<Integer, Player>> comparison) {
		if (player.getValue().getScore() > comparison.get(player.getValue().getPosition()).getRight().getScore())
			return true;
		return false;
	}

	private static Map<PlayerPosition, Pair<Integer, Player>> determineHighestPlayer(
			FanduelPlayerPicker picker) {
		Map<PlayerPosition, Pair<Integer, Player>> highest = new HashMap<PlayerPosition, Pair<Integer, Player>>();
		
		for (Entry<Integer, Player> entry : picker.getPlayers().entrySet()) {
			if (!highest.containsKey(entry.getValue().getPosition())) {
				highest.put(entry.getValue().getPosition(), new ImmutablePair<Integer, Player>(entry.getKey(), entry.getValue()));
			}
			else {
				if (highest.get(entry.getValue().getPosition()).getLeft() > entry.getKey()) {
					highest.put(entry.getValue().getPosition(), new ImmutablePair<Integer, Player>(entry.getKey(), entry.getValue()));
				}
			}
		}
		
		return highest;
	}
}
