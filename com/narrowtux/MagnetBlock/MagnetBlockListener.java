package com.narrowtux.MagnetBlock;

import org.bukkit.ChatColor;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;

public class MagnetBlockListener extends BlockListener {
	public MagnetBlockListener(){
		
	}
	@Override
	public void onBlockPlace(BlockPlaceEvent event)
	{
		if(event.getPlayer()!=null){
			MagnetBlockPlayer player = MagnetBlockPlayer.getPlayerByName(event.getPlayer().getName());
			MagnetBlockStructure structure = player.getEditing();
			if(structure!=null){
				structure.addBlock(MagnetBlockBlock.getBlock(new BlockPosition(event.getBlock())));
				player.getPlayer().sendMessage(ChatColor.GREEN.toString()+event.getBlock().getType().toString()+" added to Structure.");
			}
		}
	}
	
	@Override
	public void onBlockBreak(BlockBreakEvent event)
	{
		if(event.isCancelled()){
			return;
		}
		if(event.getPlayer()!=null){
			MagnetBlockPlayer player = MagnetBlockPlayer.getPlayerByName(event.getPlayer().getName());
			MagnetBlockStructure structure = player.getEditing();
			if(structure!=null){
				structure.removeBlock(MagnetBlockBlock.getBlock(new BlockPosition(event.getBlock())));
				player.getPlayer().sendMessage(ChatColor.RED.toString()+event.getBlock().getType().toString()+" removed from Structure.");
				return;
			}
		}
		if(MagnetBlockBlock.getBlock(new BlockPosition(event.getBlock())).getStructure()!=null)
			event.setCancelled(true);
	}
}
