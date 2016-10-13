package main.java.com.parser.nonterminal;

import main.java.com.exception.ReadCodeException;
import main.java.com.exception.SyntaxErrorException;
import main.java.com.exception.UnexpectedCodeException;
import main.java.com.parser.Lexer;
import main.java.com.parser.Token;
import main.java.com.parser.ast.ASTree;

/*
 * AAndS := MAndD { ( + | - )  MAndD }
 */
public class ASTerm implements IParser {

	public IParser mDTerm;

	@Override
	public boolean parser(Lexer lexer, ASTree tree) throws SyntaxErrorException, UnexpectedCodeException, ReadCodeException{
		ASTree tremTree = new ASTree();
		if (mDTerm.parser(lexer, tremTree)) {
			while (true) {
				Token t = lexer.peek(0);
				if ("+".equals(t.getText()) || "-".equals(t.getText())) {
					lexer.read();
					if (mDTerm.parser(lexer, tremTree)) {
						tremTree.addChild(t);
						continue;
					} else {
						//加减符号之后的token不匹配终结符mDTerm，这样的语法不正确
						throw new SyntaxErrorException(t.getLineNumber(), lexer.peek(0), t.getText()+"之后的符号不合法");
					}
				}
				break;
			}
			tree.addChild(tremTree);
			return true;
		}
		return false;
	}

}
