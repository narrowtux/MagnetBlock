package com.narrowtux.MagnetBlock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class MagnetBlockBlock {
	private Block block;
	static List<MagnetBlockBlock> instances = new ArrayList<MagnetBlockBlock>();
	static MagnetBlock plugin = null;
	private MagnetBlockStructure structure = null;
	private Material material;
	private byte data;
	protected MagnetBlockBlock(Block block)
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
	
	public static MagnetBlockBlock getBlock(BlockPosition pos){
		for(MagnetBlockBlock b:instances)
		{
			if(b.getPosition().equals(pos)){
				return b;
			}
		}
		MagnetBlockBlock b = new MagnetBlockBlock(pos.getWorld().getBlockAt(pos.toLocation()));
		return b;
	}
	public static boolean exists(BlockPosition pos){
		for(MagnetBlockBlock b:instances)
		{
			if(b.getPosition().equals(pos)){
				return true;
			}
		}
		return false;
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
	
	public BlockPosition getPosition(){
		return new BlockPosition(this);
	}
	
	public boolean testMove(BlockPosition pos)
	{ 
		World world = pos.getWorld();
		Block block = world.getBlockAt(pos.toLocation());
		if(block!=null){
			if(!block.getType().equals(Material.AIR)){
				MagnetBlockBlock mblock = getBlock(new BlockPosition(block));
				if(mblock.getStructure()==structure){
					return true;
				}
				plugin.log.log(Level.INFO, "Block collides with other block!");
				return false;
			}
		}
		return true;
	}

	public void moveTo(BlockPosition pos, int step){
		if(step==0){
			material = block.getType();
			data = block.getData();
			block.setType(Material.AIR);
		}
		if(step==1){
			block = pos.getWorld().getBlockAt(pos.toLocation());
			block.setType(material);
			block.setData(data);
		}
	}
}
