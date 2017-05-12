package me.itstheholyblack.testmodpleaseignore.core;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.util.FakePlayer;

import java.util.regex.Pattern;

/**
 * class for various player operations.
 */
public class PlayerDetection {
	private static final Pattern FAKE_PLAYER_PATTERN = Pattern.compile("^(?:\\[.*\\])|(?:ComputerCraft)$");

	/**
	 * Get whether or not an entity is a real player.
	 *
	 * @param e
	 *            The entity being examined for player-ness.
	 * @return true if the player does not match any known fake player patterns.
	 * @author Vazkii
	 */
	public static boolean isTruePlayer(Entity e) {
		if (!(e instanceof EntityPlayer))
			return false;

		EntityPlayer player = (EntityPlayer) e;

		String name = player.getName();
		return !(player instanceof FakePlayer || FAKE_PLAYER_PATTERN.matcher(name).matches());
	}
}
