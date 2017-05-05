package me.itstheholyblack.testmodpleaseignore.entity;

import me.itstheholyblack.testmodpleaseignore.mod;
import me.itstheholyblack.testmodpleaseignore.entity.render.RenderExplosiveArrow;
import me.itstheholyblack.testmodpleaseignore.entity.render.RenderGeminus_F;
import me.itstheholyblack.testmodpleaseignore.entity.render.RenderGeminus_M;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderTippedArrow;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Class to handle registration of custom entities.
 * 
 * @author McJty
 * @author Edwan Vi
 */
public class ModEntities {
	public static void init() {
		// Every entity in our mod has an ID (local to this mod)
		int id = 1;
		EntityRegistry.registerModEntity(new ResourceLocation("Missile"), EntityMissile.class,
				"testmodpleaseignore:Missile", id++, mod.instance, 64, 3, true);
		EntityRegistry.registerModEntity(new ResourceLocation("entityGeminus_M"), EntityGeminus_M.class,
				"testmodpleaseignore:entityGeminus_M", id++, mod.instance, 128, 6, true, 0x996600, 0x00ff00);
		EntityRegistry.registerModEntity(new ResourceLocation("entityGeminus_F"), EntityGeminus_F.class,
				"testmodpleaseignore:entityGeminus_F", id++, mod.instance, 128, 6, true, 0x003399, 0xff00ff);
		EntityRegistry.registerModEntity(new ResourceLocation("shulkerMinion"), EntityShulkerMinion.class,
				"testmodpleaseignore:shulkerMinion", id++, mod.instance, 128, 3, true);
		EntityRegistry.registerModEntity(new ResourceLocation("explosivearrow"), EntityExplosiveArrow.class,
				"testmodpleaseignore:explosivearrow", id++, mod.instance, 128, 3, true);
	}

	@SideOnly(Side.CLIENT)
	public static void initModels() {
		RenderingRegistry.registerEntityRenderingHandler(EntityGeminus_M.class, RenderGeminus_M.FACTORY);
		RenderingRegistry.registerEntityRenderingHandler(EntityGeminus_F.class, RenderGeminus_F.FACTORY);
		RenderingRegistry.registerEntityRenderingHandler(EntityExplosiveArrow.class, RenderExplosiveArrow.FACTORY);
	}
}
