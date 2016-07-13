package com.shaw.gds.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 因素操作类
 * 
 * */
public class Factors {
	public static final Double P = 0.5;

	/**
	 * @param factors
	 * @param compQuotas
	 * @return
	 */
	public static List<Double> getFinalGreyValues(List<Factor> factors,
			List<Quotas> compQuotas) {
		List<List<Double>> indirectDatas = getIndirectDataList(factors,
				compQuotas);
		List<List<Double>> relationDatas = getRelationDatas(indirectDatas);
		System.out.println("关联系数是：");
		print(relationDatas);

		List<Double> weis = getAllWeights(factors);
		System.out.println("指标权重计算");
		for (Double d : weis) {
			System.out.print(d + "\t");
		}
		weis = getCalculateWeights(weis);
		System.out.println("\n计算用指标权重计算");
		for (Double d : weis) {
			System.out.print(d + "\t");
		}

		weis = getGreyValues(relationDatas, weis);
		System.out.println("\n计算得关联度");
		for (Double d : weis) {
			System.out.print(d + "\t");
		}

		weis = toNormalizationAndUncenternCorrecte(weis, 0.1D);
		System.out.println("\n归一化后不确定修正得：");
		for (Double d : weis) {
			System.out.print(d + "\t");
		}
		List<Double> d2 = Arrays.asList(0.152551946, 0.200175243, 0.237524806,
				0.213331347, 0.146416659, 0.05);
		weis = DSMerger(weis, d2);
		System.out.println("\n与D2融合得：");
		for (Double d : weis) {
			System.out.print(d + "\t");
		}

		List<Double> d3 = Arrays.asList(0.122114006, 0.163230999, 0.219647782,
				0.195607824, 0.149399389, 0.15);
		weis = DSMerger(weis, d3);
		System.out.println("\n与D3融合得：");
		for (Double d : weis) {
			System.out.print(d + "\t");
		}

		return weis;
	}

	/**
	 * @param d1
	 * @param d2
	 * @return 
	 *    xsum=A62*B63+A62*C63+A62*D63+A62*E63+ B62*A63+B62*C63+B62*D63+B62*E63+
	 *         C62*A63+C62*B63+C62*D63+C62*E63+ D62*A63+D62*B63+D62*C63+D62*E63+
	 *         E62*A63+E62*B63+E62*C63+E62*D63 
	 * 	  newlist=A62*A63+A62*F63+A63*F62
	 *    lastData = newlist/(1-xsum)
	 */
	public static List<Double> DSMerger(List<Double> d1, List<Double> d2) {
		if (d1.size() != d2.size())
			throw new RuntimeException("融合数据不匹配");
		List<Double> newList = new ArrayList<Double>();
		Double xSum = 0D, u1 = d1.get(d1.size() - 1), u2 = d2
				.get(d2.size() - 1);
		for (int i = 0; i < d1.size() - 1; i++) {
			for (int j = 0; j < d2.size() - 1; j++) {
				if (i == j) {
					newList.add(d1.get(i) * d2.get(j) + d1.get(i) * u2
							+ d2.get(j) * u1);
					continue;
				} else
					xSum += d1.get(i) * d2.get(j);
			}
		}
		newList.add(u1 * u2);

		for (int i = 0; i < newList.size(); i++) {
			newList.set(i, newList.get(i) / (1 - xSum));
		}

		return newList;
	}

	/**
	 * @param data
	 *            归一化的数据并不确定修正
	 * @param u
	 *            不确定度
	 * @return 归一化后不确定修正的数据
	 */
	public static List<Double> toNormalizationAndUncenternCorrecte(
			List<Double> data, Double u) {
		List<Double> normalList = new ArrayList<Double>();
		Double sum = 0D;
		for (Double d : data) {
			sum += d;
		}
		for (Double d : data) {
			normalList.add(d / sum * (1 - u));
		}
		normalList.add(u);
		return normalList;
	}

	/**
	 * @param relationDatas
	 * @param weis
	 * @return 综合权重计算灰关联度
	 */
	public static List<Double> getGreyValues(List<List<Double>> relationDatas,
			List<Double> weis) {
		List<Double> greyValues = new ArrayList<Double>();
		for (List<Double> d1 : relationDatas) {
			Double sum = 0D;
			for (int i = 0; i < d1.size(); i++) {
				sum += (d1.get(i) * weis.get(i));
			}
			greyValues.add(sum / d1.size());
		}

		return greyValues;
	}

	/**
	 * @param data
	 *            输出关联系数数组
	 */
	private static void print(List<List<Double>> data) {
		for (List<Double> d1 : data) {
			for (Double d2 : d1) {
				System.out.print(d2 + "\t");
			}
			System.out.println("");
		}

	}

	/**
	 * @param factors
	 * @return 指标权重计算
	 */
	private static List<Double> getAllWeights(List<Factor> factors) {
		List<Double> allWeights = new ArrayList<Double>();
		for (Factor f : factors) {
			allWeights.addAll(f.getWeights());
		}
		return allWeights;
	}

	/**
	 * @param weights
	 * @return // 计算用指标权重计算
	 */
	private static List<Double> getCalculateWeights(List<Double> weights) {
		Double max = Collections.max(weights);
		List<Double> calculateWeights = new ArrayList<Double>();
		for (Double d : weights) {
			calculateWeights.add(d / max);
		}
		return calculateWeights;
	}

	/**
	 * @param factors
	 * @return // 获取所有因素的指标值
	 */
	private static List<Double> getAllQuotasList(List<Factor> factors) {
		List<Double> qs = new ArrayList<Double>();
		for (Factor f : factors) {
			qs.addAll(f.getQuotasListData());
		}

		return qs;
	}

	/**
	 * @param d1
	 * @param cd
	 * @return // 获取关联系数的间接计算数据
	 */
	private static List<Double> getIndirectData(List<Double> d1, List<Double> cd) {
		List<Double> datas = new ArrayList<Double>();
		for (int i = 0; i < d1.size(); i++) {
			datas.add(Math.abs(d1.get(i) - cd.get(i)));
		}
		return datas;
	}

	/**
	 * @param factors
	 * @param compQuotas
	 * @return // 得到计算的中间数据序列
	 */
	private static List<List<Double>> getIndirectDataList(List<Factor> factors,
			List<Quotas> compQuotas) {
		List<Double> fData = getAllQuotasList(factors);
		List<List<Double>> comData = new ArrayList<List<Double>>();
		List<List<Double>> indirectData = new ArrayList<List<Double>>();
		for (Quotas q : compQuotas) {
			comData.add(q.getAllQuotaValue());
		}
		for (List<Double> d : comData) {
			indirectData.add(getIndirectData(fData, d));
		}
		return indirectData;
	}

	/**
	 * @param data
	 * @return // 得到关联系数的序列
	 */
	private static List<List<Double>> getRelationDatas(List<List<Double>> data) {
		List<List<Double>> relationDatas = new ArrayList<List<Double>>();
		Double maxmax = getMaxMax(data);
		Double minmin = getMinMin(data);
		System.out.println("max" + maxmax + "  min:" + minmin);
		for (List<Double> d1 : data) {
			List<Double> myData = new ArrayList<Double>();
			for (Double d2 : d1) {
				myData.add((minmin + Factors.P * maxmax)
						/ (d2 + Factors.P * maxmax));
			}
			relationDatas.add(myData);
		}

		return relationDatas;
	}

	private static Double getMaxMax(List<List<Double>> data) {
		List<Double> max = new ArrayList<Double>();
		for (List<Double> d1 : data)
			max.add(Collections.max(d1));
		return Collections.max(max);
	}

	private static Double getMinMin(List<List<Double>> data) {
		List<Double> min = new ArrayList<Double>();
		for (List<Double> d1 : data)
			min.add(Collections.min(d1));
		return Collections.min(min);
	}

}
