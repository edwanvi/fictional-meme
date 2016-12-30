package me.itstheholyblack.testmodpleaseignore.blocks;

import me.itstheholyblack.testmodpleaseignore.Reference;
import me.itstheholyblack.testmodpleaseignore.blocks.tile_entities.AcceleratorTileEntity;
import me.itstheholyblack.testmodpleaseignore.items.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class StellarAccelerator extends BlockTileEntity<AcceleratorTileEntity> implements ITileEntityProvider {
	// property to store facing
	public static final PropertyDirection FACING = PropertyDirection.create("facing");

	public StellarAccelerator() {
		super(Material.IRON, "acceleratorblock");
		setUnlocalizedName("acceleratorblock");
		GameRegistry.register(this);
		GameRegistry.register(new ItemBlock(this), getRegistryName());
		// register tile entity
		GameRegistry.registerTileEntity(AcceleratorTileEntity.class, Reference.MODID + "_acceleratorblock");
		// set default face as north
		setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
		setCreativeTab(ModItems.CREATIVETAB);
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack heldstack = playerIn.getHeldItem(hand);
		float mana = this.getTileEntity(worldIn, pos).getMana();
		if (heldstack.getItem().equals(ModItems.introMirror) && !worldIn.isRemote) {
			String manastr = Float.toString(mana);
			playerIn.sendStatusMessage(new TextComponentString("<" + TextFormatting.BLUE + "" + TextFormatting.OBFUSCATED + "Accelerator" + TextFormatting.RESET + "> " + manastr), false);
		}
		return false;
	}
	
	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0,
				new ModelResourceLocation(getRegistryName(), "inventory"));
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		world.setBlockState(pos, state.withProperty(FACING, getFacingFromEntity(pos, placer)), 2);
	}

	public static EnumFacing getFacingFromEntity(BlockPos clickedBlock, EntityLivingBase entity) {
		return EnumFacing.getFacingFromVector((float) (entity.posX - clickedBlock.getX()),
				(float) (entity.posY - clickedBlock.getY()), (float) (entity.posZ - clickedBlock.getZ()));
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(FACING, EnumFacing.getFront(meta & 7));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).getIndex();
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING);
	}

	@Override
	public Class<AcceleratorTileEntity> getTileEntityClass() {
		// TODO Auto-generated method stub
		return AcceleratorTileEntity.class;
	}

	@Override
	public AcceleratorTileEntity createTileEntity(World world, IBlockState state) {
		// TODO Auto-generated method stub
		return new AcceleratorTileEntity();
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		// TODO Auto-generated method stub
		return new AcceleratorTileEntity();
	}
}
