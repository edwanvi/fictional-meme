package me.itstheholyblack.testmodpleaseignore.world.gen;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.DataFixesManager;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;

/**
 * basically stolen from UpcraftLP
 */
public class NBTStructLoader {
    public static final DataFixer fixer = DataFixesManager.createFixer();

    public static void loadFromNBT(World world, BlockPos pos, NBTTagCompound compound, PlacementSettings settings) {
        Template t = new Template();
        t.read(fixer.process(FixTypes.STRUCTURE, compound));
        t.addBlocksToWorld(world, pos, settings);
    }
}
