package com.narrowtux.MagnetBlock;

import org.bukkit.block.Block;

public class MagnetBlockBlock {
	private Block block;
	
	public MagnetBlockBlock(Block block)
	{
		this.setBlock(block);
	}

	/**
	 * @param block the block to set
	 */
	public void setBlock(Block block) {
		this.block = block;
	}

	/**
	 * @return the block
	 */
	public Block getBlock() {
		return block;
	}
}
