package me.DJ1TJOO.minecode.item.attribute;

public class ArmorAttribute extends Attribute {

	public ArmorAttribute(String slot, int operation, int amount) {
		super("generic.armor", "generic.armor", slot, operation, Attribute.counter, Attribute.counter+1, amount);//0 - 30
		Attribute.counter++;
	}

}
