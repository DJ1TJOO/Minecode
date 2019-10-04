package me.DJ1TJOO.minecode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.v1_14_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_14_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.CartographyInventory;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.GrindstoneInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.LoomInventory;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.StonecutterInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import me.DJ1TJOO.minecode.block.BlockManager;
import me.DJ1TJOO.minecode.block.CustomBlock;
import me.DJ1TJOO.minecode.block.tool.Tool;
import me.DJ1TJOO.minecode.block.tool.ToolList;
import me.DJ1TJOO.minecode.block.type.Type;
import me.DJ1TJOO.minecode.block.type.TypeList;
import me.DJ1TJOO.minecode.craft.CraftManager;
import me.DJ1TJOO.minecode.craft.CustomCraftRecipe;
import me.DJ1TJOO.minecode.craft.CustomFurnaceRecipe;
import me.DJ1TJOO.minecode.item.CustomItem;
import me.DJ1TJOO.minecode.item.ItemManager;
import me.DJ1TJOO.minecode.item.ItemType;
import me.DJ1TJOO.minecode.item.attribute.ArmorAttribute;
import me.DJ1TJOO.minecode.item.attribute.ArmorToughnessAttribute;
import me.DJ1TJOO.minecode.item.attribute.AttackDamageAttribute;
import me.DJ1TJOO.minecode.item.attribute.AttackKnockbackAttribute;
import me.DJ1TJOO.minecode.item.attribute.AttackSpeedAttribute;
import me.DJ1TJOO.minecode.item.attribute.Attribute;
import me.DJ1TJOO.minecode.item.attribute.FollowRangeAttribute;
import me.DJ1TJOO.minecode.item.attribute.KnockbackResistanceAttribute;
import me.DJ1TJOO.minecode.item.attribute.LuckAttribute;
import me.DJ1TJOO.minecode.item.attribute.MaxHealthAttribute;
import me.DJ1TJOO.minecode.item.attribute.MovementSpeedAttribute;
import me.DJ1TJOO.minecode.item.enchant.Enchant;
import me.DJ1TJOO.minecode.item.enchant.EnchantList;
import me.DJ1TJOO.minecode.item.property.Property;
import me.DJ1TJOO.minecode.item.property.PropertyList;
import net.minecraft.server.v1_14_R1.BlockPosition;
import net.minecraft.server.v1_14_R1.NBTTagCompound;
import net.minecraft.server.v1_14_R1.NBTTagList;
import net.minecraft.server.v1_14_R1.TileEntityMobSpawner;

public class Minecode extends JavaPlugin implements Listener {
	
	private CraftManager cm;
	private BlockManager bm;
	private ItemManager im;
	private Config cf;
	
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
		
		cf = new Config();
		cf.setup(this);
		saveDefaultConfig();
		
		cm = new CraftManager();
		bm = new BlockManager();d
		im = new ItemManager();
		
		//load blocks
		if(cf.getConfig().contains("blocks")) {
			for (String key : cf.getConfig().getConfigurationSection("blocks").getKeys(false)) {
				try {
					String name = cf.getConfig().getString("blocks." + key + ".name");
					String nbtName = cf.getConfig().getString("blocks." + key + ".nbtname");
					String id = cf.getConfig().getString("blocks." + key + ".id");
					boolean dropItem = cf.getConfig().getBoolean("blocks." + key + ".dropitem");
					List<String> tools = cf.getConfig().getStringList("blocks." + key + ".tools");
					List<String> types = cf.getConfig().getStringList("blocks." + key + ".types");
					registerBlock(id, name, nbtName, dropItem, new ToolList(tools), new TypeList(types));
				} catch (Exception e) {
					getLogger().info("Could not load block " + key);
				}
			}
		}

		//load items
		if(cf.getConfig().contains("items")) {
			for (String key : cf.getConfig().getConfigurationSection("items").getKeys(false)) {
				try {
					String name = cf.getConfig().getString("items." + key + ".name");
					String nbtName = cf.getConfig().getString("items." + key + ".nbtname");
					String id = cf.getConfig().getString("items." + key + ".id");
					ItemType type = ItemType.valueOf(cf.getConfig().getString("items." + key + ".type").toUpperCase());
					ConfigurationSection enchantments = cf.getConfig().getConfigurationSection("items." + key + ".enchantments");
					ConfigurationSection properties = cf.getConfig().getConfigurationSection("items." + key + ".properties");
					ConfigurationSection attributes = cf.getConfig().getConfigurationSection("items." + key + ".attributes");
					EnchantList enchantList = new EnchantList();
					if(enchantments != null) {
						for (String enchantKey : enchantments.getKeys(false)) {
							enchantList.append(new Enchant(enchantments.getInt(enchantKey + ".level"), Enchantment.getByKey(NamespacedKey.minecraft(enchantments.getString(enchantKey + ".enchantment").replaceFirst("minecraft:", "")))));
						}
					}
					PropertyList propertyList = new PropertyList();
					if(properties != null) {
						for (String propertyKey : properties.getKeys(false)) {
							propertyList.append(new Property(properties.get(propertyKey + ".value"), properties.getString(propertyKey + ".name")));
						}
					}

					
					Attribute[] attributeList = null;
					if(attributes != null) {
						attributeList = new Attribute[attributes.getKeys(false).size()];
						int i = 0;
						for (String attributeKey : attributes.getKeys(false)) {
							switch(attributes.getString(attributeKey + ".type")) {
								case "armor":
									attributeList[i] = new ArmorAttribute(attributes.getString(attributeKey + ".slot"), attributes.getInt(attributeKey + ".operation"), (int) attributes.getDouble(attributeKey + ".amount"));
									break;
								case "armortoughness":
									attributeList[i] = new ArmorToughnessAttribute(attributes.getString(attributeKey + ".slot"), attributes.getInt(attributeKey + ".operation"), (int) attributes.getDouble(attributeKey + ".amount"));
									break;
								case "attackdamage":
									attributeList[i] = new AttackDamageAttribute(attributes.getString(attributeKey + ".slot"), attributes.getInt(attributeKey + ".operation"), (int) attributes.getDouble(attributeKey + ".amount"));
									break;
								case "attackknockback":
									attributeList[i] = new AttackKnockbackAttribute(attributes.getString(attributeKey + ".slot"), attributes.getInt(attributeKey + ".operation"), attributes.getDouble(attributeKey + ".amount"));
									break;
								case "attackspeed":
									attributeList[i] = new AttackSpeedAttribute(attributes.getString(attributeKey + ".slot"), attributes.getInt(attributeKey + ".operation"), (int) attributes.getDouble(attributeKey + ".amount"));
									break;
								case "followrange":
									attributeList[i] = new FollowRangeAttribute(attributes.getString(attributeKey + ".slot"), attributes.getInt(attributeKey + ".operation"), (int) attributes.getDouble(attributeKey + ".amount"));
									break;
								case "knockbackresistance":
									attributeList[i] = new KnockbackResistanceAttribute(attributes.getString(attributeKey + ".slot"), attributes.getInt(attributeKey + ".operation"), attributes.getDouble(attributeKey + ".amount"));
									break;
								case "luck":
									attributeList[i] = new LuckAttribute(attributes.getString(attributeKey + ".slot"), attributes.getInt(attributeKey + ".operation"), (int) attributes.getDouble(attributeKey + ".amount"));
									break;
								case "maxhealth":
									attributeList[i] = new MaxHealthAttribute(attributes.getString(attributeKey + ".slot"), attributes.getInt(attributeKey + ".operation"), attributes.getDouble(attributeKey + ".amount"));
									break;
								case "movementspeed":
									attributeList[i] = new MovementSpeedAttribute(attributes.getString(attributeKey + ".slot"), attributes.getInt(attributeKey + ".operation"), attributes.getDouble(attributeKey + ".amount"));
									break;
								default:
									break;
							}
							i++;
						}
					}
					if(attributeList == null) {
						registerItem(id, name, nbtName, type, enchantList, propertyList);
					} else {
						registerItem(id, name, nbtName, type, enchantList, propertyList, attributeList);
					}
				} catch (Exception e) {
					e.printStackTrace();
					getLogger().info("Could not load item " + key);
				}
			}
		}
		
		//add block drops
		if(cf.getConfig().contains("blocks")) {
			for (String key : cf.getConfig().getConfigurationSection("blocks").getKeys(false)) {
				CustomBlock cBlock = getCustomBlock(cf.getConfig().getString("blocks." + key + ".name"));
				for (String string : cf.getConfig().getConfigurationSection("blocks." + key + ".drops").getKeys(false)) {
					ConfigurationSection drop = cf.getConfig().getConfigurationSection("blocks." + key + ".drops." + string);
					try {
						if(drop.getString("id").startsWith("minecraft:")) {
							List<ItemStack> drops = cBlock.getDrops();
							drops.add(new ItemStack(Material.matchMaterial(drop.getString("id")), drop.getInt("amount")));
							cBlock.setDrops(drops);
						} else if(drop.getString("id").startsWith("customblock:")) {
							List<ItemStack> drops = cBlock.getDrops();
							drops.add(getCustomBlock(drop.getString("id").replaceFirst("customblock:", "")).getItemStack(drop.getInt("amount")));
							cBlock.setDrops(drops);
						} else if(drop.getString("id").startsWith("customitem:")) {
							List<ItemStack> drops = cBlock.getDrops();
							drops.add(getCustomItem(drop.getString("id").replaceFirst("customitem:", "")).getItemStack(drop.getInt("amount")));
							cBlock.setDrops(drops);
						}
					} catch (Exception e) {
						getLogger().info("Could not load all drops of " + cBlock.getName());
					}
				}
			}
		}
		
		//add crafting
		if(cf.getConfig().contains("crafting")) {
			ConfigurationSection crafting = cf.getConfig().getConfigurationSection("crafting");
			for (String key : crafting.getKeys(false)) {
				List<String> recipe = crafting.getStringList(key + ".recipe");
				ItemStack result = null;
				if(cf.getConfig().getString("crafting." + key + ".result.item").startsWith("minecraft:")) {
					result = new ItemStack(Material.matchMaterial(crafting.getString(key + ".result.item").replaceFirst("minecraft:", "")), crafting.getInt(key + ".result.amount"));
				} else if(cf.getConfig().getString("crafting." + key + ".result.item").startsWith("customblock:")) {
					result = getCustomBlock(crafting.getString(key + ".result.item").replaceFirst("customblock:", "")).getItemStack(crafting.getInt(key + ".result.amount"));
				} else if(cf.getConfig().getString("crafting." + key + ".result.item").startsWith("customitem:")) {
					result = getCustomItem(crafting.getString(key + ".result.item").replaceFirst("customitem:", "")).getItemStack(crafting.getInt(key + ".result.amount"));
				}
				String[] shape = new String[3];
				shape[0] = "";
				shape[1] = "";
				shape[2] = "";
				for (int i = 0; i < 3; i++) {
					shape[0] += recipe.get(i);
				}
				for (int i = 3; i < 6; i++) {
					shape[1] += recipe.get(i);
				}
				for (int i = 6; i < 9; i++) {
					shape[2] += recipe.get(i);
				}
				ShapedRecipe shapedrecipe = new ShapedRecipe(new NamespacedKey(this, crafting.getString(key + ".name")), result);
				shapedrecipe.shape(shape);
				for (String string : recipe) {
					if(string.contains(")")) {
						shapedrecipe.setIngredient(')', Material.AIR);
					}
				}
				HashMap<Character, ItemStack> ingredients = new HashMap<>();
				int i = 1;
				for (String ingredient : crafting.getStringList(key + ".ingredients")) {
					ItemStack ingredientItem = null;
					if(ingredient.startsWith("minecraft:")) {
						ingredientItem = new ItemStack(Material.matchMaterial(ingredient.replaceFirst("minecraft:", "")), 1);
					} else if(ingredient.startsWith("customblock:")) {
						ingredientItem = getCustomBlock(ingredient.replaceFirst("customblock:", "")).getItemStack(1);
					} else if(ingredient.startsWith("customitem:")) {
						ingredientItem = getCustomItem(ingredient.replaceFirst("customitem:", "")).getItemStack(1);
					}
					switch (i) {
						case 1:
							ingredients.put('!', ingredientItem);
							shapedrecipe.setIngredient('!', ingredientItem.getType());
							break;
						case 2:
							ingredients.put('@', ingredientItem);
							shapedrecipe.setIngredient('@', ingredientItem.getType());
							break;
						case 3:
							ingredients.put('#', ingredientItem);
							shapedrecipe.setIngredient('#', ingredientItem.getType());
							break;
						case 4:
							ingredients.put('$', ingredientItem);
							shapedrecipe.setIngredient('$', ingredientItem.getType());
							break;
						case 5:
							ingredients.put('%', ingredientItem);
							shapedrecipe.setIngredient('%', ingredientItem.getType());
							break;
						case 6:
							ingredients.put('^', ingredientItem);
							shapedrecipe.setIngredient('^', ingredientItem.getType());
							break;
						case 7:
							ingredients.put('&', ingredientItem);
							shapedrecipe.setIngredient('&', ingredientItem.getType());
							break;
						case 8:
							ingredients.put('*', ingredientItem);
							shapedrecipe.setIngredient('*', ingredientItem.getType());
							break;
						case 9:
							ingredients.put('(', ingredientItem);
							shapedrecipe.setIngredient('(', ingredientItem.getType());
							break;
						default:
							break;
					}
					i++;
				}
				registerCraftRecipe(new CustomCraftRecipe(crafting.getString(key + ".name"), shapedrecipe, result, ingredients));
			}
		}
		
		//add furnacecrafting
		if(cf.getConfig().contains("furnace")) {
			for (String key : cf.getConfig().getConfigurationSection("furnace").getKeys(false)) {
				ConfigurationSection furnace = cf.getConfig().getConfigurationSection("furnace." + key);
				String name = furnace.getString(".name");
				int cookingTime = furnace.getInt("cookingtime");
				float xp = (float) furnace.getDouble("xp");
				String ingredient = furnace.getString(".ingredient.id");
				ItemStack ingredientItem = null;
				if(ingredient.startsWith("minecraft:")) {
					ingredientItem = new ItemStack(Material.matchMaterial(ingredient.replaceFirst("minecraft:", "")), furnace.getInt("ingredient.amount"));
				} else if(ingredient.startsWith("customblock:")) {
					ingredientItem = getCustomBlock(ingredient.replaceFirst("customblock:", "")).getItemStack(furnace.getInt("ingredient.amount"));
				} else if(ingredient.startsWith("customitem:")) {
					ingredientItem = getCustomItem(ingredient.replaceFirst("customitem:", "")).getItemStack(furnace.getInt("ingredient.amount"));
				}
				String result = furnace.getString("result.id");
				ItemStack resultItem = null;
				if(result.startsWith("minecraft:")) {
					resultItem = new ItemStack(Material.matchMaterial(ingredient.replaceFirst("minecraft:", "")), furnace.getInt("result.amount"));
				} else if(result.startsWith("customblock:")) {
					resultItem = getCustomBlock(result.replaceFirst("customblock:", "")).getItemStack(furnace.getInt("result.amount"));
				} else if(result.startsWith("customitem:")) {
					resultItem = getCustomItem(result.replaceFirst("customitem:", "")).getItemStack(furnace.getInt("result.amount"));
				}
				registerFurnaceRecipe(new CustomFurnaceRecipe(name, new FurnaceRecipe(new NamespacedKey(this, name), resultItem, ingredientItem.getType(), xp, cookingTime), resultItem, ingredientItem));
			}
		}
		
//		registerBlock("minecraft:stone", "Test", "Test", true, new ToolList(Tool.PICKAXE), new TypeList(Type.WOOD, Type.STONE));
//		
//		registerItem("minecraft:apple", "Testt", "Testt", ItemType.FOOD, new EnchantList(), new PropertyList(new Property(4, "foodlevel"), new Property(20f, "saturation")));
//		registerItem("minecraft:diamond_sword", "dia", "dia", ItemType.SWORD, new EnchantList(), new PropertyList());
//		
//		registerItem("minecraft:diamond_helmet", "dia_helmet", "Testa", ItemType.ARMOR, new EnchantList(new Enchant(10, Enchantment.OXYGEN)), new PropertyList(), new ArmorAttribute("head", 0, 4), new MaxHealthAttribute("head", 0, 6), new ArmorToughnessAttribute("head", 0, 4));
//		registerItem("diamond_chestplate", "dia_chestplate", "Testa", ItemType.ARMOR, new EnchantList(), new PropertyList());
//		registerItem("diamond_boots", "dia_boots", "Testa", ItemType.ARMOR, new EnchantList(), new PropertyList(), new Attribute("generic.movementSpeed", "generic.movementSpeed", "feet", 2, 5, 6, 0.4f));
//		registerItem("diamond_leggings", "dia_leggings", "Testa", ItemType.ARMOR, new EnchantList(), new PropertyList());
//				
//		//craft input special
//		HashMap<Character, ItemStack> input = new HashMap<>();
//		input.put('&', getCustomBlock("Test").getItem());
//		registerCraftRecipe(new CustomCraftRecipe("craftstone", new ShapedRecipe(new NamespacedKey(this, "craftstone"), new ItemStack(Material.STONE, 1)).shape("&&&", "&*&", "&&&").setIngredient('&', Material.STONE).setIngredient('*', Material.AIR), new ItemStack(Material.STONE, 1), input));
//	
//		//craft output special
//		HashMap<Character, ItemStack> input2 = new HashMap<>();
//		input2.put('&', new ItemStack(Material.STONE, 1));
//		input2.put('*', getCustomItem("Testt").getItem());
//		registerCraftRecipe(new CustomCraftRecipe("craftpurplestone", new ShapedRecipe(new NamespacedKey(this, "craftpurplestone"), getCustomBlock("Test").getItem()).shape("&&&", "&*&", "&&&").setIngredient('&', Material.STONE).setIngredient('*', Material.APPLE), getCustomBlock("Test").getItem(), input2));
//	
//		//furnace output special
//		registerFurnaceRecipe(new CustomFurnaceRecipe("craftpurpleapple", new FurnaceRecipe(new NamespacedKey(this, "craftpurpleapple"), getCustomItem("Testt").getItem(), Material.APPLE, 0, 20), getCustomItem("Testt").getItem(), new ItemStack(Material.APPLE, 1)));
//		
//		//furnace input special
//		registerFurnaceRecipe(new CustomFurnaceRecipe("craftapple", new FurnaceRecipe(new NamespacedKey(this, "craftapple"), new ItemStack(Material.APPLE, 1), Material.APPLE, 0, 20), new ItemStack(Material.APPLE, 1), getCustomItem("Testt").getItem()));
//				
	}
	
	public CustomCraftRecipe registerCraftRecipe(CustomCraftRecipe r) {
		cm.addCraftRecipe(r);
		return r;
	}
	
	public CustomFurnaceRecipe registerFurnaceRecipe(CustomFurnaceRecipe r) {
		cm.addFurnaceRecipe(r);
		return r;
	}

	public CustomBlock registerBlock(String id, String name, String nbtName, boolean dropItem, ToolList tools, TypeList types) {
		CustomBlock customBlock = new CustomBlock(id, name, nbtName, Material.matchMaterial(id), 0, new ArrayList<>(), dropItem, tools, types);
		bm.addBlock(customBlock);
		return customBlock;
	}

	public CustomItem registerItem(String id, String name, String nbtName, ItemType type, EnchantList enchantments, PropertyList properties, Attribute... attributes) {
		CustomItem customItem = new CustomItem(id, name, nbtName, im, Material.matchMaterial(id), type, enchantments, properties, attributes);
		im.addItem(customItem);
		return customItem;
	}

	public CustomBlock getCustomBlock(String name) {
		for (CustomBlock customBlock : bm.getBlocks()) {
			if (customBlock.getName().equals(name)) {
				return customBlock;
			}
		}
		return null;
	}

	public CustomItem getCustomItem(String name) {
		return im.getItem(name);
	}

	public CustomItem getCustomItem(ItemStack itemStack) {
		return im.getItem(itemStack);
	}

	public boolean isCustomItem(ItemStack itemStack) {
		for (CustomItem cItem : im.getItems()) {
			if(isSimilar(cItem.getItem(), itemStack)) {
				return true;
			}
		}
		return false;
	}
	
	public CustomFurnaceRecipe getCustomFurnaceRecipe(ItemStack input) {
		for (CustomFurnaceRecipe customFurnaceRecipe : cm.getFurnaceRecipes()) {
			if(customFurnaceRecipe.getInput().equals(input)) {
				return customFurnaceRecipe;
			}
		}
		return null;
	}
	
	public static boolean isSimilar(ItemStack a, ItemStack b) {
		if(a.equals(b)) {
			return true;
		}
		if(a.getType().equals(b.getType())) {
			net.minecraft.server.v1_14_R1.ItemStack aNms = CraftItemStack.asNMSCopy(a);
			net.minecraft.server.v1_14_R1.ItemStack bNms = CraftItemStack.asNMSCopy(b);
			if(aNms.hasTag() && bNms.hasTag()) {
				NBTTagCompound aCompound = aNms.getTag();
				NBTTagCompound bCompound = bNms.getTag();
				if(aCompound.hasKey("minecode") && bCompound.hasKey("minecode")) {
					if(aCompound.getCompound("minecode").getString("type").equals(bCompound.getCompound("minecode").getString("type"))) {
						if(aCompound.getCompound("minecode").hasKey("item") && bCompound.getCompound("minecode").hasKey("item")) {
							if(aCompound.getCompound("minecode").getString("item").equals(bCompound.getCompound("minecode").getString("item"))) {
								return true;
							}
						} else if(aCompound.getCompound("minecode").hasKey("block") && bCompound.getCompound("minecode").hasKey("block")) {
							if(aCompound.getCompound("minecode").getString("block").equals(bCompound.getCompound("minecode").getString("block"))) {
								return true;
							}
						}
					}
				}
			} else {
				int aAmount = a.getAmount();
				int bAmount = b.getAmount();
				a.setAmount(1);
				b.setAmount(1);
				if(a.equals(b)) {
					a.setAmount(aAmount);
					b.setAmount(bAmount);
					return true;
				} else {
					a.setAmount(aAmount);
					b.setAmount(bAmount);
					return false;
				}
			}
		}
		return false;
	}

	//Prevent events
	@EventHandler(priority = EventPriority.LOWEST)
	public void onCraftItem(PrepareItemCraftEvent e) {
		CraftingInventory ci = e.getInventory();
		boolean ret = false;
		for (CustomCraftRecipe customCraftRecipe : cm.getCraftingRecipes()) {
			if(e.getRecipe() != null) {
				boolean recipe = true;
				for (ItemStack itemStack : ci.getMatrix()) {
					if(itemStack == null) {
						continue;
					}
					boolean con = false;
					for (ItemStack itemStack2 : customCraftRecipe.getInput().values()) {
						if(isSimilar(itemStack, itemStack2)) {
							con = true;
						}
					}
					if(!con) {
						recipe = false;
					}
				}
				for (ItemStack itemStack : customCraftRecipe.getInput().values()) {
					boolean con = false;
					for (ItemStack itemStack2 : ci.getMatrix()) {
						if(itemStack2 == null) {
							con = true;
							continue;
						}
						if(isSimilar(itemStack, itemStack2)) {
							con = true;
						}
					}
					if(con) {
						continue;
					}
					recipe = false;
				}
				if(recipe) {
					if(e.getRecipe().getResult().equals(customCraftRecipe.getRecipe().getResult())) {
						e.getInventory().setResult(customCraftRecipe.getOutput());
						ret = true;
						break;
					}
				} else {
					if(e.getRecipe().getResult().equals(customCraftRecipe.getRecipe().getResult())) {
						e.getInventory().setResult(null);
						ret = true;
						break;
					}
				}
			}
		}
		if(ret) {
			return;
		}
		for (CustomBlock cBlock : bm.getBlocks()) {
			for (ItemStack itemStack : ci.getContents()) {
				if (itemStack.equals(cBlock.getItem())) {
					e.getInventory().setResult(null);
				}
			}
		}
		for (CustomItem cItem : im.getItems()) {
			for (ItemStack itemStack : ci.getContents()) {
				if (itemStack.equals(cItem.getItem())) {
					e.getInventory().setResult(null);
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onFurnaceItem(FurnaceBurnEvent e) {
		for (CustomBlock cBlock : bm.getBlocks()) {
			Furnace furnace = (Furnace) e.getBlock().getState();
			ItemStack smelting = furnace.getInventory().getSmelting();
			int amount = smelting.getAmount();
			smelting.setAmount(1);
			if (smelting.equals(cBlock.getItem())) {
				if(getCustomFurnaceRecipe(smelting) == null) {
					e.setCancelled(true);
				}
			}
			smelting.setAmount(amount);
		}
		for (CustomItem cItem : im.getItems()) {
			Furnace furnace = (Furnace) e.getBlock().getState();
			ItemStack smelting = furnace.getInventory().getSmelting();
			int amount = smelting.getAmount();
			smelting.setAmount(1);
			if (smelting.equals(cItem.getItem())) {
				if(getCustomFurnaceRecipe(smelting) == null) {
					e.setCancelled(true);
				}
			}
			smelting.setAmount(amount);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onFurnaceItem(FurnaceSmeltEvent e) {
		Furnace furnace = (Furnace) e.getBlock().getState();
		ItemStack smelting = furnace.getInventory().getSmelting();
		int amount = smelting.getAmount();
		for (CustomFurnaceRecipe customFurnaceRecipe : cm.getFurnaceRecipes()) {
			smelting.setAmount(1);
			if(smelting.equals(customFurnaceRecipe.getInput())) {
				smelting.setAmount(amount);
				e.setResult(customFurnaceRecipe.getOutput());
				return;
			}
		}
		for (CustomBlock cBlock : bm.getBlocks()) {
			smelting.setAmount(1);
			if (smelting.equals(cBlock.getItem())) {
				smelting.setAmount(amount);
				e.setCancelled(true);
			}
		}
		for (CustomItem cItem : im.getItems()) {
			smelting.setAmount(1);
			if (smelting.equals(cItem.getItem())) {
				smelting.setAmount(amount);
				e.setCancelled(true);
			}
		}
		smelting.setAmount(amount);
	}
	
	@EventHandler
	public void onEat(PlayerItemConsumeEvent e) {
		for (CustomItem customItem : im.getItems()) {
			if(isSimilar(customItem.getItem(), e.getItem())) {
				if(customItem.getType().equals(ItemType.FOOD)) {
					e.getPlayer().setSaturation((float) ((double)customItem.getPropertiesList().getVariable("saturation")));
					e.getPlayer().setFoodLevel(e.getPlayer().getFoodLevel() + (int) customItem.getPropertiesList().getVariable("foodlevel"));
				}
			}
		}
	}

//	@EventHandler
//	public void inventoryClickEvent(InventoryClickEvent event) {
//		if (!(event.getInventory() instanceof AnvilInventory)) {
//			return;
//		}
//		if (event.getSlotType() != SlotType.RESULT) {
//			return;
//		}
//		AnvilInventory ai = (AnvilInventory) event.getInventory();
//		for (CustomBlock cBlock : bm.getBlocks()) {
//			if (event.getCurrentItem().getType().equals(cBlock.getType())) {
//				if (event.getCurrentItem().getItemMeta().getDisplayName().equals(cBlock.getName())) {
//					event.setCancelled(true);
//				}
//				if(ai.getItem(0).getItemMeta().getDisplayName().equals(cBlock.getName())) {
//					event.setCancelled(true);
//				}
//			}
//		}
//		for (CustomItem cItem : im.getItems()) {
//			if (event.getCurrentItem().getType().equals(cItem.getMaterialType())) {
//				if (event.getCurrentItem().getItemMeta().getDisplayName().equals(cItem.getName())) {
//					event.setCancelled(true);
//				}
//				if(ai.getItem(0).getItemMeta().getDisplayName().equals(cItem.getName())) {
//					event.setCancelled(true);
//				}
//			}
//		}
//	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onCutItem(InventoryClickEvent e) {
		if(e.getInventory() instanceof StonecutterInventory || e.getInventory() instanceof LoomInventory || e.getInventory() instanceof GrindstoneInventory ||  e.getInventory() instanceof BrewerInventory || e.getInventory() instanceof CartographyInventory) {
			try {
				for (CustomBlock cBlock : bm.getBlocks()) {
					ItemStack cursor = e.getCursor();
					ItemStack slot = e.getCurrentItem();
					ItemStack item = cBlock.getItem();
					if(isSimilar(slot, item)) {
						e.setCancelled(true);
					}
					if(isSimilar(cursor, item)) {
						e.setCancelled(true);
					}
				}
				for (CustomItem cItem : im.getItems()) {
					ItemStack cursor = e.getCursor();
					ItemStack slot = e.getCurrentItem();
					ItemStack item = cItem.getItem();
					if(isSimilar(slot, item)) {
						e.setCancelled(true);
					}
					if(isSimilar(cursor, item)) {
						e.setCancelled(true);
					}
				}
			} catch (Exception e2) {
			}
		}
	}

	//commands
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (label.equalsIgnoreCase("customblock")) {
			if (sender instanceof Player) {
				Player p = (Player) sender;
				if (args.length < 1) {
					if(p.hasPermission("minecode.block.set")) {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&',
								"&f/customblock set <customblock> (<x> <y> <z>)"));
					}
					if(p.hasPermission("minecode.block.list")) {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f/customblock list"));
					}
					if(p.hasPermission("minecode.block.give")) {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&',
								"&f/customblock give <customblock> (<player>)"));
					}
					return true;
				} else if (args[0].equalsIgnoreCase("list")) {
					if(p.hasPermission("minecode.block.list")) {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&fAll customblocks: "));
						for (CustomBlock cBlock : bm.getBlocks()) {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&',
									"&f" + cBlock.getName() + ", id: " + cBlock.getId()));
						}
						return true;
					} else {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou don't have the permission to execute this command"));
						return true;
					}
				} else if (args[0].equalsIgnoreCase("set")) {
					if(p.hasPermission("minecode.block.set")) {
						if (args.length == 2) {
							if (getCustomBlock(args[1]) != null) {
								setCustomBlock(p.getLocation(), getCustomBlock(args[1]));
								return true;
							} else {
								p.sendMessage(ChatColor.translateAlternateColorCodes('&',
										"&cThe customblock &9" + args[1] + "&f doesn't exsists!"));
								return true;
							}
						} else if (args.length >= 5) {
							if (getCustomBlock(args[1]) != null) {
								int x, y, z;
								try {
									x = Integer.parseInt(args[2]);
								} catch (Exception e) {
									p.sendMessage(ChatColor.translateAlternateColorCodes('&',
											"&cThe value &9" + args[2] + "&f isn't a number!"));
									return true;
								}
								try {
									y = Integer.parseInt(args[3]);
								} catch (Exception e) {
									p.sendMessage(ChatColor.translateAlternateColorCodes('&',
											"&cThe value &9" + args[3] + "&f isn't a number!"));
									return true;
								}
								try {
									z = Integer.parseInt(args[4]);
								} catch (Exception e) {
									p.sendMessage(ChatColor.translateAlternateColorCodes('&',
											"&cThe value &9" + args[4] + "&f isn't a number!"));
									return true;
								}
								Location loc = new Location(p.getWorld(), x, y, z);
								setCustomBlock(loc, getCustomBlock(args[1]));
								return true;
							} else {
								p.sendMessage(ChatColor.translateAlternateColorCodes('&',
										"&cThe customblock &9" + args[1] + "&f doesn't exsists!"));
								return true;
							}
						}
					} else {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&',
											"&cYou don't have the permission to execute this command"));
						return true;
					}
				} else if (args[0].equalsIgnoreCase("give")) {
					if(p.hasPermission("minecode.block.give")) {
						if (args.length == 2) {
							if (getCustomBlock(args[1]) != null) {
								p.getInventory().addItem(getCustomBlock(args[1]).getItem());
								p.sendMessage(ChatColor.translateAlternateColorCodes('&',
										"&fYou've given your self a &9" + args[1] + "&f block"));
								return true;
							} else {
								p.sendMessage(ChatColor.translateAlternateColorCodes('&',
										"&cThe customblock &9" + args[1] + "&f doesn't exsists!"));
								return true;
							}
						} else if (args.length >= 3) {
							if (getCustomBlock(args[1]) != null) {
								if (Bukkit.getPlayer(args[2]) != null) {
									Bukkit.getPlayer(args[2]).getInventory().addItem(getCustomBlock(args[1]).getItem());
									p.sendMessage(ChatColor.translateAlternateColorCodes('&',
											"&fYou've given the player &9" + args[2] + " &fa &9" + args[1] + "&f block"));
									Bukkit.getPlayer(args[2]).sendMessage(
											ChatColor.translateAlternateColorCodes('&', "&fYou've recived the block &9"
													+ args[1] + "&f from the player &9" + p.getName()));
									return true;
								} else {
									p.sendMessage(ChatColor.translateAlternateColorCodes('&',
											"&cThe player &9" + args[2] + "&f isn't online!"));
									return true;
								}
							} else {
								p.sendMessage(ChatColor.translateAlternateColorCodes('&',
										"&cThe customblock &9" + args[1] + "&f doesn't exsists!"));
								return true;
							}
						} else {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f/customblock give <customblock> (<player>)"));
							return true;
						}
					} else {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou don't have the permission to execute this command"));
						return true;
					}
				} else {
					if(p.hasPermission("minecode.block.set")) {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&',
								"&f/customblock set <customblock> (<x> <y> <z>)"));
					}
					if(p.hasPermission("minecode.block.list")) {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f/customblock list"));
					}
					if(p.hasPermission("minecode.block.give")) {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&',
								"&f/customblock give <customblock> (<player>)"));
					}
					return true;
				}
				return true;
			} else {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cJe moet een speler zijn"));
				return true;
			}
		} else if (label.equalsIgnoreCase("customitem")) {
			if (sender instanceof Player) {
				Player p = (Player) sender;
				if (args.length < 1) {
					if(p.hasPermission("minecode.item.list")) {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f/customitem list"));
					}
					if(p.hasPermission("minecode.item.give")) {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f/customitem give <customitem> (<player>)"));
					}
					return true;
				} else if (args[0].equalsIgnoreCase("list")) {
					if(p.hasPermission("minecode.item.list")) {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&fAll customitems: "));
						for (CustomItem cItem : im.getItems()) {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&',
									"&f" + cItem.getName() + ", id: " + cItem.getId()));
						}
						return true;
					} else {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou don't have the permission to execute this command"));
						return true;
					}
				} else if (args[0].equalsIgnoreCase("give")) {
					if(p.hasPermission("minecode.item.give")) {
						if (args.length == 2) {
							if (getCustomItem(args[1]) != null) {
								p.getInventory().addItem(getCustomItem(args[1]).getItem());
								p.sendMessage(ChatColor.translateAlternateColorCodes('&',
										"&fYou've given your self a &9" + args[1] + "&f item"));
								return true;
							} else {
								p.sendMessage(ChatColor.translateAlternateColorCodes('&',
										"&cThe customitem &9" + args[1] + "&f doesn't exsists!"));
								return true;
							}
						} else if (args.length >= 3) {
							if (getCustomItem(args[1]) != null) {
								if (Bukkit.getPlayer(args[2]) != null) {
									Bukkit.getPlayer(args[2]).getInventory().addItem(getCustomItem(args[1]).getItem());
									p.sendMessage(ChatColor.translateAlternateColorCodes('&',
											"&fYou've given the player &9" + args[2] + " &fa &9" + args[1] + "&f item"));
									Bukkit.getPlayer(args[2]).sendMessage(
											ChatColor.translateAlternateColorCodes('&', "&fYou've recived the item &9"
													+ args[1] + "&f from the player &9" + p.getName()));
									return true;
								} else {
									p.sendMessage(ChatColor.translateAlternateColorCodes('&',
											"&cThe player &9" + args[2] + "&f isn't online!"));
									return true;
								}
							} else {
								p.sendMessage(ChatColor.translateAlternateColorCodes('&',
										"&cThe customitem &9" + args[1] + "&f doesn't exsists!"));
								return true;
							}
						} else {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f/customitem give <customitem> (<player>)"));
							return true;
						}
					} else {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou don't have the permission to execute this command"));
						return true;
					}
				} else {
					if(p.hasPermission("minecode.item.list")) {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f/customitem list"));
					}
					if(p.hasPermission("minecode.item.give")) {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f/customitem give <customitem> (<player>)"));
					}
					return true;
				}
			} else {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cJe moet een speler zijn"));
				return true;
			}
		}
		return super.onCommand(sender, cmd, label, args);
	}

	//block events
	@EventHandler
	public void blockPlace(BlockPlaceEvent e) {
		net.minecraft.server.v1_14_R1.ItemStack itemNms = CraftItemStack.asNMSCopy(e.getItemInHand());
		if (itemNms.hasTag()) {
			if (itemNms.getTag().hasKey("minecode")) {
				if (itemNms.getTag().getCompound("minecode").hasKey("block")) {
					for (CustomBlock cBlock : bm.getBlocks()) {
						if (e.getItemInHand().getType().equals(cBlock.getType())) {
							if (itemNms.getTag().getCompound("minecode").getString("block").equals(cBlock.getNbtName())) {
								if(e.getItemInHand().hasItemMeta()) {
									if(e.getItemInHand().getItemMeta().hasDisplayName()) {
										setCustomBlock(e.getBlockPlaced(), cBlock, e.getItemInHand().getItemMeta().getDisplayName());
										return;
									}
								}
								setCustomBlock(e.getBlockPlaced(), cBlock);
							}
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void blockBreak(BlockBreakEvent e) {
		if (e.getBlock().getType().equals(Material.SPAWNER)) {
			Block block = e.getBlock();
			TileEntityMobSpawner tespawner = (TileEntityMobSpawner) ((CraftWorld) block.getWorld()).getHandle()
					.getTileEntity(new BlockPosition(block.getLocation().getBlockX(), block.getLocation().getBlockY(),
							block.getLocation().getBlockZ()));
			NBTTagCompound spawnerTag = tespawner.b();
			if (spawnerTag.hasKey("SpawnData")) {
				if (spawnerTag.getCompound("SpawnData").hasKey("id")) {
					if (spawnerTag.getCompound("SpawnData").getString("id").equals("minecraft:armor_stand")) {
						if (spawnerTag.getCompound("SpawnData").hasKey("ArmorItems")) {
							NBTTagList armorItems = spawnerTag.getCompound("SpawnData").getList("ArmorItems", 10);
							NBTTagCompound helmet = armorItems.getCompound(3);
							if (helmet != null) {
								if (helmet.hasKey("id") && helmet.hasKey("Count") && helmet.hasKey("tag")) {
									for (CustomBlock cBlock : bm.getBlocks()) {
										if (helmet.getString("id").equals(cBlock.getId()) && helmet.getInt("Count") == 1
												&& helmet.getCompound("tag").hasKey("minecode")) {
											if (helmet.getCompound("tag").getCompound("minecode").hasKey("block")) {
												if (helmet.getCompound("tag").getCompound("minecode").getString("block")
														.equals(cBlock.getNbtName())) {
													ItemStack main = e.getPlayer().getInventory().getItemInMainHand();
													 /*CustomItem cItem = getCustomItem(main);
													if(cItem != null) {
														 if(cItem.getType().equals(ItemType.AXE) || cItem.getType().equals(ItemType.PICKAXE) || cItem.getType().equals(ItemType.SWORD) || cItem.getType().equals(ItemType.HOE) || cItem.getType().equals(ItemType.SHOVEL)) {
															 String type = (String) cItem.getPropertiesList().getVariable("type");
															 Type mainType = null; 
															 switch (type) {
															 	case "diamond":
															 		mainType = Type.DIAMOND;
															 		break;
															 	case "gold":
															 		mainType = Type.GOLD;
															 		break;
															 	case "iron":
															 		mainType = Type.IRON;
															 		break;
															 	case "stone":
															 		mainType = Type.STONE;
															 		break;
															 	case "wooden":
															 		mainType = Type.WOOD;
															 		break;

															 	default:
															 		break;
															}
															 return;
														 }
													}*/
													Tool mainTool = null;
													Type mainType = null;
													switch (main.getType()) {
														case DIAMOND_SWORD:
															mainTool = Tool.SWORD;
															mainType = Type.DIAMOND;
															break;
														case IRON_SWORD:
															mainTool = Tool.SWORD;
															mainType = Type.IRON;
															break;
														case GOLDEN_SWORD:
															mainTool = Tool.SWORD;
															mainType = Type.GOLD;
															break;
														case STONE_SWORD:
															mainTool = Tool.SWORD;
															mainType = Type.STONE;
															break;
														case WOODEN_SWORD:
															mainTool = Tool.SWORD;
															mainType = Type.WOOD;
															break;

														case DIAMOND_AXE:
															mainTool = Tool.AXE;
															mainType = Type.DIAMOND;
															break;
														case IRON_AXE:
															mainTool = Tool.AXE;
															mainType = Type.IRON;
															break;
														case GOLDEN_AXE:
															mainTool = Tool.AXE;
															mainType = Type.GOLD;
															break;
														case STONE_AXE:
															mainTool = Tool.AXE;
															mainType = Type.STONE;
															break;
														case WOODEN_AXE:
															mainTool = Tool.AXE;
															mainType = Type.WOOD;
															break;

														case DIAMOND_HOE:
															mainTool = Tool.HOE;
															mainType = Type.DIAMOND;
															break;
														case IRON_HOE:
															mainTool = Tool.HOE;
															mainType = Type.IRON;
															break;
														case GOLDEN_HOE:
															mainTool = Tool.HOE;
															mainType = Type.GOLD;
															break;
														case STONE_HOE:
															mainTool = Tool.HOE;
															mainType = Type.STONE;
															break;
														case WOODEN_HOE:
															mainTool = Tool.HOE;
															mainType = Type.WOOD;
															break;
															
														case DIAMOND_SHOVEL:
															mainTool = Tool.SHOVEL;
															mainType = Type.DIAMOND;
															break;
														case IRON_SHOVEL:
															mainTool = Tool.SHOVEL;
															mainType = Type.IRON;
															break;
														case GOLDEN_SHOVEL:
															mainTool = Tool.SHOVEL;
															mainType = Type.GOLD;
															break;
														case STONE_SHOVEL:
															mainTool = Tool.SHOVEL;
															mainType = Type.STONE;
															break;
														case WOODEN_SHOVEL:
															mainTool = Tool.SHOVEL;
															mainType = Type.WOOD;
															break;

														case DIAMOND_PICKAXE:
															mainTool = Tool.PICKAXE;
															mainType = Type.DIAMOND;
															break;
														case IRON_PICKAXE:
															mainTool = Tool.PICKAXE;
															mainType = Type.IRON;
															break;
														case GOLDEN_PICKAXE:
															mainTool = Tool.PICKAXE;
															mainType = Type.GOLD;
															break;
														case STONE_PICKAXE:
															mainTool = Tool.PICKAXE;
															mainType = Type.STONE;
															break;
														case WOODEN_PICKAXE:
															mainTool = Tool.PICKAXE;
															mainType = Type.WOOD;
															break;
	
														default:
															break;
													}
													if((cBlock.getTools().contains(mainTool) && cBlock.getTypes().contains(mainType)) || cBlock.getTools().contains(Tool.HAND)) {
														e.setDropItems(false);
														e.setExpToDrop(cBlock.getXpToDrop());
														if(cBlock.isDropItem()) {
															ItemStack item = cBlock.getItem();
															if(helmet.getCompound("tag").getCompound("minecode").hasKey("customname")) {
																ItemMeta im = item.getItemMeta();
																im.setDisplayName("\u00A7f" + helmet.getCompound("tag").getCompound("minecode").getString("customname"));
																item.setItemMeta(im);
															}
															e.getPlayer().getWorld()
															.dropItemNaturally(e.getBlock().getLocation(), item);
														}
														for (ItemStack item : cBlock.getDrops()) {
															e.getPlayer().getWorld()
																	.dropItemNaturally(e.getBlock().getLocation(), item);
														}
													} else {
														e.setDropItems(false);
														e.setExpToDrop(0);
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

	public void setCustomBlock(Location loc, CustomBlock cBlock) {
		setCustomBlock(loc.getBlock(), cBlock.getId(), cBlock, cBlock.getName());
	}

	public void setCustomBlock(Location loc, CustomBlock cBlock, String name) {
		setCustomBlock(loc.getBlock(), cBlock.getId(), cBlock, name);
	}

	public void setCustomBlock(Block block, CustomBlock cBlock) {
		setCustomBlock(block, cBlock.getId(), cBlock, cBlock.getName());
	}

	public void setCustomBlock(Block block, CustomBlock cBlock, String name) {
		setCustomBlock(block, cBlock.getId(), cBlock, name);
	}

	public void setCustomBlock(Block block, String id, CustomBlock customBlock, String name) {
		block.setType(Material.SPAWNER);
		TileEntityMobSpawner tespawner = (TileEntityMobSpawner) ((CraftWorld) block.getWorld()).getHandle()
				.getTileEntity(new BlockPosition(block.getLocation().getBlockX(), block.getLocation().getBlockY(),
						block.getLocation().getBlockZ()));
		NBTTagCompound spawnerTag = tespawner.b();
		NBTTagList armorList = new NBTTagList();
		NBTTagCompound helmet = new NBTTagCompound();
		NBTTagCompound chestplate = new NBTTagCompound();
		NBTTagCompound leggings = new NBTTagCompound();
		NBTTagCompound boots = new NBTTagCompound();

		NBTTagCompound tag = new NBTTagCompound();
		NBTTagCompound minecode = new NBTTagCompound();
		minecode.setString("type", customBlock.getType().toString());
	    minecode.setString("block", customBlock.getNbtName());
	    minecode.setString("customname", name);
		tag.set("minecode", minecode);
		helmet.set("tag", tag);
		helmet.setString("id", id);
		helmet.setShort("Count", (short) 1);

		armorList.add(boots);
		armorList.add(leggings);
		armorList.add(chestplate);
		armorList.add(helmet);

		NBTTagCompound spawnData = new NBTTagCompound();
		spawnData.set("ArmorItems", armorList);
		spawnData.setString("id", "minecraft:armor_stand");
		spawnData.setInt("Invisible", 1);
		spawnData.setInt("Marker", 1);
		spawnerTag.set("SpawnData", spawnData);
		spawnerTag.setShort("RequiredPlayerRange", (short) 0);

		tespawner.load(spawnerTag);
	}
}
