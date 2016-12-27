package me.itstheholyblack.testmodpleaseignore.core;

public class MathHelper {
	/**
	 * A table of sin values computed from 0 (inclusive) to 2*pi (exclusive),
	 * with steps of 2*PI / 65536.
	 */
	private static final float[] SIN_TABLE = new float[65536];

	public static int truncateDoubleToInt(double value) {
		return (int) (Math.floor(value * 100) / 100);
	}

	/**
	 * sin looked up in a table
	 */
	public static float sin(float value) {
		return SIN_TABLE[(int) (value * 10430.378F) & 65535];
	}

	/**
	 * Returns the value of the first parameter, clamped to be within the lower
	 * and upper limits given by the second and third parameters.
	 */
	public static int clamp(int num, int min, int max) {
		return num < min ? min : (num > max ? max : num);
	}
}
