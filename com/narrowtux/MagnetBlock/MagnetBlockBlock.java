package com.narrowtux.MagnetBlock;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;

public class MagnetBlockBlock {
	private Block block;
	static List<MagnetBlockBlock> instances = new ArrayList<MagnetBlockBlock>();
	static MagnetBlock plugin = null;
	private MagnetBlockStructure structure = null;
	private Material material;
	private int typeid;
	private List<String> signText = new ArrayList<String>();
	private World world  = null;
	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	public byte getData() {
		return data;
	}

	public void setData(byte data) {
		this.data = data;
	}

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
		this.world = block.getWorld();
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
		instances.add(b);
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
			if(!isEmptyBlock(block)){
				MagnetBlockBlock mblock = getBlock(new BlockPosition(block));
				block = mblock.getBlock();
				if(mblock.getStructure()!=null&&mblock.getStructure().equals(structure)){
					return true;
				} else {
					//plugin.log.log(Level.INFO, "Block collides with"+structure);
				}
				//plugin.log.log(Level.INFO, "Block collides with other block ("+block.getType().toString()+").");
				return false;
			}
		}
		return true;
	}

	public void moveTo(BlockPosition pos, int step){
		switch(step){
		case 0:
			typeid = world.getBlockTypeIdAt(block.getLocation());
			material = Material.getMaterial(typeid);
			data = block.getData();
			if(material.equals(Material.AIR)){
				plugin.log.log(Level.SEVERE, "Block has encountered AIR Type!");
				block = block.getWorld().getBlockAt(block.getLocation());
				material = Material.COBBLESTONE;
				data = 0;
			}
			if(material.toString().contains("SIGN")){
				Sign s = (Sign)block.getState();
				for(int i = 0; i<s.getLines().length;i++)
				{
					signText.add(s.getLine(i));
				}
			}
			break;
		case 1:
			block.setType(Material.AIR);
			if(block.getFace(BlockFace.DOWN).getType().equals(Material.WOOD_PLATE)){
				block.getFace(BlockFace.DOWN).setData((byte) 0);
			}
			break;
		case 2:
			block = pos.getWorld().getBlockAt(pos.toLocation());
			block.setTypeId(typeid);
			block.setData(data);
			if(material.toString().contains("SIGN")){
				Sign s = (Sign)block.getState();
				int i = 0;
				for(String line: signText){
					s.setLine(i, line);
					i++;
				}
				signText.clear();
			}
			break;
		}
	}
	
	public boolean isEmptyBlock(Block block){
		if(block.getType().equals(Material.AIR)){
			return true;
		}
		if(block.getType().equals(Material.STATIONARY_WATER)||block.getType().equals(Material.STATIONARY_LAVA)){
			return block.getData()>0;
		}
		return false;
	}
	
	public boolean isSolidBlock(){
		Material t = getBlock().getType();
		if(t.equals(Material.BED_BLOCK)||
				t.equals(Material.IRON_DOOR_BLOCK)||
				t.equals(Material.WOOD_DOOR)||
				t.equals(Material.WOOD_PLATE)||
				t.equals(Material.STONE_PLATE)||
				t.equals(Material.TORCH)||
				t.equals(Material.REDSTONE_WIRE)||
				t.equals(Material.REDSTONE_TORCH_OFF)||
				t.equals(Material.REDSTONE_TORCH_ON)||
				t.equals(Material.RED_MUSHROOM)||
				t.equals(Material.YELLOW_FLOWER)||
				t.equals(Material.LADDER)||
				t.equals(Material.SIGN)||
				t.equals(Material.WALL_SIGN)||
				t.equals(Material.SIGN_POST)||
				t.equals(Material.LEVER)||
				t.equals(Material.BROWN_MUSHROOM)||
				t.equals(Material.CACTUS)||
				t.equals(Material.CROPS)||
				t.equals(Material.DIODE_BLOCK_OFF)||
				t.equals(Material.DIODE_BLOCK_ON)||
				t.equals(Material.PORTAL)||
				t.equals(Material.RAILS)||
				t.equals(Material.SAPLING)||
				t.equals(Material.SNOW)||
				t.equals(Material.SUGAR_CANE_BLOCK)||
				t.equals(Material.WALL_SIGN)||
				t.equals(Material.RED_ROSE)
				){
			return false;
		}
		return true;
	}
}
