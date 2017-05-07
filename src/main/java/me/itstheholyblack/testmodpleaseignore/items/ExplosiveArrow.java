package me.itstheholyblack.testmodpleaseignore.items;

import me.itstheholyblack.testmodpleaseignore.Reference;
import me.itstheholyblack.testmodpleaseignore.entity.EntityExplosiveArrow;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

// because fuck you, that's why
public class ExplosiveArrow extends ItemArrow {

	public ExplosiveArrow() {
		super();
		setRegistryName("earrow");
		setUnlocalizedName(Reference.MODID + ".earrow");
		setCreativeTab(ModItems.CREATIVETAB);
		GameRegistry.register(this);
	}

	public EntityExplosiveArrow createArrow(World worldIn, ItemStack stack, EntityLivingBase shooter) {
		EntityExplosiveArrow entitytippedarrow = new EntityExplosiveArrow(worldIn, shooter);
		return entitytippedarrow;
	}

	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(this, 0,
				new ModelResourceLocation(Items.ARROW.getRegistryName(), "inventory"));
	}
}
