package me.itstheholyblack.testmodpleaseignore.blocks;

import me.itstheholyblack.testmodpleaseignore.blocks.tile_entities.TileEntityManaRelay;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;

public class BlockManaRelay extends BlockTileEntity<TileEntityManaRelay> {

	public BlockManaRelay() {
		super(Material.WOOD, "manarelay");
	}

	@Override
	public Class<TileEntityManaRelay> getTileEntityClass() {
		return TileEntityManaRelay.class;
	}

	@Override
	public TileEntityManaRelay createTileEntity(World world, IBlockState state) {
		// TODO Auto-generated method stub
		return new TileEntityManaRelay();
	}

}
