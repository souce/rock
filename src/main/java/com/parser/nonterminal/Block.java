package main.java.com.parser.nonterminal;

import main.java.com.exception.ReadCodeException;
import main.java.com.exception.SyntaxErrorException;
import main.java.com.exception.UnexpectedCodeException;
import main.java.com.parser.Lexer;
import main.java.com.parser.ast.ASTree;

/*
 * block := {stat [';']} [laststat [';']]
 */
public class Block implements IParser{
	
	public IParser stat;
	public IParser lastStat;
	
	@Override
	public boolean parser(Lexer lexer, ASTree tree) throws SyntaxErrorException, UnexpectedCodeException, ReadCodeException{
		boolean res = false;
		
		ASTree blockTree = new ASTree();
		
		while(stat.parser(lexer, blockTree)){
			res = true;
//			Token t = lexer.peek(0);
//			if(";".equals(t.getText())){
//				lexer.read();
//			}
		}
		
//		if(lastStat.parser(lexer, blockTree)){
//			res = true;
//			Token t = lexer.peek(0);
//			if(";".equals(t.getText())){
//				lexer.read();
//			}
//		}
		
		if(res){
			tree.addChild(blockTree);
		}
		
		return res;
	}
}
