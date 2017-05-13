package me.itstheholyblack.testmodpleaseignore.core;

import com.google.common.base.Predicate;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import javax.annotation.Nullable;

public class LibMisc {

    public static Predicate<Entity> PLAYER_SELECTOR = new Predicate<Entity>() {
        public boolean apply(@Nullable Entity p_apply_1_) {
            return p_apply_1_ instanceof EntityPlayer;
        }
    };

    public static TileEntity getTileEntitySafely(IBlockAccess blockAccess, BlockPos pos) {
        if (blockAccess instanceof ChunkCache) {
            return ((ChunkCache) blockAccess).getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK);
        } else {
            return blockAccess.getTileEntity(pos);
        }
    }

    /**
     * Generate a sphere.
     *
     * @param world         the world to put the sphere in
     * @param pos           the position to draw the sphere at
     * @param materialBlock the block to build with
     * @author WayOfTime
     */
    public static void makeSphere(World world, BlockPos pos, IBlockState materialBlock, int radius) {
        double floatingRadius = (double) radius;
        for (int i = -radius; i <= radius; i++) {
            for (int j = -radius; j <= radius; j++) {
                for (int k = -radius; k <= radius; k++) {
                    if (i * i + j * j + k * k > (floatingRadius + 0.5) * (floatingRadius + 0.5)) {
                        continue;
                    }

                    BlockPos newPos = pos.add(i, j, k);
                    IBlockState state = world.getBlockState(newPos);

                    if (world.isAirBlock(newPos)) {
                        world.setBlockState(newPos, materialBlock);
                    }
                }
            }
        }
    }
}
