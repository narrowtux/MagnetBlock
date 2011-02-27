package com.narrowtux.MagnetBlock;

public class BlockPosition {
	private int x, y, z;
	
	BlockPosition(int x, int y, int z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public operator+(BlockPosition other){
		
	}
	
	/**
	 * @param x the x to set
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * @param z the z to set
	 */
	public void setZ(int z) {
		this.z = z;
	}

	/**
	 * @return the z
	 */
	public int getZ() {
		return z;
	}
}
