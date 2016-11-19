package me.itstheholyblack.testmodpleaseignore.effect;

import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;

public class ModEffects {
	public static PotionType wither;
	public static PotionType witherLong;
	public static PotionType witherStrong;
	public ModEffects() {
		wither = new PotionTypeMod("wither", new PotionEffect(MobEffects.WITHER, 3600));
		witherLong = new PotionTypeMod("long_wither", new PotionEffect(MobEffects.WITHER, 9600));
		witherStrong = new PotionTypeMod("strong_wither", new PotionEffect(MobEffects.WITHER, 1800, 1));
	}
}
