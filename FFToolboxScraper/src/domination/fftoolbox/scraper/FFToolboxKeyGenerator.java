package domination.fftoolbox.scraper;

import java.util.HashMap;
import java.util.Map;

import domination.common.PlayerPosition;

/**
 * Generates player keys for players in FFToolbox format
 */
public final class FFToolboxKeyGenerator {
	
	private static final Map<String, String> explicitConversions;
	
	// A list of players to generate a specific key
	// This allows for manual deconfliction of players that
	//   have non-standard names (e.g. RGIII, who can be listed as Robert Griffin or Robert Griffin III)
	static {
		explicitConversions = new HashMap<String, String>();
		explicitConversions.put("St. Louis", "St Louis DEF");
//		explicitConversions.put("Charles Johnson", "Charles Johnson WR");
//		explicitConversions.put("Calvin Johnson", "Calvin Johnson WR");
//		explicitConversions.put("Malcolm Floyd", "Malcolm Floyd WR");
//		explicitConversions.put("Michael Floyd", "Michael Floyd WR");
	}
	
	/**
	 * Static utility class - do not instantiate
	 */
	private FFToolboxKeyGenerator() {
	}
	
	/**
	 * Generates a consolidated key for an FFToolbox player
	 * @param name the player name
	 * @param position the player position
	 * @return the key
	 */
	public static String generateKey(final String name, final PlayerPosition position) {

		if (explicitConversions.containsKey(name)) {
			return explicitConversions.get(name);
		}
		
		if (position != PlayerPosition.DEF){
			String [] tokens = name.split(" ");
			return tokens[0] + " " + tokens[tokens.length -1] + " " + position.toString();
		}
		else {
			return name + " " + position.toString();
		}
	}
}
