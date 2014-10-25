package domination.common;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * Filters a list of {@link Dominatable} objects based on cost and score. Will
 * remove any object where the cost is higher and the score is lower than
 * another object in the list.
 */
public final class DominationFilter {

	/**
	 * Determines whether or not to apply the domination filter
	 */
	public static boolean ENABLED = true;
	
	/**
	 * Sorts the input list by increasing cost, the iterates over the list
	 * removing any player that has a lower score then the previous player. As
	 * cost increases, we expect score to similarly increase.
	 * 
	 * @param list the list to be filtered
	 */
	public static <T extends Dominatable> void filter(final List<T> list) {
		if (ENABLED) {
			Collections.sort(list, comparator);
	
			double previousScore = 0;
			final Iterator<T> iterator = list.iterator();
			while (iterator.hasNext()) {
				final T next = iterator.next();
				if (next.getScore() < previousScore) {
					iterator.remove();
				}
				else {
					previousScore = next.getScore();
				}
			}
		}
	}

	/**
	 * Compares to {@link Dominatable} objects, sorting them by increasing cost.
	 */
	public static Comparator<Dominatable> comparator = new Comparator<Dominatable>() {
		@Override
		public int compare(final Dominatable a, final Dominatable b) {
			return Double.compare(a.getCost(), b.getCost());
		}
	};
}
