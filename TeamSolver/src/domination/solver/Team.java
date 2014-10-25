package domination.solver;

import java.util.List;

import domination.common.Dominatable;
import domination.common.Player;

/**
 * Defines the behavior of a Team object
 */
public interface Team extends Dominatable {
	/**
	 * Retrieve the total score of the team
	 * 
	 * @return the total score
	 */
	@Override
	double getScore();

	/**
	 * Retrieve the total cost of the team
	 * 
	 * @return the total cost
	 */
	@Override
	double getCost();

	/**
	 * Retrieve the list of players on the team
	 * 
	 * @return the list of players
	 */
	List<Player> getPlayers();

	/**
	 * Check if the team contains the player
	 * 
	 * @param player the player to check for
	 * @return true - the team contains the player; false - otherwise
	 */
	boolean containsPlayer(Player player);

	/**
	 * Returns a string representation of the roster for easy printing
	 * 
	 * @return the roster
	 */
	String getRoster();

	/**
	 * Gets a unique integer that represents this combination of players,
	 * disregarding order
	 * 
	 * @return the roster code
	 */
	int getRosterCode();
}
