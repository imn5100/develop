package com.shaw.bo;

import java.util.List;

public class PieChartData {
	private List<String> titles;
	private List<Index> values;

	public List<String> getTitles() {
		return titles;
	}

	public void setTitles(List<String> titles) {
		this.titles = titles;
	}

	public List<Index> getValues() {
		return values;
	}

	public void setValues(List<Index> values) {
		this.values = values;
	}

}