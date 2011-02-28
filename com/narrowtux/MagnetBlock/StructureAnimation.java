package com.narrowtux.MagnetBlock;

import org.bukkit.util.Vector;

public class StructureAnimation implements Runnable {
	private MagnetBlockStructure structure;
	private Vector toPos;
	private Vector currentPos;
	private Vector v;
	private BlockPosition targetPosition;
	private BlockPosition lastPosition;
	public StructureAnimation(MagnetBlockStructure structure, BlockPosition targetPos){
		this.structure = structure;
		targetPosition = targetPos;
		toPos = targetPos.toLocation().toVector();
		currentPos = structure.getOrigin().toLocation().toVector();
		v = toPos.subtract(currentPos).normalize();
	}
	@Override
	public void run() {
		System.out.println("run");
		currentPos = currentPos.add(v);
		lastPosition = new BlockPosition(structure.getOrigin());
		structure.moveTo(new BlockPosition(currentPos.toLocation(structure.getOrigin().getWorld())));
		if(targetPosition.equals(structure.getOrigin())||lastPosition.equals(structure.getOrigin())){
			System.out.println("stop");
			structure.endAnimation();
		}
	}
}
