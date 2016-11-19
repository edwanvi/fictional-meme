package me.itstheholyblack.testmodpleaseignore.items.armor;

import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraftforge.common.util.EnumHelper;

/**
 * Class to store various kinds of armor.
 * */
public class ArmorTypes {
	public static final ArmorMaterial ENDER_CLOTH_MAT = EnumHelper.addArmorMaterial("ENDER_CLOTH_MAT",
			"ender_cloth",
			20,
			new int[]{1, 2, 3, 1},
			40,
			SoundEvents.ITEM_ARMOR_EQUIP_LEATHER,
			0.0F);
}
