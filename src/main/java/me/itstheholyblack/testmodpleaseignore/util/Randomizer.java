package me.itstheholyblack.testmodpleaseignore.util;

import java.util.Random;

/**
 * Class for various random operations.
 * 
 * @author Edwan Vi
 */
public class Randomizer {
	private static Random rand_gen = new Random();

	/**
	 * Generate a random boolean, with a bias {@code p} for being {@code true}.
	 * 
	 * @param d
	 *            The chance of returning {@code true}. 0.5 = 50%.
	 * @return A boolean
	 * @author maxf130
	 * @author Edwan Vi
	 */
	public static boolean getRandomBoolean(double d) {
		return rand_gen.nextDouble() < d;
	}
}
