package com.narrowtux.MagnetBlock;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRightClickEvent;
import org.bukkit.util.Vector;

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

	@Override 
	public void onBlockDamage(BlockDamageEvent event){
		if(event.getPlayer()==null)
		{
			return;
		}
		BlockPosition pos = new BlockPosition(event.getBlock());
		MagnetBlockBlock block = MagnetBlockBlock.getBlock(pos);
		if(block.getStructure()!=null){
			MagnetBlockStructure structure = block.getStructure();
			if(event.getDamageLevel().getLevel()==0){
				BlockPosition vector = null;
				BlockPosition testblock;
				Player pl = event.getPlayer();
				World w = event.getBlock().getWorld();
				Vector block1 = event.getBlock().getLocation().toVector();
				block1.add(new Vector(0.5,0.5,0.5));
				Vector player = pl.getLocation().toVector();
				Vector move = block1.subtract(player);
				move = move.normalize();
				if(event.getPlayer().getItemInHand().getType().equals(Material.BONE)){
					structure.rotate(new BlockPosition(event.getBlock()));
					return;
				}
				if(event.getPlayer().getItemInHand().getType().equals(Material.FEATHER)){
					move.multiply(new Vector(1,0,1));
				} else if(event.getPlayer().getItemInHand().getType().equals(Material.STICK)){
					move.multiply(new Vector(0,1,0));
				} else {
					move.multiply(new Vector(0,0,0));
				}
				BlockPosition vec = new BlockPosition(w, (int)Math.round(move.getX()),(int)Math.round(move.getY()),(int)Math.round(move.getZ()));
				structure.moveBy(vec);
			}
		}
	}

	@Override
	public void onBlockRightClick(BlockRightClickEvent event)
	{
		if(event.getPlayer()==null)
		{
			return;
		}
		BlockPosition pos = new BlockPosition(event.getBlock());
		MagnetBlockBlock block = MagnetBlockBlock.getBlock(pos);
		if(block.getStructure()!=null){
			MagnetBlockStructure structure = block.getStructure();
			BlockPosition vector = null;
			BlockPosition testblock;
			Player pl = event.getPlayer();
			World w = event.getBlock().getWorld();
			Vector block1 = event.getBlock().getLocation().toVector();
			block1.add(new Vector(0.5,0.5,0.5));
			Vector player = pl.getLocation().toVector();
			Vector move = block1.subtract(player);
			move = move.normalize();

			if(event.getPlayer().getItemInHand().getType().equals(Material.FEATHER)){
				move.multiply(new Vector(-1,0,-1));
			} else if(event.getPlayer().getItemInHand().getType().equals(Material.STICK)){
				move.multiply(new Vector(0,-1,0));
			} else {
				move.multiply(new Vector(0,0,0));
			}
			BlockPosition vec = new BlockPosition(w, (int)Math.round(move.getX()),(int)Math.round(move.getY()),(int)Math.round(move.getZ()));
			structure.moveBy(vec);
		}
	}
}
