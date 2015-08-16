package domination.yahoo;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

import org.apache.commons.lang3.tuple.Pair;

import domination.common.Player;

public class YahooDriver {
	/** URL template for retrieving players */
	private static String URL_TEMPLATE = "http://fantasysports.yahooapis.com/fantasy/v2/game/%s/players;start=%d;count=25"
			+ ";position=QB;position=RB;position=WR;position=TE;position=K;position=DEF"
			+ "/stats;type=week;week=%d";

	/** Number of players (1000) / Number per query (25) */
	private static int NUM_QUERY = 50;

	/** Number of players per query */
	private static int PER_QUERY = 25; 
	
	/** Yahoo game key for football */
	private static String FOOTBALL_GAME_KEY = "nfl";
	
	private OAuthConnection connection;
	
	/**
	 * Constructor
	 */
	public YahooDriver() {
		this.connection = new OAuthConnection();
	}
	
	/**
	 * Authenticate with Yahoo for the first time this session
	 * @return the token and token secret required to use the connection
	 * @throws IOException if authentication fails 
	 */
	public Pair<String, String> authenticate() throws IOException {
		return connection.authenticate();
	}
	
	/**
	 * Authenticate with Yahoo using an established session
	 * @param token the token
	 * @param tokenSecret the token secret
	 */
	public void authenticate(final String token, final String tokenSecret) {
		connection.authenticate(token, tokenSecret);
	}
	
	/**
	 * Retrieve actual performance for Football game players for a given week
	 * @param week the week number
	 * @return the collection of unique retrieved players
	 * @throws IOException if the retrieval fails
	 */
	public Collection<Player> retrieveFootballActuals(final int week) throws IOException {
		Collection<Player> players = retrieveActuals(week, FOOTBALL_GAME_KEY, new YahooPlayerParser());
		return players;
	}
	
	/**
	 * Retrieve actual performance from Yahoo
	 * @param week the week to retrieve statistics for
	 * @param key the sport to retrieve statistics for
	 * @param parser a player parser to convert the Yahoo XML to players
	 * @return the collection of unique retrieved players
	 * @throws IOException if the retrieval fails
	 */
	private Collection<Player> retrieveActuals(final int week, final String key, final YahooPlayerParser parser) 
		throws IOException {
		HashMap<String, Player> players = new HashMap<>();
		
		for (int i = 0; i < NUM_QUERY; i++) {
			final String results = connection.retrieveQuery(String.format(URL_TEMPLATE, key, i * PER_QUERY, week));
			final Collection<Player> queryPlayers = parser.parse(results);
			for (Player player : queryPlayers) {
				players.put(player.getKey(), player);
			}
		}
		
		return players.values();
	}
}
