package main.java.com.parser.nonterminal;

import java.util.List;
import java.util.Stack;

import main.java.com.exception.ReadCodeException;
import main.java.com.exception.SyntaxErrorException;
import main.java.com.exception.UnexpectedCodeException;
import main.java.com.parser.Lexer;
import main.java.com.parser.Token;
import main.java.com.parser.ast.ASTree;

/*
 * namelist := Name {',' Name}
 */
public class NameList implements IParser{
	
	public IParser name;
	public List<String> reserved; //关键字列表，保留字
	
	public NameList() {
		super();
	}
	
	public NameList (List<String> reserved) {
		super();
		this.reserved = reserved;
	}

	@Override
	public boolean parser(Lexer lexer, ASTree tree) throws SyntaxErrorException, UnexpectedCodeException, ReadCodeException{
		return loop(lexer, tree);
	}
	
	private boolean loop(Lexer lexer, ASTree tree) throws SyntaxErrorException, UnexpectedCodeException, ReadCodeException{
		boolean res = false;
		Stack<Token> back = new Stack<>(); //如果lexer中的token没能在最后匹配中，那么先前拿出的token都要归还回去
		ASTree nameTree = new ASTree();
		if(name.parser(lexer, nameTree)){
			back.push(nameTree.getToken());
			res = true;
			tree.addChild(nameTree);
			Token t = lexer.peek(0);
			if (",".equals(t.getText())) {
				lexer.read();
				loop(lexer, tree);
			}
		}
		while(res == false && !back.isEmpty()){
			Token token = back.pop();
			lexer.back(token);
		}
		return res; //只要第一次的结果，后面的递归不需要结果
	}

}
