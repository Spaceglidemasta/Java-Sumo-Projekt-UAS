package org.group_three.ui;

import java.util.List;

/**
 * A JSON data class.
 *
 * @author Joel
 */
public class RecentlyOpenedDataJson {

	/**
	 * The list of recently loaded simulations.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	public List<String> sims;

	/**
	 * The empty constructor to potentially load data in later.
	 *
	 * @author Joel
	 */
	public RecentlyOpenedDataJson() {
	}

	/**
	 * The normal constructor to load this JSON data class with data.
	 *
	 * @param sims The list of recently loaded simulations.
	 */
	public RecentlyOpenedDataJson(List<String> sims) {
		this.sims = sims;
	}
}