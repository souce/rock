package main.java.com.parser.nonterminal;

import main.java.com.exception.ReadCodeException;
import main.java.com.exception.SyntaxErrorException;
import main.java.com.exception.UnexpectedCodeException;
import main.java.com.parser.Lexer;
import main.java.com.parser.Token;
import main.java.com.parser.ast.ASTree;

/*
 * function := function  '(' [namelist] ')'  block end
 */
public class FunctionStat implements IParser{
	
	public IParser nameList;
	public IParser block;
	
	@Override
	public boolean parser(Lexer lexer, ASTree tree) throws SyntaxErrorException, UnexpectedCodeException, ReadCodeException{
		Token t = lexer.peek(0);
		ASTree funTree = new ASTree();
		if("function".equals(t.getText())){
			lexer.read();
			funTree.setToken(t);
			tree.addChild(funTree);
			t = lexer.peek(0);
			if("(".equals(t.getText())){
				lexer.read();
				if(nameList.parser(lexer, funTree)){
					t = lexer.peek(0);
					if(")".equals(t.getText())){
						lexer.read();
						if(block.parser(lexer, funTree)){
							t = lexer.peek(0);
							if("end".equals(t.getText())){
								lexer.read();
								funTree.addChild(t);
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}
}
