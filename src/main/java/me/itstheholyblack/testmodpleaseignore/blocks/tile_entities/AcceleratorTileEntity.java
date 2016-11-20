package me.itstheholyblack.testmodpleaseignore.blocks.tile_entities;

import javax.annotation.Nullable;

import me.itstheholyblack.testmodpleaseignore.blocks.ModBlocks;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

public class AcceleratorTileEntity extends TileEntity implements ITickable {
	private int count;
	private boolean safe;
	private BlockPos pos = getPos();
	@Override
	public void update() {
		// safe = test_all_faces();
		//if (worldObj.isRemote) {
		if(count > 100 && !safe) {
			this.worldObj.newExplosion(null, getPos().getX(), getPos().getY(), getPos().getZ(), 4.0F, true, true);
			markDirty();
		} else {
			safe = test_all_faces();
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
    public boolean test_all_faces() {
    	boolean pos_x_safe = false;
    	boolean neg_x_safe = false;
    	if (worldObj.getBlockState(pos.add(1, 0, 0)).getBlock() == ModBlocks.tutorialBlock) {
    		pos_x_safe = true;
    	}
    	if (worldObj.getBlockState(pos.add(-1, 0, 0)).getBlock() == ModBlocks.tutorialBlock) {
    		neg_x_safe = true;
    	}
		if (pos_x_safe && neg_x_safe) {
			safe = true;
			markDirty();
		} else {
			safe = false;
			markDirty();
		}
		return safe;
    }
}
