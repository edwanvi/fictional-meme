package me.itstheholyblack.testmodpleaseignore.effect;

import org.apache.logging.log4j.Level;

import me.itstheholyblack.testmodpleaseignore.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PotionMod extends Potion {
	int iconX;
	int iconY;
	ResourceLocation resource;

	public PotionMod(String name, boolean badEffect, int color, int iconIndex) {
		super(badEffect, color);
		FMLLog.info("", "Registering potion " + name);
		GameRegistry.register(this, new ResourceLocation(Reference.MODID, name));
		iconX = iconIndex % 8;
		iconY = iconIndex / 8;
		resource = new ResourceLocation(Reference.MODID, "textures/gui/potions.png");
		setPotionName("${Reference.MODID}.potion." + name);
		if (!badEffect)
			setBeneficial();
	}

	@Override
	public boolean hasStatusIcon() {
		return false;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void renderInventoryEffect(int x, int y, PotionEffect effect, Minecraft mc) {
		Minecraft.getMinecraft().renderEngine.bindTexture(resource);
		mc.currentScreen.drawTexturedModalRect(x + 6, y + 7, 0 + iconX * 18, 198 + iconY * 18, 18, 18);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void renderHUDEffect(int x, int y, PotionEffect effect, Minecraft mc, float alpha) {
		Minecraft.getMinecraft().renderEngine.bindTexture(resource);
		mc.ingameGUI.drawTexturedModalRect(x + 3, y + 3, 0 + iconX * 18, 198 + iconY * 18, 18, 18);
	}

	public boolean hasEffect(EntityLivingBase entity, Potion potion) {
		return entity.getActivePotionEffect(potion) != null;
	}

	public PotionEffect getEffect(EntityLivingBase entity, Potion potion) {
		return entity.getActivePotionEffect(potion);
	}
}
