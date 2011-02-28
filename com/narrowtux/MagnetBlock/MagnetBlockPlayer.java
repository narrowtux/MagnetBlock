package com.narrowtux.MagnetBlock;

import java.util.HashMap;

import org.bukkit.entity.Player;

public class MagnetBlockPlayer {
	private Player player = null;
	private static HashMap<String, MagnetBlockPlayer> instances = new HashMap<String, MagnetBlockPlayer>();
	public static MagnetBlock plugin = null;
	private RequestType requestType = RequestType.None;
	private MagnetBlockStructure editing = null;

	public Player getPlayer() {
		return player;
	}
	
	private MagnetBlockPlayer(Player player){
		this.player = player;
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public static MagnetBlockPlayer getPlayerByName(String name){
		if(instances.containsKey(name)){
			return instances.get(name);
		} else {
			Player p = plugin.getServer().getPlayer(name);
			if(p!=null)
			{
				MagnetBlockPlayer player = new MagnetBlockPlayer(p);
				instances.put(name, player);
				return player;
			}
		}
		return null;
	}

	public void setRequestType(RequestType requestType) {
		this.requestType = requestType;
	}
	
	public RequestType getRequestType() {
		return requestType;
	}

	/**
	 * @param editing the editing to set
	 */
	public void setEditing(MagnetBlockStructure editing) {
		if(this.editing!=null)
			this.editing.setEditingPlayer(null);
		this.editing = editing;
		if(this.editing!=null){
			this.editing.setEditingPlayer(this);
		}
	}

	/**
	 * @return the editing
	 */
	public MagnetBlockStructure getEditing() {
		return editing;
	}
	
}
