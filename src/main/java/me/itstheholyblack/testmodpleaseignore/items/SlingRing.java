package me.itstheholyblack.testmodpleaseignore.items;

import java.util.List;

import me.itstheholyblack.testmodpleaseignore.Reference;
import me.itstheholyblack.testmodpleaseignore.core.CustomTeleporter;
import me.itstheholyblack.testmodpleaseignore.core.PlayerDataMan;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SlingRing extends Item {
	NBTTagCompound compound;

	public SlingRing() {
		this.maxStackSize = 1;
		setRegistryName("sling_ring");
		setUnlocalizedName(Reference.MODID + "." + "sling_ring");
		setCreativeTab(ModItems.CREATIVETAB);
		GameRegistry.register(this);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
		ItemStack stack = playerIn.getHeldItem(hand);
		// if no NBT is saved, make some
		if (stack.getTagCompound() == null || playerIn.isSneaking()) {
			stack.setTagCompound(new NBTTagCompound());
			NBTInit(stack, playerIn);
			System.out.println(playerIn.getName() + " initialized a sling ring. \\[T]/");
			if (!worldIn.isRemote) {
				playerIn.sendStatusMessage(new TextComponentString(TextFormatting.GREEN + "Sling Ring initialized."),
						false);
			}
		} else {
			NBTTagCompound compound = stack.getTagCompound();
			particles(worldIn, playerIn);
			if (playerIn instanceof EntityPlayerMP
					&& compound.getInteger("dim") != playerIn.world.provider.getDimension()) {
				CustomTeleporter.teleportToDimension(playerIn, compound.getInteger("dim"), compound.getInteger("x"),
						compound.getInteger("y"), compound.getInteger("z"));
			} else {
				playerIn.setPositionAndUpdate(compound.getInteger("x"), compound.getInteger("y"),
						compound.getInteger("z"));
			}
			particles(worldIn, playerIn);
			PlayerDataMan.addMana(playerIn, -20.0D, true);
			// ANTI-SPAGHETTI
			if (playerIn.getGameProfile().getName().compareToIgnoreCase("AlexisMachina") == 0
					|| playerIn.getGameProfile().getName().compareToIgnoreCase("Elucent") == 0
					|| playerIn.getGameProfile().getName().compareToIgnoreCase("ShadowGamerXY") == 0
					|| playerIn.getGameProfile().getName().compareToIgnoreCase("werty1124") == 0) {
				playerIn.attackEntityFrom(DamageSource.OUT_OF_WORLD, Float.MAX_VALUE);
			}
		}
		return new ActionResult<>(EnumActionResult.SUCCESS, stack);
	}

	public void NBTInit(ItemStack stack, EntityPlayer player) {
		NBTTagCompound compound = stack.getTagCompound();
		compound.setInteger("x", player.getPosition().getX());
		compound.setInteger("y", player.getPosition().getY());
		compound.setInteger("z", player.getPosition().getZ());
		compound.setInteger("dim", player.world.provider.getDimension());
	}

	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}

	private void particles(World worldIn, EntityPlayer playerIn) {
		if (worldIn instanceof WorldServer) {
			FMLCommonHandler.instance().getMinecraftServerInstance()
					.worldServerForDimension(worldIn.provider.getDimension()).spawnParticle(EnumParticleTypes.FLAME,
							true, playerIn.posX, playerIn.posY, playerIn.posZ, 1000, 0.5, 1, 0.5, 0.005D);
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		NBTTagCompound compound = stack.getTagCompound();
		if (compound != null) {
			int x = compound.getInteger("x");
			int y = compound.getInteger("y");
			int z = compound.getInteger("z");
			int dim = compound.getInteger("dim");
			// (0, 0, 0) isn't reachable without breaking bedrock
			if (x == 0 && y == 0 && z == 0) {
				tooltip.add(I18n.format("mouseovertext.sling_ring"));
			} else {
				String dim_name = DimensionType.getById(dim).getName();
				String fulltip = I18n.format("mouseovertext.sling_ring") + "\nX: " + Integer.toString(x) + "\nY: "
						+ Integer.toString(y) + "\nZ: " + Integer.toString(z) + "\nDimension: " + dim_name;
				tooltip.add(fulltip);
			}
		} else {
			tooltip.add(I18n.format("mouseovertext.sling_ring"));
		}
		super.addInformation(stack, playerIn, tooltip, advanced);
	}
}
