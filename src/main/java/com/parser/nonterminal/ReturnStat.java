package main.java.com.parser.nonterminal;

import main.java.com.exception.ReadCodeException;
import main.java.com.exception.SyntaxErrorException;
import main.java.com.exception.UnexpectedCodeException;
import main.java.com.parser.Lexer;
import main.java.com.parser.Token;
import main.java.com.parser.ast.ASTree;

/*
 * return := return [namelist]
 */
public class ReturnStat implements IParser{
	
	public IParser nameList;
	
	@Override
	public boolean parser(Lexer lexer, ASTree tree) throws SyntaxErrorException, UnexpectedCodeException, ReadCodeException{
		Token t = lexer.peek(0);
		ASTree returnTree = new ASTree();
		if ("return".equals(t.getText())) {
			lexer.read();
			returnTree.setToken(t);
			nameList.parser(lexer, returnTree);
			tree.addChild(returnTree);
			return true;
		}
		return false;
	}
}
