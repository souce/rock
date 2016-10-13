package main.java.com.lib;

public class PrintFunction implements IFunction{

	@Override
	public FunctionValue invoke(FunctionValue... obj) {
		switch (obj[0].getValueType()) {
		case INT:
			System.out.println(obj[0].getIntVal());
			break;
		case STRING:
			System.out.println(obj[0].getStrVal());
			break;
		case LIST:
			break;

		default:
			break;
		}
		return null;
	}

}
