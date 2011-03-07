package com.narrowtux.MagnetBlock;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
public class MagnetBlock extends JavaPlugin {
	public Logger log = null;
	private HashMap<String, MagnetBlockStructure> structures = new HashMap<String, MagnetBlockStructure>();
	private MagnetBlockListener blockListener = new MagnetBlockListener();
	private MagnetPlayerListener playerListener = new MagnetPlayerListener();
	public MagnetBlock(){
		MagnetBlockPlayer.plugin = this;
		MagnetBlockStructure.plugin = this;
		MagnetBlockBlock.plugin = this;
	}

	@Override
	public void onDisable() {

		PluginDescriptionFile pdf = getDescription();
		log.log(Level.INFO, pdf.getName()+" version "+pdf.getVersion()+" by "+pdf.getAuthors()+" has been disabled.");
	}

	@Override
	public void onEnable() {
		log = getServer().getLogger();
		load();
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Type.BLOCK_PLACED, blockListener, Priority.Normal, this);
		pm.registerEvent(Type.BLOCK_BREAK, blockListener, Priority.Normal, this);
		pm.registerEvent(Type.BLOCK_DAMAGED, blockListener, Priority.Normal, this);
		pm.registerEvent(Type.BLOCK_RIGHTCLICKED, blockListener, Priority.Normal, this);
		pm.registerEvent(Type.REDSTONE_CHANGE, blockListener, Priority.Normal, this);
		pm.registerEvent(Type.PLAYER_ITEM, playerListener, Priority.Normal, this);
		PluginDescriptionFile pdf = getDescription();
		log.log(Level.INFO, pdf.getName()+" version "+pdf.getVersion()+" by "+pdf.getAuthors()+" has been enabled.");
	}

	public boolean onCommand(CommandSender sender, Command cmd, String name, String[] args){
		MagnetBlockPlayer player = null;
		if(sender instanceof Player){
			player = MagnetBlockPlayer.getPlayerByName(((Player)sender).getName());
		}
		if(player==null){
			sender.sendMessage(ChatColor.RED.toString()+"You can use "+cmd.getName()+" ingame.");
			return false;
		}
		/*
		 * Command handling
		 */
		if(cmd.getName().equals("createstructure")){
			/*******************
			 * createstructure *
			 *******************/
			if(args.length==1){
				String name1 = args[0];
				MagnetBlockStructure structure = new MagnetBlockStructure();
				structures.put(name1, structure);
				player.setEditing(structure);
				player.getPlayer().sendMessage(ChatColor.GREEN+"Place your blocks now!");
				player.setRequestType(RequestType.EditStructure);
				return true;
			} else {
				return false;
			}
		} else if(cmd.getName().equals("editstructure")){
			/*******************
			 *  editstructure  *
			 *******************/
			if(args.length==1){
				if(structures.containsKey(args[0])){
					player.setEditing(structures.get(args[0]));
					sender.sendMessage(ChatColor.GREEN+"Edit your strucutre now!");
					player.setRequestType(RequestType.EditStructure);
				} else {
					sender.sendMessage(ChatColor.RED+"This Structure does not exist.");
				}
				return true;
			}
		} else if(cmd.getName().equals("removestructure")){
			/*******************
			 * removestructure *
			 *******************/
			if(args.length==1){
				if(structures.containsKey(args[0])){
					player.setEditing(null);
					player.setRequestType(RequestType.None);
					MagnetBlockStructure struct = structures.get(args[0]);
					for(MagnetBlockBlock b:struct.getBlocks()){
						b.setStructure(null);
					}
					for(MagnetBlockMagnet m:struct.getMagnets()){
						m.setStructure(null);
					}
					structures.remove(args[0]);
					sender.sendMessage(ChatColor.GREEN+"Structure deleted.");
					save();
					return true;
				} else {
					sender.sendMessage(ChatColor.RED+"This Structure does not exist.");
				}
			}
		} else if(cmd.getName().equals("finishstructure")){
			/*******************
			 * finishstructure *
			 *******************/
			if(player.getEditing()!=null){
				sender.sendMessage(ChatColor.GREEN+"Finished editing!");
				player.setEditing(null);
				player.setRequestType(RequestType.None);
				save();
			} else {
				player.getPlayer().sendMessage(ChatColor.RED+"You aren't editing any structures.");
			}
			return true;
		} else if(cmd.getName().equals("structurelist")){
			/*******************
			 *       LIST      *
			 *******************/
			sender.sendMessage(ChatColor.BLUE+"List of Structures");
			sender.sendMessage(ChatColor.BLUE+"==================");
			String list = "";
			for(String key: structures.keySet()){
				list+=key+" "+structures.get(key).getMagnets().size()+" magnets, ";
			}
			sender.sendMessage(list);
			return true;
		} else if(cmd.getName().equals("structuremove")){
			/*******************
			 *       MOVE      *
			 *******************/
			if(args.length==4){
				String name1 = args[0];
				int x = Integer.valueOf(args[1]);
				int y = Integer.valueOf(args[2]);
				int z = Integer.valueOf(args[3]);
				World world = player.getPlayer().getLocation().getWorld();
				BlockPosition loc = new BlockPosition(world, x,y,z);
				if(structures.containsKey(name1)){
					structures.get(name1).setTarget(loc.add(structures.get(name1).getOrigin()));
					save();
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else if(cmd.getName().equals("structureaddmagnet")){
			/*******************
			 *    ADD MAGNET   *
			 *******************/
			if(args.length==1){
				String name1 = args[0];
				if(structures.containsKey(name1)){
					MagnetBlockStructure structure = structures.get(name1);
					player.setEditing(structure);
					player.setRequestType(RequestType.AddMagnet);
					sender.sendMessage(ChatColor.GREEN+"Place an iron block now!");
					System.out.println("You can place an iron block!");
					return true;
				} else {

					System.out.println("Structure not found!");
				}
			}
		}else if(cmd.getName().equals("structureremovemagnet")){
			/*******************
			 *  REMOVE MAGNET  *
			 *******************/
			if(args.length==0){
				player.setRequestType(RequestType.RemoveMagnet);
				sender.sendMessage(ChatColor.GREEN+"Touch your magnet now.");
				return true;
			}
		}
		return false;
	}
	public HashMap<String, MagnetBlockStructure> getStructures() {
		return structures;
	}

	public void save(){
		File dataFolder = getDataFolder();
		if(!dataFolder.exists()){
			if(!dataFolder.mkdir()){
				log.log(Level.SEVERE, "Data Folder for MagnetBlock could not be created. Not saving.");
				return;
			}
		}
		

		/*********************
		 * root/info.csv     *
		 *********************/
		File mainInfo = new File(dataFolder.getAbsolutePath()+"/info.csv");
		if(!mainInfo.exists()){
			try {
				mainInfo.createNewFile();
			} catch (IOException e) {
				log.log(Level.SEVERE, "info.csv for MagnetBlock could not be created. Not saving.");
				return;
			}
		}
		try {
			FileOutputStream output = null;
			try {
				output = new FileOutputStream(mainInfo.getAbsoluteFile());
			} catch (FileNotFoundException e) {
				log.log(Level.SEVERE, "This is weird. info.csv not found after creating it.");
				return;
			}
			BufferedWriter w = new BufferedWriter(new OutputStreamWriter(output));
			w.write("version:0.1\n");
			w.write("structures:");
			for(String name: structures.keySet()){
				w.write(name+",");
			}
			w.write("\n");
			w.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		/*********************
		 * structures        *
		 *********************/
		for(String name: structures.keySet()){
			File sdir = new File(dataFolder.getAbsolutePath()+"/"+name+".structure");
			if(!sdir.exists()){
				if(!sdir.mkdir()){
					log.log(Level.SEVERE, "Directory for "+name+" could not be created. Not saving.");
					return;
				}
			}
			MagnetBlockStructure structure = structures.get(name);
			File structureFile = new File(sdir.getAbsolutePath()+"/info.csv");
			if(!structureFile.exists()){
				try {
					structureFile.createNewFile();
				} catch (IOException e) {
					log.log(Level.SEVERE, "info.csv for a structure could not be created. Not saving.");
					return;
				}
			}
			try {
				FileOutputStream output = null;
				try {
					output = new FileOutputStream(structureFile.getAbsoluteFile());
				} catch (FileNotFoundException e) {
					log.log(Level.SEVERE, "This is weird. info.csv not found after creating it.");
					return;
				}
				BufferedWriter w = new BufferedWriter(new OutputStreamWriter(output));
				w.write("name:"+name+"\n");
				BlockPosition pos = structure.getOrigin();
				w.write("origin:"+pos.getX()+","+pos.getY()+","+pos.getZ()+"\n");
				w.write("world:"+pos.getWorld().getName()+","+pos.getWorld().getEnvironment().toString());
				w.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			File blocksFile = new File(sdir.getAbsolutePath()+"/blocks.csv");
			if(!blocksFile.exists()){
				try {
					blocksFile.createNewFile();
				} catch (IOException e) {
					log.log(Level.SEVERE, "blocks.csv for a structure could not be created. Not saving.");
					return;
				}
			}
			try {
				FileOutputStream output = null;
				try {
					output = new FileOutputStream(blocksFile.getAbsoluteFile());
				} catch (FileNotFoundException e) {
					log.log(Level.SEVERE, "This is weird. blocks.csv not found after creating it.");
					return;
				}
				BufferedWriter w = new BufferedWriter(new OutputStreamWriter(output));
				for(MagnetBlockBlock block:structure.getBlocks()){
					BlockPosition pos = block.getPosition();
					Material material = block.getBlock().getType();
					byte data = block.getBlock().getData();
					w.write(pos.getX()+","+pos.getY()+","+pos.getZ()+",");
					w.write(material.toString()+",");
					w.write(String.valueOf(data));
					w.write("\n");
				}
				w.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			File magnetsFile = new File(sdir.getAbsolutePath()+"/magnets.csv");
			if(!magnetsFile.exists()){
				try {
					blocksFile.createNewFile();
				} catch (IOException e) {
					log.log(Level.SEVERE, "magnets.csv for a structure could not be created. Not saving.");
					return;
				}
			}
			try {
				FileOutputStream output = null;
				try {
					output = new FileOutputStream(magnetsFile.getAbsoluteFile());
				} catch (FileNotFoundException e) {
					log.log(Level.SEVERE, "This is weird. magnets.csv not found after creating it.");
					return;
				}
				BufferedWriter w = new BufferedWriter(new OutputStreamWriter(output));
				for(MagnetBlockMagnet block:structure.getMagnets()){
					BlockPosition pos = block.getPosition();
					w.write(pos.getX()+","+pos.getY()+","+pos.getZ());
					w.write("\n");
				}
				w.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void load(){
		File dataFolder = getDataFolder();
		if(!dataFolder.exists()){
			return;
		}
		File mainInfo = new File(dataFolder.getAbsolutePath()+"/info.csv");
		if(!mainInfo.exists())
		{
			return;
		}
		FileInputStream input;
		@SuppressWarnings("unused")
		String versionString = "0.0";
		String [] structs = null;
		try {
			input = new FileInputStream(mainInfo.getAbsoluteFile());
			InputStreamReader ir = new InputStreamReader(input);
			BufferedReader r = new BufferedReader(ir);
			while(true){
				String line = r.readLine();
				if(line==null||line.isEmpty()){
					break;
				}
				if(line.startsWith("#")){
					continue;
				}
				String[] splt = line.split(":");
				if(splt.length==2){
					String key = splt[0];
					String value = splt[1];
					
					//Key handling
					if(key.equals("version")){
						versionString = value;
					}
					if(key.equals("structures")){
						structs = value.split(",");
					}
				}
			}
			r.close();
			
			//Structure loading!
			
			if(structs == null)
			{
				return;
			}
			for(String name:structs){
				File structDir = new File(dataFolder.getAbsolutePath()+"/"+name+".structure/");
				if(!structDir.exists()){
					continue;
				}
				/**BEGIN info.csv**/
				MagnetBlockStructure structure = new MagnetBlockStructure();
				structures.put(name, structure);
				File structInfo = new File(structDir.getAbsolutePath()+"/info.csv");
				if(!structInfo.exists()){
					log.log(Level.SEVERE, "info.csv for "+name+".structure not found. Continuing...");
					structures.remove(name);
					continue;
				}
				input = new FileInputStream(structInfo.getAbsoluteFile());

				ir = new InputStreamReader(input);
				r = new BufferedReader(ir);
				BlockPosition pos = new BlockPosition(null, 0, 0, 0);
				structure.setOrigin(pos);
				while(true){
					String line = r.readLine();
					if(line==null||line.isEmpty()){
						break;
					}
					if(line.startsWith("#")){
						continue;
					}
					String[] splt = line.split(":");
					if(splt.length==2){
						String key = splt[0];
						String value = splt[1];
						
						//Key handling
						if(key.equals("name")){
							name = value;
						}
						if(key.equals("origin")){
							String [] coords = value.split(",");
							if(coords.length==3){
								int x = Integer.valueOf(coords[0]);
								int y = Integer.valueOf(coords[1]);
								int z = Integer.valueOf(coords[2]);
								pos.setX(x);
								pos.setY(y);
								pos.setZ(z);
							}
						}
						if(key.equals("world")){
							String [] values = value.split(",");
							if(values.length==2){
								World w = getServer().createWorld(values[0], Environment.valueOf(values[1]));
								if(w!=null)
								{
									pos.setWorld(w);
								}
							}
						}
					}
				}
				r.close();
				/**END info.csv**/
				
				/**BEGIN blocks.csv**/
				File structBlocks = new File(structDir.getAbsolutePath()+"/blocks.csv");
				if(!structBlocks.exists()){
					log.log(Level.SEVERE, "Could not find blocks file for "+name+".structure. Continuing...");
					structures.remove(name);
					continue;
				}
				input = new FileInputStream(structBlocks.getAbsoluteFile());
				ir = new InputStreamReader(input);
				r = new BufferedReader(ir);
				while(true){
					String line = r.readLine();
					if(line==null||line.isEmpty()){
						break;
					}
					if(line.startsWith("#")){
						continue;
					}
					String[] splt = line.split(",");
					if(splt.length==5){
						int x = Integer.valueOf(splt[0]);
						int y = Integer.valueOf(splt[1]);
						int z = Integer.valueOf(splt[2]);
						World w = pos.getWorld();
						Block b = w.getBlockAt(x, y, z);
						Material m = Material.valueOf(splt[3]);
						byte data = Byte.valueOf(splt[4]);
						b.setType(m);
						b.setData(data);
						MagnetBlockBlock block = MagnetBlockBlock.getBlock(new BlockPosition(b));
						structure.addBlock(block);
					} else {
						continue;
					}
				}
				r.close();
				/**END blocks.csv**/
				
				/**BEGIN magnets.csv**/
				File magnetsFile = new File(structDir.getAbsolutePath()+"/magnets.csv");
				if(!magnetsFile.exists()){
					log.log(Level.SEVERE, "Could not find magnets file for "+name+".structure. Continuing...");
					structures.remove(name);
					continue;
				}
				input = new FileInputStream(magnetsFile.getAbsoluteFile());
				ir = new InputStreamReader(input);
				r = new BufferedReader(ir);
				while(true){
					String line = r.readLine();
					if(line==null||line.isEmpty()){
						break;
					}
					if(line.startsWith("#")){
						continue;
					}
					String[] splt = line.split(",");
					if(splt.length==3){
						int x = Integer.valueOf(splt[0]);
						int y = Integer.valueOf(splt[1]);
						int z = Integer.valueOf(splt[2]);
						World w = pos.getWorld();
						Block b = w.getBlockAt(x, y, z);
						MagnetBlockMagnet block = MagnetBlockMagnet.getBlock(new BlockPosition(b));
						structure.addMagnet(block);
					} else {
						continue;
					}
				}
				r.close();
				/**END blocks.csv**/
			}
		} catch(IOException e){
			e.printStackTrace();
		}
	}
}
