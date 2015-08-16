package domination.hthfantasy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import domination.common.Player;
import domination.common.PlayerPosition;
import domination.solver.MultiTeamGenerator;
import domination.solver.SolverArguments;
import domination.solver.Team;
import domination.solver.TeamGenerator;

/**
 * Generates the set of teams for a Fantasy Football analysis
 */
public final class GreekTeamGenerator {
	
	private final SolverArguments arguments;

	/**
	 * Constructor
	 */
	public GreekTeamGenerator(final SolverArguments arguments) {
		this.arguments = arguments;
	}

	public List<Team> generateAlphaTeams(final int count) {
		// Generate the alpha teams with the full set of positions
		MultiTeamGenerator alphaGenerator = new MultiTeamGenerator(arguments);
		final List<Team> alphaTeams = alphaGenerator.generateTeams(count);
		return alphaTeams;
	}

	public List<Team> generateBetaTeams(final int count, final double salaryCapAdjustment, final Collection<PlayerPosition> removedPositions) { 
		
		// Generate the beta teams without defense and kicker
		final List<PlayerPosition> betaPositions = new ArrayList<>(arguments.getPositions());
		for (PlayerPosition position : removedPositions) {
			betaPositions.remove(position);
			betaPositions.remove(position);
		}
		MultiTeamGenerator betaGenerator = new MultiTeamGenerator(arguments.overridePositions(betaPositions)
				.overrideSalaryCap(arguments.getSalaryCap() - salaryCapAdjustment));
		return betaGenerator.generateTeams(count);
	}

	/**
	 * Generates a set of teams with each of a given position
	 * @param inPlayers
	 * @param positions
	 * @param salaryCap
	 * @return
	 */
	public List<Team> generateEpsilonTeams(final PlayerPosition epsilonPosition) {
		Map<PlayerPosition, Collection<Player>> map = Player.buildMap(arguments.getPlayers());
		List<Player> epsilonPlayers = new ArrayList<>(arguments.getPlayers());
		Collection<Player> qbs = map.get(epsilonPosition);

		List<Team> outTeams = new ArrayList<>();
		while (qbs.size() > 0) {	
			System.out.println(qbs.size() + " remaining quarterbacks");

			Team team = new TeamGenerator(arguments.overridePlayers(epsilonPlayers)
												   .overrideFlag(false)).generateTeam();
			
			outTeams.add(team);
			Player qb = null;
			for (Player player : team.getPlayers()) {
				if (player.getPosition() == PlayerPosition.QB) {
					qb = player;
					break;
				}
			}
			
			qbs.remove(qb);
			epsilonPlayers.remove(qb);
		}
		
		return outTeams;
	}
}
