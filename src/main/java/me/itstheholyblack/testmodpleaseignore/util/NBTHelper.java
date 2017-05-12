package me.itstheholyblack.testmodpleaseignore.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

// fixes some NPEs
public class NBTHelper {
	public static ItemStack checkNBT(ItemStack stack) {
		if (stack.getTagCompound() == null) {
			stack.setTagCompound(new NBTTagCompound());
		}
		return stack;
	}
}
