package me.itstheholyblack.testmodpleaseignore.items;

import java.util.List;

import me.itstheholyblack.testmodpleaseignore.Reference;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * I can't believe this <em>actually</em> happened.
 * 
 * @author Edwan Vi
 */
public class ItemEdibleHat extends ItemFood {

	public ItemEdibleHat() {
		super(1, false);
		setRegistryName("hat");
		setUnlocalizedName(Reference.MODID + ".hat");
		setCreativeTab(ModItems.CREATIVETAB);
		GameRegistry.register(this);
	}

	@Override
	protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {
		if (!worldIn.isRemote) {
			player.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 2400, 0));
			player.addPotionEffect(new PotionEffect(MobEffects.POISON, 100, 1));
		}
	}

	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		tooltip.add(I18n.format("mouseovertext.hat"));
		super.addInformation(stack, playerIn, tooltip, advanced);
	}
}
