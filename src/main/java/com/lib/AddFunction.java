package main.java.com.lib;

public class AddFunction implements IFunction{

	@Override
	public FunctionValue invoke(FunctionValue... obj) {
		int i = 0;
		switch (obj[0].getValueType()) {
		case INT:
			i = obj[0].getIntVal();
			//System.out.println("调用add函数："+i);
			break;
		case STRING:
			break;
		case LIST:
			break;

		default:
			break;
		}
		FunctionValue f = new FunctionValue(i+1);
		return f;
	}

}
