package me.DJ1TJOO.minecode.item.attribute;

public class AttackKnockbackAttribute extends Attribute {

	public AttackKnockbackAttribute(String slot, int operation, double amount) {
		super("generic.attackKnockback", "generic.attackKnockback", slot, operation, Attribute.counter, Attribute.counter+1, amount);//0.0 -f
		Attribute.counter++;
	}

}
