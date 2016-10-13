package main.java.com.exception;

/*
 * 读取代码遇到意外的字符串
 */
public class UnexpectedCodeException extends Exception{
	private static final long serialVersionUID = 1L;
	public int pos;
	public int lineNo;
	public String code;

	public UnexpectedCodeException(int pos, int lineNo, String code) {
		super();
		this.pos = pos;
		this.lineNo = lineNo;
		this.code = code;
	}
	
}
