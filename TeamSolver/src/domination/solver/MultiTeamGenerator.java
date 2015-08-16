package domination.solver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.tuple.Pair;

import domination.common.Player;

public class MultiTeamGenerator {
	private final SolverArguments arguments;
	private final Map<Team, Team> candidates;
	/**
	 * Constructor
	 * @param arguments the generator arguments
	 */
	public MultiTeamGenerator(final SolverArguments arguments) {
		this.arguments = arguments;
		this.candidates = new HashMap<>();
	}
	
	public List<Team> generateTeams(final int total) {
		ArrayList<Team> teams = new ArrayList<>();
		
		Team scoringTeam = new TeamGenerator(arguments).generateTeam();
		teams.add(scoringTeam);

		List<Pair<Team, Team>> nhsts = Arrays.<Pair<Team, Team>>asList(Pair.<Team, Team>of(new BaseTeam(), scoringTeam));
		while (teams.size() < total) {
			generateNextTeams(nhsts);
			
			nhsts = computeNHST(teams);
			
			for (Pair<Team, Team> nhst : nhsts) {
				if (!teams.contains(nhst.getRight())) {
					teams.add(nhst.getRight());
				}
			}
		}
		return teams;
	}

	private void generateNextTeams(final List<Pair<Team, Team>> previousTeams) {
		for (Pair<Team, Team> previousTeam : previousTeams) {
			final Team previousSeed = previousTeam.getLeft(); 
			final Team previousResult = previousTeam.getRight();
			for (Player player : previousResult.getPlayers()) {
				Team seed = new RecursiveTeam(player, previousSeed);
				if (!candidates.containsKey(seed)) {
					Collection<Player> filteredPlayers = filterPlayers(seed);
					Team candidate = new TeamGenerator(arguments.overridePlayers(filteredPlayers).overrideFlag(false)).generateTeam();
					candidates.put(seed, candidate);
				}
			}
		}
		return;
	}

	private Collection<Player> filterPlayers(final Team newSeed) {
		ArrayList<Player> filteredPlayers = new ArrayList<Player>(arguments.getPlayers());
		filteredPlayers.removeAll(newSeed.getPlayers());
		return filteredPlayers;
	}

	private List<Pair<Team, Team>> computeNHST(final Collection<Team> pickedTeams) {
		List<Pair<Team, Team>> highestScoring = new ArrayList<>();
		double highestScore = 0;
		for (Entry<Team, Team> entry : candidates.entrySet()) {
			if (highestScoring.size() == 0 || Double.compare(entry.getValue().getScore(), highestScore) > 0) {
				highestScoring.clear();
				highestScoring.add(Pair.<Team, Team>of(entry.getKey(), entry.getValue()));
				highestScore = entry.getValue().getScore();
			}
			else if (Double.compare(entry.getValue().getScore(), highestScore) == 0) {
				highestScoring.add(Pair.<Team, Team>of(entry.getKey(), entry.getValue()));
			}
		}

		for (Pair<Team, Team> team : highestScoring) {
			candidates.remove(team.getLeft());
		}
		
		return highestScoring;
	}
}
