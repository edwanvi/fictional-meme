package me.itstheholyblack.testmodpleaseignore.items;

import me.itstheholyblack.testmodpleaseignore.Reference;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class SlingRing extends Item {
	// @Override
	public SlingRing() {
		this.maxStackSize = 1;
		setRegistryName("sling_ring");
		setUnlocalizedName(Reference.MODID + "." + "sling_ring");
	}
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World worldIn, EntityPlayer playerIn, EnumHand hand) {
		// if no NBT is saved, make some
		if (stack.getTagCompound() == null) {
			stack.setTagCompound(new NBTTagCompound());
			NBTInit(stack);
			System.out.println(playerIn.getName() + " initialized a sling ring. \\[T]/");
			playerIn.addChatComponentMessage(new TextComponentString(TextFormatting.GREEN + "Sling Ring initialized."));
		} else {
			NBTTagCompound compound = stack.getTagCompound();
			playerIn.setPositionAndUpdate(compound.getInteger("x"), compound.getInteger("y"), compound.getInteger("z"));
		}
		return new ActionResult<>(EnumActionResult.SUCCESS, stack);
	}
	public void NBTInit(ItemStack stack) {
		NBTTagCompound compound = stack.getTagCompound();
		compound.setInteger("x", 0);
		compound.setInteger("y", 0);
		compound.setInteger("z", 0);
	}
}
