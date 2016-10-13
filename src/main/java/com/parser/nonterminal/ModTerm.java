package main.java.com.parser.nonterminal;

import main.java.com.exception.ReadCodeException;
import main.java.com.exception.SyntaxErrorException;
import main.java.com.exception.UnexpectedCodeException;
import main.java.com.parser.Lexer;
import main.java.com.parser.Token;
import main.java.com.parser.ast.ASTree;

/*
 * mod := factor {‘%’ factor}
 */
public class ModTerm  implements IParser {

	public IParser factor;

	@Override
	public boolean parser(Lexer lexer, ASTree tree) throws SyntaxErrorException, UnexpectedCodeException, ReadCodeException{
		ASTree modTree = new ASTree();
		if (factor.parser(lexer, modTree)) {
			while (true) {
				Token t = lexer.peek(0);
				if ("%".equals(t.getText())) {
					lexer.read();
					if (factor.parser(lexer, modTree)) {
						modTree.addChild(t);
						continue;
					} else {
						//取模符号之后的token不匹配终结符factor，这样的语法不正确
						throw new SyntaxErrorException(t.getLineNumber(), lexer.peek(0), t.getText()+"之后的符号不合法");
					}
				}
				break;
			}
			tree.addChild(modTree);
			return true;
		}
		return false;
	}

}
