package me.DJ1TJOO.minecode.item;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;

import me.DJ1TJOO.minecode.Minecode;

public class ItemManager {
	
	List<CustomItem> customItems;

	public ItemManager() {
		customItems = new ArrayList<CustomItem>();
	}
	
	public CustomItem getItem(String name) {
		for (CustomItem customItem : customItems) {
			if(customItem.getName().equals(name)) {
				return customItem;
			}
		}
		return null;
	}
	
	public void addItem(CustomItem customItem) {
		customItems.add(customItem);
	}
	
	public void removeItem(CustomItem customItem) {
		customItems.remove(customItem);
	}
	
	public void removeItem(String name) {
		for (CustomItem customItem : customItems) {
			if(customItem.getName().equals(name)) {
				customItems.remove(customItem);
			}
		}
	}

	public List<CustomItem> getItems() {
		return customItems;
	}

	public CustomItem getItem(ItemStack itemStack) {
		for (CustomItem customItem : customItems) {
			if(Minecode.isSimilar(customItem.getItem(), itemStack)) {
				return customItem;
			}
		}
		return null;
	}
	
}
