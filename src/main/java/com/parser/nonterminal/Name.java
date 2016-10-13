package main.java.com.parser.nonterminal;

import main.java.com.exception.ReadCodeException;
import main.java.com.exception.SyntaxErrorException;
import main.java.com.exception.UnexpectedCodeException;
import main.java.com.parser.Lexer;
import main.java.com.parser.Token;
import main.java.com.parser.ast.ASTree;

/*
 * name := noun
 * name不是一般的字符串，而是非关键字的名字，且不是用双引号扩起来的字符串
 */
public class Name implements IParser{
	
	@Override
	public boolean parser(Lexer lexer, ASTree tree) throws SyntaxErrorException, UnexpectedCodeException, ReadCodeException{
		Token name = lexer.peek(0);
		if(name.isNouns()){
			lexer.read();
			tree.setToken(name);
			return true;
		}
		return false;
	}

}
