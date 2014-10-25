package domination.solver;

import java.util.Comparator;

import domination.common.Player;

public class ScoreComparator {

	public static Comparator<Team> teamComparator = new Comparator<Team>() {
		@Override
		public int compare(final Team a, final Team b) {
			return Double.compare(b.getScore(), a.getScore());
		}
	};
	
	public static Comparator<Player> playerComparator = new Comparator<Player>() {
		@Override
		public int compare(final Player a, final Player b) {
			return Double.compare(b.getScore(), a.getScore());
		}
	};
}
