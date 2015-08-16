package domination.hthfantasy;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import domination.common.Player;
import domination.common.PlayerPosition;
import domination.solver.SolverArguments;
import domination.solver.Team;

public class BasketballController {
	private static final int TEAM_COUNT = 5;
	
	private final Collection<Player> costedPlayers;
	private final Collection<Player> scoredPlayers;
	private List<PlayerPosition> positions;
	private List<String> overrides;
	private double salaryCap;
	private String outputFile;
	
	public BasketballController(final Collection<Player> costedPlayers, final Collection<Player> scoredPlayers, 
			final List<PlayerPosition> positions, final double salaryCap, final String outputFile, 
			final List<String> overrides) {
		this.costedPlayers = costedPlayers;
		this.scoredPlayers = scoredPlayers;
		this.positions = positions;
		this.salaryCap = salaryCap;
		this.outputFile = outputFile;
		this.overrides = overrides;
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

		// Write the output report
		StringBuilder reportName = new StringBuilder(outputFile);
		if (overrides.size() > 0) {
			reportName.append("_override");
		}
		reportName.append(".xlsx");
		ReportRequest request = new ReportRequest.Builder()
									.setAlphaTeam(alphaTeams)
									.setPlayers(resolvedPlayers)
									.setFilename(reportName.toString())
									.build();
		BasketballReportGenerator.report(request);

		return;
	}
}
