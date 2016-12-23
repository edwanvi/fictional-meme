package me.itstheholyblack.testmodpleaseignore.blocks;

import me.itstheholyblack.testmodpleaseignore.Reference;
import me.itstheholyblack.testmodpleaseignore.mod;
import me.itstheholyblack.testmodpleaseignore.blocks.tile_entities.TileEntitySpellweaver;
import me.itstheholyblack.testmodpleaseignore.core.PlayerDataMan;
import me.itstheholyblack.testmodpleaseignore.items.ModItems;
import me.itstheholyblack.testmodpleaseignore.network.PacketHandler;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

public class BlockSpellweaver extends BlockTileEntity<TileEntitySpellweaver> {

	protected static final AxisAlignedBB CARPET_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.0625D, 1.0D);

	public BlockSpellweaver() {
		super(Material.CARPET, "spellweaver");
		setCreativeTab(ModItems.CREATIVETAB);
		setUnlocalizedName(Reference.MODID + "." + "spellweaver");
		GameRegistry.register(this);
		// why isn't this f*cking implied
		setSoundType(SoundType.CLOTH);
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
				} else if (heldItem.getItem() == ModItems.introMirror) {
					// M A G I C
					ItemStack stack = itemHandler.getStackInSlot(0);
					if (stack.getItem() == Items.FLINT) {
						PlayerDataMan.addMana(player, -10.0D, true);
						((IItemHandlerModifiable) itemHandler).setStackInSlot(0,
								new ItemStack(ModItems.bladeCaster, 1));
					} else if (stack.getItem().equals(ModItems.manaWaste)) {
						// you really shouldn't do this
						this.getTileEntity(world, pos).wrathOfGod();
						((IItemHandlerModifiable) itemHandler).setStackInSlot(0, ItemStack.EMPTY);
					} else if (!stack.isEmpty()) {
						PlayerDataMan.addMana(player, -15.0D, true);
						int numberOfItems = itemHandler.getStackInSlot(0).getCount();
						((IItemHandlerModifiable) itemHandler).setStackInSlot(0,
								new ItemStack(ModItems.manaWaste, numberOfItems));
					}
				} else {
					player.setHeldItem(hand, itemHandler.insertItem(0, heldItem, false));
				}
				tile.markDirty();
				if (world instanceof WorldServer) {
					WorldServer ws = (WorldServer) world;
					for (EntityPlayer wsplayer : ws.playerEntities) {
						EntityPlayerMP playerMP = (EntityPlayerMP) wsplayer;
						 if ((playerMP.getDistanceSq(pos) < 64 * 64) && ws.getPlayerChunkMap().isPlayerWatchingChunk(playerMP, pos.getX() >> 4, pos.getZ() >> 4)) {
							 playerMP.connection.sendPacket(tile.getUpdatePacket()); 
						 }
					}
				}
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
		TileEntitySpellweaver tile = getTileEntity(world, pos);
		IItemHandler itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side);
		System.out.println(itemHandler.getStackInSlot(0));
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
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0,
				new ModelResourceLocation(getRegistryName(), "inventory"));
	}

	/**
	 * Used to determine ambient occlusion and culling when rebuilding chunks
	 * for render
	 */
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	/**
	 * The type of render function called. MODEL for mixed tesr and static
	 * model, MODELBLOCK_ANIMATED for TESR-only, LIQUID for vanilla liquids,
	 * INVISIBLE to skip all rendering
	 */
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return CARPET_AABB;
	}
}
