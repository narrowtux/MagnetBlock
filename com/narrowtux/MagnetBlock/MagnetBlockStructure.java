package com.narrowtux.MagnetBlock;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class MagnetBlockStructure {
	private List<MagnetBlockBlock> blocks = new ArrayList<MagnetBlockBlock>();
	private BlockPosition origin = null;
	private MagnetBlockPlayer editingPlayer = null;
	static MagnetBlock plugin = null;
	private StructureAnimation animation = null;
	private int animationId = 0;
	/**
	 * @return the blocks
	 */
	public List<MagnetBlockBlock> getBlocks() {
		return blocks;
	}

	public void addBlock(MagnetBlockBlock block)
	{
		blocks.add(block);
		if(blocks.size()==1){
			origin = new BlockPosition(block);
		}
		block.setStructure(this);
	}

	/**
	 * @param origin the origin to set
	 */
	public void setOrigin(BlockPosition origin) {
		this.origin = origin;
	}

	/**
	 * @return the origin
	 */
	public BlockPosition getOrigin() {
		return origin;
	}

	public void removeBlock(MagnetBlockBlock block){
		blocks.remove(block);
		block.setStructure(null);
	}

	/**
	 * @param editingPlayer the editingPlayer to set
	 */
	public void setEditingPlayer(MagnetBlockPlayer editingPlayer) {
		this.editingPlayer = editingPlayer;
	}

	/**
	 * @return the editingPlayer
	 */
	public MagnetBlockPlayer getEditingPlayer() {
		return editingPlayer;
	}

	public void moveBy(BlockPosition vector)
	{
		moveTo(vector.add(origin));
	}

	public void moveTo(BlockPosition position)
	{
		BlockPosition vector = position.substract(origin);
		for(MagnetBlockBlock block: blocks){
			if(!block.testMove(block.getPosition().add(vector))){
				return;
			}
		}

		for(MagnetBlockBlock block: blocks){
			block.moveTo(block.getPosition().add(vector), 0);
		}
		for(MagnetBlockBlock block: blocks){
			block.moveTo(block.getPosition().add(vector), 1);
		}
		/*
		for(Player player:getPassengers()){
			Vector oldpos = player.getLocation().toVector();
			Vector newpos = vector.toLocation().toVector().add(oldpos);
			float yaw, pitch;
			yaw = player.getLocation().getYaw();
			pitch = player.getLocation().getPitch();
			Location newloc = newpos.toLocation(player.getWorld());
			newloc.setYaw(yaw);
			newloc.setPitch(pitch);
			player.teleportTo(newloc);
		}
		*/

		origin = position;
		plugin.save();
	}

	public void rotate(BlockPosition origin){
		//Test rotation
		for(MagnetBlockBlock block:blocks)
		{
			BlockPosition pos = block.getPosition();
			BlockPosition diff = pos.substract(origin);
			if(!block.testMove(diff.rotated().add(origin))){
				return;
			}
		}
		for(MagnetBlockBlock block:blocks)
		{
			BlockPosition pos = block.getPosition();
			BlockPosition diff = pos.substract(origin);
			block.moveTo(diff.rotated().add(origin), 0);
		}
		for(MagnetBlockBlock block:blocks)
		{
			BlockPosition pos = block.getPosition();
			BlockPosition diff = pos.substract(origin);
			block.moveTo(diff.rotated().add(origin), 1);
		}
		plugin.save();
	}
	
	public List<Player> getPassengers(){
		List<Player> result = new ArrayList<Player>();
		for(LivingEntity entity:origin.getWorld().getLivingEntities()){
			if(entity instanceof Player){
				Player player = (Player)entity;
				Vector plvec = player.getLocation().toVector();
				Vector orvec = origin.toLocation().toVector();
				if(plvec.isInSphere(orvec, 5)){
					result.add(player);
				}
			}
		}
		return result;
	}
	
	public void endAnimation(){
		if(animation!=null)
		{
			plugin.getServer().getScheduler().cancelTask(animationId);
			animation = null;
		}
	}
	
	public void setTarget(BlockPosition pos){
		endAnimation();
		animation = new StructureAnimation(this, pos);
		animationId = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, animation, 0, 10);
	}
}
