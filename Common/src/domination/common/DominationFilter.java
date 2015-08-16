package domination.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
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
	public static <T extends Dominatable> List<T> filter(final Collection<T> list) {
		if (ENABLED) {
			ArrayList<T> sortedList = new ArrayList<>(list);
			Collections.sort(sortedList, comparator);
	
			ArrayList<T> filteredList = new ArrayList<>();
			double previousScore = 0;
			for (T next : sortedList) {
				if (next.getScore() >= previousScore) {
					filteredList.add(next);
					previousScore = next.getScore();
				}
			}
			
			return filteredList;
		}
		else {
			return new ArrayList<>(list);
		}
	}

	/**
	 * Compares to {@link Dominatable} objects, sorting them by increasing cost.
	 */
	public static Comparator<Dominatable> comparator = new Comparator<Dominatable>() {
		@Override
		public int compare(final Dominatable a, final Dominatable b) {
			int compare = Double.compare(a.getCost(), b.getCost());
			
			if (compare != 0) {
				return compare;
			}
			else {
				return Double.compare(b.getScore(), a.getScore());
			}
		}
	};
}
