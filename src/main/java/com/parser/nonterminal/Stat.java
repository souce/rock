package main.java.com.parser.nonterminal;

import main.java.com.exception.ReadCodeException;
import main.java.com.exception.SyntaxErrorException;
import main.java.com.exception.UnexpectedCodeException;
import main.java.com.parser.Lexer;
import main.java.com.parser.ast.ASTree;

/*
 	stat := ifstat | whilestat | functionstat | FunctionCall | entrust | value | stat{stat} 
 	
 	AAndS := MAndD { ( + | - )  MAndD }
	MAndD := mod { ( * | / )  mod }
	mod := factor {‘%’ factor}
	block := {stat [';']} [laststat [';']]
	factor := number | name | string | '(' expr ')'
	expr := AAndS { (‘<‘|’>’|’<=‘|’>=‘|’==')  AAndS}
	explist := {exp ','} exp
	functioncall := name "(" [exprList] ")"
	ifstat := "if" expr "then" block {"elif" expr "then" block} ["else" block] "end"
	whilestat := while {exp | name} do block end
	laststat := return [exprlist]
	entrust := namelist '=' {functionCall | explist}
	name := noun
	namelist := Name {',' Name}
	string := string
	stat := ifstat | whilestat | functionstat | FunctionCall | entrust | value | stat{stat} 
 */
public class Stat implements IParser{
	
	public IParser ifStat;
	public IParser entrust;
	public IParser whileStat;
	public IParser functionStat;
	public IParser functionCall;
	public IParser entrustStat;
	public IParser value;
	
	@Override
	public boolean parser(Lexer lexer, ASTree tree) throws SyntaxErrorException, UnexpectedCodeException, ReadCodeException{
		boolean res = false;
		while(parserStat(lexer, tree)){
			res = true;
			continue;
		}
		return res;
	}
	
	private  boolean parserStat(Lexer lexer, ASTree tree) throws SyntaxErrorException, UnexpectedCodeException, ReadCodeException{
		ASTree statTree = new ASTree();
		tree.addChild(statTree);
		if(ifStat.parser(lexer, statTree)){
			return true;
		}
		else if(whileStat.parser(lexer, statTree)){
			return true;
		}
		else if(functionStat.parser(lexer, statTree)){
			return true;
		}
		else if(functionCall.parser(lexer, statTree)){
			return true;
		}
		else if(entrustStat.parser(lexer, statTree)){
			return true;
		}
		return false;
	}
	
}