package domination.solver;

import java.util.Comparator;

public class TeamScoreComparator {

	public static Comparator<Team> comparator = new Comparator<Team>() {
		@Override
		public int compare(final Team a, final Team b) {
			return Double.compare(b.getScore(), a.getScore());
		}
	};
}
