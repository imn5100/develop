package com.shaw.test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class GuavaCacheUtils {

	public static final int CACHE_EXPIRE_TIME = 2 * 60 - 1;
	@Autowired
	private RestTemplate restTemplate;
	private LoadingCache<String, String> cache;
	@Value("#{ config['wechat.appid'] }")
	private String APPID;
	@Value("#{ config['wechat.appsecret'] }")
	private String SECRET;
	@Value("#{ config['wechat.getTokenUrl'] }")
	private String TOKENURL;
	public static final String WECHAT_TOKEN_KEY = "wechat_token";

	public GuavaCacheUtils() {
		init();
	}

	private void init() {
		cache = CacheBuilder.newBuilder().softValues() // 使用SoftReference对象封装value,
				.expireAfterWrite(CACHE_EXPIRE_TIME, TimeUnit.MINUTES).build(new CacheLoader<String, String>() {
					@Override
					public String load(String key) throws Exception {
						return "";
					}
				});
	}

	public LoadingCache<String, String> getCache() {
		return cache;
	}

	public String getWeChatTokenAccess(Boolean byNetwork) throws ExecutionException {
		String token = "";
		token = this.cache.get(WECHAT_TOKEN_KEY);
		if (byNetwork || StringUtils.isBlank(token)) {
			return getWeChatTokenAccessByHttp();
		} else {
			return token;
		}
	}

	private String getWeChatTokenAccessByHttp() {
		String token = "";
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add("grant_type", "client_credential");
		params.add("appid", APPID);
		params.add("secret", SECRET);
		String response = restTemplate.postForObject(TOKENURL, params, String.class);
		JSONObject responseObj = JSONObject.parseObject(response);
		token = responseObj.getString("access_token");
		if (token != null) {
			this.cache.put(WECHAT_TOKEN_KEY, token);
			return token;
		} else
			return "";
	}

}
