package com.shaw.gds.data;

import java.util.ArrayList;
import java.util.List;

/**
 * 指标集合
 * */
public class Quotas {
	List<Quota> quotas;

	public List<Quota> getQuotas() {
		return quotas;
	}

	public void setQuotas(List<Quota> quotas) {
		this.quotas = quotas;
	}

	public Quotas(List<Quota> qs) {
		this.quotas = qs;
	}

	public Quotas(Double[] values, Double[] weights) throws Exception {
		if (values.length != weights.length)
			throw new RuntimeException("数据异常");
		quotas = new ArrayList<Quotas.Quota>();
		for (int i = 0; i < values.length; i++) {
			quotas.add(new Quota(values[i], weights[i]));
		}
	}

	public static Quotas MergerQuotas(Quotas q1, Quotas q2) {
		List<Quota> list = new ArrayList<Quotas.Quota>();
		list.addAll(q1.getQuotas());
		list.addAll(q2.getQuotas());
		return new Quotas(list);
	}

	public List<Double> getAllQuotaValue() {
		List<Double> vals = new ArrayList<Double>();
		for (Quota q : quotas) {
			vals.add(q.value);
		}
		return vals;
	}

	public List<Double> getAllQuotaWeight() {
		List<Double> weights = new ArrayList<Double>();
		for (Quota q : quotas) {
			weights.add(q.weight);
		}
		return weights;
	}

	private class Quota {
		Double value;
		Double weight;

		public Quota(Double value, Double weight) {
			this.value = value;
			this.weight = weight;
		}
	}
}
