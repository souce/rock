package main.java.com.parser.nonterminal;

import main.java.com.exception.ReadCodeException;
import main.java.com.exception.SyntaxErrorException;
import main.java.com.exception.UnexpectedCodeException;
import main.java.com.parser.Lexer;
import main.java.com.parser.Token;
import main.java.com.parser.ast.ASTree;

/*
 * explist := {exp ','} exp
 */
public class ExprList implements IParser{
	
	public IParser expr;
	
	@Override
	public boolean parser(Lexer lexer, ASTree tree) throws SyntaxErrorException, UnexpectedCodeException, ReadCodeException{
		return loop(lexer, tree);
	}
	
	private boolean loop(Lexer lexer, ASTree tree) throws SyntaxErrorException, UnexpectedCodeException, ReadCodeException{
		boolean res = expr.parser(lexer, tree);
		if(res){
			Token t = lexer.peek(0);
			if (",".equals(t.getText())) {
				lexer.read();
				loop(lexer, tree);
			}
		}
		return res; //只要第一次的结果，后面的递归不需要结果
	}

}
