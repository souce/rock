package main.java.com.parser.nonterminal;

import main.java.com.exception.ReadCodeException;
import main.java.com.exception.SyntaxErrorException;
import main.java.com.exception.UnexpectedCodeException;
import main.java.com.parser.Lexer;
import main.java.com.parser.Token;
import main.java.com.parser.ast.ASTree;

/*
 * function := function  name '(' [namelist] ')' '{' block  [returnStat]'}'
 */
public class FunctionStat implements IParser{
	
	public IParser name;
	public IParser nameList;
	public IParser block;
	public IParser returnStat;
	
	@Override
	public boolean parser(Lexer lexer, ASTree tree) throws SyntaxErrorException, UnexpectedCodeException, ReadCodeException{
		Token t = lexer.peek(0);
		ASTree funTree = new ASTree();
		if("function".equals(t.getText())){
			lexer.read();
			funTree.setToken(t);
			ASTree nameTree = new ASTree();
			if(name.parser(lexer, nameTree)){
				funTree.addChild(nameTree);
				t = lexer.peek(0);
				if("(".equals(t.getText())){
					lexer.read();
					nameList.parser(lexer, funTree);
					t = lexer.peek(0);
					if(")".equals(t.getText())){
						lexer.read();
						t = lexer.peek(0);
						if("{".equals(t.getText())){
							lexer.read();
							funTree.addChild(t);
							if(block.parser(lexer, funTree)){
								returnStat.parser(lexer, funTree);
								t = lexer.peek(0);
								if("}".equals(t.getText())){
									lexer.read();
									funTree.addChild(t);
									tree.addChild(funTree);
									return true;
								}
							}
						}
					}
				}
			}
		}
		return false;
	}
}
