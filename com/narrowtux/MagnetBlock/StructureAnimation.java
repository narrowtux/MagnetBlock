package com.narrowtux.MagnetBlock;

import org.bukkit.util.Vector;

public class StructureAnimation implements Runnable {
	private MagnetBlockStructure structure;
	private Vector toPos;
	private Vector currentPos;
	private BlockPosition targetPosition;
	private BlockPosition lastPosition;
	private int stucks = 0;
	public StructureAnimation(MagnetBlockStructure structure, BlockPosition targetPos){
		this.structure = structure;
		targetPosition = targetPos;
		toPos = targetPos.toLocation().toVector();
		currentPos = structure.getOrigin().toLocation().toVector();
	}
	@Override
	public void run() {
		Vector v ;
		v = toPos.clone().subtract(currentPos.clone()).normalize();
		v.setX(Math.round(v.getX()));
		v.setY(Math.round(v.getY()));
		v.setZ(Math.round(v.getZ()));
		if(v.equals(new Vector(0,0,0))){
			structure.endAnimation();
			return;
		}
		currentPos = structure.getOrigin().toLocation().toVector();
		currentPos = currentPos.add(v);
		lastPosition = new BlockPosition(structure.getOrigin());
		structure.moveTo(new BlockPosition(currentPos.toLocation(structure.getOrigin().getWorld())));
		if(lastPosition.equals(structure.getOrigin())){
			stucks++;
		} else {
			stucks = 0;
		}
		System.out.println(v);
		if(targetPosition.equals(structure.getOrigin())||stucks==3){
			structure.endAnimation();
			return;
		}
	}
}
