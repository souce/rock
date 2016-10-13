package main.java.com.lib;

import main.java.com.exception.ExecureException;

/*
 * 扩展函数接口，实现了该接口的对象可以被注册，然后在代码中调用
 */
public interface IFunction {
	public FunctionValue invoke(FunctionValue ... obj) throws ExecureException;
}
