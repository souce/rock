package main.java.com.vm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import main.java.com.lib.FunctionValue;
import main.java.com.lib.IFunction;

public class StackVM {
	private HashMap<String, Object> globalMap = new HashMap<>();
	private Stack<Object> vmStack = new Stack<>();
	
	public void registerFunction(String name, IFunction function){
		globalMap.put(name, function);
	}
	
	public void execute(List<Order> orders) throws Exception{
		int curr = 0;
		while(true){
			if(curr >= orders.size()){
				throw new Exception("非正常的结束");
			}
			Order p = orders.get(curr);
			switch (p.op) {
				case PUSH:
						vmStack.push(p.value);
					break;
				case STOREGLOBAL:{
					//保存值到全局表中
					globalMap.put(p.value.toString(), vmStack.pop());
					break;
				}
				case PUSHGLOBAL:{
					//从全局表中取值
					String key = p.value.toString();
					Object val = globalMap.get(key);
					if(null == val){
						throw new Exception(String.format("在第%d行的‘%s’ 未声明。", p.lineNo, key));
					}
					vmStack.push(val);
					break;
				}				
				case ADDOP:{
					Object j = vmStack.pop();
					Object i = vmStack.pop();
					Object val = null;
					if(j instanceof Integer && i instanceof Integer){
						val = (int)i+(int)j;
					}else if(j instanceof String || i instanceof String){
						val = String.format("%s%s", i, j);
					}
					if(null == val){
						throw new Exception(String.format("在第%d行的‘%s’ 操作失败。", p.lineNo));
					}
					vmStack.push(val);
					break;
				}
				case SUBOP:{
					Object j = vmStack.pop();
					Object i = vmStack.pop();
					if(j instanceof Integer && i instanceof Integer){
						vmStack.push((int)i-(int)j);
					}else{
						throw new Exception(String.format("在第%d行的‘%s’ 类型与‘-’操作不符。", p.lineNo));
					}
					break;
				}
				case MULTOP:{
					Object j = vmStack.pop();
					Object i = vmStack.pop();
					if(j instanceof Integer && i instanceof Integer){
						vmStack.push((int)i*(int)j);
					}else{
						throw new Exception(String.format("在第%d行的‘%s’ 类型与‘*’操作不符。", p.lineNo));
					}
					break;
				}
				case DIVOP:{
					Object j = vmStack.pop();
					Object i = vmStack.pop();
					if(j instanceof Integer && i instanceof Integer){
						vmStack.push((int)i/(int)j);
					}else{
						throw new Exception(String.format("在第%d行的‘%s’ 类型与‘/’操作不符。", p.lineNo));
					}
					break;
				}
				case EQOP:{
					Object j = vmStack.pop();
					Object i = vmStack.pop();
					if(j instanceof Integer && i instanceof Integer){
						vmStack.push((int)i==(int)j?1:0);
					}else if(j instanceof String && i instanceof String){ 
						vmStack.push(i.equals(j)?1:0);
					}else{
						throw new Exception(String.format("在第%d行的‘%s’ 类型与‘==’操作不符。", p.lineNo));
					}
					break;
				}
				case NEOP:{
					Object j = vmStack.pop();
					Object i = vmStack.pop();
					if(j instanceof Integer && i instanceof Integer){
						vmStack.push((int)i!=(int)j?1:0);
					}else{
						throw new Exception(String.format("在第%d行的‘%s’ 类型与‘!=’操作不符。", p.lineNo));
					}
					break;
				}
				case GTOP:{
					Object j = vmStack.pop();
					Object i = vmStack.pop();
					if(j instanceof Integer && i instanceof Integer){
						vmStack.push((int)i>(int)j?1:0);
					}else{
						throw new Exception(String.format("在第%d行的‘%s’ 类型与‘>’操作不符。", p.lineNo));
					}
					break;
				}
				case GEOP:{
					Object j = vmStack.pop();
					Object i = vmStack.pop();
					if(j instanceof Integer && i instanceof Integer){
						vmStack.push((int)i>=(int)j?1:0);
					}else{
						throw new Exception(String.format("在第%d行的‘%s’ 类型与‘>=’操作不符。", p.lineNo));
					}
					break;
				}
				case LTOP:{
					Object j = vmStack.pop();
					Object i = vmStack.pop();
					if(j instanceof Integer && i instanceof Integer){
						vmStack.push((int)i<(int)j?1:0);
					}else{
						throw new Exception(String.format("在第%d行的‘%s’ 类型与‘<’操作不符。", p.lineNo));
					}
					break;
				}
				case LEOP:{
					Object j = vmStack.pop();
					Object i = vmStack.pop();
					if(j instanceof Integer && i instanceof Integer){
						vmStack.push((int)i<=(int)j?1:0);
					}else{
						throw new Exception(String.format("在第%d行的‘%s’ 类型与‘<=’操作不符。", p.lineNo));
					}
					break;
				}
				case MODOP:{
					Object j = vmStack.pop();
					Object i = vmStack.pop();
					if(j instanceof Integer && i instanceof Integer){
						vmStack.push((int)i%(int)j);
					}else{
						throw new Exception(String.format("在第%d行的‘%s’ 类型与‘%’操作不符。", p.lineNo));
					}
					break;
				}
				case JMP:{
						curr = (int)p.value -1; //减去1是为了去掉后面会curr的自增效果
					break;
				}
				case IFJMP:{
					int pos = (int)p.value;
					Boolean fals = null;
					Object val = vmStack.pop();
					if(val instanceof Integer){
						fals = (int)val <= 0;
					}else if(val instanceof String){
						fals = ((String)val).length() <= 0;
					}
					if(null == fals){
						throw new Exception(String.format("在第%d行的‘%s’ 判断条件有误。", p.lineNo));
					}
					if(fals) curr = pos-1; //减去1是为了去掉后面会curr的自增效果
					break;
				}
				case UPJMP:{
					curr = ((int)p.value) - 1; //减去1是为了去掉后面会curr的自增效果
					break;
				}
				case CALLFUNC:{
					Object funcObj = vmStack.pop();
					if(null == funcObj || !(funcObj instanceof IFunction)){
						throw new Exception(String.format("在第%d行的‘%s’ 调用的函数未声明。", p.lineNo));
					}
					IFunction m = (IFunction)funcObj;
					List<FunctionValue> args = new ArrayList<>();
					while(!vmStack.isEmpty()){
						Object o = vmStack.peek();
						if(o instanceof Order){
							Order or = (Order)vmStack.peek();
							if(or.op == Order.OrderOpType.MARK){ //持续将栈顶的数据取出，作为函数的参数，直到遇到mark标记为止
								break;
							}
						}
						//为函数调用准备好参数
						Object val = vmStack.pop();
						FunctionValue value = null;
						if(val instanceof Integer){
							value = new FunctionValue((int)val);
						}else if(val instanceof String){
							value = new FunctionValue((String)val);
						}
						if(null == value){
							throw new Exception("调用函数，参数错误");
						}
						args.add(value);
					}
					
					FunctionValue fs[] = new FunctionValue[args.size()];
					for(int i=0; i < args.size(); i++){
						fs[i] = args.get(i);
					}
					FunctionValue ret = m.invoke(fs); //调用函数
					if(null != ret){
						switch (ret.getValueType()) {
						case INT:
							vmStack.push(ret.getIntVal());
							break;
						case STRING:
							vmStack.push(ret.getStrVal());
							break;
						case LIST:
							break;

						default:
							break;
						}
					}
					break;
				}
				case HALT:{
					//正常的结束
					return;
				}
				case MARK:{
					vmStack.push(p);
					break;
				}
				default:
					System.err.println("未知的指令");
					break;
			}
			curr++;
		}
	}
}
