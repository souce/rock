package main.java.com.exception;

/*
 * 读取代码失败
 */
public class ReadCodeException extends Exception{
	private static final long serialVersionUID = 1L;
	public String msg;
	
	public ReadCodeException(String msg) {
		super();
		this.msg = msg;
	}
	
	@Override
	public String getMessage() {
		return msg;
	}
}
