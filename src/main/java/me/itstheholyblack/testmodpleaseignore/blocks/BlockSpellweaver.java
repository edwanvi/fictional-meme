package me.itstheholyblack.testmodpleaseignore.blocks;

import me.itstheholyblack.testmodpleaseignore.mod;
import me.itstheholyblack.testmodpleaseignore.blocks.tile_entities.TileEntitySpellweaver;
import me.itstheholyblack.testmodpleaseignore.items.ModItems;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class BlockSpellweaver extends BlockTileEntity<TileEntitySpellweaver> {
	
	protected static final AxisAlignedBB BED_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5625D, 1.0D);
	
	public BlockSpellweaver() {
		super(Material.CARPET, "spellweaver");
		setCreativeTab(ModItems.CREATIVETAB);
		GameRegistry.register(this);
        GameRegistry.register(new ItemBlock(this), getRegistryName());
	}

	@Override
	public Class getTileEntityClass() {
		return TileEntitySpellweaver.class;
	}

	@Override
	public TileEntitySpellweaver createTileEntity(World world, IBlockState state) {
		return new TileEntitySpellweaver();
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {
			TileEntitySpellweaver tile = getTileEntity(world, pos);
			IItemHandler itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side);
			ItemStack heldItem = player.getHeldItem(hand);
			if (!player.isSneaking()) {
				if (heldItem.isEmpty()) {
					player.setHeldItem(hand, itemHandler.extractItem(0, 64, false));
				} else {
					player.setHeldItem(hand, itemHandler.insertItem(0, heldItem, false));
				}
				tile.markDirty();
			} else {
				ItemStack stack = itemHandler.getStackInSlot(0);
				if (!stack.isEmpty()) {
					String localized = mod.proxy.localize(stack.getUnlocalizedName() + ".name");
					player.sendMessage(new TextComponentString(stack.getCount() + "x " + localized));
				} else {
					player.sendMessage(new TextComponentString("Empty"));
				}
			}
		}
		return true;
	}
	// various vanilla overrides
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		TileEntitySpellweaver tile = getTileEntity(world, pos);
		IItemHandler itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);
		ItemStack stack = itemHandler.getStackInSlot(0);
		if (!stack.isEmpty()) {
			EntityItem item = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), stack);
			world.spawnEntity(item);
		}
		super.breakBlock(world, pos, state);
	}
	@SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
    @Override
    public boolean isOpaqueCube(IBlockState state) {
    	return false;
    }
    @Override
    public boolean isFullBlock(IBlockState state) {
    	return false;
    }
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return BED_AABB;
    }
}