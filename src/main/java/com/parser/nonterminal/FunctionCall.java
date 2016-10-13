package main.java.com.parser.nonterminal;

import java.util.Stack;

import main.java.com.exception.ReadCodeException;
import main.java.com.exception.SyntaxErrorException;
import main.java.com.exception.UnexpectedCodeException;
import main.java.com.parser.Lexer;
import main.java.com.parser.Token;
import main.java.com.parser.ast.ASTree;

/*
 * functioncall := name "(" exprList ")"
 */
public class FunctionCall implements IParser{
	
	public IParser exprList;
	public IParser name;
	
	@Override
	public boolean parser(Lexer lexer, ASTree tree) throws SyntaxErrorException, UnexpectedCodeException, ReadCodeException{
		Stack<Token> back = new Stack<>(); //如果lexer中的token没能在最后匹配中，那么先前拿出的token都要归还回去
		ASTree funcTree = new ASTree();
		if(name.parser(lexer, funcTree)){
			back.push(funcTree.getToken());
			Token t = lexer.peek(0);
			if("(".equals(t.getText())){
				back.push(lexer.read());
				funcTree.addChild(t);
				if(exprList.parser(lexer, funcTree)){
					t = lexer.peek(0);
					if(")".equals(t.getText())){
						back.push(lexer.read());
						funcTree.addChild(t);
						tree.addChild(funcTree);
						return true;
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
