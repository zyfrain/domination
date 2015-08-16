package domination.hthfantasy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

import domination.common.Player;

public class PlayerResolverTest {

	@Test
	public void testResolver() {
		String costedFile = "C:\\temp\\domination\\Fanduel_Week12.csv";
		Collection<Player> costedPlayers = getFilePlayers(costedFile);
		
		String scoredFile = "C:\\temp\\domination\\Yahoo_Football_Actuals_Week12.csv";
		Collection<Player> scoredPlayers = getFilePlayers(scoredFile);
		
		PlayerResolver resolver = new PlayerResolver(costedPlayers, scoredPlayers, Collections.<String>emptyList());
		resolver.run();
		
		for (Pair<Player,String> player : resolver.getUnresolvedPlayers()) {
			System.out.println("(" + player.getRight() + ") " + player.getLeft().getKey());
		}
	}
	
	static Collection<Player> getFilePlayers(final String filename) {
		List<Player> players = new ArrayList<Player>();
		String line = null;
		BufferedReader reader = null;		
		try {
			File file = new File(filename);
			reader = new BufferedReader(new FileReader(file));

			while ((line = reader.readLine()) != null) {
				players.add(Player.parseCsv(line));
			}
			reader.close();
		}
		catch (Exception ex) {
			System.out.println(ex.toString());
		}
		
		return players;
	}
}
