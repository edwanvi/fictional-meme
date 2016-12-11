package me.itstheholyblack.testmodpleaseignore.core;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Class for managing player data.
 * @author Edwan Vi
 */
public class PlayerDataMan {
	public static final String DataTag = "TMPIData.";
	public static final String FocusTag = DataTag + "focusLevel";
	
	@SubscribeEvent
	public void onPlayerTick(LivingUpdateEvent event) {
		if(event.getEntityLiving() instanceof EntityPlayer) {
			// for some reason onPlayerTick isn't always gonna give us a player
			EntityPlayer player = (EntityPlayer) event.getEntityLiving();
			// save the player's data
			NBTTagCompound data = player.getEntityData();
			// detect if player has NBT saved
			// if they don't, remidy the situation
			if (!data.hasKey(EntityPlayer.PERSISTED_NBT_TAG)) {
				data.setTag(EntityPlayer.PERSISTED_NBT_TAG, new NBTTagCompound());
			}
			// save into variable
			NBTTagCompound persist = data.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
			// set up *our* tags
			if (!persist.hasKey(FocusTag)) {
				// no focus
				persist.setInteger(FocusTag, 0);
			} else {
				if (player.isAirBorne) {
					addFocus(persist, -10);
				}
			}
		}
	}
	public void setFocus(NBTTagCompound tagcomp, int value) {
		tagcomp.setInteger(FocusTag, value);
	}
	public void addFocus(NBTTagCompound tagcomp, int value) {
		int oldFocus = tagcomp.getInteger(FocusTag);
		tagcomp.setInteger(FocusTag, oldFocus + value);
	}
}
