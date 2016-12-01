package me.itstheholyblack.testmodpleaseignore;

import baubles.api.BaublesApi;
import me.itstheholyblack.testmodpleaseignore.items.ItemTestCloak;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventHandles {
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onLivingTick(LivingUpdateEvent event) {
		// only do this if the event is on a player
		if (event.getEntityLiving() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.getEntityLiving();
			IInventory baublesInv = BaublesApi.getBaubles(player);
			ItemStack cloakStack = baublesInv.getStackInSlot(5);
			if (cloakStack != null && cloakStack.getItem() instanceof ItemTestCloak) {
				// allow flight
				player.capabilities.allowFlying = true;
			}
		}
	}
}
