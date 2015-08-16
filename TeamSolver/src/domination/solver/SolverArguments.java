package domination.solver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import domination.common.Player;
import domination.common.PlayerPosition;

/**
 * The set of arguments required by the algorithm
 */
public class SolverArguments {
	final Collection<Player> players;
	final Collection<PlayerPosition> positions; 
	final boolean flag;
	final double salaryCap;

	private SolverArguments(final Collection<Player> players, final Collection<PlayerPosition> positions, 
			final boolean flag, final double salaryCap) {
		this.players = players;
		this.positions = positions;
		this.flag = flag;
		this.salaryCap = salaryCap;
	}
	
	public SolverArguments overridePlayers(Collection<Player> players) {
		return new SolverArguments(players, positions, flag, salaryCap);
	}

	public SolverArguments overridePositions(Collection<PlayerPosition> positions) {
		return new SolverArguments(players, positions, flag, salaryCap);
	}

	public SolverArguments overrideFlag(boolean b) {
		return new SolverArguments(players, positions, b, salaryCap);
	}

	public SolverArguments overrideSalaryCap(double cap) {
		return new SolverArguments(players, positions, flag, cap);
	}

	/**
	 * @return the players
	 */
	public final Collection<Player> getPlayers() {
		return players;
	}

	/**
	 * @return the positions
	 */
	public final Collection<PlayerPosition> getPositions() {
		return positions;
	}

	/**
	 * @return the flag
	 */
	public final boolean isFlag() {
		return flag;
	}

	/**
	 * @return the salaryCap
	 */
	public final double getSalaryCap() {
		return salaryCap;
	}


	/**
	 * Static class to construct the arguments
	 */
	public static class Builder {
		List<Player> players;
		List<PlayerPosition> positions; 
		boolean flag;
		double salaryCap;

		public Builder() {
			this.players = new ArrayList<>();
			this.positions = new ArrayList<>();
			this.flag = false;
			this.salaryCap = 0;
		}
		
		public SolverArguments build() {
			return new SolverArguments(players, positions, flag, salaryCap);
		}
		
		/**
		 * Set the log flag value - indicates whether to log when running the solver
		 * @param flag the value
		 */
		public Builder setFlag(final boolean flag) {
			this.flag = flag;
			return this;
		}
		
		public Builder setSalaryCap(final double salaryCap) {
			this.salaryCap = salaryCap;
			return this;
		}
		
		/**
		 * Add players to the set of players in the request
		 * @param players the players
		 */
		public Builder addPlayers(final Collection<Player> players) {
			this.players.addAll(players);
			return this;
		}
		
		/**
		 * Add positions to the set of positions in the request
		 * @param positions the positions to solve for
		 */
		public Builder addPositions(final Collection<PlayerPosition> positions) {
			this.positions.addAll(positions);
			return this;
		}
	}

}
