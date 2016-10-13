package main.java.com.exception;

import main.java.com.parser.Token;

/*
 * 语法错误
 */
public class SyntaxErrorException extends Exception{
	private static final long serialVersionUID = 1L;
	public int lineNo;
	public Token token;
	public String msg;
	
	public SyntaxErrorException(int lineNo, Token token, String msg) {
		super();
		this.lineNo = lineNo;
		this.token = token;
		this.msg = msg;
	}

	@Override
	public String getMessage() {
		return String.format("%s 所在行:%d 符号:%s", this.msg, this.lineNo, this.token);
	}

}
