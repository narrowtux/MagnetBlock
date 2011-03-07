package com.narrowtux.MagnetBlock;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.event.player.PlayerItemEvent;
import org.bukkit.event.player.PlayerListener;

public class MagnetPlayerListener extends PlayerListener {
	@Override
	public void onPlayerItem(PlayerItemEvent event)
	{
		System.out.println("onPlayerItem");
		MagnetBlockPlayer player = MagnetBlockPlayer.getPlayerByName(event.getPlayer().getName());
		if(event.isBlock()&&player.getEditing()!=null)
		{
			Block b = event.getBlockClicked().getFace(event.getBlockFace());
			MagnetBlockStructure structure = player.getEditing();
			System.out.println(player.getRequestType());
			if(player.getRequestType().equals(RequestType.EditStructure)){
				structure.addBlock(MagnetBlockBlock.getBlock(new BlockPosition(b)));
				player.getPlayer().sendMessage(ChatColor.GREEN.toString()+b.getType().toString()+" added to Structure.");
			}
		} else if(event.isBlock()){
			System.out.println("is Block");
		}
	}
}
