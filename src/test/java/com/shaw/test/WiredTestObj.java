package com.shaw.test;

import java.math.BigDecimal;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.shaw.bo.ParkingCardType;
import com.shaw.mapper.ParkingCardTypeMapper;

public class WiredTestObj extends SpringTestCase {

	@Autowired
	private ParkingCardTypeMapper mapper;

	@Test
	public void testAi() throws Exception {
		ParkingCardType type = new ParkingCardType();
		type.setCardType("www");
		type.setCreateTime(System.currentTimeMillis());
		type.setFee(new BigDecimal("100"));
		type.setValidTime((short) 1);
		type.setIsValid((short) 1);

		System.out.println(mapper.selectAllCardType());

		System.out.println(type.getId());
	}
}
