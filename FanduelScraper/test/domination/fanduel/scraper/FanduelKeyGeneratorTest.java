package domination.fanduel.scraper;

import static org.junit.Assert.fail;

import java.io.FileNotFoundException;
import java.util.Map.Entry;

import org.junit.Test;

import domination.common.Player;

public class FanduelKeyGeneratorTest {

	/**
	 * Prints the player keys
	 */
	@Test
	public void printPlayerKeys() {
		try {
			FanduelScraper wrapper = new FanduelScraper("resources/test_wrapper_query.xml", "resources");
			String rawString = wrapper.getWebContents("");
		
			FanduelPlayerPicker picker = FanduelPlayerPicker.parse(rawString);
	
			for (Entry<Integer, Player> player : picker.getPlayers().entrySet()) {
				System.out.println(player.getValue().getKey());
			}
		}
		catch (FileNotFoundException ex) {
			System.out.println(ex.toString());
			fail();
		}
	}
}
