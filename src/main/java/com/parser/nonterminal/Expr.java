package main.java.com.parser.nonterminal;

import main.java.com.exception.ReadCodeException;
import main.java.com.exception.SyntaxErrorException;
import main.java.com.exception.UnexpectedCodeException;
import main.java.com.parser.Lexer;
import main.java.com.parser.Token;
import main.java.com.parser.ast.ASTree;

/*
 * expr := AAndS { (‘<‘|’>’|’<=‘|’>=‘|’==')  AAndS}
 */
public class Expr implements IParser{
	
	public IParser aSTerm;
	
	@Override
	public boolean parser(Lexer lexer, ASTree tree) throws SyntaxErrorException, UnexpectedCodeException, ReadCodeException{
		ASTree exprTree = new ASTree();
		if (aSTerm.parser(lexer, exprTree)) {
			while (true) {
				Token t = lexer.peek(0);
				if ("<".equals(t.getText()) || 
					">".equals(t.getText()) ||
					"<=".equals(t.getText()) ||
					">=".equals(t.getText()) ||
					"==".equals(t.getText()) ||
					"!=".equals(t.getText())) {
					lexer.read();
					if (aSTerm.parser(lexer, exprTree)) {
						exprTree.addChild(t);
						continue;
					} else {
						//对比符号之后的token不匹配终结符aSTerm，这样的语法不正确
						throw new SyntaxErrorException(t.getLineNumber(), lexer.peek(0), t.getText() + "之后的符号不合法");
					}
				}
				break;
			}
			tree.addChild(exprTree);
			return true;
		}
		return false;
	}

}
