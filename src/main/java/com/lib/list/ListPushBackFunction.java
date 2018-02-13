package main.java.com.lib.list;

import java.util.List;

import main.java.com.lib.FunctionValue;
import main.java.com.lib.IFunction;

public class ListPushBackFunction implements IFunction{

	@Override
	public FunctionValue invoke(FunctionValue... obj) {
		if(obj.length != 2){
			System.err.println("list push arg err!");
		}
		List<FunctionValue> listValue = obj[0].getListVal();
		listValue.add(listValue.size(), obj[1]);
		return null;
	}

}
