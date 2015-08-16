package domination.hthfantasy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import domination.common.Player;
import domination.common.PlayerPosition;
import domination.fanduel.scraper.FanduelPlayerPicker;
import domination.fanduel.scraper.FanduelScraper;
import domination.fftoolbox.scraper.FFToolboxDriver;
import domination.yahoo.YahooDriver;

/**
 * Generates a solution
 */
public final class SolutionGenerator {
	private List<String> playerOverrides;
	
	/**
	 * Constructor
	 */
	private SolutionGenerator() {
		playerOverrides = new ArrayList<>();
	}
	
	public static void generateActuals(final int week, final String pricingFile, final String outputFile,
			final String token, final String tokenSecret) {
		try {
			YahooDriver driver = new YahooDriver();
			driver.authenticate(token, tokenSecret);
			generateActuals(week, pricingFile, outputFile, driver);
		}
		catch (Exception ex) {
			System.out.println(ex.toString());
		}
	}
	
	public static void generateActuals(final int week, final String pricingFile, final String outputFile) {
		try {
			YahooDriver driver = new YahooDriver();
			driver.authenticate();
			generateActuals(week, pricingFile, outputFile, driver);
		}
		catch (Exception ex) {
			System.out.println(ex.toString());
		}
	}
	
	public static void generateActuals(final int week, final String pricingFile, final String outputFile, 
			final YahooDriver driver) {
		try {	
			SolutionGenerator generator = new SolutionGenerator();
			Collection<Player> costedPlayers = getFilePlayers(pricingFile);

			Collection<Player> actualPlayers = driver.retrieveFootballActuals(week);
			generator.writePlayers(actualPlayers, "C:\\temp\\domination\\Yahoo_Football_Actuals_Week" + week + ".csv");
			
			FootballController controller = new FootballController(costedPlayers, actualPlayers, 
					Arrays.<PlayerPosition>asList(PlayerPosition.QB, PlayerPosition.RB, PlayerPosition.RB,
							PlayerPosition.WR, PlayerPosition.WR, PlayerPosition.WR, PlayerPosition.TE,
							PlayerPosition.K, PlayerPosition.DEF), 
							60000.0, outputFile, Collections.<String>emptyList());
			controller.setGenerateEpsilonTeams(false);
			controller.generateTeams();
		}
		catch (IOException ex) {
			System.out.println("Failed to retrieve players from Yahoo!: " + ex.toString());
		}
		catch (Exception ex) {
			System.out.println(ex.toString());
		}
		
	}
	
	/**
	 * Generate a solution
	 * @param url the URL of the Fanduel contest page for the given week
	 * @param week the week to run for
	 * @param cookie the FFToolbox login cookie
	 */
	public static void generate(final String url, final int week, final String cookie, final String outputFile) {
		SolutionGenerator generator = new SolutionGenerator();
		generator.generate_(url, week, cookie, outputFile);
	}
	
	public static void generate2(final String url, final int week, final String filename, final String outputFile) {
		SolutionGenerator generator = new SolutionGenerator();
		generator.generate_2(url, week, filename, outputFile);
	}
	
	public static void generateBB(final String inFilename, final String outFilename, final List<String> overrides) {
		SolutionGenerator generator = new SolutionGenerator();
		generator.overridePlayers(overrides);
		generator.generate_bb(inFilename, outFilename);
	}
	
	public static void generateFF(final String inFilename, final String outFilename, final List<String> overrides) {
		SolutionGenerator generator = new SolutionGenerator();
		generator.overridePlayers(overrides);
		generator.generate_ff(inFilename, outFilename);
	}
	
	/**
	 * Generate a solution with player overrides
	 * @param url the URL of the Fanduel contest page for the given week
	 * @param week the week to run for
	 * @param cookie the FFToolbox login cookie
	 * @param overrides the list of players to override
	 */
	public static void generateWithOverrides(final String url, final int week, final String cookie, 
			final List<String> overrides, final String outputFile) {
		SolutionGenerator generator = new SolutionGenerator();
		generator.overridePlayers(overrides);
		generator.generate_(url, week, cookie, outputFile);
	}
	
	/**
	 * Generate a solution
	 * @param url the URL of the Fanduel contest page for the given week
	 * @param week the week to run for
	 * @param cookie the FFToolbox login cookie
	 */
	private void generate_(final String url, final int week, final String cookie, final String outputFile) {
		try {
			// Get the costed players from fanduel
			_TEMP fdPicker = getFanduelPlayers(week, url);
			
			// Get the pointed players from fftoolbox
			List<Player> scoredPlayers = getFFToolboxPlayers(week, cookie);
			
			FootballController metis = new FootballController(fdPicker.getPlayers(), scoredPlayers, fdPicker.getPositions(), 
					fdPicker.getSalaryCap(), outputFile, playerOverrides);
			metis.generateTeams();
		}
		catch (final Exception ex) {
			System.out.println(ex.getStackTrace());
		}
	}

	/**
	 * Generate a solution
	 * @param url the URL of the Fanduel contest page for the given week
	 * @param week the week to run for
	 * @param cookie the FFToolbox login cookie
	 */
	private void generate_2(final String url, final int week, final String filename, final String outputFile) {
		try {
			// Get the costed players from fanduel
			_TEMP fdPicker = getFanduelPlayers(week, url);
			
			// Get the pointed players from fftoolbox
			List<Player> scoredPlayers = getFilePlayers(filename);
			
			FootballController metis = new FootballController(fdPicker.getPlayers(), scoredPlayers, fdPicker.getPositions(), 
					fdPicker.getSalaryCap(), outputFile, playerOverrides);
			metis.generateTeams();
		}
		catch (final Exception ex) {
			System.out.println(ex.getStackTrace());
		}
	}
	
	private void generate_ff(final String inFilename, final String outFilename) {
		List<Player> players = getFilePlayers(inFilename);
		List<PlayerPosition> positions = Arrays.<PlayerPosition>asList(PlayerPosition.QB, PlayerPosition.RB,
				PlayerPosition.RB, PlayerPosition.WR, PlayerPosition.WR, PlayerPosition.WR, PlayerPosition.TE,
				PlayerPosition.K, PlayerPosition.DEF);
		FootballController controller = new FootballController(players, players, positions, 60000.0, outFilename,
				playerOverrides);
//		controller.setGenerateEpsilonTeams(false);
		controller.generateTeams();
	}
	
	private void generate_bb(final String inFilename, final String outFilename) {
		List<Player> players = getFilePlayers(inFilename);
		List<PlayerPosition> positions = Arrays.<PlayerPosition>asList(PlayerPosition.PG, PlayerPosition.PG,
				PlayerPosition.SG, PlayerPosition.SG, PlayerPosition.SF, PlayerPosition.SF, PlayerPosition.PF,
				PlayerPosition.PF, PlayerPosition.C);
		BasketballController metis = new BasketballController(players, players, positions, 60000.0, outFilename,
				playerOverrides);
		metis.generateTeams();
	}
	
	static List<Player> getFilePlayers(final String filename) {
		List<Player> players = new ArrayList<Player>();
		String line = null;
		BufferedReader reader = null;		
		try {
			File file = new File(filename);
			reader = new BufferedReader(new FileReader(file));

			while ((line = reader.readLine()) != null) {
				if (line.endsWith("DST")) {
					line = line.replace("DST", "DEF");
					line = line.replace("St. Louis", "St Louis");
				}
				players.add(Player.parseCsv(line));
			}
			reader.close();
		}
		catch (Exception ex) {
			System.out.println(ex.toString());
		}
		
		return players;
	}

	private _TEMP getFanduelPlayers(final int week, final String url) {
		FanduelPlayerPicker fdPicker = null;
		try {
			System.out.println("Reading fanduel players");
			FanduelScraper fdScraper = new FanduelScraper("resources/fanduel_query.xml", "resources");
			String fdPage = fdScraper.getWebContents(url);
			fdPicker = FanduelPlayerPicker.parse(fdPage);
			List<Player> costedPlayers = new ArrayList<Player>(fdPicker.getPlayers().values());
			System.out.println("Found " + costedPlayers.size() + " players.");
			writePlayers(costedPlayers, "C:\\Temp\\Domination\\Fanduel_Week" + week + ".csv");
		}
		catch (Exception ex) {
			System.out.println(ex.getStackTrace());
		}
		
		return new _TEMP(fdPicker.getSalaryCap(), fdPicker.getPlayers().values(), fdPicker.getPositions());
	}
	
	private List<Player> getFFToolboxPlayers(final int week, final String cookie) {
		List<Player> scoredPlayers = null;
		try {
			System.out.println("Reading FFToolbox players");
			scoredPlayers = FFToolboxDriver.readPlayers(week, cookie);
			System.out.println("Found " + scoredPlayers.size() + " players.");
			writePlayers(scoredPlayers, "C:\\Temp\\Domination\\FFToolbox_Week" + week + ".csv");
		}
		catch (Exception ex) {
			System.out.println(ex.toString());
		}
		return scoredPlayers;
	}

	

	/**
	 * Sets the list of overriden players - these players will be given a nearly zero cost
	 * @param players the list of players
	 */
	public void overridePlayers(final List<String> players) {
		if (!players.isEmpty()) {
			playerOverrides = players;
		}
	}

	/**
	 * Write the list of players to a CSV file using the input filename
	 * @param players the list of players
	 * @param filename the file
	 * @throws IOException if there is an exception closing the file
	 */
	private void writePlayers(final Collection<Player> players, final String filename) throws IOException {
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
	
	private static class _TEMP {
		private double salaryCap;
		private List<PlayerPosition> positions;
		private Collection<Player> players;
		
		public _TEMP(final double cap, final Collection<Player> collection, final List<PlayerPosition> list) {
			this.salaryCap = cap;
			this.positions = list;
			this.players = collection;
		}

		/**
		 * @return the salaryCap
		 */
		public final double getSalaryCap() {
			return salaryCap;
		}

		/**
		 * @return the positions
		 */
		public final List<PlayerPosition> getPositions() {
			return positions;
		}

		/**
		 * @return the players
		 */
		public final Collection<Player> getPlayers() {
			return players;
		}
	}
	
}
