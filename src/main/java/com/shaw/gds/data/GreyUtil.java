package com.shaw.gds.data;

import java.util.ArrayList;
import java.util.List;

public class GreyUtil {
	public static List<Quotas> buildComQuatas(Double[] qw, int levelCount)
			throws Exception {
		List<Quotas> qs = new ArrayList<Quotas>();

		for (int i = 1; i <= levelCount; i++) {
			Double val = Double.parseDouble(i + "");
			Double[] da = { val, val, val, val, val, val, val, val, val };
			Quotas q = new Quotas(da, qw);
			qs.add(q);
		}
		return qs;
	}

}
