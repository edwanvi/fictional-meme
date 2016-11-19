package me.itstheholyblack.testmodpleaseignore.items;

import me.itstheholyblack.testmodpleaseignore.items.armor.LeapingBoots;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class ModItems {
	
	public static AutoItem testingItem;
	public static LeapingBoots leapingboots;
	
	public static void createItems() {
		testingItem = new AutoItem("firstitem");
		leapingboots = new LeapingBoots(EntityEquipmentSlot.FEET, "leapingboots");
	}
	@SideOnly(Side.CLIENT)
    public static void initModels() {
		System.out.println("Loading model for item firstitem");
        testingItem.initModel();
    }
}
