package me.DJ1TJOO.minecode.item.attribute;

public class MovementSpeedAttribute extends Attribute {

	public MovementSpeedAttribute(String slot, int operation, double amount) {
		super("generic.movementSpeed", "generic.movementSpeed", slot, operation, 4, 4, amount);//0.0 - 1024.0f
	}

}
