package com.shaw.aop;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shaw.service.impl.RedisClient;

/**
 * 查询不敏感数据使用缓存，缓存每10S过期。
 */
@Aspect
@Component
public class CacheAop {
	@Autowired
	private RedisClient redisClient;
	public static Long EXPIRETIME = 10L;

	@Pointcut("execution(public * com.shaw.service.impl.*.select*(..))")
	public void actionMethod() {
	}

	@Around("actionMethod()")
	public Object around(ProceedingJoinPoint pjp) throws Throwable {
		Object value = null;
		String targetName = pjp.getTarget().getClass().getName();
		// 拦截的方法名称
		String methodName = pjp.getSignature().getName();
		Object[] arguments = pjp.getArgs();
		String key = getCacheKey(targetName, methodName, arguments);
		// 保证redis 的key值不能超过50个字符
		if (key.length() > 50) {
			return pjp.proceed();
		}
		try {
			// 判断是否有缓存
			if (exists(key)) {
				return getCache(key);
			}
			// 写入缓存
			value = pjp.proceed();
			if (value != null) {
				final String tkey = key;
				final Object tvalue = value;
				new Thread(new Runnable() {
					public void run() {
						setCache(tkey, tvalue, EXPIRETIME);
					}
				}).start();
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (value == null) {
				return pjp.proceed();
			}
		}
		return value;
	}

	private String getCacheKey(String targetName, String methodName, Object[] arguments) {
		StringBuffer sbu = new StringBuffer();
		sbu.append(targetName).append("_").append(methodName);
		if ((arguments != null) && (arguments.length != 0)) {
			for (int i = 0; i < arguments.length; i++) {
				sbu.append("_").append(arguments[i]);
			}
		}
		return sbu.toString();
	}

	public boolean exists(final String key) {
		return redisClient.exists(key);
	}

	public Object getCache(final String key) {
		Object result = null;
		result = redisClient.get(key);
		return result;
	}

	public boolean setCache(final String key, Object value, Long expireTime) {
		if (StringUtils.isBlank(key) || value == null) {
			return false;
		}
		boolean result = false;
		try {
			redisClient.set(key, value);
			redisClient.expire(key, expireTime);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
