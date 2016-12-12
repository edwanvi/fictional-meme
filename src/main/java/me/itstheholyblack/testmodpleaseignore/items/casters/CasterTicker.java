package me.itstheholyblack.testmodpleaseignore.items.casters;

import me.itstheholyblack.testmodpleaseignore.core.PlayerDataMan;
import me.itstheholyblack.testmodpleaseignore.items.ModItems;
import net.minecraft.entity.player.EntityPlayer;
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
				if (player.getHeldItemMainhand().getItem() == ModItems.bladeCaster) {
					
				}
			}
		}
	}
}
