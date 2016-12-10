package me.itstheholyblack.testmodpleaseignore.items.casters;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import me.itstheholyblack.testmodpleaseignore.Reference;
import me.itstheholyblack.testmodpleaseignore.core.NBTHelper;
import me.itstheholyblack.testmodpleaseignore.core.Raycasting;
import me.itstheholyblack.testmodpleaseignore.items.ModItems;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
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
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemHalfCaster_Off extends ItemSword {
	// standard constructor
	public ItemHalfCaster_Off() {
		super(Item.ToolMaterial.GOLD);
		this.maxStackSize = 1;
		setRegistryName("halfCaster_Off");
		setUnlocalizedName(Reference.MODID + "." + "halfCaster_Off");
		setCreativeTab(ModItems.CREATIVETAB);
		// set property for multitexture
		this.addPropertyOverride(new ResourceLocation("deployed"), new IItemPropertyGetter() {
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
            	boolean condidtion = entityIn != null && entityIn instanceof EntityPlayer && NBTHelper.checkNBT(stack).getTagCompound().getBoolean("isactive");
                return condidtion ? 1.0F : 0.0F;
            }
		});
		GameRegistry.register(this);
	}
	// right click
	// I :clap: stole :clap: this :clap: code :clap: from :clap: blood :clap: magic
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
		ItemStack stack = playerIn.getHeldItem(hand);
		// this caster will *only* activate in the off hand
		if (hand == EnumHand.OFF_HAND) {
			if (stack.getTagCompound() == null) {
				stack.setTagCompound(new NBTTagCompound());
				NBTTagCompound compound = stack.getTagCompound();
				compound.setBoolean("isactive", false);
			}
			NBTTagCompound compound = stack.getTagCompound();
			if (playerIn.isSneaking()) {
				compound.setBoolean("isactive", !getActivated(stack));
			} else {
				// Get the entity the player is looking at, and harm it
				Entity hit_entity = Raycasting.getEntityLookedAt(playerIn);
				if (hit_entity != null) {
					System.out.println("Hit!");
					hit_entity.attackEntityFrom(DamageSource.MAGIC, 4);
					playerIn.getCooldownTracker().setCooldown(this, 20);
				}
			}
			if (getActivated(stack)) {
				setFull3D();
			}
			return new ActionResult<>(EnumActionResult.SUCCESS, stack);
		} else {
			return new ActionResult<>(EnumActionResult.FAIL, stack);
		}
	}
	/**
	 * Gets whether or not stack is active
	 * @param stack - the ItemStack we want to look at
	 * @return The boolean stored in key "isactive" for `stack`. False if the stack has no such key.
	 */
	boolean getActivated(ItemStack stack){
		return stack != null && stack != ItemStack.EMPTY && NBTHelper.checkNBT(stack).getTagCompound().getBoolean("isactive");
	}
	/**
	 * Sets caster damage and attack speed. The attack speed is constant and damage operates on the return of getActivated.
	 */
	@Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot equipmentSlot, ItemStack stack) {
        Multimap<String, AttributeModifier> multimap = HashMultimap.<String, AttributeModifier>create();
        if (equipmentSlot == EntityEquipmentSlot.MAINHAND) {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", getActivated(stack) ? 4 : 0, 0)); // add 0 if not active
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", 5, 0));
        }
        return multimap;
    }
	/**
	 * Causes a re-equip animation when oldStack and newStack have differing values for NBT tag isactive.
	 */
	@Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return NBTHelper.checkNBT(oldStack).getTagCompound().getBoolean("isactive") != NBTHelper.checkNBT(newStack).getTagCompound().getBoolean("isactive") | oldStack.getItem() != newStack.getItem();
    }
	/**
	 * Initialize the model for this item
	 */
	@SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
	@SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		tooltip.add(I18n.format("mouseovertext.half_caster_off"));
		super.addInformation(stack, playerIn, tooltip, advanced);
	}
}
