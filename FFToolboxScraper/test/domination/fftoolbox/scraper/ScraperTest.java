package domination.fftoolbox.scraper;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.io.FileNotFoundException;

import org.junit.Test;

import domination.common.Player;
import domination.common.PlayerPosition;

public class ScraperTest {

	@Test
	public void generateSample() {
		try
		{
			FFToolboxScraper scraper = new FFToolboxScraper("resources/fftoolbox_query.xml", "resources", "");
			String rawString = scraper.getWebContents("QB", "1", "1");
			assertFalse(rawString.isEmpty());
		}
		catch (FileNotFoundException ex) {
			System.out.println(ex);
			fail();
		}
	}
	
	@Test
	public void parseSample() {
		try
		{
			FFToolboxScraper scraper = new FFToolboxScraper("resources/test_wrapper_query.xml", "resources", "");
			String rawString = scraper.getWebContents("QB", "1", "1");
			
			FFToolboxPlayerPicker players = FFToolboxPlayerPicker.parse(rawString, PlayerPosition.QB);
			for (Player player : players.getPlayers()) {
				System.out.println(String.format("%s - %.2f", player.getName(), player.getScore()));
			}
		}
		catch (FileNotFoundException ex) {
			System.out.println(ex);
			fail();
		}
	}
	
	
}
