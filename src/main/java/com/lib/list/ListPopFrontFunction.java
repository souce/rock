package main.java.com.lib.list;

import java.util.List;

import main.java.com.lib.FunctionValue;
import main.java.com.lib.IFunction;

public class ListPopFrontFunction implements IFunction{

	@Override
	public FunctionValue invoke(FunctionValue... obj) {
		if(obj.length != 1){
			System.err.println("list pop arg err!");
		}
		List<FunctionValue> listValue = obj[0].getListVal();
		if(listValue.size() == 0){
			System.err.println("list size is zero!");
		}
		return listValue.get(0);
	}

}
