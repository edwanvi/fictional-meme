package me.itstheholyblack.testmodpleaseignore.core;

import javax.annotation.Nonnull;

import me.itstheholyblack.testmodpleaseignore.network.MessageDataSync;
import me.itstheholyblack.testmodpleaseignore.network.PacketHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
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
	@SubscribeEvent(priority=EventPriority.HIGH)
	public void onPlayerTick(LivingUpdateEvent event) {
		if(event.getEntityLiving() instanceof EntityPlayer) {
			// for some reason onPlayerTick isn't always gonna give us a player
			EntityPlayer player = (EntityPlayer) event.getEntityLiving();
			// save the player's data
			if (!player.world.isRemote) {
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
					persist.setFloat(FocusTag, 0);
					if (player.isInLava()) {
						System.out.println(player.getName() + " is in lava");
						addFocus(persist, -10);
					}
					if (player.isWet()) {
						System.out.println(player.getName() + " is wet");
						addFocus(persist, -2);
					}
					if (player.motionX != 0 || player.motionZ != 0) {
						// addFocus(persist, 0.0F);
					}
					if (player.isBurning()) {
						System.out.println(player.getName() + " is burning");
						addFocus(persist, -15.0F);
					}
					if (player.isPlayerSleeping()) {
						addFocus(persist, 10.0F);
					}
					if (player.isCollided) {
						// addFocus(persist, -0.5F);
					}
					if (player.isElytraFlying()) {
						addFocus(persist, 20.0F);
					}
					if (player.isSneaking()) {
						addFocus(persist, 1.0F);
					}
				}
			}
		}
	}
	@SubscribeEvent(priority=EventPriority.HIGH)
	public void ItemUse(LivingEntityUseItemEvent.Tick event) {
		if(event.getEntityLiving() instanceof EntityPlayer) {
			EntityPlayer e = (EntityPlayer) event.getEntityLiving();
			if (!e.world.isRemote) {
				ItemStack heldstack = event.getItem();
				Item helditem = heldstack.getItem();
				if (helditem == Items.BOW) {
					NBTTagCompound data = e.getEntityData();
					// detect if player has NBT saved
					// if they don't, remedy the situation
					if (!data.hasKey(EntityPlayer.PERSISTED_NBT_TAG)) {
						data.setTag(EntityPlayer.PERSISTED_NBT_TAG, new NBTTagCompound());
					}
					// save into variable
					NBTTagCompound persist = data.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
					// float f = event.getDuration() * event.getDuration();
					addFocus(persist, 2.5F);
				}
			}
		}
	}
	/**
	 * The actual mana calculation. This must happen after all focus calculations.
	 * @author Edwan Vi
	 */
	@SubscribeEvent(priority=EventPriority.LOWEST)
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
						double value = (persist.getFloat(FocusTag) * persist.getFloat(FocusTag))/100.0D;
						addMana(persist, value);
					}
				}
				if (persist.getDouble(ManaPool) < 0) {
					persist.setDouble(ManaPool, 0.0D);
					e.attackEntityFrom(DamageSource.MAGIC, 2.0F);
				}
				// e.sendStatusMessage(new TextComponentString(TextFormatting.BLUE + "Mana: " + Double.toString(persist.getDouble(ManaPool))), true);
				// System.out.println(Double.toString(persist.getDouble(ManaPool)));
				PacketHandler.sendToPlayer(new MessageDataSync(persist.getDouble(ManaPool)), (EntityPlayerMP) e);
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
	public void addMana(NBTTagCompound tagcomp, double value) {
		double oldMana = tagcomp.getFloat(ManaPool);
		tagcomp.setDouble(ManaPool, oldMana + value);
	}
}
