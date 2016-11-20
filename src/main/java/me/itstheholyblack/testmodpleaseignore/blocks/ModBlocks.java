package me.itstheholyblack.testmodpleaseignore.blocks;

import net.minecraft.block.Block;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class ModBlocks {
	public static TransparentBlock tutorialBlock;
	private static StellarAccelerator s_accel;
	public static void createBlocks() {
		tutorialBlock = new TransparentBlock("firstblock");
		s_accel = new StellarAccelerator();
	}
	@SideOnly(Side.CLIENT)
    public static void initModels() {
		tutorialBlock.initModel();
		s_accel.initModel();
	}
}
