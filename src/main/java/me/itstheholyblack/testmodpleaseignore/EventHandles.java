package me.itstheholyblack.testmodpleaseignore;

import baubles.api.BaublesApi;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class EventHandles {
	IInventory baublesInv = BaublesApi.getBaubles(null);
	ItemStack amuletStack = baublesInv.getStackInSlot(0);
}
