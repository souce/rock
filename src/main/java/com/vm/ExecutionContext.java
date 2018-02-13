package main.java.com.vm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * 函数的执行上下文
 */
public class ExecutionContext {
	private Map<String, Object> variableObjects;   	//局部作用域
	private List<ExecutionContext> scopeChain; 		//作用域链
	private int scopeChainPos = 0;					//当前处在作用域的哪一层
	
	public ExecutionContext(List<ExecutionContext> scopeChain){
		variableObjects = new HashMap<>();
		scopeChainPos = scopeChain.size();
		this.scopeChain = scopeChain;
		scopeChain.add(this);
	}
	
	public ExecutionContext(List<ExecutionContext> scopeChain, Map<String, Object> variableObjects){
		this.variableObjects = variableObjects;
		scopeChainPos = scopeChain.size();
		this.scopeChain = scopeChain;
		scopeChain.add(this);
	}
	
	public Map<String, Object> getAllVariableObjects(){
		Map<String, Object> variableObjects = new HashMap<>();
		for(int i = 0; i < this.scopeChainPos; i++){
			ExecutionContext scope = this.scopeChain.get(i);
			variableObjects.putAll(scope.variableObjects);
		}
		return variableObjects;
	}
	
	public Object getVariableObject(String key){
		if(this.variableObjects.containsKey(key)){
			return variableObjects.get(key);
		}else{
			for(int i = 0; i < this.scopeChainPos; i++){
				ExecutionContext scope = this.scopeChain.get(i);
				Object ret = scope.getVariableObject(key);
				if(null != ret)
					return ret;
			}
		}
		return null;
	}

	public void setVariableObject(String key, Object obj){
		this.variableObjects.put(key, obj);
	}

	public List<ExecutionContext> getScopeChain() {
		return scopeChain;
	}
	
}
