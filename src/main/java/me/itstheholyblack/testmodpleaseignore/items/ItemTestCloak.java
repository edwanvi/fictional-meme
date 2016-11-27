package me.itstheholyblack.testmodpleaseignore.items;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import me.itstheholyblack.testmodpleaseignore.Reference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemTestCloak extends Item implements IBauble {
	
	public ItemTestCloak() {
		super();
		setRegistryName("itemTestCloak");
		setUnlocalizedName(Reference.MODID + ".itemTestCloak");
		setCreativeTab(ModItems.CREATIVETAB);
		setMaxStackSize(1);
		GameRegistry.register(this);
	}
	
	@Override
	public BaubleType getBaubleType(ItemStack itemstack) {
		return BaubleType.BODY;
	}
}
