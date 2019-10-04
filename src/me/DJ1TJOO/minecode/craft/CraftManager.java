package me.DJ1TJOO.minecode.craft;

import java.util.ArrayList;
import java.util.List;

public class CraftManager {

	List<CustomCraftRecipe> customCraftingRecipes;
	List<CustomFurnaceRecipe> customFurnaceRecipes;
	
	public CraftManager() {
		customCraftingRecipes = new ArrayList<CustomCraftRecipe>();
		customFurnaceRecipes = new ArrayList<CustomFurnaceRecipe>();
	}
	
	public CustomCraftRecipe getCraftRecipe(String name) {
		for (CustomCraftRecipe customCraftingRecipes : customCraftingRecipes) {
			if(customCraftingRecipes.getName().equals(name)) {
				return customCraftingRecipes;
			}
		}
		return null;
	}
	
	public void addCraftRecipe(CustomCraftRecipe customCraftingRecipe) {
		customCraftingRecipes.add(customCraftingRecipe);
	}
	
	public void removeCraftRecipe(CustomCraftRecipe customCraftRecipe) {
		customCraftingRecipes.remove(customCraftRecipe);
	}
	
	public void removeCraftRecipe(String name) {
		for (CustomCraftRecipe customCraftingRecipe : customCraftingRecipes) {
			if(customCraftingRecipe.getName().equals(name)) {
				customCraftingRecipes.remove(customCraftingRecipe);
			}
		}
	}

	public List<CustomCraftRecipe> getCraftingRecipes() {
		return customCraftingRecipes;
	}
	
	public CustomFurnaceRecipe getFurnaceRecipe(String name) {
		for (CustomFurnaceRecipe customFurnaceRecipes : customFurnaceRecipes) {
			if(customFurnaceRecipes.getName().equals(name)) {
				return customFurnaceRecipes;
			}
		}
		return null;
	}
	
	public void addFurnaceRecipe(CustomFurnaceRecipe customFurnaceRecipe) {
		customFurnaceRecipes.add(customFurnaceRecipe);
	}
	
	public void removeFurnaceRecipe(CustomFurnaceRecipe customFurnaceRecipe) {
		customFurnaceRecipes.remove(customFurnaceRecipe);
	}
	
	public void removeFurnaceRecipe(String name) {
		for (CustomFurnaceRecipe customFurnaceRecipe : customFurnaceRecipes) {
			if(customFurnaceRecipe.getName().equals(name)) {
				customFurnaceRecipes.remove(customFurnaceRecipe);
			}
		}
	}

	public List<CustomFurnaceRecipe> getFurnaceRecipes() {
		return customFurnaceRecipes;
	}

}
