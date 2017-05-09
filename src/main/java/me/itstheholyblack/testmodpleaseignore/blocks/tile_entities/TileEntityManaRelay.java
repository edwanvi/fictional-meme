package me.itstheholyblack.testmodpleaseignore.blocks.tile_entities;

import me.itstheholyblack.testmodpleaseignore.core.IMana;
import me.itstheholyblack.testmodpleaseignore.core.LibMisc;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.ITickable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityManaRelay extends TileEntity implements ITickable {

    private float manaheld;
    private float manamax;
    private BlockPos input_block;
    private BlockPos output_block;

    @Override
    public void tick() {
        if (input_block == null || output_block == null) {
            return;
        } else {
            // i'm going to trust that you gave me a valid te here
            TileEntity in_te = LibMisc.getTileEntitySafely(world, input_block);
            TileEntity out_te = LibMisc.getTileEntitySafely(world, output_block);
            if (in_te instanceof IMana) {
                ((IMana) in_te).extractEnergy(10, false);
            }
            if (out_te instanceof IMana) {
                ((IMana) out_te).receiveEnergy(10, false);
            }
        }
    }

    public void setIn(BlockPos in) {
        this.input_block = in;
    }

    public BlockPos getIn() {
        return this.input_block;
    }

    public void setOut(BlockPos out) {
        output_block = out;
    }

    public BlockPos getOut() {
        return output_block;
    }

    // data sync code
    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return oldState.getBlock() != newState.getBlock();
    }

    @Override
    public final SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(getPos(), -999, writeToNBT(new NBTTagCompound()));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public final void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        readFromNBT(pkt.getNbtCompound());
    }

    @Override
    public final NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    @Override
    public final void handleUpdateTag(NBTTagCompound tag) {
        readFromNBT(tag);
    }
}
