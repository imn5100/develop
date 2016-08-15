package com.shaw.thread;

import java.util.concurrent.Callable;

public class AsynTask implements Callable<ReturnEntity> {
	// 任务名，方便定位错误
	private String taskName;
	// 具体执行
	private Execute execute;

	private AsynTask(String taskName) {
		this.taskName = taskName;
	}

	public static AsynTask newTask(String taskName) {
		return new AsynTask(taskName);
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public AsynTask registExecute(Execute execute) {
		this.execute = execute;
		return this;
	}

	@Override
	// 执行是两种情况，错误执行者值返回异常回去
	public ReturnEntity call() throws Exception {
		ReturnEntity returnEntity = new ReturnEntity();
		try {
			returnEntity.setResult(execute.execute());
		} catch (Exception e) {
			returnEntity.setException(e);
		}
		return returnEntity;
	}

	public interface Execute {
		Object execute() throws Exception;
	}
}
