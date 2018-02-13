package main.java.com.vm;

/*
 * 指令的抽象

NOP,
//赋值操作
PUSHNIL,
PUSH0, PUSH1, PUSH2, //保存0、1、2值的时候的指令
PUSHBYTE, //保存非0、1、2值的时候的指令
PUSHWORD,
PUSHFLOAT,
PUSHSTRING,

//从局部变量中抽取数据到当前栈顶，栈顶再往后移一位
PUSHLOCAL0, PUSHLOCAL1, PUSHLOCAL2, PUSHLOCAL3, PUSHLOCAL4,
PUSHLOCAL5, PUSHLOCAL6, PUSHLOCAL7, PUSHLOCAL8, PUSHLOCAL9,
PUSHLOCAL,
PUSHGLOBAL,

PUSHINDEXED,
PUSHMARK,
PUSHOBJECT,
//end:赋值操作

//在栈中存放数据操作
//LOCAL系列的操作是将当前栈顶的数据放入到base中，栈顶再后退一位
STORELOCAL0, STORELOCAL1, STORELOCAL2, STORELOCAL3, STORELOCAL4,
STORELOCAL5, STORELOCAL6, STORELOCAL7, STORELOCAL8, STORELOCAL9,
STORELOCAL,

STOREGLOBAL,
STOREINDEXED0,
STOREINDEXED,
STOREFIELD,
//end:在栈中存放数据操作

ADJUST, //修正，让栈顶指向base+pc的位置，防止base和top重合
CREATEARRAY, //在当前的栈的前一位创建一个hash（array）

EQOP, //判断值是否相等
LTOP, //比较左数是否小于右数
LEOP, //比较左数是否小于等于右数

ADDOP, //加
SUBOP, //减
MULTOP, //乘
DIVOP, //除

CONCOP,
MINUSOP,
NOTOP,
ONTJMP,
ONFJMP,
JMP, //字节码往后跳,偏移量
IFFJMP, //判断的逻辑，判断栈顶的条件是否成立，不成立则跳过if的结构体
UPJMP, //字节码往前跳,偏移量

IFFJMP,
IFFUPJMP,
POP,
CALLFUNC, //调用函数

RETCODE, //方法结束，将原流程和局部栈还原

HALT, //文件正常执行完毕

SETFUNCTION, //将当前执行的方法放入到函数栈中
SETLINE, //如果开启“$debug”,每个操作都会带上一个设置行号，用于debug

RESET //函数栈，栈顶出栈
*/

public class Order {
	public enum OrderOpType {
		PUSH, 			//数据放入栈中
		
		EQOP, 			//==
		NEOP, 			//!=
		GTOP,			//>
		GEOP,			//>=
		LTOP,			//<
		LEOP, 			//<=
		MODOP,			//%
		ADDOP, 			//加
		SUBOP, 			//减
		MULTOP, 		//乘
		DIVOP, 			//除
		
		JMP,			//字节码往后跳
		IFJMP, 			//判断栈顶数据，然后决定是否向后跳
		UPJMP, 			//字节码往前跳
		
		CALLFUNC,		//调用函数
		
		HALT, 			//文件正常执行完毕
		STOREVAR, 	//将栈顶的数据保存到作用域变量表中
		PUSHVAR, 	//以栈顶的数据为key，从作用域变量表中查询，并将结果存放在栈顶
		MARK
	}
	
	public int lineNo;		//生成指令的代码所在行
	public OrderOpType op;	//指令类型
	public Object value;	//指令对应的值
	
	public Order(int lineNo, OrderOpType op, Object value) {
		super();
		this.lineNo = lineNo;
		this.op = op;
		this.value = value;
	}

	//debug
	public String toString(){
		switch (op) {
		case JMP:
			return value!=null?"JMP:"+value.toString():"none";
		case IFJMP:
			return value!=null?"IFJMP:"+value.toString():"none";
		case UPJMP:
			return value!=null?"UPJMP:"+value.toString():"none";
		case PUSHVAR:
			return value!=null?"PUSHVAR:"+value.toString():"none";
		case STOREVAR:
			return value!=null?"STOREVAR:"+value.toString():"none";
		case MARK:
			return "mark";
		default:
			break;
		}
		return value!=null?value.toString():"none";
	}
}
