package main.java.com.parser.nonterminal;

import java.util.Stack;

import main.java.com.exception.ReadCodeException;
import main.java.com.exception.SyntaxErrorException;
import main.java.com.exception.UnexpectedCodeException;
import main.java.com.parser.Lexer;
import main.java.com.parser.Token;
import main.java.com.parser.ast.ASTree;

/*
 * ifstat := "if" expr "then" block {"elif" expr "then" block} ["else" block] "end"
 */
public class IFStat implements IParser{
	
	public IParser expr;
	public IParser block;
	
	@Override
	public boolean parser(Lexer lexer, ASTree tree) throws SyntaxErrorException, UnexpectedCodeException, ReadCodeException{
		//解析if
		if(!parseIf(lexer, tree))
			return false;
		
		//if之后可能接上end表示结束
		if(parseEnd(lexer, tree)){
			return true;
		}
		
		//if之后没有end，那么就是可能后接elif或者else
		boolean res = false;
		while(parseElif(lexer, tree)){
			res = true;
			continue;
		}
		if(res){
			//elif之后可能接end表示结束
			if(parseEnd(lexer, tree)){
				return true;
			}
		}
		
		//if或者elif后接的是else
		parseElse(lexer, tree);
		return parseEnd(lexer, tree);
	}
	
	private boolean parseIf(Lexer lexer, ASTree tree) throws SyntaxErrorException, UnexpectedCodeException, ReadCodeException{
		Token t = lexer.peek(0);
		ASTree ifTree = new ASTree();
		Stack<Token> back = new Stack<>(); //如果lexer中的token没能在最后匹配中，那么先前拿出的token都要归还回去
		if("if".equals(t.getText())){
			back.push(lexer.read());
			ifTree.setToken(t);
			tree.addChild(ifTree);
			if(expr.parser(lexer, ifTree)){
				t = lexer.peek(0);
				if("then".equals(t.getText())){
					back.push(lexer.read());
					if(block.parser(lexer, ifTree)){
						//end的处理不在这
						return true;
					}
				}else{
					throw new SyntaxErrorException(t.getLineNumber(), lexer.peek(0), t.getText() + "之后的符号不合法");
				}
			}else{
				throw new SyntaxErrorException(t.getLineNumber(), lexer.peek(0), t.getText() + "之后的符号不合法");
			}
		}
		while(!back.isEmpty()){
			Token token = back.pop();
			lexer.back(token);
		}
		return false;
	}
	
	private boolean parseElif(Lexer lexer, ASTree tree) throws SyntaxErrorException, UnexpectedCodeException, ReadCodeException{
		Token t = lexer.peek(0);
		ASTree ifTree = new ASTree();
		Stack<Token> back = new Stack<>(); //如果lexer中的token没能在最后匹配中，那么先前拿出的token都要归还回去
		if("elif".equals(t.getText())){
			back.push(lexer.read());
			ifTree.setToken(t);
			tree.addChild(ifTree);
			if(expr.parser(lexer, ifTree)){
				t = lexer.peek(0);
				if("then".equals(t.getText())){
					back.push(lexer.read());
					if(block.parser(lexer, ifTree)){
						//end的处理不在这
						return true;
					}
				}else{
					throw new SyntaxErrorException(t.getLineNumber(), lexer.peek(0), t.getText() + "之后的符号不合法");
				}
			}else{
				throw new SyntaxErrorException(t.getLineNumber(), lexer.peek(0), t.getText() + "之后的符号不合法");
			}
		}
		while(!back.isEmpty()){
			Token token = back.pop();
			lexer.back(token);
		}
		return false;
	}
	
	private boolean parseElse(Lexer lexer, ASTree tree) throws SyntaxErrorException, UnexpectedCodeException, ReadCodeException{
		Token t = lexer.peek(0);
		ASTree elseTree = new ASTree();
		Stack<Token> back = new Stack<>(); //如果lexer中的token没能在最后匹配中，那么先前拿出的token都要归还回去
		if("else".equals(t.getText())){
			back.push(lexer.read());
			elseTree.setToken(t);
			tree.addChild(elseTree);
			t = lexer.peek(0);
			if("then".equals(t.getText())){
				back.push(lexer.read());
				if(block.parser(lexer, elseTree)){
					//end的处理不在这
					return true;
				}else{
					throw new SyntaxErrorException(t.getLineNumber(), lexer.peek(0), t.getText() + "之后的符号不合法");
				}
			}else{
				throw new SyntaxErrorException(t.getLineNumber(), lexer.peek(0), t.getText() + "之后的符号不合法");
			}
		}
		while(!back.isEmpty()){
			Token token = back.pop();
			lexer.back(token);
		}
		return false;
	}
	
	private boolean parseEnd(Lexer lexer, ASTree tree) throws UnexpectedCodeException, ReadCodeException{
		Token t = lexer.peek(0);
		if("end".equals(t.getText())){
			lexer.read();
			tree.addChild(t);
			return true;
		}
		return false;
	}
	
}
