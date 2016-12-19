package me.itstheholyblack.testmodpleaseignore.blocks;

import me.itstheholyblack.testmodpleaseignore.blocks.tile_entities.TileEntitySpellweaver;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class ModBlocks {
	public static TransparentBlock tutorialBlock;
	private static StellarAccelerator s_accel;
	private static BlockSpellweaver s_weaver;

	public static void createBlocks() {
		tutorialBlock = new TransparentBlock("firstblock");
		s_accel = new StellarAccelerator();
		s_weaver = new BlockSpellweaver();
		GameRegistry.registerTileEntity(TileEntitySpellweaver.class, s_weaver.getRegistryName().toString());
	}

	@SideOnly(Side.CLIENT)
	public static void initModels() {
		tutorialBlock.initModel();
		s_accel.initModel();
		s_weaver.initModel();
	}
}
