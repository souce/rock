package main.java.com.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import main.java.com.parser.ast.ASTree;
import main.java.com.vm.Order;

/*
 * 遍历修正后的语法树，生成vm指令
 */
public class Translator {
	
	public List<Order> translate(ASTree tree) throws Exception{
		if(null == tree) return null;
		List<Order> orders = new ArrayList<>();
		List<Token> ts = convert(tree);
		
		Stack<Order> ifJmpStack = new Stack<>();
		Stack<Order> jmpStack = new Stack<>();
		Stack<Integer> beginStack = new Stack<>();
		for(Token t : ts){
			if(t.isNumber()){
				orders.add(new Order(t.getLineNumber(), Order.OrderOpType.PUSH, t.getNumber()));
			}else if(t.isString()){
				orders.add(new Order(t.getLineNumber(), Order.OrderOpType.PUSH, t.getText()));
			}else if(t.isNouns()){
				orders.add(new Order(t.getLineNumber(), Order.OrderOpType.PUSHGLOBAL, t.getText()));
			}else if(t.isIdentifier()){
				switch (t.getText()) {
				case "begin":{
					jmpStack.push(null);
					break;
				}
				case "ifJmp":{
					Order o = new Order(t.getLineNumber(), Order.OrderOpType.IFJMP, "ifJmp");
					orders.add(o);
					ifJmpStack.push(o);
					break;
				}
				case "ifEnd":{
					Order o = ifJmpStack.peek();
					if(null == o) break;
					if(o.value.equals("end")) break;
					if(o.value.equals("ifJmp")){
						o.value = orders.size();
						ifJmpStack.pop();
					}
					break;
				}
				case "jmp":{
					//jmp指令是有可能不会被重定位的（指令冗余），加1是简单的跳过它，当前的orders.size()是它的上一位
					Order o = new Order(t.getLineNumber(), Order.OrderOpType.JMP, orders.size()+1); 
					orders.add(o);
					jmpStack.push(o);
					break;
				}
				case "jmpEnd":{
					while(!jmpStack.isEmpty()){
						Order o = jmpStack.pop();
						if(null == o) break;
						if(o.value.equals("end")) break;
						if(o.op == Order.OrderOpType.JMP){
							o.value = orders.size();
						}else{
							jmpStack.push(o);
							break;
						}
					}
					break;
				}
				case "end":{
					//end是各个流程的边界
					while(!jmpStack.isEmpty()){
						if(null == jmpStack.pop())
							break;
					}
					break;
				}
				case "upBegin":{
					beginStack.push(orders.size());
					break;
				}
				case "upJmp":{
					if(!beginStack.isEmpty()){
						int pos = beginStack.pop();
						Order o = new Order(t.getLineNumber(), Order.OrderOpType.UPJMP, pos);
						orders.add(o);
					}else{
						System.err.println("no pos!!!");
					}
					break;
				}
				case "callFunc":{
					Order o = new Order(t.getLineNumber(), Order.OrderOpType.CALLFUNC, "callFunc");
					orders.add(o);
					break;
				}
				case "entrust":{
					orders.get(orders.size()-1).op = Order.OrderOpType.STOREGLOBAL;
					break;
				}
				case "push":{
					orders.get(orders.size()-1).op = Order.OrderOpType.PUSHGLOBAL;
					break;
				}
				case "mark":{
					orders.add(new Order(t.getLineNumber(), Order.OrderOpType.MARK, ""));
					break;
				}
				case "=":{
					//orders.add(new Order(t.getLineNumber(), OrderOpType.MARK, OrderValueType.STRING, ""));
					break;
				}
				default:
					orders.add(new Order(t.getLineNumber(), parsePperational(t.getText()), t.getText()));
					break;
				}
			}
		}
		//最后的结尾标记
		orders.add(new Order(0, Order.OrderOpType.HALT, ""));
		return orders;
	}
	
	public List<Token> convert(ASTree tree) throws Exception{
		if(null == tree) return null;
		List<Token> tokens = new ArrayList<>();
		for(int i = 0; i < tree.numChildren(); i++){
			List<Token> c = convert(tree.child(i));
			if(null != c)
				tokens.addAll(c);
		}
		
		Token token = tree.getToken();
		if(null != token){
			tokens.add(tree.getToken());
		}
		return tokens;
	}
	
	private static Order.OrderOpType parsePperational(String opStr){
		switch (opStr) {
		case "+":
			return Order.OrderOpType.ADDOP;
		case "-":
			return Order.OrderOpType.SUBOP;
		case "*":
			return Order.OrderOpType.MULTOP;
		case "/":
			return Order.OrderOpType.DIVOP;
		case "%":
			return Order.OrderOpType.MODOP;
		case "==":
			return Order.OrderOpType.EQOP;
		case "!=":
			return Order.OrderOpType.NEOP;
		case ">":
			return Order.OrderOpType.GTOP;
		case ">=":
			return Order.OrderOpType.GEOP;
		case "<":
			return Order.OrderOpType.LTOP;
		case "<=":
			return Order.OrderOpType.LEOP;
		default:
			break;
		}
		return Order.OrderOpType.PUSH;
	}
	
}
