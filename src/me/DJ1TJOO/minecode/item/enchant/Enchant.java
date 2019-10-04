package me.DJ1TJOO.minecode.item.enchant;

import org.bukkit.enchantments.Enchantment;

public class Enchant {
	
	private int level;
	private Enchantment enchantment;
	
	public Enchant(int level, Enchantment enchantment) {
		this.level = level;
		this.enchantment = enchantment;
	}

	public Enchantment getEnchantment() {
		return enchantment;
	}
	
	public void setEnchantment(Enchantment enchantment) {
		this.enchantment = enchantment;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

}
