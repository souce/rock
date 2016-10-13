package main.java.com.parser;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import main.java.com.exception.ReadCodeException;
import main.java.com.exception.UnexpectedCodeException;

public class Lexer {
    public static String regexPat
        = "\\s*((//.*)|([0-9]+)|\"([^\"]*)\"|(if|elif|else|then|while|do|end|\\(|\\)|\\+|-|\\*|/|,|%|<=|>=|<|>|==|!=|=)|([A-Za-z0-9])*)?";
    private Pattern pattern = Pattern.compile(regexPat);
    public ArrayList<Token> queue = new ArrayList<Token>();
    private boolean hasMore;
    private LineNumberReader reader;

    public Lexer(Reader r) {
        hasMore = true;
        reader = new LineNumberReader(r);
    }
    
    //read和peek的区别在于,read是按照顺序将token出队列,而peek只是获取并没有从队列中移除
    public Token read() throws UnexpectedCodeException, ReadCodeException {
        if (fillQueue(0))
            return queue.remove(0);
        else
            return Token.EOF;
    }
    public Token peek(int i) throws UnexpectedCodeException, ReadCodeException {
        if (fillQueue(i))
            return queue.get(i);
        else
            return Token.EOF; 
    }
    
    public void back(Token token){
    	queue.add(0, token);
    }
    
    private boolean fillQueue(int i) throws UnexpectedCodeException, ReadCodeException {
        while (i >= queue.size())
            if (hasMore)
				try {
					readLine();
				} catch (IOException e) {
					throw new ReadCodeException("读取代码失败");
				}
			else
                return false;
        return true;
    }
    
    protected void readLine() throws UnexpectedCodeException, IOException {
        String line = reader.readLine();
        if (line == null) {
            hasMore = false;
            return;
        }
        int lineNo = reader.getLineNumber();
        Matcher matcher = pattern.matcher(line);
        matcher.useTransparentBounds(true).useAnchoringBounds(false);
        int pos = 0;
        int endPos = line.length();
        while (pos < endPos) {
            matcher.region(pos, endPos);
            if (matcher.lookingAt()) {
                addToken(lineNo, matcher);
                pos = matcher.end();
            }else
                throw new UnexpectedCodeException(pos, lineNo, line);
        }
    }
    
    protected void addToken(int lineNo, Matcher matcher) {
		//根据正则匹配到的几种类型能获知当前的token是哪种类型
        String m = matcher.group(1);
        if (m != null) // if not a space
            if (matcher.group(2) == null) { // if not a comment
                Token token;
                if (matcher.group(3) != null)
                    token = new NumToken(lineNo, Integer.parseInt(m));	//数字
                else if (matcher.group(4) != null) 
                    token = new StrToken(lineNo, toStringLiteral(m)); //由双引号扩起来的字符串
                else if(matcher.group(5) != null) 
                    token = new IdentifierToken(lineNo, m); //关键字、操作符
                else
                	token = new NounToken(lineNo, m); //变量名
                queue.add(token);
            }
    }
    
    protected String toStringLiteral(String s) {
        StringBuilder sb = new StringBuilder();
        int len = s.length() - 1;
        for (int i = 1; i < len; i++) {
            char c = s.charAt(i);
            if (c == '\\' && i + 1 < len) {
                int c2 = s.charAt(i + 1);
                if (c2 == '"' || c2 == '\\')
                    c = s.charAt(++i);
                else if (c2 == 'n') {
                    ++i;
                    c = '\n';
                }
            }
            sb.append(c);
        }
        return sb.toString();
    }

    protected static class NumToken extends Token {
        private int value;

        protected NumToken(int line, int v) {
            super(line);
            value = v;
        }
        public boolean isNumber() { return true; }
        public String getText() { return Integer.toString(value); }
        public int getNumber() { return value; }
    }

    protected static class IdentifierToken extends Token {
        private String text; 
        protected IdentifierToken(int line, String id) {
            super(line);
            text = id;
        }
        public boolean isIdentifier() { return true; }
        public String getText() { return text; }
    }
    
    protected static class NounToken extends Token {
        private String text; 
        protected NounToken(int line, String id) {
            super(line);
            text = id;
        }
        public boolean isNouns() { return true; }
        public String getText() { return text; }
    }

    protected static class StrToken extends Token {
        private String literal;
        StrToken(int line, String str) {
            super(line);
            literal = str;
        }
        public boolean isString() { return true; }
        public String getText() { return literal; }
    }
}
