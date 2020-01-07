package me.DJ1TJOO.minecode.item.attribute;

public class LuckAttribute extends Attribute {

	public LuckAttribute(String slot, int operation, int amount) {
		super("generic.luck", "generic.luck", slot, operation, Attribute.counter, Attribute.counter+1, amount); //-1024 - 1024
		Attribute.counter++;
	}

}
