package me.itstheholyblack.testmodpleaseignore.items;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public final class ModItems {
	
	public static BasicItem testingItem;
	
	public static void createItems() {
		testingItem = new BasicItem();
	}
}
