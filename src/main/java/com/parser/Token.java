package main.java.com.parser;

public class Token {	
    public static final Token EOF = new Token(-1){}; // end of file
    public static final String EOL = "\\n";          // end of line 
    private int lineNumber;
    protected Token(int line) {
        lineNumber = line;
    }
    public int getLineNumber() { return lineNumber; } 
    
    /*
     * token的身份标识，它们之间是互斥的关系，一个token只能是一个身份
     */
    public boolean isNouns(){ return false;}			//是否名词，变量或者函数名
    public boolean isIdentifier() { return false; } 	//是否标识符
    public boolean isNumber() { return false; } 		//是否值为int类型
    public boolean isString() { return false; } 		//是否值为string类型
    
    public int getNumber() throws Exception { throw new Exception("not number token"); }
    public String getText() { return ""; }
    
    //debug
    public String toString(){
    	if(isString() || isIdentifier() || isNouns()){
    		return getText();
    	}else if(isNumber()){
    		try {
    			return Integer.toString(getNumber());
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}
    	return "unknow";
    }
}
