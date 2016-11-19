package me.itstheholyblack.testmodpleaseignore.items.armor;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;

public class LeapingBoots extends ItemArmor {

	public LeapingBoots(EntityEquipmentSlot type, String name) {
		this(type, name, ArmorTypes.ENDER_CLOTH_MAT);
		// TODO Auto-generated constructor stub
	}
	public LeapingBoots(EntityEquipmentSlot type, String name, ArmorMaterial mat){
		super(mat, 0, type);
	}
}
