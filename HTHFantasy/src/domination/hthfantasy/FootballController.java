package domination.hthfantasy;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import domination.common.Player;
import domination.common.PlayerPosition;
import domination.solver.SolverArguments;
import domination.solver.Team;

/**
 * Executes the Metis algorithm to solve for a solution
 */
public class FootballController {
	/**
	 * The amount to reduce the budget when not selecting BETA players
	 */
	private static final int BETA_BUDGET_ADJUSTMENT = 11000;

	private static final int TEAM_COUNT = 5;
	
	private final Collection<Player> costedPlayers;
	private final Collection<Player> scoredPlayers;
	private List<PlayerPosition> positions;
	private List<String> overrides;
	private double salaryCap;
	private String outputFile;
	
	boolean generateEpsilonTeams;
	
	public FootballController(final Collection<Player> costedPlayers, final Collection<Player> scoredPlayers, 
			final List<PlayerPosition> positions, final double salaryCap, final String outputFile, 
			final List<String> overrides) {
		this.costedPlayers = costedPlayers;
		this.scoredPlayers = scoredPlayers;
		this.positions = positions;
		this.salaryCap = salaryCap;
		this.outputFile = outputFile;
		this.overrides = overrides;
		
		this.generateEpsilonTeams = true;
	}
	
	public void generateTeams() {

			// Resolve the two sets of players to combine the information
			PlayerResolver resolver = new PlayerResolver(costedPlayers, scoredPlayers, overrides);
			resolver.run();
			
			// Output the list of players that were in one list but the other
			for (Pair<Player, String> player : resolver.getUnresolvedPlayers()) {
				System.out.println("Unresolved Player (" + player.getRight() + "): " + player.getLeft().getKey());
			}

			Collection<Player> resolvedPlayers = resolver.getResolvedPlayers();
			
			SolverArguments arguments = new SolverArguments.Builder()
														   .addPlayers(resolvedPlayers)
														   .addPositions(positions)
														   .setFlag(true)
														   .setSalaryCap(salaryCap)
														   .build();
			GreekTeamGenerator generator = new GreekTeamGenerator(arguments);
			List<Team> alphaTeams = generator.generateAlphaTeams(TEAM_COUNT);
			
			// Generate the Epsilon teams
			List<Team> betaTeams = null;
			List<Team> epsilons = null;
			if (generateEpsilonTeams) {
				betaTeams = generator.generateBetaTeams(TEAM_COUNT, BETA_BUDGET_ADJUSTMENT, 
						Arrays.<PlayerPosition>asList(PlayerPosition.K, PlayerPosition.DEF));
				epsilons = generator.generateEpsilonTeams(PlayerPosition.QB);
			}
			else {
				betaTeams = Collections.<Team>emptyList();
				epsilons = Collections.<Team>emptyList();
			}
			
			// Write the output report
			StringBuilder reportName = new StringBuilder(outputFile);
			if (overrides.size() > 0) {
				reportName.append("_override");
			}
			reportName.append(".xlsx");
			ReportRequest request = new ReportRequest.Builder()
										.setAlphaTeam(alphaTeams)
										.setBetaTeam(betaTeams)
										.setEpsilonTeams(epsilons)
										.setPlayers(resolvedPlayers)
										.setBetaCap(salaryCap - BETA_BUDGET_ADJUSTMENT)
										.setFilename(reportName.toString())
										.build();
			FootballReportGenerator.report(request);
	}
	
	/**
	 * Toggle the generation of epsilon teams (this is very time consuming)
	 * @param value
	 */
	public void setGenerateEpsilonTeams(final boolean value) {
		this.generateEpsilonTeams = value;
	}
}
