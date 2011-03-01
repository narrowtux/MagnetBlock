package com.narrowtux.MagnetBlock;

import org.bukkit.util.Vector;

public class StructureAnimation implements Runnable {
	private MagnetBlockStructure structure;
	private Vector toPos;
	private Vector currentPos;
	private Vector v;
	private BlockPosition targetPosition;
	private BlockPosition lastPosition;
	private int stepsneeded;
	private int steps = 0;
	public StructureAnimation(MagnetBlockStructure structure, BlockPosition targetPos){
		this.structure = structure;
		targetPosition = targetPos;
		toPos = targetPos.toLocation().toVector();
		currentPos = structure.getOrigin().toLocation().toVector();
		v = toPos.clone().subtract(currentPos.clone()).normalize();
		stepsneeded = (int) (toPos.clone().subtract(currentPos.clone()).length()/v.length());
	}
	@Override
	public void run() {
		currentPos = currentPos.add(v);
		lastPosition = new BlockPosition(structure.getOrigin());
		structure.moveTo(new BlockPosition(currentPos.toLocation(structure.getOrigin().getWorld())));
		steps++;
		if(targetPosition.equals(structure.getOrigin())||lastPosition.equals(structure.getOrigin())||steps==stepsneeded){
			structure.endAnimation();
		}
	}
}
