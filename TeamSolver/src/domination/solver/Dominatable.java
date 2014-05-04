package domination.solver;

/**
 * Provides a cost and score to operate in a {@link DominationFilter}.
 */
public interface Dominatable {
	double getCost();

	double getScore();
}
