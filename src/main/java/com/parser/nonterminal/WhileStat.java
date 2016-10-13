package main.java.com.parser.nonterminal;

import java.util.Stack;

import main.java.com.exception.ReadCodeException;
import main.java.com.exception.SyntaxErrorException;
import main.java.com.exception.UnexpectedCodeException;
import main.java.com.parser.Lexer;
import main.java.com.parser.Token;
import main.java.com.parser.ast.ASTree;

/*
 * whilestat := while {exp | name} do block end
 */
public class WhileStat implements IParser{
	
	public IParser name;
	public IParser expr;
	public IParser block;
	
	@Override
	public boolean parser(Lexer lexer, ASTree tree) throws SyntaxErrorException, UnexpectedCodeException, ReadCodeException{
		Stack<Token> back = new Stack<>(); //如果lexer中的token没能在最后匹配中，那么先前拿出的token都要归还回去
		Token t = lexer.peek(0);
		ASTree whileTree = new ASTree();
		if("while".equals(t.getText())){
			back.push(lexer.read());
			whileTree.setToken(t);
			tree.addChild(whileTree);
			
			boolean ok = false;
			ok = expr.parser(lexer, whileTree);
			if(!ok){
				ASTree nameTree = new ASTree();
				name.parser(lexer, nameTree);
				whileTree.addChild(nameTree);
				ok = true;
			}
			
			if(ok){
				t = lexer.peek(0);
				if("do".equals(t.getText())){
					back.push(lexer.read());
					//tree.children.add(new ASTree(t));
					if(block.parser(lexer, whileTree)){
						t = lexer.peek(0);
						if("end".equals(t.getText())){
							lexer.read();
							whileTree.addChild(t);
							return true;
						}
					}
				}
			}
		}
		while(!back.isEmpty()){
			Token token = back.pop();
			lexer.back(token);
		}
		return false;
	}
}
