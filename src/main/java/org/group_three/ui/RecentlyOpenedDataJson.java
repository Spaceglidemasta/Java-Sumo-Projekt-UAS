package org.group_three.ui;

import java.util.List;

public class RecentlyOpenedDataJson {

	public RecentlyOpenedDataJson () {}
	public RecentlyOpenedDataJson (List<String> sims) {
		this.sims = sims;
	}

	public List<String> sims;
}
