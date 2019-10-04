package me.DJ1TJOO.minecode.block;

import java.util.ArrayList;
import java.util.List;

public class BlockManager {
	
	List<CustomBlock> customBlocks;

	public BlockManager() {
		customBlocks = new ArrayList<CustomBlock>();
	}
	
	public CustomBlock getBlock(String name) {
		for (CustomBlock customBlock : customBlocks) {
			if(customBlock.getName().equals(name)) {
				return customBlock;
			}
		}
		return null;
	}
	
	public void addBlock(CustomBlock customBlock) {
		customBlocks.add(customBlock);
	}
	
	public void removeBlock(CustomBlock customBlock) {
		customBlocks.remove(customBlock);
	}
	
	public void removeBlock(String name) {
		for (CustomBlock customBlock : customBlocks) {
			if(customBlock.getName().equals(name)) {
				customBlocks.remove(customBlock);
			}
		}
	}

	public List<CustomBlock> getBlocks() {
		return customBlocks;
	}
	
}
