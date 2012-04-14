package com.narrowtux.MagnetBlock;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class Permission {
	private File pluginDir;
	private String group = "users";
	private HashMap<String, Boolean> permissions;
	
	public Permission(File pluginDir){
		this.pluginDir = pluginDir;
		reload();
	}
	
	public Permission(File pluginDir, String group){
		this.pluginDir = pluginDir;
		this.group = group;
		reload();
	}
	
	public void reload(){
		permissions =  new HashMap<String, Boolean>();
		if(pluginDir.exists()){
			File file = new File(pluginDir.getAbsolutePath()+"/"+group+".lst");
			if(file.exists()){
				FileInputStream input;
				try{
					input = new FileInputStream(file.getAbsoluteFile());
					InputStreamReader ir = new InputStreamReader(input);
					BufferedReader r = new BufferedReader(ir);
					while(true){
						String line = r.readLine();
						if(line==null)
							break;
						if(!line.startsWith("#")){
							permissions.put(line, true);
						}
					}
					r.close();
					
				} catch(IOException e){
					e.printStackTrace();
				}
			}
		}
	}
	
	public boolean hasRight(String player){
		if(permissions.size()==0){
			return true;
		}
		if(permissions.containsKey(player)){
			return permissions.get(player);
		} else {
			permissions.put(player, false);
			return false;
		}
	}
}
