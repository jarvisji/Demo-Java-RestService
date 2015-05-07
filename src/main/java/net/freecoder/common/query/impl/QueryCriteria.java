/**
 * 
 */
package net.freecoder.common.query.impl;

/**
 * Object which wrap column, value and operator for where clause, and construct
 * query string.
 * 
 * @author JiTing
 */
public class QueryCriteria {

	/**
	 * Operator enum of query criteria.
	 * 
	 * @author JiTing
	 */
	public static enum OP {
		EQ("="), GT(">"), LT("<"), GE(">="), LE("<="), LIKE(" like "), NOTLIKE(
				" not like ");

		private String operator;

		private OP(String operator) {
			this.operator = operator;
		}

		/**
		 * Retrun SQL operator.
		 * 
		 * @return SQL operator.
		 */
		public String getOperator() {
			return this.operator;
		}
	}

	private String column;
	private String value;
	private OP operator;

	/**
	 * Constructor.
	 * 
	 * @param column
	 *            Column name.
	 * @param value
	 *            Value.
	 * @param operator
	 *            Operator.
	 */
	public QueryCriteria(String column, String value, OP operator) {
		this.column = column;
		this.value = value;
		this.operator = operator;
	}

	/**
	 * Construct query string.
	 * 
	 * @return Query string.
	 */
	public String toString() {
		StringBuffer queryStr = new StringBuffer(column)
				.append(operator.getOperator()).append("'").append(value)
				.append("'");
		return queryStr.toString();

	}
}
