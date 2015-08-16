package domination.hthfantasy;

import java.util.Collection;

import domination.common.Player;
import domination.solver.Team;

/**
 * Information required to generate the report
 */
public final class ReportRequest {
	private final Collection<Team> alphaTeams;
	private final Collection<Team> betaTeams;
	private final Collection<Team> epsilonTeams;
	private final Collection<Player> players;
	private final double betaCap;
	private final String filename;
	
	/**
	 * Private constructor - use the builder class to create
	 * @param alphaTeam the alpha team
	 * @param betaTeam the beta team
	 * @param players the list of players
	 * @param betaCap the salary cap used to generate the beta players
	 * @param filename the filename into which to write the report
	 */
	private ReportRequest(final Collection<Team> alphaTeams, final Collection<Team> betaTeams,
			final Collection<Team> epsilonTeams, final Collection<Player> players, final double betaCap, 
			final String filename) {
		this.alphaTeams = alphaTeams;
		this.betaTeams = betaTeams;
		this.epsilonTeams = epsilonTeams;
		this.players = players;
		this.betaCap = betaCap;
		this.filename = filename;
	}

	/**
	 * @return the alphaTeam
	 */
	public Collection<Team> getAlphaTeam() {
		return alphaTeams;
	}

	/**
	 * @return the betaTeam
	 */
	public Collection<Team> getBetaTeam() {
		return betaTeams;
	}

	/**
	 * @return the epsilonTeams
	 */
	public final Collection<Team> getEpsilonTeams() {
		return epsilonTeams;
	}
	
	/**
	 * @return the players
	 */
	public Collection<Player> getPlayers() {
		return players;
	}

	/**
	 * @return the betaCap
	 */
	public double getBetaCap() {
		return betaCap;
	}

	/**
	 * @return the filename
	 */
	public String getFilename() {
		return filename;
	}
	
	public static class Builder {
		private Collection<Team> alphaTeam;
		private Collection<Team> betaTeam;
		private Collection<Team> epsilonTeams;
		private Collection<Player> players;
		private double betaCap;
		private String filename;

		public Builder() {
		}

		public ReportRequest build() {
			return new ReportRequest(alphaTeam, betaTeam, epsilonTeams, players, betaCap, filename);
		}
		
		/**
		 * @param alphaTeam the alphaTeam to set
		 */
		public final Builder setAlphaTeam(final Collection<Team> alphaTeams) {
			this.alphaTeam = alphaTeams;
			return this;
		}

		/**
		 * @param betaTeam the betaTeam to set
		 */
		public final Builder setBetaTeam(final Collection<Team> betaTeams) {
			this.betaTeam = betaTeams;
			return this;
		}

		/**
		 * @param epsilonTeams the epsilon teams to set
		 * @return this
		 */
		public final Builder setEpsilonTeams(final Collection<Team> epsilonTeams) {
			this.epsilonTeams = epsilonTeams;
			return this;
		}
		
		/**
		 * @param players the players to set
		 */
		public final Builder setPlayers(final Collection<Player> players) {
			this.players = players;
			return this;
		}

		/**
		 * @param betaCap the betaCap to set
		 */
		public final Builder setBetaCap(final double betaCap) {
			this.betaCap = betaCap;
			return this;
		}

		/**
		 * @param filename the filename to set
		 */
		public final Builder setFilename(final String filename) {
			this.filename = filename;
			return this;
		}
	}
}
