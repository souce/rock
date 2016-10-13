package main.java.com.parser.nonterminal;

import main.java.com.exception.ReadCodeException;
import main.java.com.exception.SyntaxErrorException;
import main.java.com.exception.UnexpectedCodeException;
import main.java.com.parser.Lexer;
import main.java.com.parser.ast.ASTree;

/*
 	 stat := ifstat | whilestat | functionstat | FunctionCall | entrust | value | stat{stat} 
 	 
 	 
 	 stat ::=  namelist '=' explist |
     functioncall |
     do block end |
     while exp do block end |
     repeat block until exp |
     if expr then block {elseif exp then block} [else block] end |
     for Name '=' exp ',' exp [',' exp] do block end |
     for namelist in explist do block end |
     function  '(' [namelist] ')'  block end|
     local namelist ['=' explist]
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
//		else if(functionStat.parser(lexer, statTree)){
//			return true;
//		}
		else if(functionCall.parser(lexer, statTree)){
			return true;
		}
		else if(entrustStat.parser(lexer, statTree)){
			return true;
		}
		return false;
	}
	
}