package me.itstheholyblack.testmodpleaseignore.items.casters;

import java.util.List;

import javax.annotation.Nullable;

import me.itstheholyblack.testmodpleaseignore.Reference;
import me.itstheholyblack.testmodpleaseignore.core.NBTHelper;
import me.itstheholyblack.testmodpleaseignore.core.PlayerDataMan;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemCasterShield extends Item {
	public ItemCasterShield() {
		super();
		this.maxStackSize = 1;
		this.setRegistryName("shield_caster");
		setUnlocalizedName(Reference.MODID + "." + "shield_caster");
		this.addPropertyOverride(new ResourceLocation("deployed"), new IItemPropertyGetter() {
			@Override
			@SideOnly(Side.CLIENT)
			public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
				return entityIn != null && entityIn instanceof EntityPlayer
						&& NBTHelper.checkNBT(stack).getTagCompound().getBoolean("isActive") ? 1.0F : 0.0F;
			}
		});
		GameRegistry.register(this);
	}

	// right click
	// I :clap: stole :clap: this :clap: code :clap: from :clap: blood :clap:
	// magic
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
		ItemStack stack = playerIn.getHeldItem(hand);
		if (hand == EnumHand.MAIN_HAND) {
			if (stack.getTagCompound() == null) {
				stack.setTagCompound(new NBTTagCompound());
				NBTTagCompound compound = stack.getTagCompound();
				compound.setBoolean("isActive", false);
			}
			NBTTagCompound compound = stack.getTagCompound();
			compound.setBoolean("isActive", !getActivated(stack));
			if (getActivated(stack)) {
				setFull3D();
				NBTTagCompound data = playerIn.getEntityData();
				// detect if player has NBT saved
				// if they don't, remedy the situation
				if (!data.hasKey(EntityPlayer.PERSISTED_NBT_TAG)) {
					data.setTag(EntityPlayer.PERSISTED_NBT_TAG, new NBTTagCompound());
				}
				NBTTagCompound persist = data.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
				String key = "TMPIData.shielded";
				if (!persist.hasKey(key)) {
					persist.setBoolean(key, true);
					PlayerDataMan.addMana(persist, -1);
				} else {
					boolean shield = persist.getBoolean(key);
					if (!shield) {
						PlayerDataMan.addMana(playerIn, -1, true);
					}
					persist.setBoolean(key, !shield);
				}
			}
			return new ActionResult<>(EnumActionResult.SUCCESS, stack);
		} else {
			return new ActionResult<>(EnumActionResult.FAIL, stack);
		}
	}

	boolean getActivated(ItemStack stack) {
		return stack != null && NBTHelper.checkNBT(stack).getTagCompound().getBoolean("isActive");
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		tooltip.add(I18n.format("mouseovertext.hat"));
		super.addInformation(stack, playerIn, tooltip, advanced);
	}

	// cause re-equip
	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return NBTHelper.checkNBT(oldStack).getTagCompound().getBoolean("isActive") != NBTHelper.checkNBT(newStack)
				.getTagCompound().getBoolean("isActive") || oldStack.getItem() != newStack.getItem();
	}

	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}
}
