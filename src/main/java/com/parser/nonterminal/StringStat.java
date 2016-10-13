package main.java.com.parser.nonterminal;

import main.java.com.exception.ReadCodeException;
import main.java.com.exception.SyntaxErrorException;
import main.java.com.exception.UnexpectedCodeException;
import main.java.com.parser.Lexer;
import main.java.com.parser.Token;
import main.java.com.parser.ast.ASTree;

/*
 * string := string
 * 一般的字符串，用双引号扩起来的字符串
 */
public class StringStat implements IParser{
	
	@Override
	public boolean parser(Lexer lexer, ASTree tree) throws SyntaxErrorException, UnexpectedCodeException, ReadCodeException{
		Token str = lexer.peek(0);
		if(str.isString()){
			lexer.read();
			tree.setToken(str);
			return true;
		}
		return false;
	}

}
