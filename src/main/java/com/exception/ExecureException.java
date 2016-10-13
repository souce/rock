package main.java.com.exception;

/*
 * 
 */
public class ExecureException extends Exception{
	private static final long serialVersionUID = 1L;
	public String msg;
	
	public ExecureException(String msg) {
		super();
		this.msg = msg;
	}
	
	@Override
	public String getMessage() {
		return msg;
	}
}
