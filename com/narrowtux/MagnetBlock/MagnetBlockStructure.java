package com.narrowtux.MagnetBlock;

import java.util.ArrayList;
import java.util.List;

public class MagnetBlockStructure {
	private List<MagnetBlockBlock> blocks = new ArrayList<MagnetBlockBlock>();
	private BlockPosition origin = null;
	private MagnetBlockPlayer editingPlayer = null;
	static MagnetBlock plugin = null;
	/**
	 * @return the blocks
	 */
	public List<MagnetBlockBlock> getBlocks() {
		return blocks;
	}
	
	public void addBlock(MagnetBlockBlock block)
	{
		blocks.add(block);
		if(blocks.size()==1){
			origin = new BlockPosition(block);
		}
		block.setStructure(this);
	}
	
	/**
	 * @param origin the origin to set
	 */
	public void setOrigin(BlockPosition origin) {
		this.origin = origin;
	}

	/**
	 * @return the origin
	 */
	public BlockPosition getOrigin() {
		return origin;
	}
	
	public void removeBlock(MagnetBlockBlock block){
		blocks.remove(block);
		block.setStructure(null);
	}

	/**
	 * @param editingPlayer the editingPlayer to set
	 */
	public void setEditingPlayer(MagnetBlockPlayer editingPlayer) {
		this.editingPlayer = editingPlayer;
	}

	/**
	 * @return the editingPlayer
	 */
	public MagnetBlockPlayer getEditingPlayer() {
		return editingPlayer;
	}

	public void moveBy(BlockPosition vector)
	{
		moveTo(vector.add(origin));
	}
	
	public void moveTo(BlockPosition position)
	{
		BlockPosition vector = position.substract(origin);
		for(MagnetBlockBlock block: blocks){
			if(!block.testMove(block.getPosition().add(vector))){
				return;
			}
		}
		
		for(MagnetBlockBlock block: blocks){
			block.moveTo(block.getPosition().add(vector), 0);
		}
		for(MagnetBlockBlock block: blocks){
			block.moveTo(block.getPosition().add(vector), 1);
		}
		
		origin = position;
		plugin.save();
	}
}
