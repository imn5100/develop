package com.shaw.test;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;

public class BdSign {

	public static void main(String[] args) {
		String url = "http://api.map.baidu.com/geocoder/v2/";
		RestTemplate restTemplate = new RestTemplate();
		MultiValueMap<String, String> params2 = new LinkedMultiValueMap<String, String>();
//		params.add("ak", "HL2mUQEeUG1gwD5UL8Y7xVLP");
//		params.add("output", "json");
//		// 必须参数
//		params.add("address", "教工路552号");
//		// 非必须
//		params.add("city", "杭州市");
//		// 签名非必须。
//		// String sign = genSign(params);
//		// params.add("sn", sign);
//		String response = restTemplate.postForObject(url, params, String.class);
//		System.out.println("getLocation:" + response);

		params2.add("location", "30.30170711341427,120.14058752415298");
		params2.add("ak", "qm0VHAwWQKPrD29f6uOUTEj2");
		params2.add("output", "json");
		String response2 = restTemplate.postForObject(url, params2, String.class);
		System.out.println("getAdress:" + response2);

	}

	/**
	 * 根据方法参数，生成签名
	 */
	public static String genSign(Map paramsMap) {
		// 排序，拼接json字符串
		Map<String, Object> sortedMap = sortMap(paramsMap);
		sortedMap.remove("sign");
		JSONObject jsonObject = new JSONObject(true);
		jsonObject.putAll(sortedMap);
		String jsonStr = jsonObject.toString();
		// unicode编码过滤
		jsonStr = chineseToUnicode(jsonStr);

		// "{}"替换为"[]"
		jsonStr = jsonStr.replaceAll("\\{\\}", "\\[\\]");

		// 请求中的url处理
		jsonStr = jsonStr.replace("/", "\\/");

		// 计算md5
		return MD5Util.MD5(jsonStr).toUpperCase();
	}

	/**
	 * 请求参数解码
	 */
	public static boolean decode(Map<String, String> params, String enc) {
		if (params == null) {
			return true;
		}
		try {
			// 遍历map，key是英文的不需要转码，value需要转码
			for (Map.Entry<String, String> entry : params.entrySet()) {
				Object value = entry.getValue();
				if (value != null) {
					if (value instanceof String) {
						params.put(entry.getKey(), URLDecoder.decode((String) value, enc));
					} else if (value instanceof Map) {
						decode((Map<String, String>) value, enc);
					}
				}
			}
		} catch (UnsupportedEncodingException e) {
			return false;
		}
		return true;
	}

	/**
	 * 对map按key的ASCⅡ升序排列
	 */
	@SuppressWarnings("unchecked")
	private static Map<String, Object> sortMap(Map<String, Object> params) {
		if (params == null) {
			return null;
		}
		TreeMap<String, Object> sortedMap = new TreeMap<>();
		for (Map.Entry<String, Object> entry : params.entrySet()) {
			Object obj = entry.getValue();
			if (obj instanceof Map) {
				Map<String, Object> subSortedMap = sortMap((Map<String, Object>) obj);
				obj = subSortedMap == null ? StringUtils.EMPTY : subSortedMap;
			} else if (obj instanceof List) {
				List<Object> list = (List<Object>) obj;
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i) instanceof Map) {
						list.set(i, sortMap((Map) list.get(i)));
					}
				}
			} else {
				obj = entry.getValue();
			}
			sortedMap.put(entry.getKey(), obj);
		}
		return sortedMap;
	}

	/**
	 * 计算签名时，将字符串中非ASCⅡ字符转为unicode编码
	 */
	private static String filterWithUnicode(String str) {
		if (str == null) {
			return null;
		}

		String tmp;
		char c;
		int value;
		StringBuilder sb = new StringBuilder(str.length());
		for (int i = 0; i < str.length(); i++) {
			c = str.charAt(i);
			// 判断是否ASCⅡ字符(0-127)
			if (c <= 127) {
				sb.append(c);
				continue;
			}

			sb.append("\\u");
			// 取出高8位
			value = (c >>> 8);
			tmp = Integer.toHexString(value);
			if (tmp.length() == 1)
				sb.append("0");
			sb.append(tmp);
			// 取出低8位
			value = (c & 0xFF);
			tmp = Integer.toHexString(value);
			if (tmp.length() == 1)
				sb.append("0");
			sb.append(tmp);
		}
		return sb.toString();
	}

	/**
	 * 将字符串中中文字符转为unicode编码
	 */
	public static String chineseToUnicode(String str) {
		if (str == null) {
			return null;
		}

		StringBuilder sb = new StringBuilder(str.length());
		for (int i = 0; i < str.length(); i++) {
			int chr1 = str.charAt(i);
			// 如果是中文字符
			if (chr1 >= 19968 && chr1 <= 171941) {
				sb.append("\\u").append(Integer.toHexString(chr1));
			} else {
				sb.append(str.charAt(i));
			}
		}
		return sb.toString();
	}
}
