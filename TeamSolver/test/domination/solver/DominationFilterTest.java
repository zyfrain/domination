package domination.solver;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import domination.common.DominationFilter;
import domination.common.Player;
import domination.common.PlayerPosition;

public class DominationFilterTest {
	@Test
	public void testFilter() {
		final List<Player> players = new ArrayList<Player>();
		players.add(new Player("Key", "Player1", PlayerPosition.QB, 15, 15));
		final Player removed1 = new Player("Key", "Player2", PlayerPosition.QB, 13, 13);
		players.add(removed1);
		players.add(new Player("Key", "Player3", PlayerPosition.QB, 10, 14));
		final Player removed2 = new Player("Key", "Player4", PlayerPosition.QB, 9, 9);
		players.add(removed2);
		players.add(new Player("Key", "Player5", PlayerPosition.QB, 8, 10));

		List<Player> filteredPlayers = DominationFilter.filter(players);
		assertTrue(filteredPlayers.size() == 3);
		assertFalse(filteredPlayers.contains(removed1));
		assertFalse(filteredPlayers.contains(removed2));
	}
}
