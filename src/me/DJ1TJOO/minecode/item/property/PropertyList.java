package me.DJ1TJOO.minecode.item.property;

import java.util.Arrays;

public class PropertyList {

	Property[] properties;
	
	public PropertyList(Property... properties) {
		this.properties = properties;
	}

	public Property[] getProperties() {
		return properties;
	}

	public void setProperties(Property[] properties) {
		this.properties = properties;
	}
	
	public Property[] append(Property property) {
	    final int N = this.properties.length;
	    this.properties = Arrays.copyOf(this.properties, N + 1);
	    this.properties[N] = property;
	    return this.properties;
	}
	
	public Property getProperty(String name) {
		for (Property property : properties) {
			if(property.getName().equals(name)) {
				return property;
			}
		}
		return null;
	}
	
	public Object getVariable(String name) {
		for (Property property : properties) {
			if(property.getName().equals(name)) {
				return property.getVariable();
			}
		}
		return null;
	}
	
	public Boolean contains(String name) {
		for (Property property : properties) {
			if(property.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}
	
	public Boolean contains(Property property) {
		for (Property properties : properties) {
			if(properties.equals(property)) {
				return true;
			}
		}
		return false;
	}

}
