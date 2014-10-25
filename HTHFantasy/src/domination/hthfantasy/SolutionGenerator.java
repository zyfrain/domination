package domination.hthfantasy;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import domination.common.Player;
import domination.common.PlayerPosition;
import domination.fanduel.scraper.FanduelPlayerPicker;
import domination.fanduel.scraper.FanduelScraper;
import domination.fftoolbox.scraper.FFToolboxDriver;
import domination.solver.ScoreComparator;
import domination.solver.Team;
import domination.solver.TeamGenerator;

/**
 * Generates a solution
 */
public final class SolutionGenerator {
	
	/**
	 * The amount to reduce the budget when not selecting BETA players
	 */
	private static final int BETA_BUDGET_ADJUSTMENT = 11000;
	/**
	 * The number of top teams to print while processing
	 */
	private static final int PRINT_COUNT = 5;
	
	private boolean override;
	private List<String> playerOverrides;
	
	/**
	 * Constructor
	 */
	private SolutionGenerator() {
		override = false;
		playerOverrides = new ArrayList<>();
	}
	
	/**
	 * Generate a solution
	 * @param url the URL of the Fanduel contest page for the given week
	 * @param week the week to run for
	 * @param cookie the FFToolbox login cookie
	 */
	public static void generate(final String url, final int week, final String cookie) {
		SolutionGenerator generator = new SolutionGenerator();
		generator.generate_(url, week, cookie);
	}
	
	/**
	 * Generate a solution with player overrides
	 * @param url the URL of the Fanduel contest page for the given week
	 * @param week the week to run for
	 * @param cookie the FFToolbox login cookie
	 * @param overrides the list of players to override
	 */
	public static void generateWithOverrides(final String url, final int week, final String cookie, 
			final List<String> overrides) {
		SolutionGenerator generator = new SolutionGenerator();
		generator.overridePlayers(overrides);
		generator.generate_(url, week, cookie);
	}
	
	/**
	 * Generate a solution
	 * @param url the URL of the Fanduel contest page for the given week
	 * @param week the week to run for
	 * @param cookie the FFToolbox login cookie
	 */
	private void generate_(final String url, final int week, final String cookie) {
		try {
			// Get the costed players from fanduel
			System.out.println("Reading fanduel players");
			FanduelScraper fdScraper = new FanduelScraper("resources/fanduel_query.xml", "resources");
			String fdPage = fdScraper.getWebContents(url);
			FanduelPlayerPicker fdPicker = FanduelPlayerPicker.parse(fdPage);
			List<Player> costedPlayers = new ArrayList<Player>(fdPicker.getPlayers().values());
			System.out.println("Found " + costedPlayers.size() + " players.");
			writePlayers(costedPlayers, "C:\\Temp\\Domination\\Fanduel_Week" + week + ".csv");
			
			// Get the pointed players from fftoolbox
			System.out.println("Reading FFToolbox players");
			List<Player> scoredPlayers = FFToolboxDriver.readPlayers(week, cookie);
			System.out.println("Found " + scoredPlayers.size() + " players.");
			writePlayers(scoredPlayers, "C:\\Temp\\Domination\\FFToolbox_Week" + week + ".csv");
			
			// Resolve the two sets of players to combine the information
			PlayerResolver resolver = new PlayerResolver(costedPlayers, scoredPlayers);
			resolver.run();
			
			// Output the list of players that were in one list but the other
			for (Pair<Player, String> player : resolver.getUnresolvedPlayers()) {
				System.out.println("Unresolved Player (" + player.getRight() + "): " + player.getLeft().getKey());
			}
			
			// Process any player overrides
			List<Player> overriden = processOverrides(resolver.getResolvedPlayers());
			
			// Generate the alpha teams with the full set of positions
			final TeamGenerator alphaGenerator = new TeamGenerator(overriden, fdPicker.getPositions());
			final List<Team> alphaTeams = alphaGenerator.generateTeams();

			// Determine the alpha team
			System.out.println("Alphas:");
			Team alphaTeam = processTeams(alphaTeams, fdPicker.getSalaryCap());

			// Generate the beta teams without defense and kicker
			final List<PlayerPosition> betaPositions = new ArrayList<PlayerPosition>(fdPicker.getPositions());
			betaPositions.remove(PlayerPosition.DEF);
			betaPositions.remove(PlayerPosition.K);
			final TeamGenerator betaGenerator = new TeamGenerator(overriden, betaPositions);
			final List<Team> betaTeams = betaGenerator.generateTeams();

			// Determine the beta team with the modified salary cap
			System.out.println("Betas:");
			double betaCap = fdPicker.getSalaryCap() - BETA_BUDGET_ADJUSTMENT;
			Team betaTeam = processTeams(betaTeams, betaCap);
			
			// Write the output report
			StringBuilder reportName = new StringBuilder("C:\\temp\\domination\\Week_" + week);
			if (override) {
				reportName.append("_override");
			}
			reportName.append(".xlsx");
			ReportRequest request = new ReportRequest.Builder()
										.setAlphaTeam(alphaTeam)
										.setBetaTeam(betaTeam)
										.setPlayers(overriden)
										.setBetaCap(betaCap)
										.setFilename(reportName.toString())
										.build();
			XSSFReportGenerator.report(request);
		}
		catch (final Exception ex) {
			System.out.println(ex.getStackTrace());
		}
	}

	/**
	 * Sets the list of overriden players - these players will be given a nearly zero cost
	 * @param players the list of players
	 */
	public void overridePlayers(final List<String> players) {
		if (players.isEmpty()) {
			override = false;
		}
		else {
			override = true;
			playerOverrides = players;
		}
	}

	/**
	 * If there are overriden players, find them in the list and replace them with an overriden copy
	 * @param players the list of players
	 * @return the list of players with overrides applied
	 */
	private List<Player> processOverrides(final List<Player> players) {
		List<Player> overridenPlayers = new ArrayList<Player>();
		
		if (override) {
			for (Player player : players) {
				if (playerOverrides.contains(player.getKey())) {
					overridenPlayers.add(Player.override(player, 6.0));
				}
				else {
					overridenPlayers.add(player);
				}
			}
		}
		else {
			overridenPlayers.addAll(players);
		}
		
		return overridenPlayers;
	}

	/**
	 * Sorts the list of teams and runs through them looking for the top scoring team under the salary cap
	 * @param teams the list of teams
	 * @param salaryCap the salary cap
	 * @return the highest scoring team
	 */
	private Team processTeams(final List<Team> teams, final double salaryCap) {
		int count = 0;
		Team topTeam = null;
		final List<Team> reportedTeams = new ArrayList<Team>();
		
		// Sort the teams according to score
		Collections.sort(teams, ScoreComparator.teamComparator);

		// Iterate over the sorted list looking for the top team, and printing the top teams
		final Iterator<Team> iterator = teams.iterator();
		while (count < PRINT_COUNT && iterator.hasNext()) {
			final Team team = iterator.next();
			// If the team is under the salary cap
			if (team.getCost() < salaryCap) {
				// If we haven't found a team yet, this is the highest scoring!
				if (topTeam == null) {
					topTeam = team;
				}
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
		
		return topTeam;
	}

	/**
	 * Write the list of players to a CSV file using the input filename
	 * @param players the list of players
	 * @param filename the file
	 * @throws IOException if there is an exception closing the file
	 */
	private void writePlayers(final List<Player> players, final String filename) throws IOException {
		FileWriter outputFile = null;
		try {
			outputFile = new FileWriter(filename);
			for (Player player : players) {
				outputFile.write(Player.toCsv(player) + "\n");
			}
		}
		catch (Exception ex) {
			System.out.println("Exception caught writing output file: " + ex.toString());
		}
		finally {
			if (outputFile != null) 
				outputFile.close();
		}		
	}
	
}
