package main.java.com.parser.nonterminal;

import java.util.Stack;

import main.java.com.exception.ReadCodeException;
import main.java.com.exception.SyntaxErrorException;
import main.java.com.exception.UnexpectedCodeException;
import main.java.com.parser.Lexer;
import main.java.com.parser.Token;
import main.java.com.parser.ast.ASTree;

/*
 * entrust := namelist '=' {functionCall | explist}
 */
public class Entrust implements IParser{
	
	public IParser nameList;
	public IParser exprList;
	public IParser functionCall;
	
	@Override
	public boolean parser(Lexer lexer, ASTree tree) throws SyntaxErrorException, UnexpectedCodeException, ReadCodeException{
		Stack<Token> back = new Stack<>(); //如果lexer中的token没能在最后匹配中，那么先前拿出的token都要归还回去
		ASTree entrustTree = new ASTree();
		if(nameList.parser(lexer, entrustTree)){
			Token t = lexer.peek(0);
			if("=".equals(t.getText())){
				back.push(lexer.read());
				entrustTree.setToken(t);
				if(functionCall.parser(lexer, entrustTree) || exprList.parser(lexer, entrustTree)){
					tree.addChild(entrustTree);
					return true;
				}
			}
		}
		while(!back.isEmpty()){
			Token token = back.pop();
			lexer.back(token);
		}
		return false;
	}
	
}
