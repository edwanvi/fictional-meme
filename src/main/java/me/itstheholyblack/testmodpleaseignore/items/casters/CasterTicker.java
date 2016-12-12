package me.itstheholyblack.testmodpleaseignore.items.casters;

import me.itstheholyblack.testmodpleaseignore.core.PlayerDataMan;
import me.itstheholyblack.testmodpleaseignore.items.ModItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CasterTicker {
	public static final String DataTag = PlayerDataMan.DataTag;
	public static final String ManaPool = PlayerDataMan.ManaPool;
	@SubscribeEvent
	public void onPlayerTick(LivingUpdateEvent event) {
		if(event.getEntityLiving() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.getEntityLiving();
			// server side work
			if (!player.world.isRemote) {
				NBTTagCompound data = player.getEntityData();
				if (!data.hasKey(EntityPlayer.PERSISTED_NBT_TAG)) {
					data.setTag(EntityPlayer.PERSISTED_NBT_TAG, new NBTTagCompound());
				}
				NBTTagCompound persist = data.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
				double manapoollevel = persist.getDouble(ManaPool);
				// holding blade caster
				Item mainhelditem = player.getHeldItemMainhand().getItem();
				ItemStack mainheldstack = player.getHeldItemMainhand();
				if (mainhelditem instanceof ItemBladeCaster && ((ItemBladeCaster) mainhelditem).getActivated(mainheldstack)) {
					manapoollevel = manapoollevel - 1.0D;
				} else if (mainhelditem == ModItems.halfCaster_Main && ((ItemHalfCaster_Main) mainhelditem).getActivated(mainheldstack)) {
					manapoollevel = manapoollevel - 0.5D;
				} else if (mainhelditem == ModItems.halfCaster_Off && ((ItemHalfCaster_Off) mainhelditem).getActivated(mainheldstack)) {
					
				}
				persist.setDouble(ManaPool, manapoollevel);
			}
		}
	}
}
