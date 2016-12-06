package me.itstheholyblack.testmodpleaseignore.core;

import java.util.Random;

import javax.annotation.Nullable;

/**
 * Class for various random operations.
 * @author Edwan Vi
 */
public class Randomizer {
	private static Random rand_gen = new Random();
	/**
	 * Generate a random boolean, with a bias {@code p} for being {@code true}.
	 * @param p The chance of returning {@code true}. 0.5 = 50%.
	 * @param direction Set this to false to return {@code false} instead of true.
	 * @return A boolean
	 * @author maxf130
	 * @author Edwan Vi
	 */
	public static boolean getRandomBoolean(float p, @Nullable Boolean direction){
		if (direction == null || direction == true)
			return rand_gen.nextFloat() < p;
		else {
			return !(rand_gen.nextFloat() < p);
		}
	}
}
