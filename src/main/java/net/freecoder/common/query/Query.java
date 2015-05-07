package net.freecoder.common.query;

import java.util.List;

/**
 * Describes basic methods for querying.
 * 
 * @param <T>
 * @param <U>
 * @author Frederik Heremans
 */
public interface Query<T extends Query<?, ?>, U extends Object> {

	/**
	 * Order the results ascending on the given property as defined in this
	 * class (needs to come after a call to one of the orderByXxxx methods).
	 * 
	 * @return <T>
	 */
	T asc();

	/**
	 * Order the results descending on the given property as defined in this
	 * class (needs to come after a call to one of the orderByXxxx methods).
	 * 
	 * @return <T>
	 */
	T desc();

	/**
	 * Executes the query and returns the number of results.
	 * 
	 * @return long
	 */
	long count();

	/**
	 * Executes the query and returns the resulting entity or null if no entity
	 * matches the query criteria.
	 * 
	 * @return <U>
	 */
	U singleResult();

	/**
	 * Executes the query and get a list of entities as the result.
	 * 
	 * @return List<U>
	 */
	List<U> list();

	/**
	 * Executes the query and get a list of entities as the result.
	 * 
	 * @param firstResult
	 *            The number of first result.
	 * @param maxResults
	 *            Max number of results.
	 * @return List<U>
	 */
	List<U> listPage(int firstResult, int maxResults);
}
