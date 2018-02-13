package main.java.com.lib;

import java.util.List;

/*
 * 方法的参数和返回值的封装
 */
public class FunctionValue {
	public enum valueType{
		INT,
		STRING,
		LIST
	}
	private valueType type;
	private int intVal;
	private String strVal;
	private List<FunctionValue> listVal;
	
	public FunctionValue(int intVal) {
		this.intVal = intVal;
		type = valueType.INT;
	}
	public FunctionValue(String strVal) {
		this.strVal = strVal;
		type = valueType.STRING;
	}
	public FunctionValue(List<FunctionValue> listVal) {
		this.listVal = listVal;
		type = valueType.LIST;
	}

	public valueType getValueType() {
		return type;
	}
	public int getIntVal() {
		return intVal;
	}
	public void setIntVal(int intVal) {
		this.intVal = intVal;
		type = valueType.INT;
	}
	public String getStrVal() {
		return strVal;
	}
	public void setStrVal(String strVal) {
		this.strVal = strVal;
		type = valueType.STRING;
	}
	public List<FunctionValue> getListVal() {
		return listVal;
	}
	public void setListVal(List<FunctionValue> listVal) {
		this.listVal = listVal;
		type = valueType.LIST;
	}
	
}
