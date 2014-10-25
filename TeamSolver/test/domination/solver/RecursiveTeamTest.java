package domination.solver;

import static org.junit.Assert.*;

import org.junit.Test;

import domination.common.Player;
import domination.common.PlayerPosition;

public class RecursiveTeamTest {
	@Test
	public void testTeamConstruction() {
		final Player playerOne = new Player("Key", "One", PlayerPosition.QB, 10, 9);
		final Player playerTwo = new Player("Key", "Two", PlayerPosition.RB, 5, 4);
		final Player playerThree = new Player("Key", "Three", PlayerPosition.WR, 3, 2);
		
		final Team teamOne = new RecursiveTeam(playerOne, new BaseTeam());
		final Team teamTwo = new RecursiveTeam(playerTwo, teamOne);
		final Team teamThree = new RecursiveTeam(playerThree, teamOne);
		final Team teamFour = new RecursiveTeam(playerThree, teamTwo);
		
		assertEquals(10, teamOne.getCost(), .0001);
		assertEquals(9, teamOne.getScore(), .0001);
		assertEquals(15, teamTwo.getCost(), .0001);
		assertEquals(13, teamTwo.getScore(), .0001);
		assertEquals(13, teamThree.getCost(), .0001);
		assertEquals(11, teamThree.getScore(), .0001);
		assertEquals(18, teamFour.getCost(), .0001);
		assertEquals(15, teamFour.getScore(), .0001);
	}

}
