package me.DJ1TJOO.minecode.block.type;

import java.util.List;

public class TypeList {

	Type[] types;
	
	public TypeList(Type... types) {
		this.types = types;
	}

	public TypeList(List<String> types) {
		Type[] types2 = new Type[types.size()];
		int i = 0;
		for (String string : types) {
			types2[i] = Type.valueOf(string.toUpperCase());
			i++;
		}
		this.types = types2;
	}

	public Type[] getTypes() {
		return types;
	}

	public void setTypes(Type[] types) {
		this.types = types;
	}
	
	public Boolean contains(Type type) {
		for (Type types : types) {
			if(types.equals(type)) {
				return true;
			}
		}
		return false;
	}
	
}
