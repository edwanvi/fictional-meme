package me.itstheholyblack.testmodpleaseignore.blocks.tile_entities;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntitySpellweaver extends TileEntity implements ITickable {
	private ItemStackHandler inventory = new ItemStackHandler(1);
	public int tickCount;
	public float pageFlip;
	public float pageFlipPrev;
	public float flipT;
	public float flipA;
	public float bookSpread;
	public float bookSpreadPrev;
	public float bookRotation;
	public float bookRotationPrev;
	public float tRot;
	private static final Random rand = new Random();
	private String customName;

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setTag("inventory", inventory.serializeNBT());
		return super.writeToNBT(compound);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		inventory.deserializeNBT(compound.getCompoundTag("inventory"));
		super.readFromNBT(compound);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (T) inventory
				: super.getCapability(capability, facing);
	}
	public ItemStackHandler getInv() {
		return inventory;
	} 

	// ~~ BEGIN TileEntityEnchantmentTable CODE ~~
	@Override
	public void update() {
		this.bookSpreadPrev = this.bookSpread;
		this.bookRotationPrev = this.bookRotation;
		EntityPlayer entityplayer = this.world.getClosestPlayer(this.pos.getX() + 0.5F,
				this.pos.getY() + 0.5F, this.pos.getZ() + 0.5F, 3.0D, false);

		if (entityplayer != null) {
			double d0 = entityplayer.posX - (this.pos.getX() + 0.5F);
			double d1 = entityplayer.posZ - (this.pos.getZ() + 0.5F);
			this.tRot = (float) net.minecraft.util.math.MathHelper.atan2(d1, d0);
			this.bookSpread += 0.1F;

			if (this.bookSpread < 0.5F || rand.nextInt(40) == 0) {
				float f1 = this.flipT;

				while (true) {
					this.flipT += rand.nextInt(4) - rand.nextInt(4);

					if (f1 != this.flipT) {
						break;
					}
				}
			}
		} else {
			this.tRot += 0.02F;
			this.bookSpread -= 0.1F;
		}

		while (this.bookRotation >= (float) Math.PI) {
			this.bookRotation -= ((float) Math.PI * 2F);
		}

		while (this.bookRotation < -(float) Math.PI) {
			this.bookRotation += ((float) Math.PI * 2F);
		}

		while (this.tRot >= (float) Math.PI) {
			this.tRot -= ((float) Math.PI * 2F);
		}

		while (this.tRot < -(float) Math.PI) {
			this.tRot += ((float) Math.PI * 2F);
		}

		float f2;

		for (f2 = this.tRot - this.bookRotation; f2 >= (float) Math.PI; f2 -= ((float) Math.PI * 2F)) {
			;
		}

		while (f2 < -(float) Math.PI) {
			f2 += ((float) Math.PI * 2F);
		}

		this.bookRotation += f2 * 0.4F;
		this.bookSpread = net.minecraft.util.math.MathHelper.clamp(this.bookSpread, 0.0F, 1.0F);
		++this.tickCount;
		this.pageFlipPrev = this.pageFlip;
		float f = (this.flipT - this.pageFlip) * 0.4F;
		float f3 = 0.2F;
		f = net.minecraft.util.math.MathHelper.clamp(f, -0.2F, 0.2F);
		this.flipA += (f - this.flipA) * 0.9F;
		this.pageFlip += this.flipA;
	}
	// ~~ END TileEntityEnchantmentTable CODE ~~
	public void wrathOfGod() {
		BlockPos pos = this.getPos();
		world.addWeatherEffect(new EntityLightningBolt(world, pos.getX(), pos.getY(), pos.getZ(), false));
	}
}
