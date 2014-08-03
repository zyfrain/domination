package domination.fanduel.scraper;

import java.util.ArrayList;
import java.util.List;

public final class FanduelPlayerPicker {

	private final int salaryCap;
	private final List<String> positions;
	private final List<PlayerFullData> players;

	/**
	 * Private constructor - use the parse method to create objects of this type
	 * @param salaryCap the salary cap for this league
	 * @param positions the positions that are included in this league
	 * @param players the players that are included in this league
	 */
	private FanduelPlayerPicker(int salaryCap, List<String> positions, List<PlayerFullData> players) {
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
		
		int salaryCap = parseSalaryCap(fdString);
		List<String> positions = parsePositions(fdString);
		List<PlayerFullData> players = parsePlayers(fdString);
		
		return new FanduelPlayerPicker(salaryCap, positions, players);
	}
	
	private static List<PlayerFullData> parsePlayers(final String fdString) {
		final List<PlayerFullData> players = new ArrayList<PlayerFullData>();
		
		final int beginIndex = fdString.indexOf("FD.playerpicker.allPlayersFullData");
		final int dataBeginIndex = fdString.indexOf("{", beginIndex);
		final int endIndex = fdString.indexOf("}", beginIndex);
		final String subString = fdString.substring(dataBeginIndex, endIndex);

		final String[] playerStrings = subString.split("]");
		
		for (final String playerString : playerStrings) {
			players.add(PlayerFullData.parse(playerString));
		}
		
		return players;
	}

	private static List<String> parsePositions(String fdString) {
		// TODO Auto-generated method stub
		return null;
	}

	private static int parseSalaryCap(String fdString) {
		// TODO Auto-generated method stub
		return 0;
	}

	private static String getFDString(final String rawString) {
		int beginIndex = rawString.indexOf("if (!window.FD)");
		int endIndex = rawString.indexOf("</script>", beginIndex);
		
		return rawString.substring(beginIndex, endIndex);
	}

	public int getSalaryCap() {
		return salaryCap;
	}

	public List<String> getPositions() {
		return positions;
	}

	public List<PlayerFullData> getPlayers() {
		return players;
	}	
}
