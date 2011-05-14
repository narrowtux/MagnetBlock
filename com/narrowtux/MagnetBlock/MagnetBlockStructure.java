package com.narrowtux.MagnetBlock;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class MagnetBlockStructure {
	private List<MagnetBlockBlock> blocks = new ArrayList<MagnetBlockBlock>();
	private List<MagnetBlockBlock> nonsolidBlocks = new ArrayList<MagnetBlockBlock>();	
	private List<MagnetBlockBlock> solidBlocks = new ArrayList<MagnetBlockBlock>();
	private BlockPosition origin = null;
	private MagnetBlockPlayer editingPlayer = null;
	static MagnetBlock plugin = null;
	private StructureAnimation animation = null;
	private int animationId = 0;
	private boolean ironset = false;
	private List<MagnetBlockMagnet> magnets = new ArrayList<MagnetBlockMagnet>();

	public List<MagnetBlockMagnet> getMagnets() {
		return magnets;
	}

	public void addMagnet(MagnetBlockMagnet magnet){
		magnets.add(magnet);
		magnet.setStructure(this);
		plugin.save();
	}

	public void removeMagnet(MagnetBlockMagnet magnet){
		magnets.remove(magnet);
		magnet.setStructure(null);
		plugin.save();
	}

	/**
	 * @return the blocks
	 */
	public List<MagnetBlockBlock> getBlocks() {
		return blocks;
	}

	public void addBlock(MagnetBlockBlock block)
	{
		if(blocks.contains(block)){
			return;
		}
		blocks.add(block);
		if(origin == null){
			origin = new BlockPosition(block);
		}
		if(block.getBlock().getType().equals(plugin.config.getMagnetBlockType())&&!ironset){
			origin = new BlockPosition(block);
			ironset = true;
		}
		block.setStructure(this);
		if(block.isSolidBlock()){
			solidBlocks.add(block);
		} else {
			nonsolidBlocks.add(block);
		}
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
		solidBlocks.remove(block);
		nonsolidBlocks.remove(block);
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
		if(position.equals(new BlockPosition(position.getWorld(),0,0,0))){
			return;
		}
		BlockPosition vector = position.substract(origin);
		List<Entity> passengers = getPassengers();
		Vector realMoved = new Vector(0,0,0);
		realMoved.add(realMove(vector.multiply(1, 0, 0)));
		realMoved.add(realMove(vector.multiply(0, 1, 0)));
		realMoved.add(realMove(vector.multiply(0, 0, 1)));

		for(Entity player:passengers){
			Vector oldpos = player.getLocation().toVector();
			Vector newpos = realMoved.clone().add(oldpos);
			float yaw, pitch;
			yaw = player.getLocation().getYaw();
			pitch = player.getLocation().getPitch();
			Location newloc = newpos.toLocation(player.getWorld());
			newloc.setYaw(yaw);
			newloc.setPitch(pitch);
			player.teleport(newloc);
		}
	}

	private Vector realMove(BlockPosition vector){
		if(vector.equals(new BlockPosition(vector.getWorld(), 0,0,0))){
			return new Vector(0,0,0);
		}
		for(MagnetBlockBlock block: blocks){
			if(!block.testMove(block.getPosition().add(vector))){
				return new Vector(0,0,0);
			}
		}
		for(MagnetBlockBlock block: blocks){
			block.moveTo(block.getPosition().add(vector), 0);
		}
		for(MagnetBlockBlock block: nonsolidBlocks){
			block.moveTo(block.getPosition().add(vector), 1);
		}
		for(MagnetBlockBlock block: solidBlocks){
			block.moveTo(block.getPosition().add(vector), 1);
		}
		for(MagnetBlockBlock block: solidBlocks){
			block.moveTo(block.getPosition().add(vector), 2);
		}
		for(MagnetBlockBlock block: nonsolidBlocks){
			block.moveTo(block.getPosition().add(vector), 2);
		}
		for(int i = 0;i<=2;i++){
			
		}
		origin = origin.add(vector);
		return vector.toLocation().toVector();
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
		for(int i = 0;i<=2;i++){
			for(MagnetBlockBlock block:blocks)
			{
				BlockPosition pos = block.getPosition();
				BlockPosition diff = pos.substract(origin);
				block.moveTo(diff.rotated().add(origin), i);
			}
		}
		plugin.save();
	}

	public List<Entity> getPassengers(){
		List<Entity> result = new ArrayList<Entity>();
		for(Entity entity:origin.getWorld().getEntities()){
			try{
				if(entity instanceof LivingEntity || entity instanceof Minecart){
					if(entity instanceof Player){
					Player player = (Player)entity;
						MagnetBlockPlayer mplayer = MagnetBlockPlayer.getPlayerByName(player.getName());
						if(mplayer.getFollowing()!=null){
							continue;
						}
					}
					BlockPosition pl = new BlockPosition(entity.getLocation());
					Block b = pl.getWorld().getBlockAt(pl.toLocation());
					Block down = b.getFace(BlockFace.DOWN);
					BlockPosition dp = new BlockPosition(down);
					MagnetBlockBlock dm = MagnetBlockBlock.getBlock(dp);
					MagnetBlockBlock mb = MagnetBlockBlock.getBlock(pl);
					if(dm.getStructure()!=null){
						if(dm.getStructure().equals(this))
						{
							result.add(entity);
							//System.out.println("Player is above block");
							continue;
						}
					}
					if(mb.getStructure()!=null){
						if(mb.getStructure().equals(this))
						{
							result.add(entity);
							//System.out.println("Player is in block");
							continue;
						}
					}
				}
			} catch(NullPointerException e){
				
			}
		}
		return result;
	}

	public void endAnimation(){
		if(animation!=null)
		{
			plugin.getServer().getScheduler().cancelTask(animationId);
			animation = null;
			//plugin.log.log(Level.INFO,"Animation finished");
		}
	}

	public void setTarget(BlockPosition pos){
		endAnimation();
		animation = new StructureAnimation(this, pos);
		animationId = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, animation, 0, 5);
	}
}
