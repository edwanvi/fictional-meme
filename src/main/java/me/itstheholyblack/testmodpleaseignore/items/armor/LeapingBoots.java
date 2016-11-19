package me.itstheholyblack.testmodpleaseignore.items.armor;

import me.itstheholyblack.testmodpleaseignore.Reference;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class LeapingBoots extends ItemArmor {

	public LeapingBoots(EntityEquipmentSlot type, String name) {
		this(type, name, ArmorTypes.ENDER_CLOTH_MAT);
	}
	public LeapingBoots(EntityEquipmentSlot type, String name, ArmorMaterial mat){
		super(mat, 0, type);
		setUnlocalizedName(Reference.MODID + "." + "leapingboots");
		setRegistryName("leapingboots");
		GameRegistry.register(this);
	}
	public int getMaxAllowedJumps() {
		return 5;
	}
}
