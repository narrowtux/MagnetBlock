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
	private FlatFileReader reader;
	private int maximumSpeed;
	
	public Configuration(File file){
		this.file = file;
		reader = new FlatFileReader(file, false);
		initItems();
		load();
	}
	
	private void load(){
		magnetBlockType = reader.getMaterial("magnet", Material.IRON_BLOCK);
		maximumSpeed = reader.getInteger("maximumspeed", 1);
		for (String value: reader.values("item")){
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
				printErrorMessage("item", value, e, "MagnetBlock");
			}
		}
		if(items.size()==0){
			initItems();
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
	
	public int getMaximumSpeed(){
		return maximumSpeed;
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
