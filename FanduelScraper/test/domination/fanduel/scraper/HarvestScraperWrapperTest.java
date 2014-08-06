package domination.fanduel.scraper;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import domination.solver.Player;
import domination.solver.Team;
import domination.solver.TeamGenerator;
import domination.solver.TeamScoreComparator;

public class HarvestScraperWrapperTest {

	@Test
	public void runWrapper() {
		try {
			HarvestScraperWrapper wrapper = new HarvestScraperWrapper("resources/test_wrapper_query.xml", "resources");
			String rawString = wrapper.getWebContents("36404783", "BUJBBYXGE");
		
			FanduelPlayerPicker picker = FanduelPlayerPicker.parse(rawString);
			
			for (Player player : picker.getPlayers()) {
				System.out.println(String.format("%s %.4f", player.getName(), (player.getScore() > 0 ? player.getCost() / player.getScore() : -1)));
//				if (player.getScore() >= 26.2) {
//					System.out.println(String.format("%s %.2f $%.2f", player.getName(), player.getScore(), player.getCost()));
//				}
			}
			
			assertEquals(1025, picker.getPlayers().size());
			assertEquals(9, picker.getPositions().size());
			assertEquals(60000, picker.getSalaryCap(), .0001);
		} 
		catch (final Exception ex) {
			System.out.println(ex);
		}
	}
	
	@Test
	public void doIt() {
		try {
			HarvestScraperWrapper wrapper = new HarvestScraperWrapper("resources/test_wrapper_query.xml", "resources");
			String rawString = wrapper.getWebContents("36404783", "BUJBBYXGE");
		
			FanduelPlayerPicker picker = FanduelPlayerPicker.parse(rawString);
			
			final TeamGenerator generator = new TeamGenerator(picker.getPlayers(), picker.getPositions());
			final List<Team> teams = generator.generateTeams();
			Collections.sort(teams, TeamScoreComparator.comparator);

			final List<Team> reportedTeams = new ArrayList<Team>();

			int count = 0;
			final Iterator<Team> iterator = teams.iterator();
			while (count < 30 && iterator.hasNext()) {
				final Team team = iterator.next();
				if (team.getCost() < picker.getSalaryCap()) {
					boolean previouslyReported = false;
					for (final Team previousTeam : reportedTeams) {
						if (previousTeam.getRosterCode() == team.getRosterCode()) {
							previouslyReported = true;
							break;
						}
					}

					if (!previouslyReported) {
						count++;
						System.out.println(String.format("%f, %f, %s", team.getScore(), team.getCost(), team.getRoster()));
						reportedTeams.add(team);
					}
				}
			}

		}
		catch (final Exception ex) {
			System.out.println(ex);
		}
	}
}
