package domination.fanduel.scraper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import domination.common.Player;
import domination.common.PlayerPosition;

/**
 * Parses a set of {@link Player}s from the raw input string
 */
public final class FanduelPlayerPicker {

	private final double salaryCap;
	private final List<PlayerPosition> positions;
	private final SortedMap<Integer, Player> players;

	/**
	 * Private constructor - use the parse method to create objects of this type
	 * @param salaryCap the salary cap for this league
	 * @param positions the positions that are included in this league
	 * @param players the players that are included in this league
	 */
	private FanduelPlayerPicker(double salaryCap, List<PlayerPosition> positions, SortedMap<Integer, Player> players) {
		this.salaryCap = salaryCap;
		this.positions = positions;
		this.players = players;
	}
	
	/**
	 * Creates a FanduelPlayerPicker from the input string.  Looks for a <script> block that begins with the correct
	 * script signature and pulls the required information from it.
	 * @param rawString the raw string
	 * @return the created player picker object
	 */
	public static FanduelPlayerPicker parse(final String rawString) {
		String fdString = getFDString(rawString);
		
		double salaryCap = parseSalaryCap(fdString);
		List<PlayerPosition> positions = parsePositions(fdString);
		Map<Integer, Player> players = parsePlayers(fdString);
		Map<Integer, Integer> sortedPlayerId = parsePlayerOrder(fdString);
		
		SortedMap<Integer, Player> sortedPlayers = mapPlayersToOrder(players, sortedPlayerId);
		
		return new FanduelPlayerPicker(salaryCap, positions, sortedPlayers);
	}
	
	/**
	 * 
	 * @param fdString
	 * @return
	 */
	private static Map<Integer, Integer> parsePlayerOrder(final String fdString) {
		Map<Integer, Integer> playerOrder = new HashMap<Integer, Integer>();
		
		final int beginIndex = fdString.indexOf("FD.playerpicker.sortedPlayerIds");
		final int dataBeginIndex = fdString.indexOf("[", beginIndex);
		final int endIndex = fdString.indexOf("]", beginIndex);
		String subString = fdString.substring(dataBeginIndex + 1, endIndex);
		
		String [] ids = subString.split(",");
		int index = 0;
		for (String id : ids) {
			playerOrder.put(index++, Integer.parseInt(id));
		}
		
		return playerOrder;
	}

	private static SortedMap<Integer, Player> mapPlayersToOrder(Map<Integer, Player> players,
			Map<Integer, Integer> sortedPlayerId) {
		TreeMap<Integer, Player> map = new TreeMap<Integer, Player>();
		
		for (Entry<Integer, Integer> entry : sortedPlayerId.entrySet()) {
			if (players.containsKey(entry.getValue())) {
				map.put(entry.getKey(), players.get(entry.getValue()));
			}
		}
		
		return map;
	}

	private static Map<Integer, Player> parsePlayers(final String fdString) {
		final Map<Integer, Player> players = new HashMap<Integer, Player>();
		
		final int beginIndex = fdString.indexOf("FD.playerpicker.allPlayersFullData");
		final int dataBeginIndex = fdString.indexOf("{", beginIndex);
		final int endIndex = fdString.indexOf("}", beginIndex);
		String subString = fdString.substring(dataBeginIndex + 1, endIndex);
		
		int start = 0;
		int end = subString.indexOf("]");
		while (end < subString.length() && end > 0) {
			FanduelPlayer player = parsePlayer(subString.substring(start, end + 1));
			players.put(player.getId(), player);

			start = end + 2;
			end = subString.indexOf("]", start);
		}
		
		return players;
	}
	
	/**
	 * Parse the player data from an input string
	 * @param playerString the player string
	 * @return the created player data object
	 */
	public static FanduelPlayer parsePlayer(final String playerString) {
		String [] idAndArray = playerString.split(":");
		int id = Integer.parseInt(stripFirstAndLast(idAndArray[0]));

		String csd = stripFirstAndLast(idAndArray[1]);
		String [] descriptions = csd.split(",");
		
		PlayerPosition position = parsePlayerPosition(stripFirstAndLast(descriptions[0]));
		String name = stripFirstAndLast(descriptions[1]);
		double salary = Double.parseDouble(stripFirstAndLast(descriptions[5]));
		double points = Double.parseDouble(descriptions[6]);
		
		return new FanduelPlayer(id, FanduelKeyGenerator.generateKey(name, position), name, position, salary, points);
	}
	
	private static PlayerPosition parsePlayerPosition(final String position) {

		if (position.equals("D")) {
			return PlayerPosition.DEF;
		}
		return PlayerPosition.valueOf(position);
	}

	private static List<PlayerPosition> parsePositions(String fdString) {
		List<PlayerPosition> positions = new ArrayList<PlayerPosition>();
		int beginIndex = fdString.indexOf("FD.playerpicker.positions = ");
		int dataBeginIndex = fdString.indexOf("[", beginIndex);
		int endIndex = fdString.indexOf("]", beginIndex);
		String subString = fdString.substring(dataBeginIndex + 1, endIndex);
		String [] tokens = subString.split(",");
		
		for (String token : tokens) {
			positions.add(parsePlayerPosition(stripFirstAndLast(token)));
		}
		
		return positions;
	}

	private static double parseSalaryCap(String fdString) {
		int beginIndex = fdString.indexOf("FD.playerpicker.salaryCap = ");
		int beginDataIndex = fdString.indexOf("=", beginIndex);
		int endIndex = fdString.indexOf(";", beginIndex);
		String subString = fdString.substring(beginDataIndex + 1, endIndex);
		
		return Double.parseDouble(subString);
	}

	private static String getFDString(final String rawString) {
		int beginIndex = rawString.indexOf("if (!window.FD)");
		int endIndex = rawString.indexOf("</script>", beginIndex);
		
		return rawString.substring(beginIndex, endIndex);
	}

	public static String stripFirstAndLast(final String target) {
		if (target.length() < 3) {
			return "";
		}
		return target.substring(1, target.length() - 1);
	}

	public double getSalaryCap() {
		return salaryCap;
	}

	public List<PlayerPosition> getPositions() {
		return positions;
	}

	public SortedMap<Integer, Player> getPlayers() {
		return players;
	}	
}
