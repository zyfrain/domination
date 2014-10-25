package domination.fftoolbox.scraper;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import domination.common.Player;
import domination.common.PlayerPosition;

/**
 * Iteratively calls the fftoolbox website and scrapes predicted fantasy values
 */
public final class FFToolboxDriver {
	private FFToolboxDriver() {
	}
	
	/**
	 * Read the players for the given week using the input cookie
	 * @param week the week
	 * @param cookie the cookie
	 * @return the list of players
	 */
	public static List<Player> readPlayers(final int week, final String cookie) {
		List<Player> players = new ArrayList<Player>();
		FFToolboxScraper scraper = new FFToolboxScraper("resources/fftoolbox_query.xml", "resources", cookie);
		
		try {
		players.addAll(parsePosition(scraper, PlayerPosition.QB, 1, week));
		players.addAll(parsePosition(scraper, PlayerPosition.RB, 1, week));
		players.addAll(parsePosition(scraper, PlayerPosition.WR, 1, week));
		players.addAll(parsePosition(scraper, PlayerPosition.K, 1, week));
		players.addAll(parsePosition(scraper, PlayerPosition.TE, 1, week));
		players.addAll(parsePosition(scraper, PlayerPosition.DEF, 1, week));
		}
		catch (final FileNotFoundException ex) {
			System.out.println(ex);
		}
		
		return players;
	}

	/**
	 * Reads the site for a position for player projections and parses those projections using the appropriate picker
	 * @param scraper the scraper
	 * @param pos the position
	 * @return the list of players
	 * @throws FileNotFoundException
	 */
	private static List<Player> parsePosition(
			final FFToolboxScraper scraper, final PlayerPosition pos, final int page, final int week) throws FileNotFoundException {
		final String rawString = scraper.getWebContents(pos.toString(), String.format("%d", page), String.format("%d", week));
		FFToolboxPlayerPicker picker = FFToolboxPlayerPicker.parse(rawString, pos);
		return picker.getPlayers();
	}
}
