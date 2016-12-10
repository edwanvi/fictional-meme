package me.itstheholyblack.testmodpleaseignore.entity;

import java.util.List;

import com.google.common.base.Predicates;

import me.itstheholyblack.testmodpleaseignore.core.Vector3;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.monster.IMob;
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
	private static final DataParameter<Integer> TARGET = EntityDataManager.createKey(EntityMissile.class, DataSerializers.VARINT);
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
	/**
	 * Simple method to set the missile's target.
	 * @author Vazkii
	 */
	public void setTarget(EntityLivingBase e) {
		dataManager.set(TARGET, e == null ? -1 : e.getEntityId());
	}
	/**
	 * Simple method to get the missile's target. Returns null if that entity doesn't exist or the missile has no target.
	 * @author Vazkii
	 */
	public EntityLivingBase getTargetEntity() {
		int id = dataManager.get(TARGET);
		Entity e = world.getEntityByID(id);
		if(e != null && e instanceof EntityLivingBase) {
			return (EntityLivingBase) e;
		}
		return null;
	}
	/**
	 * @author Vazkii (minor edits by Edwan Vi)
	 */
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
            double d2 = (this.closestPlayer.posY + (double)this.closestPlayer.getEyeHeight() / 2.0D - this.posY) / 8.0D;
            double d3 = (this.closestPlayer.posZ - this.posZ) / 8.0D;
            double d4 = Math.sqrt(d1 * d1 + d2 * d2 + d3 * d3);
            double d5 = 1.0D - d4;

            if (d5 > 0.0D)
            {
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
			if (e == this.closestPlayer)
				e.attackEntityFrom(DamageSource.MAGIC, 5.0F);
		}
	}
	/**
	 * @author Vazkii
	 */
	public boolean getTarget() {
		EntityLivingBase target = getTargetEntity();
		if(target != null && target.getHealth() > 0 && !target.isDead && world.loadedEntityList.contains(target))
			return true;
		if(target != null)
			setTarget(null);

		double range = 12;
		List entities = world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(posX - range, posY - range, posZ - range, posX + range, posY + range, posZ + range), Predicates.instanceOf(EntityPlayer.class));
		while(entities.size() > 0) {
			int rand_index = world.rand.nextInt(entities.size());
			Entity e = (Entity) entities.get(rand_index);
			if(!(e instanceof EntityLivingBase) || e.isDead) { // Just in case...
				entities.remove(e);
				continue;
			}
			target = (EntityLivingBase) e;
			setTarget(target);
			break;
		}
		return target != null;
	}
	@Override
	protected float getGravityVelocity() {
		return 0.0F;
	}
}
