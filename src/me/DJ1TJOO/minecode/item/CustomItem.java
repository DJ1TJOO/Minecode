package me.DJ1TJOO.minecode.item;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_14_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.DJ1TJOO.minecode.item.attribute.Attribute;
import me.DJ1TJOO.minecode.item.enchant.Enchant;
import me.DJ1TJOO.minecode.item.enchant.EnchantList;
import me.DJ1TJOO.minecode.item.property.Property;
import me.DJ1TJOO.minecode.item.property.PropertyList;
import net.minecraft.server.v1_14_R1.NBTTagCompound;
import net.minecraft.server.v1_14_R1.NBTTagList;

public class CustomItem {

	private String id, name, nbtName;
	private Material materialType;
	private ItemType type;
	private PropertyList properties;
	private Attribute[] attributes;
	private EnchantList enchants;
	private ItemManager im;
	
	public CustomItem(String id, String name, String itemName, ItemManager im, Material materialType, ItemType type, EnchantList enchants, PropertyList properties, Attribute... attributes) {
		this.id = id;
		this.name = name;
		this.materialType = materialType;
		this.nbtName = itemName;
		this.type = type;
		this.attributes = attributes;
		this.properties = properties;
		this.im = im;
		this.enchants = enchants;
		if(getType().equals(ItemType.FOOD)) {
			if (!getPropertiesList().contains("saturation")) {
				System.err.println("Can't load the item " + getName() + " missing variable!");
				im.removeItem(this);
				return;
			}
			if (!getPropertiesList().contains("foodlevel")) {
				System.err.println("Can't load the item " + getName() + " missing variable!");
				im.removeItem(this);
				return;
			}
		} /*
			 * else if(getType().equals(ItemType.AXE) || getType().equals(ItemType.PICKAXE)
			 * || getType().equals(ItemType.SWORD) || getType().equals(ItemType.HOE) ||
			 * getType().equals(ItemType.SHOVEL)) { if
			 * (!getPropertiesList().contains("type")) {
			 * System.err.println("Can't load the item " + getName() +
			 * " missing variable!"); im.removeItem(this); return; } }
			 */
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public Material getMaterialType() {
		return materialType;
	}

	public void setType(Material type) {
		this.materialType = type;
	}
	
	public ItemType getType() {
		return type;
	}

	public void setType(ItemType type) {
		this.type = type;
	}

	public Attribute[] getAttributes() {
		return attributes;
	}

	public void setAttributes(Attribute[] attributes) {
		this.attributes = attributes;
	}

	public String getNbtName() {
		return nbtName;
	}

	public void setNbtName(String itemName) {
		this.nbtName = itemName;
	}

	public Property[] getProperties() {
		return properties.getProperties();
	}

	public PropertyList getPropertiesList() {
		return properties;
	}

	public void setProperties(PropertyList properties) {
		this.properties = properties;
	}

	public void setProperties(Property... properties) {
		this.properties = new PropertyList(properties);
	}

	public ItemManager getIm() {
		return im;
	}

	public void setIm(ItemManager im) {
		this.im = im;
	}

	public EnchantList getEnchanments() {
		return enchants;
	}

	public void setEnchans(EnchantList enchants) {
		this.enchants = enchants;
	}

	public void setEnchans(Enchant ... enchant) {
		this.enchants = new EnchantList(enchant);
	}

	public ItemStack getItem() {
		return getItem(getName());
	}

	public ItemStack getItem(String name) {
		ItemStack item = new ItemStack(this.getMaterialType(), 1);
		ItemMeta im = item.getItemMeta();
		im.setDisplayName("\u00A7f" + name);
		im.setLocalizedName(getName());
		for (Enchant enchant : enchants.getEnchants()) {
			im.addEnchant(enchant.getEnchantment(), enchant.getLevel(), true);
		}
		item.setItemMeta(im);
		
		net.minecraft.server.v1_14_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
		NBTTagCompound compound = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
        NBTTagList modifiers = new NBTTagList();
                
        for (Attribute attribute : attributes) {
            modifiers.add(attribute.getCompound());
		}
        //Attribute attackSpeed = new Attribute("generic.attackSpeed", "generic.attackSpeed", "mainhand", 0, 894654, 2872, 0.01);
        
        compound.set("AttributeModifiers", modifiers);
        NBTTagCompound minecode = new NBTTagCompound();
        minecode.setString("type", getType().toString());
        minecode.setString("item", getNbtName());
        compound.set("minecode", minecode);
        nmsItem.setTag(compound);
        item = CraftItemStack.asBukkitCopy(nmsItem);
        
		return item;
	}
	
	public ItemStack getItemStack(int amount) {
		return getItemStack(getName(), amount);
	}

	public ItemStack getItemStack(String name, int amount) {
		ItemStack item = new ItemStack(this.getMaterialType(), amount);
		ItemMeta im = item.getItemMeta();
		im.setDisplayName("\u00A7f" + name);
		im.setLocalizedName(getName());
		for (Enchant enchant : enchants.getEnchants()) {
			im.addEnchant(enchant.getEnchantment(), enchant.getLevel(), true);
		}
		item.setItemMeta(im);
		
		net.minecraft.server.v1_14_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
		NBTTagCompound compound = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
        NBTTagList modifiers = new NBTTagList();
                
        for (Attribute attribute : attributes) {
            modifiers.add(attribute.getCompound());
		}
        //Attribute attackSpeed = new Attribute("generic.attackSpeed", "generic.attackSpeed", "mainhand", 0, 894654, 2872, 0.01);
        
        compound.set("AttributeModifiers", modifiers);
        NBTTagCompound minecode = new NBTTagCompound();
        minecode.setString("type", getType().toString());
        minecode.setString("item", getNbtName());
        compound.set("minecode", minecode);
        nmsItem.setTag(compound);
        item = CraftItemStack.asBukkitCopy(nmsItem);
        
		return item;
	}
	
}
