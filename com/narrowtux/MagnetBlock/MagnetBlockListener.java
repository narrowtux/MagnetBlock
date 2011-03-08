package com.narrowtux.MagnetBlock;

import javax.swing.text.rtf.RTFEditorKit;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.BlockRightClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class MagnetBlockListener extends BlockListener {
	public MagnetBlockListener(){

	}
	@Override
	public void onBlockPlace(BlockPlaceEvent event)
	{
		if(event.isCancelled())
			return;
		if(event.getPlayer()!=null){
			MagnetBlockPlayer player = MagnetBlockPlayer.getPlayerByName(event.getPlayer().getName());
			MagnetBlockStructure structure = player.getEditing();
			if(structure!=null){
				System.out.println(player.getRequestType());
				if(player.getRequestType().equals(RequestType.EditStructure)){
					structure.addBlock(MagnetBlockBlock.getBlock(new BlockPosition(event.getBlock())));
					player.getPlayer().sendMessage(ChatColor.GREEN.toString()+event.getBlock().getType().toString()+" added to Structure.");
				} else if(player.getRequestType().equals(RequestType.AddMagnet)&&event.getBlock().getType().equals(Material.IRON_BLOCK)){
					structure.addMagnet(MagnetBlockMagnet.getBlock(new BlockPosition(event.getBlock())));
					player.getPlayer().sendMessage(ChatColor.GREEN+"Magnet added.");
					player.setRequestType(RequestType.None);
					player.setEditing(null);
				}
			}
		}
	}

	@Override
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
				structure.removeBlock(MagnetBlockBlock.getBlock(new BlockPosition(event.getBlock())));
				player.getPlayer().sendMessage(ChatColor.RED.toString()+event.getBlock().getType().toString()+" removed from Structure.");
				return;
			}
		}
		if(block.getStructure()!=null){
			event.setCancelled(true);
		}
	}

	@Override 
	public void onBlockDamage(BlockDamageEvent event){
		// TODO: Check if the block gets destroyed by tnt or a creeper. or even other plugins

		if(event.getDamageLevel().getLevel()!=0){
			//return;
		}
		BlockPosition pos = new BlockPosition(event.getBlock());
		MagnetBlockBlock block = MagnetBlockBlock.getBlock(pos);
		if(block.getStructure()!=null){
			MagnetBlockStructure structure = block.getStructure();
			Player pl = event.getPlayer();
			World w = event.getBlock().getWorld();
			Vector block1 = event.getBlock().getLocation().toVector();
			block1.add(new Vector(0.5,0.5,0.5));
			Vector player = pl.getLocation().toVector();
			player.add(new Vector(0,1,0));
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
		if(MagnetBlockMagnet.exists(pos)){
			MagnetBlockPlayer player = MagnetBlockPlayer.getPlayerByName(event.getPlayer().getName());
			if(player.getRequestType().equals(RequestType.RemoveMagnet)){
				MagnetBlockMagnet magnet = MagnetBlockMagnet.getBlock(pos);
				MagnetBlockMagnet.removeMagnet(magnet);
				Block b = magnet.getBlock();
				Material type = b.getType();
				b.setType(Material.AIR);
				b.getWorld().dropItemNaturally(new BlockPosition(b).toLocation(), new ItemStack(type.getId()));
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
			Player pl = event.getPlayer();
			World w = event.getBlock().getWorld();
			Vector block1 = event.getBlock().getLocation().toVector();
			block1.add(new Vector(0.5,0.5,0.5));
			Vector player = pl.getLocation().toVector();
			player.add(new Vector(0,1,0));
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

	@Override
	public void onBlockRedstoneChange(BlockRedstoneEvent event)
	{
		if(event.getNewCurrent()==0)
			return;
		Block b = event.getBlock();
		BlockFace[] faces = {BlockFace.NORTH, BlockFace.WEST, BlockFace.SOUTH, BlockFace.EAST};
		for(BlockFace face: faces){
			Block bf = b.getFace(face);
			if(bf.getType().equals(Material.IRON_BLOCK)){
				if(MagnetBlockMagnet.exists(new BlockPosition(bf))){
					MagnetBlockMagnet block = MagnetBlockMagnet.getBlock(new BlockPosition(bf));
					block.getStructure().setTarget(new BlockPosition(bf));
				} else {
					System.out.println("Magnet not found at "+bf.getLocation());
				}
			}
		}
	}
}
