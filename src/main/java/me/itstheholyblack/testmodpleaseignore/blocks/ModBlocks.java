package me.itstheholyblack.testmodpleaseignore.blocks;

import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class ModBlocks {
    public static TransparentBlock tutorialBlock;
    private static StellarAccelerator s_accel;
    private static BlockSpellweaver s_weaver;
    private static BlockManaRelay m_relay;
    public static BlockPoisonGas m_fumes;

    public static void createBlocks() {
        tutorialBlock = new TransparentBlock("firstblock");
        s_accel = new StellarAccelerator();
        s_weaver = new BlockSpellweaver();
        m_relay = new BlockManaRelay();
        m_fumes = new BlockPoisonGas();
        GameRegistry.registerTileEntity(s_weaver.getTileEntityClass(), s_weaver.getRegistryName().toString());
        GameRegistry.registerTileEntity(m_relay.getTileEntityClass(), m_relay.getRegistryName().toString());
    }

    @SideOnly(Side.CLIENT)
    public static void initModels() {
        tutorialBlock.initModel();
        s_accel.initModel();
        s_weaver.initModel();
        m_fumes.initModel();
    }
}
