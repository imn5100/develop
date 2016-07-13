package com.shaw.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shaw.bo.ParkingCardType;
import com.shaw.mapper.ParkingCardTypeMapper;
import com.shaw.service.IParkingCardTypeService;

@Service
public class ParkingCardTypeServiceImpl implements IParkingCardTypeService {
	@Autowired
	ParkingCardTypeMapper parkingCardTypeMapper;

	@Override
	public int deleteByPrimaryKey(Integer id) {
		return parkingCardTypeMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(ParkingCardType record) {
		return parkingCardTypeMapper.insert(record);
	}

	@Override
	public int insertList(List<ParkingCardType> list) {
		if (list != null && list.size() > 0)
			return parkingCardTypeMapper.batchInsert(list);
		else
			return 0;
	}

	@Override
	public ParkingCardType selectByTypeName(String name) {
		return this.parkingCardTypeMapper.selectByTypeName(name);
	}

	@Override
	public Map<String, ParkingCardType> selectAllCardType() {
		List<ParkingCardType> list = this.parkingCardTypeMapper.selectAllCardType();
		Map<String, ParkingCardType> map = new HashMap<>();
		if (list != null && list.size() > 0) {
			for (ParkingCardType cardType : list) {
				map.put(cardType.getCardType(), cardType);
			}
		}
		return map;
	}

	@Override
	public ParkingCardType selectByPrimaryKey(Integer id) {
		return this.parkingCardTypeMapper.selectByPrimaryKey(id);
	}
}
