package com.shaw.thread;

public class ReturnEntity {
	private Exception exception;
	private Object result;

	public Exception getException() {
		return exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	// 抛出异常
	public void throwException() throws Exception {
		if (this.exception != null) {
			throw this.exception;
		}
	}

	// 查看是否包含结果
	public boolean hasResult() {
		return null != this.result;
	}
}
