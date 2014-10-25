package domination.hthfantasy;

import java.util.List;

import domination.common.Player;
import domination.solver.Team;

/**
 * Information required to generate the report
 */
public final class ReportRequest {
	private final Team alphaTeam;
	private final Team betaTeam;
	private final List<Player> players;
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
	private ReportRequest(final Team alphaTeam, final Team betaTeam, final List<Player> players, final double betaCap, 
			final String filename) {
		this.alphaTeam = alphaTeam;
		this.betaTeam = betaTeam;
		this.players = players;
		this.betaCap = betaCap;
		this.filename = filename;
	}

	/**
	 * @return the alphaTeam
	 */
	public Team getAlphaTeam() {
		return alphaTeam;
	}

	/**
	 * @return the betaTeam
	 */
	public Team getBetaTeam() {
		return betaTeam;
	}

	/**
	 * @return the players
	 */
	public List<Player> getPlayers() {
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
		private Team alphaTeam;
		private Team betaTeam;
		private List<Player> players;
		private double betaCap;
		private String filename;

		public Builder() {
		}

		public ReportRequest build() {
			return new ReportRequest(alphaTeam, betaTeam, players, betaCap, filename);
		}
		
		/**
		 * @param alphaTeam the alphaTeam to set
		 */
		public final Builder setAlphaTeam(final Team alphaTeam) {
			this.alphaTeam = alphaTeam;
			return this;
		}

		/**
		 * @param betaTeam the betaTeam to set
		 */
		public final Builder setBetaTeam(final Team betaTeam) {
			this.betaTeam = betaTeam;
			return this;
		}

		/**
		 * @param players the players to set
		 */
		public final Builder setPlayers(final List<Player> players) {
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
