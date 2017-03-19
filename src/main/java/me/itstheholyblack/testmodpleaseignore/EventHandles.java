package me.itstheholyblack.testmodpleaseignore;

import org.apache.logging.log4j.Level;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import me.itstheholyblack.testmodpleaseignore.items.ItemTestCloak;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityShulkerBullet;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryTable;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.FMLLog;
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
			} else if (file.equals("stronghold_library")) {
				evt.getTable().addPool(getInjectPool("simple_dungeon"));
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onDMG(LivingHurtEvent e) {
		String key = "TMPIData.shielded";
		if (e.getEntity() instanceof EntityPlayer) {
			System.out.println("Intercepting player damage");
			EntityPlayer player = (EntityPlayer) e.getEntity();
			if (!e.getEntity().world.isRemote) {
				NBTTagCompound data = player.getEntityData();
				// detect if player has NBT saved
				// if they don't, remedy the situation
				if (!data.hasKey(EntityPlayer.PERSISTED_NBT_TAG)) {
					data.setTag(EntityPlayer.PERSISTED_NBT_TAG, new NBTTagCompound());
				}
				// save into variable
				NBTTagCompound persist = data.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
				if (!persist.hasKey("TMPIData.shielded")) {
					persist.setBoolean("TMPIData.shielded", false);
				} else {
					if (persist.getBoolean("TMPIData.shielded")) {
						e.setCanceled(true);
						FMLLog.log(Level.DEBUG, "Returning package to sender...");
						Entity es = e.getSource().getSourceOfDamage();
						if (es instanceof EntityLiving) {
							((EntityLiving) es).setHealth(((EntityLiving) es).getHealth() - e.getAmount() / 2); // fucking
																												// lisp
						}
					}
				}
			}
		} else {
			FMLLog.log(Level.DEBUG, "Did not intercept damage - was not player");
		}
	}

	private LootPool getInjectPool(String entryName) {
		return new LootPool(new LootEntry[] { getInjectEntry(entryName, 1) }, new LootCondition[0],
				new RandomValueRange(1), new RandomValueRange(0, 1), "tmpiInjectedPool");
	}

	private LootEntryTable getInjectEntry(String name, int weight) {
		return new LootEntryTable(new ResourceLocation(Reference.MODID, "inject/" + name), weight, 0,
				new LootCondition[0], "tmpiInjectedLoot");
	}
}
