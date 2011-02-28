package com.narrowtux.MagnetBlock;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

public class BlockPosition{
	private int x, y, z;
	private World world;

	BlockPosition(World world, int x, int y, int z){
		this.x = x;
		this.y = y;
		this.z = z;
		this.world = world;
	}
	
	BlockPosition(BlockPosition other)
	{
		x = other.x;
		y = other.y;
		z = other.z;
		world = other.world;
	}
	
	BlockPosition(MagnetBlockBlock magnetBlock)
	{
		Block block = magnetBlock.getBlock();
		x = block.getX();
		y = block.getY();
		z = block.getZ();
		world = block.getWorld();
	}
	
	BlockPosition(Block block){
		x = block.getX();
		y = block.getY();
		z = block.getZ();
		world = block.getWorld();
	}
	
	BlockPosition(Location loc)
	{
		x = loc.getBlockX();
		y = loc.getBlockY();
		z = loc.getBlockZ();
		world = loc.getWorld();
	}
	
	public BlockPosition add(BlockPosition other){
		BlockPosition result = new BlockPosition(this);
		result.x+=other.x;
		result.y+=other.y;
		result.z+=other.z;
		return result;
	}
	
	public BlockPosition negative(){
		return new BlockPosition(world,-x,-y,-z);
	}
	
	public BlockPosition substract(BlockPosition other){
		return new BlockPosition(this.add(other.negative()));
	}
	
	/**
	 * @param x the x to set
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * @param z the z to set
	 */
	public void setZ(int z) {
		this.z = z;
	}

	/**
	 * @return the z
	 */
	public int getZ() {
		return z;
	}
	
	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}
	
	public Location toLocation(){
		return new Location(world, x, y, z);
	}
	
	public boolean equals(BlockPosition other){
		return (x==other.x&&y==other.y&&z==other.z&&world==other.world);
	}
	
	public BlockPosition rotated(){
		BlockPosition result = new BlockPosition(this);
		result.x = z;
		result.z = -x;
		return result;
	}
}
