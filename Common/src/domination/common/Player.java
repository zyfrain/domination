package domination.common;

import java.util.Objects;

/**
 * The definition of a player object
 */
public class Player implements Dominatable {
	private final PlayerPosition position;
	private final double cost;
	private double score;
	private final String name;
	private final String key;

	/**
	 * Constructor
	 * 
	 * @param key the consolidated key representing the player
	 * @param name the players name
	 * @param position the position the player primarily plays
	 * @param cost the cost of the player
	 * @param score the score projected for the player
	 */
	public Player(final String key, final String name, final PlayerPosition position, final double cost, final double score) {
		this.key = key;
		this.name = name;
		this.cost = cost;
		this.score = score;
		this.position = position;
	}

	public Player(Player player) {
		this.key = player.key;
		this.name = player.name;
		this.cost = player.cost;
		this.score = player.score;
		this.position = player.position;
	}

	/**
	 * Creates a player from another player, overriding their cost
	 * @param player the original player
	 * @param cost the new cost
	 * @return the overriden player
	 */
	public static Player override(final Player player, final double cost) {
		return new Player(player.key, player.name, player.position, cost, player.score);
	}
	
	/**
	 * Create a player from a comma separated representation
	 * 
	 * @param csvString the csv string
	 * @return the created player
	 */
	public static Player parseCsv(final String csvString) {
		Objects.requireNonNull(csvString, "Attempted to parse a NULL string");

		final String[] tokens = csvString.split(",");
		if (tokens.length != 5)
			throw new IllegalArgumentException("Attempted to parse invalid string.");

		final String name = tokens[0];
		final PlayerPosition position = PlayerPosition.valueOf(PlayerPosition.class, tokens[1]);
		final double cost = Double.parseDouble(tokens[2]);
		final double score = Double.parseDouble(tokens[3]);
		final String key = tokens[4];
		
		return new Player(key, name, position, cost, score);
	}

	/**
	 * Create a CSV representation of the player
	 * 
	 * @param player the player
	 * @return the csv
	 */
	public static String toCsv(final Player player) {
		Objects.requireNonNull(player, "Attepted to CSVify a NULL player");

		return String.format("%s,%s,%.2f,%.2f,%s", player.name, player.position.toString(), player.cost, player.score, player.key);
	}

	/**
	 * @return the position
	 */
	public PlayerPosition getPosition() {
		return this.position;
	}

	/**
	 * @return the cost
	 */
	@Override
	public double getCost() {
		return this.cost;
	}

	/**
	 * @return the score
	 */
	@Override
	public double getScore() {
		return this.score;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * @return the key
	 * @return
	 */
	public String getKey() {
		return this.key;
	}
}
