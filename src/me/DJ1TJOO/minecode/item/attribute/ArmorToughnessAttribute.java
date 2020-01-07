package me.DJ1TJOO.minecode.item.attribute;

public class ArmorToughnessAttribute extends Attribute {

	public ArmorToughnessAttribute(String slot, int operation, int amount) {
		super("generic.armorToughness", "generic.armorToughness", slot, operation, Attribute.counter, Attribute.counter+1, amount);//0 - 20
		Attribute.counter++;
	}

}
