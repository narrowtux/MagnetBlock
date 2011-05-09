package com.narrowtux.MagnetBlock;

import org.bukkit.util.Vector;

public class StructureAnimation implements Runnable {
	private MagnetBlockStructure structure;
	private Vector toPos;
	private Vector currentPos;
	private BlockPosition targetPosition;
	private BlockPosition lastPosition;
	private int stucks = 0;
	@SuppressWarnings("unused")
	private boolean useLogistic = false;
	@SuppressWarnings("unused")
	private int neededsteps = 0;
	@SuppressWarnings("unused")
	private int steps = 0;
	private int currentSpeed = 0;
	public StructureAnimation(MagnetBlockStructure structure, BlockPosition targetPos){
		this.structure = structure;
		targetPosition = targetPos;
		toPos = targetPos.toLocation().toVector();
		currentPos = structure.getOrigin().toLocation().toVector();
		Vector v = toPos.clone().subtract(currentPos).normalize();
		v.setX(Math.round(v.getX()));
		v.setY(Math.round(v.getY()));
		v.setZ(Math.round(v.getZ()));
		neededsteps = (int) (toPos.clone().subtract(currentPos.clone()).length()/v.length());
	}
	@Override
	public void run() {
		Vector v ;
		v = toPos.clone().subtract(currentPos.clone());
		int distance = (int)v.length();
		v = v.normalize();
		v.setX(Math.round(v.getX()));
		v.setY(Math.round(v.getY()));
		v.setZ(Math.round(v.getZ()));
		int maxSpeed = MagnetBlock.instance.config.getMaximumSpeed();
		if(distance>maxSpeed){
			if(currentSpeed<maxSpeed){
				currentSpeed++;
			}
		} else {
			if(currentSpeed>=distance){
				currentSpeed/=2;
			}
		}
		if(currentSpeed == 0){
			currentSpeed = 1;
		}
		if(v.equals(new Vector(0,0,0))){
			structure.endAnimation();
			return;
		}
		v = v.multiply(currentSpeed);
		currentPos = structure.getOrigin().toLocation().toVector();
		currentPos = currentPos.add(v);
		lastPosition = new BlockPosition(structure.getOrigin());
		structure.moveTo(new BlockPosition(currentPos.toLocation(structure.getOrigin().getWorld())));
		if(lastPosition.equals(structure.getOrigin())){
			stucks++;
			currentSpeed = 1;
		} else {
			stucks = 0;
		}
		if(targetPosition.equals(structure.getOrigin())||stucks==3){
			structure.endAnimation();
			return;
		}
	}
}
