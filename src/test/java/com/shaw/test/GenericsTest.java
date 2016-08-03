package com.shaw.test;

import java.util.ArrayList;
import java.util.List;

public class GenericsTest {

	public static void main(String[] args) throws InstantiationException, IllegalAccessException {
		List<String> list = makeList(String.class);
		System.out.println(list.get(0));
	}

	public static <T> List<T> makeList(Class<T> c) throws InstantiationException, IllegalAccessException {
		List<T> list = new ArrayList<T>();
		list.add(c.newInstance());
		return list;
	}

}
