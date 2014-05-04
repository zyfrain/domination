package domination.solver;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class DominationFilterTest {
	@Test
	public void testFilter() {
		final List<Player> players = new ArrayList<Player>();
		players.add(new Player("Player1", PlayerPosition.QB, 15, 15));
		final Player removed1 = new Player("Player2", PlayerPosition.QB, 13, 13);
		players.add(removed1);
		players.add(new Player("Player3", PlayerPosition.QB, 10, 14));
		final Player removed2 = new Player("Player4", PlayerPosition.QB, 9, 9);
		players.add(removed2);
		players.add(new Player("Player5", PlayerPosition.QB, 8, 10));

		DominationFilter.filter(players);
		assertTrue(players.size() == 3);
		assertFalse(players.contains(removed1));
		assertFalse(players.contains(removed2));
	}
}
