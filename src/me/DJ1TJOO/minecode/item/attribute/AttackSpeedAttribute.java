package me.DJ1TJOO.minecode.item.attribute;

public class AttackSpeedAttribute extends Attribute {

	public AttackSpeedAttribute(String slot, int operation, int amount) {
		super("generic.attackSpeed", "generic.attackSpeed", slot, operation, Attribute.counter, Attribute.counter+1, amount);//0 - 1024
		Attribute.counter++;
	}

}
