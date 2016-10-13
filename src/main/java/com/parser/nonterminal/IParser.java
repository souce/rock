package main.java.com.parser.nonterminal;

import main.java.com.exception.ReadCodeException;
import main.java.com.exception.SyntaxErrorException;
import main.java.com.exception.UnexpectedCodeException;
import main.java.com.parser.Lexer;
import main.java.com.parser.ast.ASTree;

public interface IParser {
	public boolean parser(Lexer lexer, ASTree tree) throws SyntaxErrorException, UnexpectedCodeException, ReadCodeException;
}
