package me.DJ1TJOO.minecode.item.attribute;

public class FollowRangeAttribute extends Attribute {

	public FollowRangeAttribute(String slot, int operation, int amount) {
		super("generic.followRange", "generic.followRange", slot, operation, Attribute.counter, Attribute.counter+1, amount);//0 - 2048
		Attribute.counter++;
	}

}
