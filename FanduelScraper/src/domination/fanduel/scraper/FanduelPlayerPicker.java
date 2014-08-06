package domination.fanduel.scraper;

import java.util.ArrayList;
import java.util.List;

import domination.solver.Player;
import domination.solver.PlayerPosition;

public final class FanduelPlayerPicker {

	private final double salaryCap;
	private final List<PlayerPosition> positions;
	private final List<Player> players;

	/**
	 * Private constructor - use the parse method to create objects of this type
	 * @param salaryCap the salary cap for this league
	 * @param positions the positions that are included in this league
	 * @param players the players that are included in this league
	 */
	private FanduelPlayerPicker(double salaryCap, List<PlayerPosition> positions, List<Player> players) {
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
		List<Player> players = parsePlayers(fdString);
		
		return new FanduelPlayerPicker(salaryCap, positions, players);
	}
	
	private static List<Player> parsePlayers(final String fdString) {
		final List<Player> players = new ArrayList<Player>();
		
		final int beginIndex = fdString.indexOf("FD.playerpicker.allPlayersFullData");
		final int dataBeginIndex = fdString.indexOf("{", beginIndex);
		final int endIndex = fdString.indexOf("}", beginIndex);
		String subString = fdString.substring(dataBeginIndex + 1, endIndex);
		
		int start = 0;
		int end = subString.indexOf("]");
		while (end < subString.length() && end > 0) {
			players.add(parsePlayer(subString.substring(start, end + 1)));

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
	public static Player parsePlayer(final String playerString) {
		String [] idAndArray = playerString.split(":");
//		int id = Integer.parseInt(stripFirstAndLast(idAndArray[0]));

		String csd = stripFirstAndLast(idAndArray[1]);
		String [] descriptions = csd.split(",");
		
		PlayerPosition position = parsePlayerPosition(stripFirstAndLast(descriptions[0]));
		String name = stripFirstAndLast(descriptions[1]);
		double salary = Double.parseDouble(stripFirstAndLast(descriptions[5]));
		double points = Double.parseDouble(descriptions[6]);
		
		return new Player(name, position, salary, points);
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

	public List<Player> getPlayers() {
		return players;
	}	
}
