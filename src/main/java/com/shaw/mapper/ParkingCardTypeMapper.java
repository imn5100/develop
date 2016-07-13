package com.shaw.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.shaw.bo.ParkingCardType;

public interface ParkingCardTypeMapper {
	int deleteByPrimaryKey(Integer id);

	int insert(ParkingCardType record);

	int insertSelective(ParkingCardType record);

	ParkingCardType selectByPrimaryKey(Integer id);

	ParkingCardType selectByTypeName(@Param("name") String name);

	int updateByPrimaryKeySelective(ParkingCardType record);

	int updateByPrimaryKey(ParkingCardType record);

	int batchInsert(List<ParkingCardType> list);

	List<ParkingCardType> selectAllCardType();

}