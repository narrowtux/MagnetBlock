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
		initItems();
	}
	
	private void load(){
		if(file.exists()){
			FileInputStream input;
			try{
				input = new FileInputStream(file.getAbsoluteFile());
				InputStreamReader ir = new InputStreamReader(input);
				BufferedReader r = new BufferedReader(ir);
				items.clear();
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
									printErrorMessage(key, value, e, "MagnetBlock");
									magnetBlockType = Material.IRON_BLOCK;
								}
							}
							
							if(key.equalsIgnoreCase("item")){
								try{
									String values[] = value.split(",");
									if(values.length==4){
										Material item;
										try{
											item = Material.valueOf(values[0]);
										} catch(Exception e){
											throw new Exception("Material not found. Be sure to refer to the Material-ENUM-Documentation.");
										}
										Integer x, y, z;
										try{
											x = Integer.valueOf(values[1]);
											y = Integer.valueOf(values[2]);
											z = Integer.valueOf(values[3]);
										} catch(Exception e){
											throw new Exception("Could not parse coordinates. They have to be integers.");
										}
										BlockVector vector = new BlockVector(x,y,z);
										items.put(item, vector);
									} else {
										throw new Exception("Not enough arguments (material,x,y,z)");
									}
								} catch (Exception e){
									printErrorMessage(key, value, e, "MagnetBlock");
								}
							}
						}
					}
				}
				if(items.size()==0){
					initItems();
				}
				r.close();
				
			} catch(IOException e){
				e.printStackTrace();
			}
		} else {
			System.out.println("No MagnetBlock configuration file found. For configuration, create one in %bukkitdir%/plugins/MagnetBlock/magnetblock.cfg and refer to the forum post.");
		}
	}

	public Material getMagnetBlockType() {
		return magnetBlockType;
	}
	
	public BlockVector getVector(Material item){
		if(items.containsKey(item)){
			return items.get(item);
		} else {
			return new BlockVector(0,0,0);
		}
	}
	
	public void printErrorMessage(String key, String value, Exception e, String pluginname){
		pluginname = "["+pluginname+"]";
		System.out.println(pluginname+"Error on loading line:");
		System.out.println(pluginname+key+"="+value+" :");
		System.out.println(pluginname+e.getCause());
		System.out.println(pluginname+"----------------------");
	}
	
	private void initItems(){
		items.put(Material.FEATHER, new BlockVector(1,0,1));
		items.put(Material.STICK, new BlockVector(0,1,0));
	}
}
