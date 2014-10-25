package domination.solver;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import domination.common.PlayerPosition;

public class PlayerPositionTest {

	@Test
	public void testSatisfies() {
		assertTrue(PlayerPosition.QB.satisfies(PlayerPosition.QB));
		assertTrue(PlayerPosition.RB.satisfies(PlayerPosition.RBWR));
		assertTrue(PlayerPosition.WR.satisfies(PlayerPosition.RBWR));
		assertFalse(PlayerPosition.RBWR.satisfies(PlayerPosition.RB));
	}
}
