package com.shaw.vo;

import java.util.List;

public class TURINGFoodMenuResponse extends TURINGResponse {
	private List<FoodMenu> list;
	public List<FoodMenu> getList() {
		return list;
	}
	public void setList(List<FoodMenu> list) {
		this.list = list;
	}
}
