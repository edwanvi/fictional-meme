package me.itstheholyblack.testmodpleaseignore.effect;

import me.itstheholyblack.testmodpleaseignore.Reference;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Class to make new potion types, NOT new effects
 *
 * @param name          The unlocalized name of the potion
 * @param potionEffects The PotionEffect this potion gives
 * @author wiresegal
 */
public class PotionTypeMod extends PotionType {
    public PotionTypeMod(String name, PotionEffect... potionEffects) {
        super(name, potionEffects);
        GameRegistry.register(this, new ResourceLocation(Reference.MODID, name));
    }
}
