package domination.solver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import domination.common.DominationFilter;
import domination.common.Player;
import domination.common.PlayerPosition;

public final class TeamGenerator {
	/** The number of top teams to print while processing */
	private static final int PRINT_COUNT = 5;

	final private SolverArguments arguments;
	final private ReducedRepository repository;

	public TeamGenerator(final SolverArguments arguments) {
		this.repository = new ReducedRepository(arguments.getPositions(), arguments.getPlayers(), arguments.isFlag());
		this.arguments = arguments;
	}
	
	public Team generateTeam() {
		List<Team> teams = new ArrayList<Team>();
		teams.add(new BaseTeam());

		for (final PlayerPosition position : this.arguments.getPositions()) {
			teams = addPlayersAtPosition(position, teams);
			teams = DominationFilter.filter(teams);
		}

		return processTeams(teams);
	}

	private List<Team> addPlayersAtPosition(final PlayerPosition position, final List<Team> teams) {
		final List<Team> newTeams = new ArrayList<Team>();
		final List<Player> players = this.repository.getPlayers(position);

		for (final Team team : teams) {
			for (final Player player : players) {
				if (!team.containsPlayer(player)) {
					newTeams.add(new RecursiveTeam(player, team));
				}
			}
		}

		return newTeams;
	}
	
	/**
	 * Sorts the list of teams and runs through them looking for the top scoring team under the salary cap
	 * @param teams the list of teams
	 * @param salaryCap the salary cap
	 * @param printFlag indicates whether or not to print the results
	 * @return the highest scoring team
	 */
	private Team processTeams(final Collection<Team> unsortedTeams) {
		int count = 0;
		Team topTeam = null;
		final List<Team> reportedTeams = new ArrayList<Team>();
		
		final ArrayList<Team> teams = new ArrayList<>(unsortedTeams);
		// Sort the teams according to score
		Collections.sort(teams, ScoreComparator.teamComparator);

		// Iterate over the sorted list looking for the top team, and printing the top teams
		final Iterator<Team> iterator = teams.iterator();
		while (count < PRINT_COUNT && iterator.hasNext()) {
			final Team team = iterator.next();
			// If the team is under the salary cap
			if (team.getCost() < arguments.getSalaryCap()) {
				// If we haven't found a team yet, this is the highest scoring!
				if (topTeam == null) {
					topTeam = team;
				}

				if (!arguments.isFlag()) {
					break;
				}
				else {
					// Figure out if we've already printed a team with this configuration of players
					// This removes the duplicates where the same players are included in a different order
					// e.g.  Manning, Forte, Murray and Manning, Murray, Forte
					boolean previouslyReported = false;
					for (final Team previousTeam : reportedTeams) {
						if (previousTeam.getRosterCode() == team.getRosterCode()) {
							previouslyReported = true;
							break;
						}
					}
	
					// If we passed the duplicate test, increment the count and print the team
					if (!previouslyReported) {
						count++;
						System.out.println(String.format("%f, %f, %s", team.getScore(), team.getCost(), team.getRoster()));
						reportedTeams.add(team);
					}
				}
			}
		}
		
		return topTeam;
	}
}
