package com.narrowtux.MagnetBlock;

//import org.bukkit.Material;
import org.bukkit.World;
//import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;
//import org.bukkit.inventory.ItemStack;

public class MagnetPlayerListener extends PlayerListener {
	static public MagnetBlock plugin;
	@Override
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		Vector direction = null;
		switch(event.getAction()){
		case LEFT_CLICK_BLOCK:
			direction = new Vector(1,1,1);
			break;
		case RIGHT_CLICK_BLOCK:
			direction = new Vector(-1,-1,-1);
			break;
		}
		BlockPosition pos = null;
		if(event.hasBlock()){
			pos = new BlockPosition(event.getClickedBlock());
		}
		if(direction!=null){
			MagnetBlockBlock block = MagnetBlockBlock.getBlock(pos);
			if(block.getStructure()!=null){
				MagnetBlockStructure structure = block.getStructure();
				Player pl = event.getPlayer();
				MagnetBlockPlayer mplayer = MagnetBlockPlayer.getPlayerByName(pl.getName());
				if(!mplayer.hasRight())
					return;
				World w = event.getClickedBlock().getWorld();
				Vector block1 = event.getClickedBlock().getLocation().toVector();
				block1.add(new Vector(0.5,0.5,0.5));
				Vector player = pl.getLocation().toVector();
				player.add(new Vector(0,1,0));
				Vector move = block1.subtract(player);
				move = move.normalize();

				move.multiply(plugin.config.getVector(event.getMaterial()));
				move.multiply(direction);
				BlockPosition vec = new BlockPosition(w, (int)Math.round(move.getX()),(int)Math.round(move.getY()),(int)Math.round(move.getZ()));
				structure.moveBy(vec);
			}
		}
		if(event.getAction()==Action.LEFT_CLICK_BLOCK){

			if(MagnetBlockMagnet.exists(pos)){
				MagnetBlockPlayer player = MagnetBlockPlayer.getPlayerByName(event.getPlayer().getName());
				if(player.getRequestType().equals(RequestType.RemoveMagnet)){
					MagnetBlockMagnet magnet = MagnetBlockMagnet.getBlock(pos);
					MagnetBlockMagnet.removeMagnet(magnet);
					//Block b = magnet.getBlock();
					//Material type = b.getType();
					//b.setType(Material.AIR);
					//b.getWorld().dropItemNaturally(new BlockPosition(b).toLocation(), new ItemStack(type.getId()));
				}
			}
		}
	}

	@Override
	public void onPlayerMove(PlayerMoveEvent event){
		MagnetBlockPlayer player = MagnetBlockPlayer.getPlayerByName(event.getPlayer().getName());
		if(player.hasRight()){
			if(player.getFollowing()!=null){
				BlockPosition plpos = new BlockPosition(event.getTo()).substract(new BlockPosition(event.getTo().getWorld(),0,1,0));
				BlockPosition stpos = player.getFollowing().getOrigin();
				BlockPosition d = plpos.substract(stpos);
				Vector v = d.toLocation().toVector();
				if(Math.round(player.getPlayer().getLocation().getPitch())>80){
					plpos.setY(plpos.getY()-1);
				}
				if(v.lengthSquared()>3){
					player.getFollowing().setTarget(plpos);
				} else {
					player.getFollowing().moveTo(plpos);
				}
			}
		}
	}
} 
