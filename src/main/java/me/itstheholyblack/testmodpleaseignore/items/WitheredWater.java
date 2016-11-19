package me.itstheholyblack.testmodpleaseignore.items;

import me.itstheholyblack.testmodpleaseignore.Reference;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemFood;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WitheredWater extends ItemFood {
	public WitheredWater(String unlocalizedName, int amount, float saturation, boolean isWolfFood) {
		super(amount, saturation, isWolfFood);
		setRegistryName(unlocalizedName);
		setUnlocalizedName(Reference.MODID + "." + unlocalizedName);
		setCreativeTab(CreativeTabs.FOOD);
		setPotionEffect(new PotionEffect(MobEffects.WITHER, 3600), 1.0f);
		GameRegistry.register(this);
	}
	@SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
}
