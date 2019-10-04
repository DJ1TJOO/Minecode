package me.DJ1TJOO.minecode.item.attribute;

public class KnockbackResistanceAttribute extends Attribute {

	public KnockbackResistanceAttribute(String slot, int operation, double amount) {
		super("generic.knockbackResistance", "generic.knockbackResistance", slot, operation, 3, 3, amount);//0.0 - 1.0f
	}

}
