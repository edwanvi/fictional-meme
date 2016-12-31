package me.itstheholyblack.testmodpleaseignore.blocks.tile_entities;

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
	private IBlockState input_block;
	private IBlockState output_block;
	
	@Override
	public void tick() {

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
