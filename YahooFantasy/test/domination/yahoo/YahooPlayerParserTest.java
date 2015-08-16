package domination.yahoo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;

import org.junit.Test;

import domination.common.Player;

public class YahooPlayerParserTest {

	@Test
	public void parseTestFile() throws FileNotFoundException, IOException {
		String filename = "C:\\Temp\\Domination\\Yahoo\\testing.txt";
		
		File inputFile = new File(filename);
		InputStreamReader reader = new FileReader(inputFile);
		BufferedReader buff = new BufferedReader(reader);
		
		String line = null;
		final StringBuilder xmlString = new StringBuilder();
		while ((line = buff.readLine()) != null) {
			xmlString.append(line);
		}
		
		YahooPlayerParser parser = new YahooPlayerParser();
		Collection<Player> players = parser.parse(xmlString.toString());
		for (Player player : players) {
			System.out.println(Player.toCsv(player));
		}
	}
}
