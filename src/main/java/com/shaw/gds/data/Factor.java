package com.shaw.gds.data;

import java.util.ArrayList;
import java.util.List;

/**
 * 因素
 * */
public class Factor {
	private Double weight;
	private Quotas qs;

	public List<Double> getWeights() {
		List<Double> flist = new ArrayList<Double>();
		List<Double> qWeights = qs.getAllQuotaWeight();
		for (Double d : qWeights) {
			flist.add(d * weight);
		}

		return flist;

	}

	public Factor(Double weight, Quotas qs) {
		this.weight = weight;
		this.qs = qs;
	}

	public List<Double> getQuotasListData() {
		return qs.getAllQuotaValue();
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public Quotas getQs() {
		return qs;
	}

	public void setQs(Quotas qs) {
		this.qs = qs;
	}

}
