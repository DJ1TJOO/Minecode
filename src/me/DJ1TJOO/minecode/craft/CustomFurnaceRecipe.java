package me.DJ1TJOO.minecode.craft;

import org.bukkit.Bukkit;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;

public class CustomFurnaceRecipe {

	private String name;
	private FurnaceRecipe recipe;
	private ItemStack output;
	private ItemStack input;
	
	public CustomFurnaceRecipe(String name, FurnaceRecipe recipe, ItemStack output, ItemStack input) {
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

	public FurnaceRecipe getRecipe() {
		return recipe;
	}

	public void setRecipe(FurnaceRecipe recipe) {
		this.recipe = recipe;
	}

	public ItemStack getOutput() {
		return output;
	}

	public void setOutput(ItemStack output) {
		this.output = output;
	}

	public ItemStack getInput() {
		return input;
	}

	public void setInput(ItemStack input) {
		this.input = input;
	}
}
