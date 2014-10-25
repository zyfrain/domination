package domination.fanduel.scraper;

import static org.junit.Assert.assertEquals;

import java.io.FileWriter;
import java.io.IOException;

import org.junit.Test;

import domination.common.Player;

public class FanduelScraperTest {

	@Test
	public void runWrapper() {
		try {
			FanduelScraper wrapper = new FanduelScraper("resources/test_wrapper_query.xml", "resources");
			String rawString = wrapper.getWebContents("");
		
			FanduelPlayerPicker picker = FanduelPlayerPicker.parse(rawString);
			
			for (Player player : picker.getPlayers().values()) {
				System.out.println(String.format("%s %.4f", player.getName(), (player.getScore() > 0 ? player.getCost() / player.getScore() : -1)));
//				if (player.getScore() >= 26.2) {
//					System.out.println(String.format("%s %.2f $%.2f", player.getName(), player.getScore(), player.getCost()));
//				}
			}
			
			assertEquals(1025, picker.getPlayers().values().size());
			assertEquals(9, picker.getPositions().size());
			assertEquals(60000, picker.getSalaryCap(), .0001);
		} 
		catch (final Exception ex) {
			System.out.println(ex);
		}
	}
	
	@Test
	public void pregameDump() throws IOException {
		FileWriter outputFile = null;
		try {
			String url = "https://www.fanduel.com/e/Game/NFL_Salary_Cap_10788/View";
			int week = 8;
			
			FanduelScraper wrapper = new FanduelScraper("resources/fanduel_query.xml", "resources");
			String rawString = wrapper.getWebContents(url);
		
			FanduelPlayerPicker picker = FanduelPlayerPicker.parse(rawString);
			
			outputFile = new FileWriter("C:\\Temp\\Domination\\Fanduel_Week" + week + ".csv");
			for (Player player : picker.getPlayers().values()) {
				outputFile.write(Player.toCsv(player) + "\n");
			}

		}
		catch (Exception ex) {
			System.out.println(ex);
		}
		finally {
			if (outputFile != null) 
				outputFile.close();
		}
	}
}
