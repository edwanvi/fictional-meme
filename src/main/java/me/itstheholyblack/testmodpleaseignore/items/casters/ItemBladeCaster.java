package me.itstheholyblack.testmodpleaseignore.items.casters;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import me.itstheholyblack.testmodpleaseignore.Reference;
import me.itstheholyblack.testmodpleaseignore.core.NBTHelper;
import me.itstheholyblack.testmodpleaseignore.items.ModItems;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemBladeCaster extends ItemSword {
	public ItemBladeCaster() {
		super(Item.ToolMaterial.GOLD);
		this.maxStackSize = 1;
		setRegistryName("bladeCaster");
		setUnlocalizedName(Reference.MODID + "." + "bladeCaster");
		setCreativeTab(ModItems.CREATIVETAB);
		GameRegistry.register(this);
	}
	// right click
	// I :clap: stole :clap: this :clap: code :clap: from :clap: blood :clap: magic
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World worldIn, EntityPlayer playerIn, EnumHand hand) {
		if (hand == EnumHand.MAIN_HAND) {
			if (stack.getTagCompound() == null) {
				stack.setTagCompound(new NBTTagCompound());
				NBTTagCompound compound = stack.getTagCompound();
				compound.setBoolean("isActive", false);
			}
			NBTTagCompound compound = stack.getTagCompound();
			compound.setBoolean("isActive", !getActivated(stack));
		}
		return new ActionResult<>(EnumActionResult.SUCCESS, stack);
	}
	boolean getActivated(ItemStack stack){
		return stack != null && NBTHelper.checkNBT(stack).getTagCompound().getBoolean("isActive");
	}
	@Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot equipmentSlot, ItemStack stack)
    {
        Multimap<String, AttributeModifier> multimap = HashMultimap.<String, AttributeModifier>create();
        if (equipmentSlot == EntityEquipmentSlot.MAINHAND)
        {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getAttributeUnlocalizedName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", getActivated(stack) ? 8 : 2, 0));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getAttributeUnlocalizedName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -2.4, 0));
        }
        return multimap;
    }
	// cause re-equip
	@Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return oldStack.getItem() != newStack.getItem();
    }
}
