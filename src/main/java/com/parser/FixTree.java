package main.java.com.parser;

import main.java.com.exception.SyntaxErrorException;
import main.java.com.parser.ast.ASTree;

/*
 * 修正抽象语法树
 * 语法树在生成虚拟机指令前需要进行一番修正。
 * 语法树是代码逻辑的体现，是一种高度抽象，不与实现细节有任何联系，所以他是很通用、原始、基本的，
 * 它所携带的信息还不足以使用一种通用的方式来生成字节码。
 * 所以它需要一番修正，在比较完善、成熟的语言中，这步修正过程主要是为了：
 *      为各节点加上类型信息；
 *      为不同类型间的运算加上转换指令；
 *      将表达式中用到的变量与声明绑定。
 * 由此可见在这一步的修正中，语法树不再是“通用的、与实现无关”的了，它开始携带语言的实现细节（添加与VM操作指令相关的信息）。
 */
public class FixTree {
	
	public void fixTree(ASTree tree) throws Exception{
		if(null == tree) return;
		if(null != tree.getToken()){
			int lineNumber = tree.getToken().getLineNumber();
			switch (tree.getToken().getText()) {
			case "if":{
				ASTree begineNode = new ASTree(new BeginToken(lineNumber));
				tree.addChild(0, begineNode);
				
				//在condition节点之上加上一个“ifJmp”的标志节点，当条件不成立时候跳转该分支的最后，运行其他分支的判断
				ASTree ifJumpNode = new ASTree(new IfJmpToken(lineNumber));
				ASTree exptTree = tree.child(1);
				ifJumpNode.addChild(exptTree);
				tree.setChild(1, ifJumpNode);
				
				//当if条件成立，在body之后设置一个跳转标记，body的逻辑执行了之后跳到所有分支之后（结束if的流程）
				ASTree jumpNode = new ASTree(new JmpToken(lineNumber));
				tree.addChild(3, jumpNode);
				
				//在if的最后设置一个“ifJmp”对应的结束标记“ifEnd”，当condition不成立时，跳转到此
				tree.setToken(new IfEndToken(lineNumber));
				break;
			}
			case "elif":{
				//在condition节点之上加上一个“ifJmp”的标志节点，当条件不成立时候跳转该分支的最后，运行其他分支的判断
				ASTree ifJumpNode = new ASTree(new IfJmpToken(lineNumber));
				ASTree exptTree = tree.child(0);
				ifJumpNode.addChild(exptTree);
				tree.setChild(0, ifJumpNode);
				
				//当if条件成立，在body之后设置一个跳转标记，body的逻辑执行了之后跳到所有分支之后（结束if的流程）
				ASTree jumpNode = new ASTree(new JmpToken(lineNumber));
				tree.addChild(2, jumpNode);
				
				//在if的最后设置一个“ifJmp”对应的结束标记“ifEnd”，当condition不成立时，跳转到此
				tree.setToken(new IfEndToken(lineNumber));
				break;
			}
			
			case "else":
				//这个“jmpEnd”是当前if的所有判断分支的“jmp”的对应点，当if中任意一个分支被执行，执行将跳转至此，整个if的最后，意味着结束了if
				//注意：这个“jmpEnd”是在else的执行流程之后，并非是跳转到此就是执行els分支！
				tree.setToken(new JmpEndToken(lineNumber));
				break;
				
			case "while":{
				//在while的判断流程执行设置一个起点“upBegin”
				ASTree upBeginNode = new ASTree(new UpBeginToken(lineNumber));
				tree.addChild(0, upBeginNode);
				
				//while的判断condition节点之上加上一个“ifJmp”的标志节点，当条件不成立时候跳转到while的最后，也就是退出while
				ASTree ifJumpNode = new ASTree(new IfJmpToken(lineNumber));
				ASTree exptTree = tree.child(1);
				ifJumpNode.addChild(exptTree);
				tree.setChild(1, ifJumpNode);

				//在while的body之后加上一个“upJmp”标记，当执行到此的时候会跳转到condition判断之前，重新再运行（循环）
				ASTree upJmpNode = new ASTree(new UpJmpToken(lineNumber));
				tree.addChild(3, upJmpNode);
				
				//为condition的“ifJmp”加上一个对应的“ifEnd”标记，当condition不成立，则跳转至此，结束while循环
				tree.setToken(new IfEndToken(lineNumber));
				break;
			}	
			case"=":{
				//赋值操作需要调整位置
				if(tree.numChildren()%2 !=0){
					new SyntaxErrorException(lineNumber, tree.getToken(), "“＝”两边的赋值数量不符");
				}
				int pos = tree.numChildren()/2;
				// 调整为： “值->变量->entrust” 的形式
				ASTree entrustNode = new ASTree(new EntrustToken(lineNumber));
				int namePos = 0; //变量的位置
				int valPos = pos; //值的位置
				for(; valPos < tree.numChildren(); ){
					tree.addChild(namePos, tree.child(valPos));
					tree.addChild(namePos+2, entrustNode);
					valPos+=2;
					namePos+=3;
					tree.rmChild(valPos);
				}
				break;
			}
			case"function":
				//函数
				ASTree funcName = tree.child(0);
				tree.rmChild(0);
				
				ASTree funcBeginNode = new ASTree(new FuncBeginToken(lineNumber));
				tree.setChild(0, funcBeginNode);
				
				ASTree funcEndNode = new ASTree(new FuncEndToken(lineNumber));
				tree.setChild(tree.numChildren()-1, funcEndNode);
				
				tree.addChild(tree.numChildren(), funcName);
				
				tree.setToken(null);
				break;
			default:
				//函数的调用
				if(null != tree.getToken() && tree.getToken().isNouns()){
					if(tree.numChildren() < 2)break;
					if("(".equals(tree.child(0).getToken().getText()) && 
					   ")".equals(tree.child(tree.numChildren()-1).getToken().getText())){
						//is function
						//在最开的子节点处加上一个mark标记，用作确定边界、保存函数返回地址
						//“mark->参数->参数->函数名字->callFunc”
						ASTree markNode = new ASTree(new MarkToken(lineNumber));
						tree.addChild(0, markNode);
						
						//移除括号标记
						tree.rmChild(1);
						tree.rmChild(tree.numChildren()-1);
						
						//加上函数名
						ASTree funcNode = new ASTree(tree.getToken());
						tree.addChild(tree.numChildren(), funcNode);
						
						//修改token为callFunc
						tree.setToken(new CallFuncToken(lineNumber));
					}
				}
				break;
			}
		}
		for(int i = 0; i < tree.numChildren(); i++){
			fixTree(tree.child(i));
		}
	}
	
}

/*
 * 函数的开头
 */
class FuncBeginToken extends Token{
    private String literal = "funcBegin";
    public FuncBeginToken(int line) {
        super(line);
    }
    public boolean isIdentifier() { return true; } //定义，并非普通字符串
    public String getText() { return literal; }
}

/*
 * 函数的结尾
 */
class FuncEndToken extends Token{
    private String literal = "funcEnd";
    public FuncEndToken(int line) {
        super(line);
    }
    public boolean isIdentifier() { return true; } //定义，并非普通字符串
    public String getText() { return literal; }
}

/*
 * 边界指令
 */
class BeginToken extends Token{
    private String literal = "begin";
    public BeginToken(int line) {
        super(line);
    }
    public boolean isIdentifier() { return true; } //定义，并非普通字符串
    public String getText() { return literal; }
}

/*
 * “跳转指令”的标记
 */
class JmpToken extends Token{
    private String literal = "jmp";
    public JmpToken(int line) {
        super(line);
    }
    public boolean isIdentifier() { return true; } //定义，并非普通字符串
    public String getText() { return literal; }
}

/*
 * 对应“跳转指令”标记的结束标记
 */
class JmpEndToken extends Token{
    private String literal = "jmpEnd";
    public JmpEndToken(int line) {
        super(line);
    }
    public boolean isIdentifier() { return true; } //定义，并非普通字符串
    public String getText() { return literal; }
}

/*
 * “判断条件的跳转指令”的标记
 */
class IfJmpToken extends Token{
    private String literal = "ifJmp";
    public IfJmpToken(int line) {
        super(line);
    }
    public boolean isIdentifier() { return true; } //定义，并非普通字符串
    public String getText() { return literal; }
}

/*
 * 对应“判断条件的跳转指令”的标记的结束标记
 */
class IfEndToken extends Token{
    private String literal = "ifEnd";
    public IfEndToken(int line) {
        super(line);
    }
    public boolean isIdentifier() { return true; } //定义，并非普通字符串
    public String getText() { return literal; }
}

/*
 * “回跳指令”的起点标记
 */
class UpBeginToken extends Token{
    private String literal = "upBegin";
    public UpBeginToken(int line) {
        super(line);
    }
    public boolean isIdentifier() { return true; } //定义，并非普通字符串
    public String getText() { return literal; }
}

/*
 * 对应“回跳指令的起点”标记的跳转标记
 */
class UpJmpToken extends Token{
    private String literal = "upJmp";
    public UpJmpToken(int line) {
        super(line);
    }
    public boolean isIdentifier() { return true; } //定义，并非普通字符串
    public String getText() { return literal; }
}

/*
 * “标记指令”的标记
 */
class MarkToken extends Token{
    private String literal = "mark";
    public MarkToken(int line) {
        super(line);
    }
    public boolean isIdentifier() { return true; } //定义，并非普通字符串
    public String getText() { return literal; }
}

/*
 * “调用方法指令”的标记
 */
class CallFuncToken extends Token{
    private String literal = "callFunc";
    public CallFuncToken(int line) {
        super(line);
    }
    public boolean isIdentifier() { return true; } //定义，并非普通字符串
    public String getText() { return literal; }
}

/*
 * 给栈上的变量赋值
 */
class EntrustToken extends Token{
    private String literal = "entrust";
    public EntrustToken(int line) {
        super(line);
    }
    public boolean isIdentifier() { return true; } //定义，并非普通字符串
    public String getText() { return literal; }
}

/*
 * 为栈上的变量取到值，存放在栈上
 */
class PushToken extends Token{
    private String literal = "push";
    public PushToken(int line) {
        super(line);
    }
    public boolean isIdentifier() { return true; } //定义，并非普通字符串
    public String getText() { return literal; }
}
