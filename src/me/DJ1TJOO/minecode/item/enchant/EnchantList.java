package me.DJ1TJOO.minecode.item.enchant;

import java.util.Arrays;

public class EnchantList {

	Enchant[] enchants;
	
	public EnchantList(Enchant... enchantments) {
		this.enchants = enchantments;
	}

	public Enchant[] getEnchants() {
		return enchants;
	}

	public void setEnchantments(Enchant[] enchants) {
		this.enchants = enchants;
	}
	
	public Enchant[] append(Enchant enchant) {
	    final int N = this.enchants.length;
	    this.enchants = Arrays.copyOf(this.enchants, N + 1);
	    this.enchants[N] = enchant;
	    return this.enchants;
	}
	
	public Boolean contains(Enchant enchant) {
		for (Enchant enchants : enchants) {
			if(enchants.equals(enchant)) {
				return true;
			}
		}
		return false;
	}

}
