package main.java.com.lib.list;

import java.util.ArrayList;

import main.java.com.lib.FunctionValue;
import main.java.com.lib.IFunction;

public class ListNewFunction implements IFunction{

	@Override
	public FunctionValue invoke(FunctionValue... obj) {
		return new FunctionValue(new ArrayList<FunctionValue>());
	}

}
