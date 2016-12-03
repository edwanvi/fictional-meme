package me.itstheholyblack.testmodpleaseignore.items;

import me.itstheholyblack.testmodpleaseignore.items.armor.LeapingBoots;
import me.itstheholyblack.testmodpleaseignore.items.casters.ItemBladeCaster;
import me.itstheholyblack.testmodpleaseignore.items.casters.ItemHalfCaster_Main;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class ModItems {
	
	public static AutoItem testingItem;
	public static AutoItem endHide;
	public static LeapingBoots leapingboots;
	public static SlingRing slingRing;
	public static ItemTestCloak testCloak;
	public static ItemBladeCaster bladeCaster;
	public static ItemHalfCaster_Main halfCaster_Main;
	public static final CreativeTabs CREATIVETAB = new CreativeTabs("testModPleaseIgnore") {
	    @Override public Item getTabIconItem() {
	        return endHide;
	    }
	};
	
	public static void createItems() {
		testingItem = new AutoItem("firstitem");
		endHide = new AutoItem("end_hide");
		leapingboots = new LeapingBoots(EntityEquipmentSlot.FEET, "leapingboots");
		slingRing = new SlingRing();
		testCloak = new ItemTestCloak();
		bladeCaster = new ItemBladeCaster();
		halfCaster_Main = new ItemHalfCaster_Main();
	}
	@SideOnly(Side.CLIENT)
    public static void initModels() {
		System.out.println("Loading item models");
        testingItem.initModel();
        leapingboots.initModel();
        endHide.initModel();
        slingRing.initModel();
        testCloak.initModel();
        bladeCaster.initModel();
        halfCaster_Main.initModel();
    }
}
