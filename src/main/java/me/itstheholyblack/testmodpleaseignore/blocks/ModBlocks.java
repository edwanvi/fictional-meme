package me.itstheholyblack.testmodpleaseignore.blocks;

import net.minecraft.block.Block;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class ModBlocks {
	public static TransparentBlock tutorialBlock;
	public static void createBlocks() {
		tutorialBlock = new TransparentBlock("firstblock");
	}
	@SideOnly(Side.CLIENT)
    public static void initModels() {
		tutorialBlock.initModel();
	}
}
