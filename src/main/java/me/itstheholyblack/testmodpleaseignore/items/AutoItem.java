package me.itstheholyblack.testmodpleaseignore.items;

import me.itstheholyblack.testmodpleaseignore.Reference;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class AutoItem extends Item {
	public AutoItem(String unlocalizedName) {
		super();
		setRegistryName(unlocalizedName);
		setUnlocalizedName(Reference.MODID + "." + unlocalizedName);
		setCreativeTab(CreativeTabs.MATERIALS);
		GameRegistry.register(this);
	}
}
