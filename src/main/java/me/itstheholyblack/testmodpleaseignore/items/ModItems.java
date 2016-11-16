package me.itstheholyblack.testmodpleaseignore.items;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public final class ModItems {
	
	public static AutoItem testingItem;
	
	public static void createItems() {
		testingItem = new AutoItem("firstitem");
	}
}
