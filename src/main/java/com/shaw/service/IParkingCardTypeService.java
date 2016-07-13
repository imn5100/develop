package com.shaw.service;

import java.util.List;
import java.util.Map;

import com.shaw.bo.ParkingCardType;

public interface IParkingCardTypeService {
	 int deleteByPrimaryKey(Integer id);

	 int insert(ParkingCardType record);

	 int insertList(List<ParkingCardType> list);
	 
	 ParkingCardType  selectByTypeName(String name);

	 ParkingCardType  selectByPrimaryKey(Integer id);
	 
	 Map<String,ParkingCardType>    selectAllCardType();
}
