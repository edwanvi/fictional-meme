package me.itstheholyblack.testmodpleaseignore.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nonnull;

import me.itstheholyblack.testmodpleaseignore.core.PlayerDetection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityGeminus_M extends EntityLiving {
	private static final float MAX_HP = 320F;
	// list of players who attacked the geminus pair
	private final List<UUID> playersWhoAttacked = new ArrayList<>();
	public EntityGeminus_M(World worldIn) {
		super(worldIn);
		// bout player sized
		setSize(0.6F, 1.8F);
		// duh.
		isImmuneToFire = true;
	}
	/**
	 * Called when the entity is attacked. Causes blindness and adds the attacker to a hit list.
	 * @author Vazkii, minimal edits by Edwan Vi
	 */
	@Override
	public boolean attackEntityFrom(@Nonnull DamageSource source, float par2) {
		Entity e = source.getEntity();

		if (e instanceof EntityPlayer && PlayerDetection.isTruePlayer(e)) {
			EntityPlayer player = (EntityPlayer) e;

			if(!playersWhoAttacked.contains(player.getUniqueID()))
				playersWhoAttacked.add(player.getUniqueID());

			player.isOnLadder();
			player.isInWater();
			player.isPotionActive(MobEffects.BLINDNESS);
			player.isRiding();

			int cap = 25;
			return super.attackEntityFrom(source, Math.min(cap, par2));
		}
		return false;
	}
}
