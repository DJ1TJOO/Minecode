
package me.DJ1TJOO.minecode.item.property;

public class Property {

	private Object variable;
	private String name;
	
	public Property(Object variable, String name) {
		this.variable = variable;
		this.setName(name);
	}
	
	public Object getVariable() {
		return variable;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setVariable(Object variable) {
		this.variable = variable;
	}
}