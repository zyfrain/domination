package domination.solver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.junit.Test;

import domination.common.Player;
import domination.common.PlayerPosition;

/**
 * Tests for the {@link TeamGenerator} class.
 */
public class TeamGeneratorTest {

	@Test
	public void testGenerate() {
		final List<PlayerPosition> positions = generatePositions();
		final List<Player> players = generatePlayers();

		final TeamGenerator generator = new TeamGenerator(players, positions);
		final List<Team> teams = generator.generateTeams();
		Collections.sort(teams, ScoreComparator.teamComparator);

		final List<Team> reportedTeams = new ArrayList<Team>();

		int count = 0;
		final Iterator<Team> iterator = teams.iterator();
		while (count < 10 && iterator.hasNext()) {
			final Team team = iterator.next();
			if (team.getCost() < 125.0) {
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

	private List<Player> generatePlayers() {
		final List<Player> players = new ArrayList<Player>();

		players.addAll(generateRandomPlayers(PlayerPosition.QB, 30, 20, 30));
		players.addAll(generateRandomPlayers(PlayerPosition.RB, 30, 15, 20));
		players.addAll(generateRandomPlayers(PlayerPosition.WR, 60, 10, 10));
		players.addAll(generateRandomPlayers(PlayerPosition.TE, 15, 10, 10));
		players.addAll(generateRandomPlayers(PlayerPosition.K, 10, 5, 10));
		players.addAll(generateRandomPlayers(PlayerPosition.DEF, 15, 5, 15));

		return players;
	}

	private List<Player> generateRandomPlayers(final PlayerPosition position, final int count, final int averageCost,
			final int averageScore) {
		final List<Player> players = new ArrayList<Player>();
		final Random rand = new Random();

		for (int i = 0; i < count; i++) {
			final double scoreFactor = rand.nextDouble() - .5;
			final double score = averageScore + (averageScore * scoreFactor);
			final double costFactor = (rand.nextDouble() - .5) * scoreFactor;
			final double cost = averageCost + (averageCost * costFactor);

			players.add(new Player("Key", String.format("%s_%d", position.toString(), i), position, cost, score));
		}

		return players;
	}

	private List<PlayerPosition> generatePositions() {
		final List<PlayerPosition> positions = new ArrayList<PlayerPosition>();

		positions.add(PlayerPosition.QB);
		positions.add(PlayerPosition.RB);
		positions.add(PlayerPosition.RB);
		positions.add(PlayerPosition.WR);
		positions.add(PlayerPosition.WR);
		positions.add(PlayerPosition.RBWR);
		positions.add(PlayerPosition.TE);
		positions.add(PlayerPosition.K);
		positions.add(PlayerPosition.DEF);

		return positions;
	}
}
