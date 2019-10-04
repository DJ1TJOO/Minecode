package me.DJ1TJOO.minecode.craft;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

public class CustomCraftRecipe {

	private String name;
	private ShapedRecipe recipe;
	private ItemStack output;
	private HashMap<Character, ItemStack> input;
	
	public CustomCraftRecipe(String name, ShapedRecipe recipe, ItemStack output, HashMap<Character, ItemStack> input) {
		this.name = name;
		this.recipe = recipe;
		this.output = output;
		this.input = input;
		Bukkit.getServer().addRecipe(this.recipe);
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public ShapedRecipe getRecipe() {
		return recipe;
	}

	public void setRecipe(ShapedRecipe recipe) {
		this.recipe = recipe;
	}

	public ItemStack getOutput() {
		return output;
	}

	public void setOutput(ItemStack output) {
		this.output = output;
	}

	public HashMap<Character, ItemStack> getInput() {
		return input;
	}

	public void setInput(HashMap<Character, ItemStack> input) {
		this.input = input;
	}
	
	public void addInput(char key, ItemStack input) {
		this.input.put(key, input);
	}
}
