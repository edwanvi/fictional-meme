package me.itstheholyblack.testmodpleaseignore.entity;

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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

/**
 * @author Vazkii unless otherwise stated.
 */
public class EntityMissile extends EntityThrowable {
	// variable to store the target of this missile
	private static final DataParameter<BlockPos> TARGET = EntityDataManager.createKey(EntityMissile.class,
			DataSerializers.BLOCK_POS);
	// time alive
	int time = 0;
	// nbt key for time alive
	private static final String TAG_TIME = "time";
	// homing variables
	double lockX, lockY = -1, lockZ;
	private Entity tag;

	public EntityMissile(World worldIn) {
		super(worldIn);
		// set our size to *nothing*
		setSize(0.1F, 0.1F);
	}

	@Override
	protected void entityInit() {
		dataManager.register(TARGET, BlockPos.ORIGIN);
	}

	public EntityMissile(EntityLivingBase thrower, Entity t) {
		this(thrower.world);
		this.tag = t;
	}

	@Override
	public void onUpdate() {
		double lastTickPosX = this.lastTickPosX;
		double lastTickPosY = this.lastTickPosY;
		double lastTickPosZ = this.lastTickPosZ;

		super.onUpdate();

		if (this.tag == null || this.tag.getDistanceSqToEntity(this) > 64.0D) {
			this.tag = this.world.getClosestPlayerToEntity(this, 8.0D);
		}
		if (this.tag != null) {
			double d1 = (this.tag.posX - this.posX) / 8.0D;
			double d2 = (this.tag.posY + this.tag.getEyeHeight() / 2.0D - this.posY) / 8.0D;
			double d3 = (this.tag.posZ - this.posZ) / 8.0D;
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
			this.setDead();
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
				e.attackEntityFrom(DamageSource.causeIndirectMagicDamage(this, this.getThrower()), 1.0F);
			}
			this.setDead();
		}
	}

	@Override
	protected float getGravityVelocity() {
		return 0.0F;
	}
}
