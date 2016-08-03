package com.shaw.test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Random;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

public class ProxyTest {
	public static void main(String[] args) {
		// jdk 代理测试
		Object[] elements = new Object[10];
		for (int i = 0; i < elements.length; i++) {
			Integer value = i + 1;
			elements[i] = new TraceHandler().bind(value);
		}
		Integer key = new Random().nextInt(elements.length);
		int result = Arrays.binarySearch(elements, key);
		System.out.println(result);

		// cglib 代理测试 无法使用final类作为代理对象
		ProxyTest target = new CGLIBHandler<ProxyTest>().createProxyInstance(new ProxyTest());
		System.out.println(target.getMyId());

	}

	public Integer getMyId() {
		return 1;
	}
}
//JDK代理
class TraceHandler implements InvocationHandler {
	private Object target;

	public Object bind(Object target) {
		// jdk代理需要绑定接口
		this.target = target;
		// 这里无法使用泛型，强行转换为 泛型类型,出现cast异常。因为代理类和目标类 没有关系。但CGLIB可以。
		return Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), this);
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		System.out.println("method Name:" + method.getName());
		System.out.println("Args :" + args);
		return method.invoke(target, args);
	}

}

// 目标对象不须实现接口。因为生成的代理对象时目标对象的子类。
// 缺点 Cannot subclass final class class java.lang.Integer 无法代理final类
// 使用泛型的代理类
class CGLIBHandler<T> implements MethodInterceptor {
	private T target;

	@SuppressWarnings("unchecked")
	public T createProxyInstance(T target) {
		this.target = target;
		Enhancer enhancer = new Enhancer();
		// 设置代理父类 ,这里如果不绑定父类 则无法正常执行目标方法。
		enhancer.setSuperclass(this.target.getClass());
		// 代理回调函数
		enhancer.setCallback(this);
		return (T) enhancer.create();
	}

	@Override
	public Object intercept(Object object, Method method, Object[] arg1, MethodProxy proxy) throws Throwable {
		//排除对Object方法的代理
		boolean objFlag = method.getDeclaringClass().getName().equals("java.lang.Object");
		if (!objFlag) {
			System.out.println("before");
		}
		System.out.println("method Name:" + method.getName());
		System.out.println("start invoke method");
		/* method.invoke(target,arg1); 相同效果 */
		Object result = proxy.invokeSuper(object, arg1);
		System.out.println("invoke end");
		return result;
	}
}
