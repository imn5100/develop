package com.shaw.remote;

import java.rmi.RemoteException;

import javax.naming.Context;

import javax.naming.InitialContext;
import javax.naming.NamingException;

public class WarehouseServer {
	public static void main(String[] args) throws RemoteException, NamingException {
		//创建服务
		WarehouseImpl centralWarehouse = new WarehouseImpl();
		//绑定 服务到 registry 注册中心
		Context namingContext = new InitialContext();
		namingContext.bind("rmi:central_warehouse", centralWarehouse);
		
		//等待clients
		
	}
}
