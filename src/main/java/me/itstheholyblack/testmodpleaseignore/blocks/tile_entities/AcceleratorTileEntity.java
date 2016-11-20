package me.itstheholyblack.testmodpleaseignore.blocks.tile_entities;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

public class AcceleratorTileEntity extends TileEntity implements ITickable {
	private int count;
	private boolean ruined = false;
	private BlockPos pos = getPos();
	@Override
	public void update() {
		//if (worldObj.isRemote) {
		if(count > 100 && !ruined) {
			this.worldObj.newExplosion(null, getPos().getX(), getPos().getY(), getPos().getZ(), 4.0F, true, true);
			ruined = true;
			markDirty();
		} else if (ruined) {
			worldObj.setBlockState(pos, Blocks.AIR.getDefaultState());
			this.invalidate();
		} else {
			increase();
		}
	}
	public int increase() {
		count++;
		// tell minecraft this data needs to be saved
		markDirty();
		return count;
	}
	public int decrease() {
		count--;
		markDirty();
		return count;
	}
	// read and write NBT data
	@Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        count = compound.getInteger("counter");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("counter", count);
        return compound;
    }
}
