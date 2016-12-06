package me.itstheholyblack.testmodpleaseignore.entity;

import me.itstheholyblack.testmodpleaseignore.mod;
import net.minecraftforge.fml.common.registry.EntityRegistry;

/**
 * Class to handle registration of custom entities.
 * @author McJty
 * @author Edwan Vi
 */
public class ModEntities {
	public static void init() {
		// Every entity in our mod has an ID (local to this mod)
		int id = 1;
		EntityRegistry.registerModEntity(EntityMissile.class, "Missile", id++, mod.instance, 64, 3, true, 0x996600, 0x00ff00);
		EntityRegistry.registerModEntity(EntityGeminus_M.class, "entityGeminus_M", id++, mod.instance, 100, 6, true, 0x996600, 0x00ff00);
	}		
}
