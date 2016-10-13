package main.java.com.parser.nonterminal;

import main.java.com.exception.ReadCodeException;
import main.java.com.exception.SyntaxErrorException;
import main.java.com.exception.UnexpectedCodeException;
import main.java.com.parser.Lexer;
import main.java.com.parser.Token;
import main.java.com.parser.ast.ASTree;

/*
 * factor := number | name | string | '(' expr ')'
 */
public class Factor implements IParser{
	
	public IParser string;
	public IParser name;
	public IParser expr;
	
	@Override
	public boolean parser(Lexer lexer, ASTree tree) throws SyntaxErrorException, UnexpectedCodeException, ReadCodeException{
		ASTree factorTree = new ASTree();
		Token t = lexer.peek(0);
		if(t.isNumber()){
			lexer.read();
			factorTree.addChild(t);
			tree.addChild(factorTree);
			return true;
		}else if(t.isNouns()){
			ASTree nameTree = new ASTree();
			name.parser(lexer, nameTree);
			factorTree.addChild(nameTree);
			tree.addChild(factorTree);
			return true;
		}else if(t.isString()){
			ASTree strTree = new ASTree();
			string.parser(lexer, strTree);
			factorTree.addChild(strTree);
			tree.addChild(factorTree);
			return true;
		}else{
			if("(".equals(t.getText())){
				lexer.read();
				if(expr.parser(lexer, factorTree)){
					t = lexer.peek(0);
					if(")".equals(t.getText())){
						lexer.read();
						tree.addChild(factorTree);
						return true;
					}else{
						//缺少 )
						throw new SyntaxErrorException(t.getLineNumber(), lexer.peek(0), "缺少')'");
					}
				}else{
					//( 之后的token不匹配表达式"(" expr ")"，这样的语法不正确
					throw new SyntaxErrorException(t.getLineNumber(), lexer.peek(0), "'('之后的符号不合法");
				}
			}
		}
		return false;
	}

}
