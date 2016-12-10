package me.itstheholyblack.testmodpleaseignore.effect;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;

public class PotionWitherMod extends PotionMod {
	public PotionWitherMod(int iconIndex) {
		super("wither_pot", true, 0x352A27, iconIndex);
	}
	@Override
	public void performEffect(EntityLivingBase entity, int amplifier) {
		entity.attackEntityFrom(DamageSource.WITHER, 1.0F);
	}
}
