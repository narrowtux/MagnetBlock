package com.narrowtux.MagnetBlock;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.block.Block;

public class MagnetBlockMagnet{
	private boolean powered;
	private Block block;
	static List<MagnetBlockMagnet> instances = new ArrayList<MagnetBlockMagnet>();
	static public MagnetBlock plugin = null;
	private MagnetBlockStructure structure = null;
	private MagnetBlockMagnet(Block block) {
		this.setBlock(block);
		block.setType(plugin.config.getMagnetBlockType());
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
	public static MagnetBlockMagnet getBlock(BlockPosition pos) {
		for(MagnetBlockMagnet b:instances)
		{
			if(b.getPosition().equals(pos)){
				return b;
			}
		}
		MagnetBlockMagnet b = new MagnetBlockMagnet(pos.getWorld().getBlockAt(pos.toLocation()));
		instances.add(b);
		return b;
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
	
	public BlockPosition getPosition(){
		return new BlockPosition(block);
	}
	/**
	 * @param structure the structure to set
	 */
	public void setStructure(MagnetBlockStructure structure) {
		this.structure = structure;
	}
	/**
	 * @return the structure
	 */
	public MagnetBlockStructure getStructure() {
		return structure;
	}
	
	public static void removeMagnet(MagnetBlockMagnet magnet)
	{
		magnet.getStructure().removeMagnet(magnet);
		instances.remove(magnet);
	}
	
	public static boolean exists(BlockPosition blockPosition) {
		for(MagnetBlockMagnet magnet:instances)
		{
			if(magnet.getPosition().equals(blockPosition))
				return true;
		}
		return false;
	}
}
