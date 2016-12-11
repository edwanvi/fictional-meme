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
	// yes mana is stupidly generic get over it
	public static final String ManaPool = DataTag + "manaPool";
	
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
				persist.setFloat(FocusTag, 0);
			} else {
				// various condition/response things, PRs to this list welcome
				if (player.isAirBorne) {
					addFocus(persist, -10);
				}
				if (player.isWet()) {
					addFocus(persist, -2);
				}
				if (player.motionX != 0 || player.motionZ != 0) {
					addFocus(persist, 0.0F);
				}
				if (player.isBurning()) {
					addFocus(persist, -15);
				}
				if (player.isPlayerSleeping()) {
					addFocus(persist, 10);
				}
				if (player.isCollided) {
					addFocus(persist, -0.5F);
				}
				if (player.isElytraFlying()) {
					addFocus(persist, 1.0F);
				}
			}
			if (!persist.hasKey(ManaPool)) {
				persist.setDouble(ManaPool, 0.0D);
			} else {
				if (persist.getFloat(FocusTag) >= 0) {
					double value = Math.sqrt(persist.getDouble(FocusTag));
					persist.setDouble(ManaPool, value);
				} else {
					persist.setDouble(ManaPool, persist.getFloat(FocusTag));
				}
			}
		}
	}
	public void setFocus(NBTTagCompound tagcomp, float value) {
		tagcomp.setFloat(FocusTag, value);
	}
	public void addFocus(NBTTagCompound tagcomp, float value) {
		float oldFocus = tagcomp.getFloat(FocusTag);
		tagcomp.setFloat(FocusTag, oldFocus + value);
	}
}
