package domination.fftoolbox.scraper;

import java.util.List;

import org.junit.Test;

import domination.common.Player;

/**
 * Test classes for the FFToolboxKeyGenerator
 */
public class FFToolboxKeyGeneratorTest {

	@Test
	public void printKeys() {
		List<Player> players = FFToolboxDriver.readPlayers(1, "");

		for (Player player : players) {
			System.out.println(player.getKey());
		}
	}
}
