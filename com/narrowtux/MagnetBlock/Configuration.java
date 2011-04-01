package com.narrowtux.MagnetBlock;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.util.BlockVector;

public class Configuration {
	private Material magnetBlockType = Material.IRON_BLOCK;
	private HashMap<Material, BlockVector> items = new HashMap<Material, BlockVector>();
	private File file;
	
	public Configuration(File file){
		this.file = file;
		load();
	}
	
	private void load(){
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
						String splt[] = line.split("=");
						if(splt.length==2){
							String key = splt[0];
							String value = splt[1];
							if(key.equalsIgnoreCase("magnet")){
								try{
									magnetBlockType = Material.valueOf(value);
								} catch(Exception e){
									magnetBlockType = Material.IRON_BLOCK;
								}
							}
							
						}
					}
				}
				r.close();
				
			} catch(IOException e){
				e.printStackTrace();
			}
		} else {
			System.out.println("No MagnetBlock configuration file found. For configuration, create one in %bukkitdir%/plugins/MagnetBlock/magnetblock.cfg and refer to the forum post.");
		}
	}
}
