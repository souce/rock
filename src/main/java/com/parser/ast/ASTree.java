package main.java.com.parser.ast;

import java.util.ArrayList;
import java.util.List;

import main.java.com.parser.Token;

/*
 * 抽象语法树节点
 */
public class ASTree {
	private Token token;
	private List<ASTree> children = new ArrayList<>();

	public ASTree() {
		super();
	}
	
	public ASTree(Token token) {
		super();
		this.token = token;
	}

	public Token getToken() {
		return token;
	}

	public void setToken(Token token) {
		this.token = token;
	}

	public void addChild(ASTree child){
		children.add(child);
	}
	
	public void addChild(Token token){
		ASTree tree = new ASTree();
		tree.setToken(token);
		children.add(tree);
	}
	
	public void addChild(int index, ASTree child){
		children.add(index, child);
	}
	
	public void addChild(int index, Token token){
		ASTree tree = new ASTree();
		tree.setToken(token);
		children.add(index, tree);
	}
	
	public void setChild(int index, ASTree child){
		children.set(index, child);
	}
	
	public ASTree child(int i) throws Exception{
		return children.get(i);
	}
	
	public ASTree rmChild(int i) throws Exception{
		return children.remove(i);
	}

	public int numChildren() throws Exception{
		return children.size();
	}

}
