package me.itstheholyblack.testmodpleaseignore;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import me.itstheholyblack.testmodpleaseignore.items.ItemTestCloak;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryTable;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventHandles {
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onLivingTick(LivingUpdateEvent event) {
		// only do this if the event is on a player
		if (event.getEntityLiving() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.getEntityLiving();
			IBaublesItemHandler baublesInv = BaublesApi.getBaublesHandler(player);
			ItemStack cloakStack = baublesInv.getStackInSlot(5);
			if (cloakStack != null && cloakStack.getItem() instanceof ItemTestCloak) {
				// allow flight
				player.capabilities.allowFlying = true;
			}
		}
	}
	// loot tables
	@SubscribeEvent
	public void lootLoad(LootTableLoadEvent evt) {
		String prefix = "minecraft:chests/";
        String name = evt.getName().toString();

        if (name.startsWith(prefix)) {
            String file = name.substring(name.indexOf(prefix) + prefix.length());
            if (file.equals("simple_dungeon")) {
                evt.getTable().addPool(getInjectPool("simple_dungeon"));
            }
            else if (file.equals("stronghold_library")) {
            	evt.getTable().addPool(getInjectPool("simple_dungeon"));
            }
        }
	}
	private LootPool getInjectPool(String entryName) {
		return new LootPool(new LootEntry[] { getInjectEntry(entryName, 1) }, new LootCondition[0], new RandomValueRange(1), new RandomValueRange(0, 1), "tmpiInjectedPool");
	}
	private LootEntryTable getInjectEntry(String name, int weight) {
        return new LootEntryTable(new ResourceLocation(Reference.MODID, "inject/" + name), weight, 0, new LootCondition[0], "tmpiInjectedLoot");
	}
}
