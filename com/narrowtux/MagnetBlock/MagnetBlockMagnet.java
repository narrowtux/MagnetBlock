package com.narrowtux.MagnetBlock;

import org.bukkit.block.Block;

public class MagnetBlockMagnet extends MagnetBlockBlock {
	private boolean powered;
	public MagnetBlockMagnet(Block block) {
		super(block);
	}
	/**
	 * @param powered the powered to set
	 */
	public void setPowered(boolean powered) {
		this.powered = powered;
	}
	/**
	 * @return the powered
	 */
	public boolean isPowered() {
		return powered;
	}

}
