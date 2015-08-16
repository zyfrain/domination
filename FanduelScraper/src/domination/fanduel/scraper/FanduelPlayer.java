package domination.fanduel.scraper;

import domination.common.Player;
import domination.common.PlayerPosition;

/**
 * A player with Fanduel associated data
 */
public class FanduelPlayer extends Player {
	private Integer id;
	
	public FanduelPlayer(final Integer id, final String key, final String name, final PlayerPosition position, 
			final double cost, final double score) {
		super(key, name, position, cost, score);
		this.id = id;		
	}
	
	public Integer getId() {
		return id;
	}
}
