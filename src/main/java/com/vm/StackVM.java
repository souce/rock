package main.java.com.vm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import main.java.com.exception.ExecureException;
import main.java.com.lib.FunctionValue;
import main.java.com.lib.IFunction;
import main.java.com.vm.Order.OrderOpType;

public class StackVM {
	//默认的全局的执行上下文，也就是作用域链的开端
	private ExecutionContext globalExecutionContext = null;
	private ExecutionContext currentExecutionContext = null;
	
	private HashMap<String, Object> extGlobalMap = new HashMap<>(); //扩展的特殊全局变量
	
	private Stack<Object> vmStack = new Stack<>();
	
	protected StackVM vm;
	
	public StackVM(){
		List<ExecutionContext> scopeChain = new ArrayList<>();
		globalExecutionContext = new ExecutionContext(scopeChain);
		vm = this;
	}
	
	public void registerFunction(String name, IFunction function){
		extGlobalMap.put(name, function);
	}
	
	public void execute(List<Order> orders) throws Exception{
		execute(orders, null);
	}
	
	public void execute(List<Order> orders, ExecutionContext context) throws Exception{
		int curr = 0;
		
		//为函数初始化执行上下文
		if(null == context){
			if(null == globalExecutionContext){
				currentExecutionContext = globalExecutionContext;
			}else{
				currentExecutionContext = new ExecutionContext(globalExecutionContext.getScopeChain());
			}
		}else{
			currentExecutionContext = context;
		}
		
		while(true){
			if(curr >= orders.size()){
				if(currentExecutionContext == globalExecutionContext )
					throw new Exception("非正常的结束");
				break;
			}
			Order p = orders.get(curr);
			switch (p.op) {
				case PUSH:
					//特殊处理
					if(p.value.equals("funcBegin")){
						scanFunction(orders);
						curr--;
					}else if(p.value.equals("return")){
						//return的处理没能完成
					}else
						vmStack.push(p.value);
					break;
				case STOREVAR:{
					//保存值到当前作用域中
					currentExecutionContext.setVariableObject(p.value.toString(), vmStack.pop());
					break;
				}
				case PUSHVAR:{
					//从当前作用域中取值
					String key = p.value.toString();
					Object val = currentExecutionContext.getVariableObject(key);
					if(null == val){
						//从特殊映射表中再查一次
						val = extGlobalMap.get(key);
						if(null == val)
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
						throw new Exception(String.format("在第%d行的‘%s’ 调用的函数未声明。", p.lineNo, p.value.toString()));
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
						}else if(val instanceof FunctionValue){
							value = (FunctionValue)val;
						}
						if(null == value){
							throw new Exception(String.format("在第%d行 调用函数，参数错误。", p.lineNo));
						}
						args.add(value);
					}
					//参数的声明顺序在此处需要反转
					FunctionValue fs[] = new FunctionValue[args.size()];
					for(int i = 0; i < args.size() ; i++){
						fs[i] = args.get(args.size() - 1 - i);
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
							vmStack.push(ret);
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
		}//end:while
		if(context == null && currentExecutionContext != globalExecutionContext){
			List<ExecutionContext> scopeChain = globalExecutionContext.getScopeChain();
			scopeChain.remove(scopeChain.size() - 1);
			currentExecutionContext = scopeChain.get(scopeChain.size() - 1);
		}
	}
	
	//扫描流程中的函数，将其填充到当前的执行上下文的局部作用域中
	private void scanFunction(List<Order> orders){
		Order funcName = null;
		int startPos = -1;
		int endPos = -1;
		int skipCount = 0; //可能存在闭包，需要跳过方法中的方法
		for(int i = 0; i < orders.size(); i++){
			Order order = orders.get(i);
			if(order.value.equals("funcBegin")){
				if(-1 == startPos){
					startPos = i;
				}else{
					skipCount++;
					continue;
				}
			}
			if(order.value.equals("funcEnd")){
				if(0 == skipCount){
					endPos = i;
					//break;
				}
				skipCount--;
			}else{
				//方法之后的一位必然是函数名
				if(endPos > 0  && orders.get(i).op == OrderOpType.PUSHVAR){
					funcName = orders.get(i);
					break;
				}
			}
		}
		
		if(null != funcName && startPos > -1 && endPos > startPos){
			List<Order> funcOrders = orders.subList(startPos+1, endPos); //不保留funcBegin和funcEnd
			currentExecutionContext.setVariableObject(funcName.value.toString(), 
													  new Function(new ArrayList<>(funcOrders), 
													  currentExecutionContext.getAllVariableObjects())); 
			//清除指令集中的方法定义
			orders.removeAll(funcOrders);
			//删除 funcBegin、funcEnd、函数名
			orders.remove(startPos);
			orders.remove(startPos);
			orders.remove(startPos);
			orders = new ArrayList<>(orders); //被修改后的list不能再次操作，暂时先这么做。
		}
	}
	
	class Function implements IFunction{
		private List<Order> funcOrders;
		private ExecutionContext tmpContext = null;
		
		public Function(List<Order> funcOrders, Map<String, Object> tmpVariableObjects) {
			super();
			this.funcOrders = funcOrders;
			this.tmpContext = new ExecutionContext(globalExecutionContext.getScopeChain(), tmpVariableObjects);
		}

		@Override
		public FunctionValue invoke(FunctionValue... obj) throws ExecureException {
			try {
				vm.execute(funcOrders, tmpContext);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null; //暂时：返回值在栈上，所以无需返回
		}
		
	}
	
}
