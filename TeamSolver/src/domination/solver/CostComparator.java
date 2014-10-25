package domination.solver;

import java.util.Comparator;

import domination.common.Player;

public class CostComparator {

	public static Comparator<Team> teamComparator = new Comparator<Team>() {
		@Override
		public int compare(final Team a, final Team b) {
			return Double.compare(a.getCost(), b.getCost());
		}
	};
	
	public static Comparator<Player> playerComparator = new Comparator<Player>() {
		@Override
		public int compare(final Player a, final Player b) {
			return Double.compare(a.getCost(), b.getCost());
		}
	};
}
