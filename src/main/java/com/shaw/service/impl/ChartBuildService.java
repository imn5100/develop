package com.shaw.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.shaw.bo.Index;
import com.shaw.bo.PieChartData;
import com.shaw.gds.data.Factor;
import com.shaw.gds.data.Factors;
import com.shaw.gds.data.GreyUtil;
import com.shaw.gds.data.Quotas;

@Service
public class ChartBuildService {

	public PieChartData getFinalData(List<String> titles) throws Exception {
		// 各因素权重
		// 0.4，0.133，0.267，0.2

		// 打分
		// 3.5,3.0,4.0,3.0,2.0,3.5,4.0,2.5,3.0

		// 各指标权重
		// 0.333，,0.222,0.445
		// 0.4,0.6
		// 0.6,0.4
		// 0.5,0.5
		// 初始化因素1 包含指标的 的打分和权重
		Double[] d1 = { 3.5, 3.0, 4.0 };
		Double[] d2 = { 0.333, 0.222, 0.445 };
		Quotas q1 = new Quotas(d1, d2);
		//
		// // 初始化因素2 包含指标 的打分和权重
		Double[] d3 = { 3.0, 2.0 };
		Double[] d4 = { 0.4, 0.6 };
		Quotas q2 = new Quotas(d3, d4);
		// // 初始化因素3 包含指标 的打分和权重
		Double[] d5 = { 3.5, 4.0 };
		Double[] d6 = { 0.6, 0.4 };
		Quotas q3 = new Quotas(d5, d6);
		// 初始化因素4 包含指标 的打分和权重
		Double[] d7 = { 2.5, 3.0 };
		Double[] d8 = { 0.5, 0.5 };
		Quotas q4 = new Quotas(d7, d8);
		List<Factor> fs = new ArrayList<Factor>();
		Factor f1 = new Factor(0.4, q1);
		Factor f2 = new Factor(0.133, q2);
		Factor f3 = new Factor(0.267, q3);
		Factor f4 = new Factor(0.2, q4);
		fs.add(f1);
		fs.add(f2);
		fs.add(f3);
		fs.add(f4);
		Double[] qw = { 0.333, 0.222, 0.445, 0.4, 0.6, 0.6, 0.4, 0.5, 0.5 };
		List<Quotas> qs = GreyUtil.buildComQuatas(qw, 5);
		PieChartData data = new PieChartData();
		data.setTitles(titles);
		List<Index> values = new ArrayList<Index>();
		List<Double> indexs = Factors.getFinalGreyValues(fs, qs);
		int i = 0;
		for (String name : titles) {
			if (indexs.size() != titles.size()) {
				return null;
			}
			Index index = new Index();
			index.setName(name);
			index.setValue(indexs.get(i++));
			values.add(index);
		}
		data.setValues(values);
		return data;
	}
}
