package com.narrowtux.MagnetBlock;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class BlockAdder implements Runnable {
	private Block block;
	private MagnetBlockStructure structure;
	private MagnetBlockPlayer player;
	public BlockAdder(Block b, MagnetBlockStructure structure, MagnetBlockPlayer player){
		block = b;
		this.structure = structure;
		this.player = player;
	}
	
	@Override
	public void run() {
		if(block.getType().equals(Material.AIR))
			return;
		player.getPlayer().sendMessage(ChatColor.GREEN+block.getType().toString()+" added to structure.");
		structure.addBlock(MagnetBlockBlock.getBlock(new BlockPosition(block)));
	}
}
