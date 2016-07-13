package com.shaw.test;

import org.python.core.Py;
import org.python.core.PyFunction;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.core.PySystemState;
import org.python.util.PythonInterpreter;

public class TestClass {
	private static PythonInterpreter interpreter = new PythonInterpreter();

	public static void main(String[] args) throws Exception {
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
		// Double[] d1 = { 3.5, 3.0, 4.0 };
		// Double[] d2 = { 0.333, 0.222, 0.445 };
		// Quotas q1 = new Quotas(d1, d2);
		// //
		// // // 初始化因素2 包含指标 的打分和权重
		// Double[] d3 = { 3.0, 2.0 };
		// Double[] d4 = { 0.4, 0.6 };
		// Quotas q2 = new Quotas(d3, d4);
		// // // 初始化因素3 包含指标 的打分和权重
		// Double[] d5 = { 3.5, 4.0 };
		// Double[] d6 = { 0.6, 0.4 };
		// Quotas q3 = new Quotas(d5, d6);
		// // 初始化因素4 包含指标 的打分和权重
		// Double[] d7 = { 2.5, 3.0 };
		// Double[] d8 = { 0.5, 0.5 };
		// Quotas q4 = new Quotas(d7, d8);
		// List<Factor> fs = new ArrayList<Factor>();
		// Factor f1 = new Factor(0.4, q1);
		// Factor f2 = new Factor(0.133, q2);
		// Factor f3 = new Factor(0.267, q3);
		// Factor f4 = new Factor(0.2, q4);
		// fs.add(f1);
		// fs.add(f2);
		// fs.add(f3);
		// fs.add(f4);
		//
		// Double[] qw = { 0.333, 0.222, 0.445, 0.4, 0.6, 0.6, 0.4, 0.5, 0.5 };O
		// List<Quotas> qs = GreyUtil.buildComQuatas(qw, 5);
		// Factors.getFinalGreyValues(fs, qs);

		// 根据本地python import sys print sys.path 获得的完整库
		String[] paths = { "D:\\Python27\\Lib\\idlelib", "D:\\Python27\\lib\\site-packages\\requests-2.9.1-py2.7.egg",
				"D:\\Python27\\lib\\site-packages\\bencode-1.0-py2.7.egg",
				"D:\\Python27\\lib\\site-packages\\magneturi-1.2-py2.7.egg", "C:\\Windows\\system32\\python27.zip",
				"D:\\Python27\\DLLs", "D:\\Python27\\lib", "D:\\Python27\\lib\\plat-win", "D:\\Python27\\lib\\lib-tk",
				"D:\\Python27", "D:\\Python27\\lib\\site-packages" };
		PySystemState sys = Py.getSystemState();
		sys.setdefaultencoding("UTF-8");
		for (String path : paths) {
			sys.path.append(new PyString(path));
		}
		interpreter.execfile("E://download//complete//Torrent2Magnet.py");
		PyFunction func = (PyFunction) interpreter.get("Torrent2Magnet", PyFunction.class);

		String filepath = "0.torrent";
		PyObject pyobj = func.__call__(new PyString(filepath));
		System.out.println("magnet = " + pyobj.toString());

	}

}