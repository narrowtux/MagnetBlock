package com.narrowtux.MagnetBlock;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;

public class MagnetBlockListener implements Listener {
	public static MagnetBlock plugin;
	public MagnetBlockListener(MagnetBlock instance){
		plugin=instance;
	}
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event)
	{
		if(event.isCancelled())
			return;
		if(event.getPlayer()!=null){
			MagnetBlockPlayer player = MagnetBlockPlayer.getPlayerByName(event.getPlayer().getName());
			if(!player.hasRight())
				return;
			MagnetBlockStructure structure = player.getEditing();
			if(structure!=null){
				System.out.println(player.getRequestType());
				if(player.getRequestType().equals(RequestType.EditStructure)){
					structure.addBlock(MagnetBlockBlock.getBlock(new BlockPosition(event.getBlock())));
					player.getPlayer().sendMessage(ChatColor.GREEN.toString()+event.getBlock().getType().toString()+" added to Structure.");
				} else if(player.getRequestType().equals(RequestType.AddMagnet)&&event.getBlock().getType().equals(plugin.config.getMagnetBlockType())){
					structure.addMagnet(MagnetBlockMagnet.getBlock(new BlockPosition(event.getBlock())));
					player.getPlayer().sendMessage(ChatColor.GREEN+"Magnet added.");
					player.setRequestType(RequestType.None);
					player.setEditing(null);
				}
			}
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event)
	{
		MagnetBlockBlock block = MagnetBlockBlock.getBlock(new BlockPosition(event.getBlock()));
		if(event.isCancelled()){
			return;
		}
		if(event.getPlayer()!=null){
			MagnetBlockPlayer player = MagnetBlockPlayer.getPlayerByName(event.getPlayer().getName());
			
			MagnetBlockStructure structure = player.getEditing();
			if(structure!=null&&player.getRequestType().equals(RequestType.EditStructure)){
				if(!player.hasRight()){
					event.setCancelled(true);
					return;
				}
				structure.removeBlock(MagnetBlockBlock.getBlock(new BlockPosition(event.getBlock())));
				player.getPlayer().sendMessage(ChatColor.RED.toString()+event.getBlock().getType().toString()+" removed from Structure.");
				return;
			}
		}
		if(block.getStructure()!=null){
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onBlockRedstoneChange(BlockRedstoneEvent event)
	{
		if(event.getNewCurrent()==0)
			return;
		Block b = event.getBlock();
		BlockFace[] faces = {BlockFace.NORTH, BlockFace.WEST, BlockFace.SOUTH, BlockFace.EAST, BlockFace.DOWN, BlockFace.UP};
		for(BlockFace face: faces){
			Block bf = b.getRelative(face);
			if(bf.getType().equals(plugin.config.getMagnetBlockType())){
				if(MagnetBlockMagnet.exists(new BlockPosition(bf))){
					MagnetBlockMagnet block = MagnetBlockMagnet.getBlock(new BlockPosition(bf));
					block.getStructure().setTarget(new BlockPosition(bf));
				}
			}
		}
	}
}
