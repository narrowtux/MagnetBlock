package com.narrowtux.MagnetBlock;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class BlockPosition{
	private int x, y, z;
	private World world;

	public BlockPosition(World world, int x, int y, int z){
		this.x = x;
		this.y = y;
		this.z = z;
		this.world = world;
	}
	
	public BlockPosition(BlockPosition other)
	{
		x = other.x;
		y = other.y;
		z = other.z;
		world = other.world;
	}
	
	public BlockPosition(MagnetBlockBlock magnetBlock)
	{
		Block block = magnetBlock.getBlock();
		x = block.getX();
		y = block.getY();
		z = block.getZ();
		world = block.getWorld();
	}
	
	public BlockPosition(Block block){
		x = block.getX();
		y = block.getY();
		z = block.getZ();
		world = block.getWorld();
	}
	
	public BlockPosition(Location loc)
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
	
	public BlockPosition multiply(BlockPosition other){
		BlockPosition result = new BlockPosition(world,0,0,0);
		result.x = x*other.x;
		result.y = y*other.y;
		result.z = z*other.z;
		return result;
	}
	
	public BlockPosition multiply(int x, int y, int z)
	{
		BlockPosition result = new BlockPosition(world,0,0,0);
		result.x = x*this.x;
		result.y = y*this.y;
		result.z = z*this.z;
		return result;
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
	
	public Material getType(){
		return Material.getMaterial(world.getBlockTypeIdAt(x, y, z));
	}
}
