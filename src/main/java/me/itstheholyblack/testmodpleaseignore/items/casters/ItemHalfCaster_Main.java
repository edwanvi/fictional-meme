package me.itstheholyblack.testmodpleaseignore.items.casters;

import javax.annotation.Nullable;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import me.itstheholyblack.testmodpleaseignore.Reference;
import me.itstheholyblack.testmodpleaseignore.core.NBTHelper;
import me.itstheholyblack.testmodpleaseignore.items.ModItems;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemHalfCaster_Main extends ItemSword {
	// the main hand of the fan-style caster
	// fast, not too hard hitting
	public ItemHalfCaster_Main() {
		super(Item.ToolMaterial.GOLD);
		this.maxStackSize = 1;
		setRegistryName("halfCaster_Main");
		setUnlocalizedName(Reference.MODID + "." + "halfCaster_Main");
		setCreativeTab(ModItems.CREATIVETAB);
		// set property for multitexture
		this.addPropertyOverride(new ResourceLocation("deployed"), new IItemPropertyGetter() {
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
                return entityIn != null && entityIn instanceof EntityPlayer && NBTHelper.checkNBT(stack).getTagCompound().getBoolean("isActive") ? 1.0F : 0.0F;
            }
		});
		GameRegistry.register(this);
	}
	// right click
	// I :clap: stole :clap: this :clap: code :clap: from :clap: blood :clap: magic
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World worldIn, EntityPlayer playerIn, EnumHand hand) {
		// this caster will *only* activate in the main hand
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
			}
		}
		return new ActionResult<>(EnumActionResult.SUCCESS, stack);
	}
	/**
	 * Gets whether or not stack is active
	 * @param stack - the ItemStack we want to look at
	 * @return The boolean stored in key "isActive" for `stack`. False if the stack has no such key.*/
	boolean getActivated(ItemStack stack){
		return stack != null && NBTHelper.checkNBT(stack).getTagCompound().getBoolean("isActive");
	}
	/**
	 * Sets caster damage and attack speed. The attack speed is constant and damage operates on the return of getActivated.
	 * */
	@Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot equipmentSlot, ItemStack stack) {
        Multimap<String, AttributeModifier> multimap = HashMultimap.<String, AttributeModifier>create();
        if (equipmentSlot == EntityEquipmentSlot.MAINHAND) {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getAttributeUnlocalizedName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", getActivated(stack) ? 8 : 2, 0));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getAttributeUnlocalizedName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -5.0, 0));
        }
        return multimap;
    }
	/**
	 * Causes a re-equip animation when oldStack and newStack have differing values for NBT tag isActive.
	 */
	@Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return NBTHelper.checkNBT(oldStack).getTagCompound().getBoolean("isActive") != NBTHelper.checkNBT(newStack).getTagCompound().getBoolean("isActive");
    }
}