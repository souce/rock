package main.java.com.parser.nonterminal;

import main.java.com.exception.ReadCodeException;
import main.java.com.exception.SyntaxErrorException;
import main.java.com.exception.UnexpectedCodeException;
import main.java.com.parser.Lexer;
import main.java.com.parser.Token;
import main.java.com.parser.ast.ASTree;

/*
 * laststat := return [exprlist]
 */
public class LastStat implements IParser{
	
	public IParser exprList;
	
	@Override
	public boolean parser(Lexer lexer, ASTree tree) throws SyntaxErrorException, UnexpectedCodeException, ReadCodeException{
		Token t = lexer.peek(0);
		if("return".equals(t.getText())){
			lexer.read();
			return exprList.parser(lexer, tree);
		}
		return false;
	}
	
}

