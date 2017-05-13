package me.itstheholyblack.testmodpleaseignore.items.casters;

import me.itstheholyblack.testmodpleaseignore.Reference;
import me.itstheholyblack.testmodpleaseignore.core.Raycasting;
import me.itstheholyblack.testmodpleaseignore.items.ModItems;
import me.itstheholyblack.testmodpleaseignore.util.NBTHelper;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class ItemExplosiveCaster extends Item {
    public ItemExplosiveCaster() {
        super();
        this.maxStackSize = 1;
        setRegistryName("explosivecaster");
        setUnlocalizedName(Reference.MODID + "." + "explosivecaster");
        setCreativeTab(ModItems.CREATIVETAB);
        // set property for multitexture
        this.addPropertyOverride(new ResourceLocation("deployed"), new IItemPropertyGetter() {
            @Override
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
                return entityIn != null && entityIn instanceof EntityPlayer
                        && NBTHelper.checkNBT(stack).getTagCompound().getBoolean("isactive") ? 1.0F : 0.0F;
            }
        });
        GameRegistry.register(this);
    }

    // right click
    // I :clap: stole :clap: this :clap: code :clap: from :clap: blood :clap:
    // magic
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
        // save itemstack so w can work on it. Why isn't it an arg anymore n o b
        // o d y k n o w s
        ItemStack stack = playerIn.getHeldItem(hand);
        if (hand == EnumHand.OFF_HAND) {
            return new ActionResult<>(EnumActionResult.FAIL, stack);
        } else {
            if (stack.getTagCompound() == null) {
                stack.setTagCompound(new NBTTagCompound());
                NBTTagCompound compound = stack.getTagCompound();
                compound.setBoolean("isactive", false);
            }
            // use newly created compound
            NBTTagCompound compound = stack.getTagCompound();
            if (playerIn.isSneaking()) {
                compound.setBoolean("isactive", !getActivated(stack));
            } else {
                // it's that time of the day
                // when we R A Y C A S T
                RayTraceResult result = Raycasting.raycast(playerIn, 20.0D);
                if (result == null) {
                    return new ActionResult<>(EnumActionResult.FAIL, stack);
                } else if (getActivated(stack)) {
                    playerIn.getCooldownTracker().setCooldown(this, 20);
                    // server side only
                    if (!playerIn.world.isRemote) {
                        if (result.typeOfHit.equals(RayTraceResult.Type.ENTITY)) {
                            BlockPos pos = result.entityHit.getPosition();
                            playerIn.world.createExplosion(null, pos.getX(), pos.getY(), pos.getZ(), 4.0F, true);
                        } else if (result.typeOfHit.equals(RayTraceResult.Type.BLOCK)) {
                            BlockPos pos = result.getBlockPos();
                            playerIn.world.createExplosion(null, pos.getX(), pos.getY(), pos.getZ(), 4.0F, true);
                        } else {
                            // miss
                            return new ActionResult<>(EnumActionResult.FAIL, stack);
                        }
                    }
                }
            }
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        }
    }

    /**
     * Gets whether or not stack is active
     *
     * @param stack - the ItemStack we want to look at
     * @return The boolean stored in key "isactive" for `stack`. False if the
     * stack has no such key.
     */
    boolean getActivated(ItemStack stack) {
        return stack != null && stack != ItemStack.EMPTY
                && NBTHelper.checkNBT(stack).getTagCompound().getBoolean("isactive");
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
}
