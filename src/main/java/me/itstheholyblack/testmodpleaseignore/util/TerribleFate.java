package me.itstheholyblack.testmodpleaseignore.util;

import me.itstheholyblack.testmodpleaseignore.Reference;
import net.minecraft.util.DamageSource;

public class TerribleFate extends DamageSource {

    public static final TerribleFate FATE = new TerribleFate();

    public TerribleFate() {
        super(Reference.MODID + ".t_fate");
        setDamageBypassesArmor();
        setMagicDamage();
        setDamageAllowedInCreativeMode();
    }

}
