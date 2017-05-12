package me.itstheholyblack.testmodpleaseignore.core;

import me.itstheholyblack.testmodpleaseignore.network.MessageDataSync;
import me.itstheholyblack.testmodpleaseignore.network.PacketHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ManaCalc {
	private String ManaPool = PlayerDataMan.ManaPool;
	private String FocusTag = PlayerDataMan.FocusTag;

	/**
	 * The actual mana calculation. This must happen after all focus
	 * calculations.
	 *
	 * @author Edwan Vi
	 */
	@SubscribeEvent
	public void manaCalc(LivingUpdateEvent event) {
		if (event.getEntity() instanceof EntityPlayer) {
			EntityPlayer e = (EntityPlayer) event.getEntity();
			if (!e.world.isRemote) {
				NBTTagCompound data = e.getEntityData();
				// detect if player has NBT saved
				// if they don't, remedy the situation
				if (!data.hasKey(EntityPlayer.PERSISTED_NBT_TAG)) {
					data.setTag(EntityPlayer.PERSISTED_NBT_TAG, new NBTTagCompound());
				}
				// save into variable
				NBTTagCompound persist = data.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
				if (!persist.hasKey(ManaPool)) {
					persist.setDouble(ManaPool, 0.0D);
				} else {
					if (persist.getFloat(FocusTag) >= 0 && persist.getFloat(ManaPool) < 100000.0F) {
						double value = (persist.getFloat(FocusTag) * persist.getFloat(FocusTag)) / 100.0D;
						PlayerDataMan.addMana(persist, value);
					}
				}
				if (persist.getDouble(ManaPool) < 0) {
					persist.setDouble(ManaPool, 0.0D);
					e.attackEntityFrom(DamageSource.MAGIC, 2.0F);
				}
				// e.sendStatusMessage(new
				// TextComponentString(TextFormatting.BLUE + "Mana: " +
				// Double.toString(persist.getDouble(ManaPool))), true);
				PacketHandler.sendToPlayer(new MessageDataSync(persist.getDouble(ManaPool)), (EntityPlayerMP) e);
			}
		}
	}
}
