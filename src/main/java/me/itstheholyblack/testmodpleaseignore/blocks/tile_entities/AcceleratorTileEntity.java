package me.itstheholyblack.testmodpleaseignore.blocks.tile_entities;

import me.itstheholyblack.testmodpleaseignore.blocks.ModBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class AcceleratorTileEntity extends TileEntity implements ITickable {
	private int count;
	private float mana;
	private boolean safe;
	private BlockPos pos = getPos();

	@Override
	public void update() {
		if (count > 100 && !safe) {
			this.world.newExplosion(null, getPos().getX(), getPos().getY(), getPos().getZ(), 4.0F, true, true);
			markDirty();
		} else {
			safe = test_all_faces();
			if (!safe) {
				increase();
			} else {
				if (count < 0) {
					decrease();
				} // else no-op on count
				mana = mana + 0.0001F;
			}
			markDirty();
		}
	}

	public int increase() {
		count++;
		markDirty();
		return count;
	}

	public int decrease() {
		count--;
		markDirty();
		return count;
	}

	public float getMana() {
		return this.mana;
	}
	
	// read and write NBT data
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		count = compound.getInteger("counter");
		mana = compound.getFloat("iveforgottenmyname");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setInteger("counter", count);
		compound.setFloat("iveforgottenmyname", mana);
		return compound;
	}

	public boolean test_all_faces() {
		for (EnumFacing facing : EnumFacing.VALUES) {
			IBlockState state = getWorld().getBlockState(getPos().offset(facing));
			if (state.getBlock() == ModBlocks.tutorialBlock)
				return true;
		}
		return false;
	}
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
