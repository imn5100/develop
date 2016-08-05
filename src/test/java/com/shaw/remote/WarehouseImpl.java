package com.shaw.remote;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class WarehouseImpl extends UnicastRemoteObject implements Warehouse {
	private static final long serialVersionUID = 1L;
	private Map<String, Double> prices;

	protected WarehouseImpl() throws RemoteException {
		// super();
		prices = new HashMap<String, Double>();
		prices.put("Blackwell Toaster", 24.99);
		prices.put("ZapXpress Microware Oven", 49.95);
	}

	@Override
	public double getPrice(String desc) throws RemoteException {
		return prices.get(desc) == null ? 0 : prices.get(desc);
	}
}

