package me.DJ1TJOO.minecode.item.attribute;

import net.minecraft.server.v1_14_R1.NBTTagCompound;
import net.minecraft.server.v1_14_R1.NBTTagDouble;
import net.minecraft.server.v1_14_R1.NBTTagInt;
import net.minecraft.server.v1_14_R1.NBTTagString;

public class Attribute {

	public static int counter = 10;
	
	String name, attributeName, slot;
	int operation, uuidLeast, uuidMost;
	boolean integer;
	double amount;
	
	public Attribute(String name, String attributeName, String slot, int operation, int uuidLeast, int uuidMost,
			double amount) {
		this.name = name;
		this.attributeName = attributeName;
		this.slot = slot;
		this.operation = operation;
		this.uuidLeast = uuidLeast;
		this.uuidMost = uuidMost;
		this.amount = amount;
		this.integer = false;
	}
	
	public Attribute(String name, String attributeName, String slot, int operation, int uuidLeast, int uuidMost,
			int amount) {
		this.name = name;
		this.attributeName = attributeName;
		this.slot = slot;
		this.operation = operation;
		this.uuidLeast = uuidLeast;
		this.uuidMost = uuidMost;
		this.amount = amount;
		this.integer = true;
	}
	
	public NBTTagCompound getCompound() {
		NBTTagCompound attribute = new NBTTagCompound();
		attribute.set("Name", new NBTTagString(name));
		attribute.set("AttributeName", new NBTTagString(attributeName));
		if(integer) {
			attribute.set("Amount", new NBTTagInt((int) amount));
		} else {
			attribute.set("Amount", new NBTTagDouble(amount));
		}
        attribute.set("Operation", new NBTTagInt(operation));
        attribute.set("UUIDLeast", new NBTTagInt(uuidLeast));
        attribute.set("UUIDMost", new NBTTagInt(uuidLeast));
        if(!slot.equals("")) {
            attribute.set("Slot", new NBTTagString(slot));
        }
        return attribute;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getAttributeName() {
		return attributeName;
	}
	
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}
	
	public String getSlot() {
		return slot;
	}
	
	public void setSlot(String slot) {
		this.slot = slot;
	}
	
	public int getOperation() {
		return operation;
	}
	
	public void setOperation(int operation) {
		this.operation = operation;
	}
	
	public int getUuidLeast() {
		return uuidLeast;
	}
	
	public void setUuidLeast(int uuidLeast) {
		this.uuidLeast = uuidLeast;
	}
	
	public int getUuidMost() {
		return uuidMost;
	}
	
	public void setUuidMost(int uuidMost) {
		this.uuidMost = uuidMost;
	}
	
	public double getAmount() {
		return amount;
	}
	
	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	public boolean isInteger(double variable) {
		if ((variable == Math.floor(variable)) && !Double.isInfinite(variable)) {
		    return true;
		} else {
			return false;
		}
	}
	
}
