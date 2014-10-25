package domination.fftoolbox.scraper;

import java.util.ArrayList;
import java.util.List;

import domination.common.Player;
import domination.common.PlayerPosition;

public class FFToolboxPlayerPicker {
	private List<Player> players;
	
	/**
	 * Constructor
	 * @param players the list of players
	 */
	private FFToolboxPlayerPicker(final List<Player> players) {
		this.players = players;
	}
	
	public static FFToolboxPlayerPicker parse(final String rawString, final PlayerPosition position) {
		List<Player> players = new ArrayList<Player>();
		
		int endOfHeader = rawString.lastIndexOf("</th>");
		int beginIndex = rawString.indexOf("<tr", endOfHeader);
		while (beginIndex != -1) {
			int endIndex = rawString.indexOf("</tr>", beginIndex);
			Player player = parsePlayer(rawString.substring(beginIndex + 4, endIndex), position);
			if (player != null) {
				players.add(player);
			}
			beginIndex = rawString.indexOf("<tr", endIndex);
		}
		
		return new FFToolboxPlayerPicker(players);
	}

	private static Player parsePlayer(final String rowString, final PlayerPosition position) {
		if (rowString.contains("Next Page")) {
			return null;
		}
		
		int skip = rowString.indexOf("<td>", 0);

		int	beginPlayerIndex = rowString.indexOf("<td>", skip + 4);
		int endPlayerIndex = rowString.indexOf("</td>", beginPlayerIndex);
		String playerName = parsePlayerName(rowString.substring(beginPlayerIndex + 4, endPlayerIndex), 
				position == PlayerPosition.DEF);
				
		skip = rowString.indexOf("<td>", endPlayerIndex);
		skip = rowString.indexOf("<td>", skip + 4);
		if (position != PlayerPosition.DEF) {
			// The Injury, Average, and Head-to-Head columns don't apply to defenses
			skip = rowString.indexOf("<td>", skip + 4);
			skip = rowString.indexOf("<td>", skip + 4);
			skip = rowString.indexOf("<td>", skip + 4);
		}
		int beginPointsIndex = rowString.indexOf("<td>", skip + 4);
		int endPointsIndex = rowString.indexOf("</td>", beginPointsIndex);
		double points = parsePoints(rowString.substring(beginPointsIndex + 4, endPointsIndex));
		
		return new Player(FFToolboxKeyGenerator.generateKey(playerName, position), playerName, position, 0.0, points);
	}

	private static double parsePoints(String rowString) {
		double returnValue = 0.0;
		try {
			String sanitized = rowString.replace((char)160, ' ').trim();
			returnValue = Double.parseDouble(sanitized);
		}
		catch (final NumberFormatException ex) {
			char character = rowString.charAt(4);
			int charCode = (int) character;
			System.out.println(ex);
		}
		
		return returnValue;
	}


	private static String parsePlayerName(final String rowString, final boolean isD) {
		int beginNameIndex = 0;
		int endNameIndex = rowString.length();
		String subString = rowString;
		
		if (subString.contains("<img")) {
			int imgIndex = subString.indexOf("<img");
			endNameIndex = subString.lastIndexOf("<a", imgIndex);
			subString = subString.substring(0, endNameIndex);
		}

		if (!isD) {
			if (subString.contains("<a")) {
				int startIndex = subString.indexOf("<a");
				beginNameIndex = subString.indexOf(">", startIndex) + 1;
				endNameIndex = subString.indexOf("</a>", beginNameIndex);
			}
		}
		else {
			if (subString.contains("<a")) {
				endNameIndex = subString.indexOf("<a", beginNameIndex);
			}
		}
		
		while (endNameIndex > beginNameIndex && (Character.isWhitespace(subString.charAt(endNameIndex - 1)) || ((int)subString.charAt(endNameIndex -1) == 160))) {
			endNameIndex -= 1;
		}
		
		return subString.substring(beginNameIndex, endNameIndex);
	}

	public List<Player> getPlayers() {
		return players;
	}
}
