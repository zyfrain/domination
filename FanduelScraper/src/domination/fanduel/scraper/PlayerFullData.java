package domination.fanduel.scraper;

public final class PlayerFullData {
	private final String id;
	private final String position;
	private final String name;
	private final String price;
	private final String score;
	
	/**
	 * Private constructor - use the parse method to create objects
	 * @param id the FD identifier for the player
	 * @param position the players position
	 * @param name the players name
	 * @param price the price of the player
	 * @param score the score of the player
	 */
	private PlayerFullData(final String id, final String position, final String name, final String price, final String score) {
		this.id = id;
		this.position = position;
		this.name = name;
		this.price = price;
		this.score = score;
	}
	
	/**
	 * Parse the player data from an input string
	 * @param playerString the player string
	 * @return the created player data object
	 */
	public static PlayerFullData parse(final String playerString) {
		System.out.println(playerString);
		return null;
	}
}

