package me.DJ1TJOO.minecode.item.attribute;

public class AttackDamageAttribute extends Attribute {

	public AttackDamageAttribute(String slot, int operation, int amount) {
		super("generic.attackDamage", "generic.attackDamage", slot, operation, Attribute.counter, Attribute.counter+1, amount);//0 - 2048
		Attribute.counter++;
	}

}
