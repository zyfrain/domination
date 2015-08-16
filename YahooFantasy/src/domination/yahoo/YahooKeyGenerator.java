package domination.yahoo;

import java.util.HashMap;
import java.util.Map;

import domination.common.PlayerPosition;

/**
 * A key generator for Fanduel
 */
public final class YahooKeyGenerator {

	private static final Map<String, String> explicitConversions;
	
	// A list of players to generate a specific key for
	// This allows for manual deconfliction of players that
	//   have non-standard names (e.g. RGIII, who can be listed as Robert Griffin or Robert Griffin III)
	static {
		explicitConversions = new HashMap<String, String>();
		explicitConversions.put("Roy Helu Jr.", "Roy Helu RB");
		explicitConversions.put("Boobie Dixon", "Anthony Dixon RB");
		explicitConversions.put("Daniel Herron", "Dan Herron RB");
		explicitConversions.put("EJ Manuel", "E.J. Manuel QB");
		explicitConversions.put("Cecil Shorts III", "Cecil Shorts WR");
		explicitConversions.put("Chris Ivory", "Christopher Ivory RB");
		explicitConversions.put("Louis Murphy Jr.", "Louis Murphy WR");
		explicitConversions.put("Robert Griffin III", "Robert III QB");
		explicitConversions.put("St. Louis", "St Louis DEF");
		explicitConversions.put("Stevie Johnson", "Steve Johnson WR");
		explicitConversions.put("Ted Ginn Jr.", "Ted Jr. WR");
		explicitConversions.put("Harold Hoskins", "Gator Hoskins TE");
		explicitConversions.put("Odell Beckham Jr.", "Odell Jr. WR");
		explicitConversions.put("Silas Redd Jr.", "Silas Redd RB");
		explicitConversions.put("Steve Smith Sr.", "Steve Smith WR");
	}

	/**
	 * Static utility class - do not instantiate
	 */
	private YahooKeyGenerator() {
		
	}
	
	/**
	 * Generates a key for Fanduel players
	 * @param name the player name
	 * @param position the player position
	 * @return the key
	 */
	public static String generateKey(final String name, final PlayerPosition position) {
		
		if (explicitConversions.containsKey(name)) {
			return explicitConversions.get(name);
		}
		
		return name + " " + position.toString();
	}

}
