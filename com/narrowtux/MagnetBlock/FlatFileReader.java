package com.narrowtux.MagnetBlock;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Material;

public class FlatFileReader {
	private File file;
	private boolean caseSensitive;
	private Map<String, List<String>> keySet = new HashMap<String,List<String>>();
	
	public FlatFileReader(File file, boolean caseSensitive){
		this.file = file;
		this.caseSensitive = caseSensitive;
		reload();
	}
	
	public void reload(){
		keySet.clear();
		load();
	}
	
	public int getInteger(String key, int fallback){
		if(keySet.containsKey(key)){
			int ret;
			try{
				ret = Integer.valueOf(get(key));
			} catch(Exception e){
				ret = fallback;
			}
			return ret;
		} else {
			return fallback;
		}
	}
	
	public String getString(String key, String fallback){
		if(keySet.containsKey(key)){
			return get(key);
		} else {
			return fallback;
		}
	}
	
	public boolean getBoolean(String key, boolean fallback){
		if(keySet.containsKey(key)){
			boolean ret;
			try{
				ret = Boolean.valueOf(get(key));
			} catch(Exception e){
				ret = fallback;
			}
			return ret;
		} else {
			return fallback;
		}
	}
	
	public double getDouble(String key, double fallback){
		if(keySet.containsKey(key)){
			double ret;
			try{
				ret = Double.valueOf(get(key));
			} catch(Exception e){
				ret = fallback;
			}
			return ret;
		} else {
			return fallback;
		}
	}
	
	public float getFloat(String key, float fallback){
		if(keySet.containsKey(key)){
			float ret;
			try{
				ret = Float.valueOf(get(key));
			} catch(Exception e){
				ret = fallback;
			}
			return ret;
		} else {
			return fallback;
		}
	}
	
	public Material getMaterial(String key, Material fallback){
		if(keySet.containsKey(key)){
			Material ret;
			try{
				ret = Material.getMaterial(get(key));
			} catch(Exception e){
				try{
					ret = Material.getMaterial(Integer.valueOf(get(key)));
				} catch(Exception ex){
					return fallback;
				}
			}
			return ret;
		}
		return fallback;
	}
	
	public Set<String> keys(){
		return keySet.keySet();
	}
	
	public List<String> values(String key){
		if(keySet.containsKey(key)){
			return keySet.get(key);
		} else {
			return new ArrayList<String>();
		}
	}
	
	private boolean load(){
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
							if(!caseSensitive){
								key = key.toLowerCase();
							}
							if(keySet.containsKey(key)){
								keySet.get(key).add(value);
							} else {
								List<String> list = new ArrayList<String>();
								list.add(value);
								keySet.put(key, list);
							}
						}
					}
				}
				r.close();
			} catch(IOException e){
				e.printStackTrace();
			}
		} else {
			System.out.println("File "+file.getAbsoluteFile()+" not found.");
			return false;
		}
		return true;
	}
	
	private String get(String key){
		return keySet.get(key).get(0);
	}
}
