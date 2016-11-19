package me.itstheholyblack.testmodpleaseignore.items;

import me.itstheholyblack.testmodpleaseignore.Reference;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BasicItem extends Item {
	public BasicItem(){
		setRegistryName("firstitem");        // The unique name (within your mod) that identifies this item
        setUnlocalizedName(Reference.MODID + ".firstitem");     // Used for localization (en_US.lang)
        GameRegistry.register(this);
	}
}
