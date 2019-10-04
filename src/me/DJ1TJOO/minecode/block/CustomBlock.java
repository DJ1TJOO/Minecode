package me.DJ1TJOO.minecode.block;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_14_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.DJ1TJOO.minecode.block.tool.Tool;
import me.DJ1TJOO.minecode.block.tool.ToolList;
import me.DJ1TJOO.minecode.block.type.Type;
import me.DJ1TJOO.minecode.block.type.TypeList;
import net.minecraft.server.v1_14_R1.NBTTagCompound;

public class CustomBlock {

	private String id, name, nbtName;
	private Material type;
	private int xpToDrop;
	private List<ItemStack> drops;
	private boolean dropItem;
	private ToolList tools;
	private TypeList types;
	
	public CustomBlock(String id, String name, String blockName, Material type, int xpToDrop, List<ItemStack> drops, boolean dropItem, ToolList tools, TypeList types) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.xpToDrop = xpToDrop;
		this.drops = drops;
		this.nbtName = blockName;
		this.dropItem = dropItem;
		this.tools = tools;
		this.types = types;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Material getType() {
		return type;
	}

	public void setType(Material type) {
		this.type = type;
	}

	public List<ItemStack> getDrops() {
		return drops;
	}

	public void setDrops(List<ItemStack> drops) {
		this.drops = drops;
	}

	public int getXpToDrop() {
		return xpToDrop;
	}
	
	public void setXpToDrop(int xpToDrop) {
		this.xpToDrop = xpToDrop;
	}
	
	public String getNbtName() {
		return nbtName;
	}

	public void setNbtName(String blockName) {
		this.nbtName = blockName;
	}

	public TypeList getTypes() {
		return types;
	}

	public void setTypes(Type[] types) {
		this.types = new TypeList(types);
	}

	public void setTypes(TypeList types) {
		this.types = types;
	}

	public ToolList getTools() {
		return tools;
	}

	public void setTools(Tool[] tools) {
		this.tools = new ToolList(tools);
	}

	public void setTools(ToolList tools) {
		this.tools = tools;
	}

	public boolean isDropItem() {
		return dropItem;
	}

	public void setDropItem(boolean dropItem) {
		this.dropItem = dropItem;
	}

	public ItemStack getItem() {
		return getItem(getName());
	}

	public ItemStack getItem(String name) {
		ItemStack item = new ItemStack(this.getType(), 1);
		ItemMeta im = item.getItemMeta();
		im.setDisplayName("\u00A7f" + name);
		im.setLocalizedName(getName());
		item.setItemMeta(im);
		
		net.minecraft.server.v1_14_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
		NBTTagCompound compound = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
        NBTTagCompound minecode = new NBTTagCompound();
        minecode.setString("type", getType().toString());
        minecode.setString("block", getNbtName());
        compound.set("minecode", minecode);
        nmsItem.setTag(compound);
        item = CraftItemStack.asBukkitCopy(nmsItem);
        
		return item;
	}
	
	public ItemStack getItemStack(int amount) {
		return getItemStack(getName(), amount);
	}

	public ItemStack getItemStack(String name, int amount) {
		ItemStack item = new ItemStack(this.getType(), amount);
		ItemMeta im = item.getItemMeta();
		im.setDisplayName("\u00A7f" + name);
		im.setLocalizedName(getName());
		item.setItemMeta(im);
		
		net.minecraft.server.v1_14_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
		NBTTagCompound compound = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
        NBTTagCompound minecode = new NBTTagCompound();
        minecode.setString("type", getType().toString());
        minecode.setString("block", getNbtName());
        compound.set("minecode", minecode);
        nmsItem.setTag(compound);
        item = CraftItemStack.asBukkitCopy(nmsItem);
        
		return item;
	}
	
}
