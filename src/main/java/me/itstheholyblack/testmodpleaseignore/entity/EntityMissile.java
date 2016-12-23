package me.itstheholyblack.testmodpleaseignore.entity;

import java.util.List;

import com.google.common.base.Predicates;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

/**
 * @author Vazkii unless otherwise stated.
 */
public class EntityMissile extends EntityThrowable {
	// variable to store the target of this missile
	private static final DataParameter<Integer> TARGET = EntityDataManager.createKey(EntityMissile.class,
			DataSerializers.VARINT);
	// time alive
	int time = 0;
	// nbt key for time alive
	private static final String TAG_TIME = "time";
	// homing variables
	double lockX, lockY = -1, lockZ;
	private EntityPlayer closestPlayer;

	public EntityMissile(World worldIn) {
		super(worldIn);
		// set our size to *nothing*
		setSize(0.5F, 0.1F);
	}

	@Override
	protected void entityInit() {
		dataManager.register(TARGET, 0);
	}

	public EntityMissile(EntityLivingBase thrower) {
		this(thrower.world);
	}
	
	@Override
	public void onUpdate() {
		double lastTickPosX = this.lastTickPosX;
		double lastTickPosY = this.lastTickPosY;
		double lastTickPosZ = this.lastTickPosZ;

		super.onUpdate();

		if (this.closestPlayer == null || this.closestPlayer.getDistanceSqToEntity(this) > 64.0D) {
			this.closestPlayer = this.world.getClosestPlayerToEntity(this, 8.0D);
		}
		// don't go for specators
		if (this.closestPlayer != null && this.closestPlayer.isSpectator()) {
			this.closestPlayer = null;
		}
		if (this.closestPlayer != null) {
			double d1 = (this.closestPlayer.posX - this.posX) / 8.0D;
			double d2 = (this.closestPlayer.posY + this.closestPlayer.getEyeHeight() / 2.0D - this.posY) / 8.0D;
			double d3 = (this.closestPlayer.posZ - this.posZ) / 8.0D;
			double d4 = Math.sqrt(d1 * d1 + d2 * d2 + d3 * d3);
			double d5 = 1.0D - d4;

			if (d5 > 0.0D) {
				d5 = d5 * d5;
				this.motionX += d1 / d4 * d5 * 0.1D;
				this.motionY += d2 / d4 * d5 * 0.1D;
				this.motionZ += d3 / d4 * d5 * 0.1D;
			}
		}

		this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
		if (time > 40)
			this.kill();
		// increment tick counter
		time++;
	}

	// read/write nbt, save time
	@Override
	public void writeEntityToNBT(NBTTagCompound cmp) {
		super.writeEntityToNBT(cmp);
		cmp.setInteger(TAG_TIME, time);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound cmp) {
		super.readEntityFromNBT(cmp);
		time = cmp.getInteger(TAG_TIME);
	}

	@Override
	protected void onImpact(RayTraceResult result) {
		if (result.typeOfHit == RayTraceResult.Type.ENTITY) {
			Entity e = result.entityHit;
			if (e instanceof EntityMissile || e instanceof EntityGeminus_M || e instanceof EntityGeminus_F) {
				// no-op
			} else {
				e.attackEntityFrom(DamageSource.MAGIC, 1.0F);
			}
			this.kill();
		}
	}
	
	@Override
	protected float getGravityVelocity() {
		return 0.0F;
	}
}
