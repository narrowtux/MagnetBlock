package com.narrowtux.MagnetBlock;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.ChatColor;
public class MagnetBlock extends JavaPlugin {
	Logger log = null;
	public MagnetBlock(){
		MagnetBlockPlayer.plugin = this;
	}
	
	@Override
	public void onDisable() {

		PluginDescriptionFile pdf = getDescription();
		log.log(Level.INFO, pdf.getName()+" version "+pdf.getVersion()+" by "+pdf.getAuthors()+" has been disabled.");
	}

	@Override
	public void onEnable() {
		log = getServer().getLogger();
		
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
			
		} else if(cmd.getName().equals("editstructure")){
			/*******************
			 *  editstructure  *
			 *******************/
			
		} else if(cmd.getName().equals("removestructure")){
			/*******************
			 * removestructure *
			 *******************/
			
		} else if(cmd.getName().equals("finishstructure")){
			/*******************
			 * finishstructure *
			 *******************/
			
		}
		return false;
	}

}
