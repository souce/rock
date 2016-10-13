package main.java.com.parser;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.java.com.parser.nonterminal.ASTerm;
import main.java.com.parser.nonterminal.Block;
import main.java.com.parser.nonterminal.Entrust;
import main.java.com.parser.nonterminal.Expr;
import main.java.com.parser.nonterminal.ExprList;
import main.java.com.parser.nonterminal.Factor;
import main.java.com.parser.nonterminal.FunctionCall;
import main.java.com.parser.nonterminal.IFStat;
import main.java.com.parser.nonterminal.IParser;
import main.java.com.parser.nonterminal.Name;
import main.java.com.parser.nonterminal.NameList;
import main.java.com.parser.nonterminal.Stat;
import main.java.com.parser.nonterminal.StringStat;
import main.java.com.parser.nonterminal.MDTerm;
import main.java.com.parser.nonterminal.ModTerm;
import main.java.com.parser.nonterminal.WhileStat;

public class NonterminalFactory {
	
	private Map<String, IParser> content = new HashMap<>();
	
	public void initNonterminal(){
		//关键字列表
		List<String> operators = new ArrayList<>();
		operators.add("if");
		operators.add("elif");
		operators.add("else");
		operators.add("while");
		operators.add("end");
		operators.add("then");
		operators.add(";");
		operators.add("+");
		operators.add("-");
		operators.add("*");
		operators.add("/");
		
		content.put("name", new Name());
		content.put("expr", new Expr());
		content.put("aSTerm", new ASTerm());
		content.put("mDTerm", new MDTerm());
		content.put("modTerm", new ModTerm());
		content.put("factor", new Factor());
		content.put("exprList", new ExprList());
		content.put("block", new Block());
		content.put("ifStat", new IFStat());
		content.put("entrustStat", new Entrust());
		content.put("nameList", new NameList());
		content.put("stat", new Stat());
		content.put("functionCall", new FunctionCall());
		content.put("whileStat", new WhileStat());
		content.put("string", new StringStat());
		
		for(String name : content.keySet()){
			IParser obj = content.get(name);
			Field[] fs = obj.getClass().getDeclaredFields();
			for(Field f : fs){
				if("reserved".equals(f.getName())){
					try {
						f.set(obj, operators);
					} catch (IllegalArgumentException | IllegalAccessException e) {
						e.printStackTrace();
					}
				}
				if(content.containsKey(f.getName())){
					try {
						f.set(obj, content.get(f.getName())); //为“非终结符解析器”注入它所依赖的其他“非终结符解析器”
					} catch (IllegalArgumentException | IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	public IParser getParser(){
		return content.get("stat");
	}
	
	//debug
	public IParser getExprParser(){
		return content.get("expr");
	}
	
}
