package me.itstheholyblack.testmodpleaseignore.blocks;

import me.itstheholyblack.testmodpleaseignore.blocks.tile_entities.TileEntityManaRelay;
import me.itstheholyblack.testmodpleaseignore.items.ModItems;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockManaRelay extends BlockTileEntity<TileEntityManaRelay> {

	public BlockManaRelay() {
		super(Material.WOOD, "manarelay");
		setUnlocalizedName("manarelay");
		GameRegistry.register(this);
		GameRegistry.register(new ItemBlock(this), getRegistryName());
		setCreativeTab(ModItems.CREATIVETAB);
	}

	@Override
	public Class<TileEntityManaRelay> getTileEntityClass() {
		return TileEntityManaRelay.class;
	}

	@Override
	public TileEntityManaRelay createTileEntity(World world, IBlockState state) {
		return new TileEntityManaRelay();
	}

}
