package com.shaw.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5SHA256 {

	private MD5SHA256() {
		throw new AssertionError();
	}

	private static final String ALGORITHM_SHA256 = "SHA-256";
	private static final String ALGORITHM_MD5 = "MD5";

	/**
	 * Encrypt(进行SHA256或者MD5加密)
	 * 
	 * @param name
	 *            参数描述
	 * @return name 返回值描述
	 * @Exception 异常对象
	 */
	private static String Encrypt(String orignal, String algorithm) {

		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance(algorithm);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		if (null != md) {
			try {
				byte[] origBytes = orignal.getBytes("UTF-8");
				md.update(origBytes);
				byte[] digestRes = md.digest();
				String resultString = byteToHexString(digestRes);
				return resultString;
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return null;
		}
		return null;
	}

	/**
	 * getDigestStr(将指定byte数组转换成16进制字符串 )
	 * 
	 * @param name
	 *            参数描述
	 * @return name 返回值描述
	 * @Exception 异常对象
	 */
	private static String byteToHexString(byte[] origBytes) {

		StringBuilder hexString = new StringBuilder();
		for (int i = 0; i < origBytes.length; i++) {

			// 这里按位与是为了把字节转整时候取其正确的整数，java中一个int是4个字节
			// 如果origBytes[i]最高位为1，则转为int时，int的前三个字节都被1填充了
			// 0XFF表示十进制的255，其中0X是java中用来申明16进制字符使用的，此时表示取当前字节的反码
			String tempStr = Integer.toHexString(origBytes[i] & 0xff);
			if (tempStr.length() == 1) {
				hexString.append("0");
			}
			hexString.append(tempStr);
		}
		return hexString.toString();
	}

	/**
	 * getEncryptedPwd(SHA256+MD5+盐加密)
	 * 
	 * @param name
	 *            参数描述
	 * @return name 返回值描述
	 * @Exception 异常对象
	 */
	public static String getEncryptedPwd(String sourceString) {
		String salt = PropertiesUtil.getConfiguration().getString("salt");
		return Encrypt(Encrypt(sourceString + salt, ALGORITHM_SHA256), ALGORITHM_MD5);
	}

	/**
	 * md5(MD5加密)
	 * 
	 * @param str
	 *            加密前的内容
	 * @return String 加密后的内容
	 */
	public static String md5(String str) {
		return Encrypt(str, ALGORITHM_MD5);
	}

	public static void main(String[] args) {
		System.out.println(md5("123456"));
	}
}