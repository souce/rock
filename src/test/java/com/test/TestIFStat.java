package test.java.com.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;

import main.java.com.lib.AddFunction;
import main.java.com.lib.PrintFunction;
import main.java.com.parser.FixTree;
import main.java.com.parser.Lexer;
import main.java.com.parser.NonterminalFactory;
import main.java.com.parser.Translator;
import main.java.com.parser.ast.ASTree;
import main.java.com.parser.nonterminal.IParser;
import main.java.com.vm.Order;
import main.java.com.vm.StackVM;

public class TestIFStat {
	private static void printTree(ASTree tree) throws Exception{
		if(null == tree) return;
		for(int i = 0; i < tree.numChildren(); i++){
			printTree(tree.child(i));
		}
		if(null != tree.getToken()){
			System.out.print(tree.getToken().getText()+" ");
		}
	}
	
	public static void main(String[] args) {
		try {
			NonterminalFactory factory = new NonterminalFactory();
			factory.initNonterminal();
			IParser stat = factory.getParser();
			
			Lexer lexer = new Lexer(new BufferedReader(new FileReader("if.rock")));
			ASTree tree = new ASTree();
			if(stat.parser(lexer, tree)){
				printTree(tree);
			}
			System.out.println("\n");
			
			new FixTree().fixTree(tree);
			printTree(tree);
			System.out.println("\n");
			
			List<Order> os = new Translator().translate(tree);
			System.out.println(os);
			System.out.println("");
			
			StackVM vm = new StackVM();
			vm.registerFunction("print", new PrintFunction());
			vm.registerFunction("add", new AddFunction());
			vm.execute(os);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
