package domination.solver;

import java.util.ArrayList;
import java.util.List;

public final class TeamGenerator {
	final private ReducedRepository repository;
	final private List<PlayerPosition> positions;

	public TeamGenerator(final List<Player> players, final List<PlayerPosition> positions) {
		this.repository = new ReducedRepository(positions, players);
		this.positions = positions;
	}

	public List<Team> generateTeams() {
		List<Team> teams = new ArrayList<Team>();
		teams.add(new BaseTeam());

		for (final PlayerPosition position : this.positions) {
			teams = addPlayersAtPosition(position, teams);
			DominationFilter.filter(teams);
		}

		return teams;
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
}
