package com.narrowtux.MagnetBlock;

import org.bukkit.util.Vector;

public class Matrix {
	private double 
	a, b, c, 
	d, e, f, 
	g, h, i;

	// Please keep the alignment as above as it shows how the matrix is aligned.

	/*
	 * Creates a new Matrix from the given params.
	 */
	public Matrix(double a0, double a1, double a2, double b0, double b1,
			double b2, double c0, double c1, double c2) {
		this.a = a0;
		this.b = a1;
		this.c = a2;
		this.d = b0;
		this.e = b1;
		this.f = b2;
		this.g = c0;
		this.h = c1;
		this.i = c2;
	}

	/*
	 * returns the inverse matrix, if available
	 */
	public Matrix inverse() throws Exception {
		if (isSingular()) {
			throw new Exception("Matrix is singular.");
		} else {
			return adjoint().divide(determinant());
		}
	}

	/*
	 * returns the determinant of the matrix
	 */

	public double determinant() {
		return a * e * i + b * f * g + c * d * h - c * e * g - b * d * i - a
				* f * h;
	}

	/*
	 * returns true if the matrix is singular
	 */

	public boolean isSingular() {
		return determinant() == 0;
	}

	/**
	 * multiplies the matrix with the given vector.
	 * 
	 * @returns a new multiplied vector.
	 * @param vec the factor.
	 */
	public Vector multiply(Vector vec) {
		Vector ret = new Vector();
		double x = vec.getX(), y = vec.getY(), z = vec.getZ();
		ret.setX(a * x + b * y + c * z);
		ret.setY(d * x + e * y + f * z);
		ret.setZ(g * x + h * y + i * z);
		return ret;
	}

	/**
	 * @return the product.
	 * @param mat the factor.
	 */
	public Matrix multiply(Matrix mat) {
		Matrix ret = new Matrix(0, 0, 0, 0, 0, 0, 0, 0, 0);
		double j = mat.a, k = mat.b, l = mat.c, m = mat.d, n = mat.e, o = mat.f, p = mat.g, q = mat.h, r = mat.i;

		ret.a = a * j + b * m + c * p;
		ret.b = a * k + b * n + c * q;
		ret.c = c * r + a * l + b * o;

		ret.d = d * j + e * m + f * p;
		ret.e = d * k + e * n + f * q;
		ret.f = f * r + d * l + e * o;
		
		ret.g = g * j + h * m + i * p;
		ret.h = g * k + h * n + i * q;
		ret.i = i * r + g * l + h * o;
		
		return ret;
	}

	/*
	 * returns the product.
	 */
	public Matrix multiply(double scale) {
		return new Matrix(a * scale, b * scale, c * scale, d * scale,
				e * scale, f * scale, g * scale, h * scale, i * scale);
	}
	
	/**
	 * @return the adjoint
	 */
	public Matrix adjoint(){
		Matrix ret = new Matrix(0, 0, 0, 0, 0, 0, 0, 0, 0);

		ret.a = e * i - f * h;
		ret.b = c * h - b * i;
		ret.c = b * f - c * e;

		ret.d = f * g - d * i;
		ret.e = a * i - c * g;
		ret.f = c * d - a * f;

		ret.g = d * h - e * g;
		ret.h = b * g - a * h;
		ret.i = a * e - b * d;

		return ret;
	}

	/**
	 * @returns the division.
	 */
	public Matrix divide(double scale) {
		return multiply(1 / scale);
	}

	public static Matrix valueOf(String init) throws IllegalArgumentException,
			Exception {
		String params[] = init.split(",");
		if (params.length == 9) {
			try {
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
				return new Matrix(a0, a1, a2, b0, b1, b2, c0, c1, c2);
			} catch (Exception e) {
				throw e;
			}
		} else {
			throw new IllegalArgumentException("Not enough arguments");
		}
	}

	@Override
	public String toString() {
		return String.valueOf(a) + "," + String.valueOf(b) + ","
				+ String.valueOf(c) + "," + String.valueOf(d) + ","
				+ String.valueOf(e) + "," + String.valueOf(f) + ","
				+ String.valueOf(g) + "," + String.valueOf(h) + ","
				+ String.valueOf(i);
	}

	/**
	 * @return the a
	 */
	public double getA() {
		return a;
	}

	/**
	 * @param a
	 *            the a to set
	 */
	public void setA(double a) {
		this.a = a;
	}

	/**
	 * @return the b
	 */
	public double getB() {
		return b;
	}

	/**
	 * @param b
	 *            the b to set
	 */
	public void setB(double b) {
		this.b = b;
	}

	/**
	 * @return the c
	 */
	public double getC() {
		return c;
	}

	/**
	 * @param c
	 *            the c to set
	 */
	public void setC(double c) {
		this.c = c;
	}

	/**
	 * @return the d
	 */
	public double getD() {
		return d;
	}

	/**
	 * @param d
	 *            the d to set
	 */
	public void setD(double d) {
		this.d = d;
	}

	/**
	 * @return the e
	 */
	public double getE() {
		return e;
	}

	/**
	 * @param e
	 *            the e to set
	 */
	public void setE(double e) {
		this.e = e;
	}

	/**
	 * @return the f
	 */
	public double getF() {
		return f;
	}

	/**
	 * @param f
	 *            the f to set
	 */
	public void setF(double f) {
		this.f = f;
	}

	/**
	 * @return the g
	 */
	public double getG() {
		return g;
	}

	/**
	 * @param g
	 *            the g to set
	 */
	public void setG(double g) {
		this.g = g;
	}

	/**
	 * @return the h
	 */
	public double getH() {
		return h;
	}

	/**
	 * @param h
	 *            the h to set
	 */
	public void setH(double h) {
		this.h = h;
	}

	/**
	 * @return the i
	 */
	public double getI() {
		return i;
	}

	/**
	 * @param i
	 *            the i to set
	 */
	public void setI(double i) {
		this.i = i;
	}
}
