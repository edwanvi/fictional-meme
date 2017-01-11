package me.itstheholyblack.testmodpleaseignore.blocks;

import me.itstheholyblack.testmodpleaseignore.blocks.tile_entities.TileEntityManaRelay;
import me.itstheholyblack.testmodpleaseignore.blocks.tile_entities.TileEntitySpellweaver;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class ModBlocks {
	public static TransparentBlock tutorialBlock;
	private static StellarAccelerator s_accel;
	private static BlockSpellweaver s_weaver;
	private static BlockManaRelay m_relay;

	public static void createBlocks() {
		tutorialBlock = new TransparentBlock("firstblock");
		s_accel = new StellarAccelerator();
		s_weaver = new BlockSpellweaver();
		m_relay = new BlockManaRelay();
		GameRegistry.registerTileEntity(s_weaver.getTileEntityClass(), s_weaver.getRegistryName().toString());
		GameRegistry.registerTileEntity(m_relay.getTileEntityClass(), m_relay.getRegistryName().toString());
	}

	@SideOnly(Side.CLIENT)
	public static void initModels() {
		tutorialBlock.initModel();
		s_accel.initModel();
		s_weaver.initModel();
	}
}
