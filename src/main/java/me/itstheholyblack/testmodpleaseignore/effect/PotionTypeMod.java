package me.itstheholyblack.testmodpleaseignore.effect;

import me.itstheholyblack.testmodpleaseignore.Reference;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.util.ResourceLocation;

/**
 * @author wiresegal
 * */
public class PotionTypeMod  extends PotionType {
	public PotionTypeMod(String name, PotionEffect... potionEffects) {
		super(name, potionEffects);
        GameRegistry.register(this, new ResourceLocation(Reference.MODID, name));
	}
}
