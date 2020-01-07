package me.DJ1TJOO.minecode.item.attribute;

public class MaxHealthAttribute extends Attribute {

	public MaxHealthAttribute(String slot, int operation, double amount) {
		super("generic.maxHealth", "generic.maxHealth", slot, operation, Attribute.counter, Attribute.counter+1, amount); //0.0 - 1024.0f
		Attribute.counter++;
	}

}
