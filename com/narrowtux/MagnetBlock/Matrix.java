package com.narrowtux.MagnetBlock;

import org.bukkit.util.Vector;

public class Matrix {
	private double a0, a1, a2, b0, b1, b2, c0, c1, c2;

	// Please keep the alignment as above as it shows how the matrix is aligned.
	
	/*
	 * Creates a new Matrix from the given params.
	 */
	public Matrix(double a0, double a1, double a2, double b0, double b1,
			double b2, double c0, double c1, double c2) {
		this.a0 = a0;
		this.a1 = a1;
		this.a2 = a2;
		this.b0 = b0;
		this.b1 = b1;
		this.b2 = b2;
		this.c0 = c0;
		this.c1 = c1;
		this.c2 = c2;
	}

	public Matrix inverse() {
		return new Matrix(0, 0, 0, 0, 0, 0, 0, 0, 0);
	}

	public Vector multiply(Vector vec) {
		Vector ret = new Vector();
		double x = vec.getX(), y = vec.getY(), z = vec.getZ();
		ret.setX(a0 * x + a1 * y + a2 * z);
		ret.setY(b0 * x + b1 * y + b2 * z);
		ret.setZ(c0 * x + c1 * y + c2 * z);
		return ret;
	}
	
	public static Matrix valueOf(String init) throws IllegalArgumentException, Exception{
		String params[] = init.split(",");
		if(params.length==9){
			try{
				Double a0, a1, a2, b0, b1, b2, c0, c1, c2;
				a0 = Double.valueOf(params[0]);
				a1 = Double.valueOf(params[1]);
				a2 = Double.valueOf(params[2]);
				b0 = Double.valueOf(params[3]);
				b1 = Double.valueOf(params[4]);
				b2 = Double.valueOf(params[5]);
				c0 = Double.valueOf(params[6]);
				c1 = Double.valueOf(params[7]);
				c2 = Double.valueOf(params[8]);
				return new Matrix(a0,a1,a2,b0,b1,b2,c0,c1,c2);
			} catch (Exception e){
				throw e;
			}
		} else {
			throw new IllegalArgumentException("Not enough arguments");
		}
	}
	
	@Override
	public String toString(){
		return String.valueOf(a0)+","+String.valueOf(a1)+","+String.valueOf(a2)+","+String.valueOf(b0)+","+String.valueOf(b1)+","+String.valueOf(b2)+","+String.valueOf(c0)+","+String.valueOf(c1)+","+String.valueOf(c2);
	}
}
