package domination.solver;

import java.util.Objects;

/**
 * The definition of a player object
 */
public final class Player implements Dominatable {
	private final PlayerPosition position;
	private final double cost;
	private final double score;
	private final String name;

	/**
	 * Constructor
	 * 
	 * @param name the players name
	 * @param position the position the player primarily plays
	 * @param cost the cost of the player
	 * @param score the score projected for the player
	 */
	public Player(final String name, final PlayerPosition position, final double cost, final double score) {
		this.name = name;
		this.cost = cost;
		this.score = score;
		this.position = position;
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
		if (tokens.length != 4)
			throw new IllegalArgumentException("Attempted to parse invalid string.");

		final String name = tokens[0];
		final PlayerPosition position = PlayerPosition.valueOf(PlayerPosition.class, tokens[1]);
		final double cost = Double.parseDouble(tokens[2]);
		final double score = Double.parseDouble(tokens[3]);

		return new Player(name, position, cost, score);
	}

	/**
	 * Create a CSV representation of the player
	 * 
	 * @param player the player
	 * @return the csv
	 */
	public static String toCsv(final Player player) {
		Objects.requireNonNull(player, "Attepted to CSVify a NULL player");

		return String.format("%s,%s,%.2f,%.2f", player.name, player.position.toString(), player.cost, player.score);
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
}
