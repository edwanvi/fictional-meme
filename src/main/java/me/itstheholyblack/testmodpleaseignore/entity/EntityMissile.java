package me.itstheholyblack.testmodpleaseignore.entity;

import java.util.List;

import com.google.common.base.Predicates;

import me.itstheholyblack.testmodpleaseignore.core.Vector3;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
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
		this(thrower.worldObj);
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
		Entity e = worldObj.getEntityByID(id);
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

		if(!worldObj.isRemote && (!getTarget() || time > 40)) {
			setDead();
			return;
		}
		Vector3 thisVec = Vector3.fromEntityCenter(this);
		Vector3 oldPos = new Vector3(lastTickPosX, lastTickPosY, lastTickPosZ);
		Vector3 diff = thisVec.subtract(oldPos);
		Vector3 step = diff.normalize().multiply(0.05);
		int steps = (int) (diff.mag() / step.mag());
		Vector3 particlePos = oldPos;
		
		// TODO: put particle code here. (Botania has a whole system in place for this, I don't)
		
		//get target entity
		EntityLivingBase target = getTargetEntity();
		if(target != null) {
			if(lockY == -1) {
				lockX = target.posX;
				lockY = target.posY;
				lockZ = target.posZ;
			}

			Vector3 targetVec = new Vector3(lockX, lockY, lockZ);
			Vector3 diffVec = targetVec.subtract(thisVec);
			Vector3 motionVec = diffVec.normalize().multiply(0.5);
			motionX = motionVec.x;
			motionY = motionVec.y;
			if(time < 10)
				motionY = Math.abs(motionY);
			motionZ = motionVec.z;

			List<EntityLivingBase> targetList = worldObj.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(posX - 0.5, posY - 0.5, posZ - 0.5, posX + 0.5, posY + 0.5, posZ + 0.5));
			if(targetList.contains(target) && target != null) {
				// get what threw us (inherited method)
				EntityLivingBase thrower = getThrower();
				if(thrower != null) {
					// if the thrower is a player, store them
					// if not, set this to null
					EntityPlayer player = thrower instanceof EntityPlayer ? (EntityPlayer) thrower : null;
					// damage target, either via player or mob damage (depending on thrower)
					target.attackEntityFrom(player == null ? DamageSource.causeMobDamage(thrower) : DamageSource.causePlayerDamage(player), 12);
				} else target.attackEntityFrom(DamageSource.generic, 12);

				setDead();
			}
			// die if close to target
			if(diffVec.mag() < 1)
				setDead();
		}
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
		
	}
	/**
	 * @author Vazkii
	 */
	public boolean getTarget() {
		EntityLivingBase target = getTargetEntity();
		if(target != null && target.getHealth() > 0 && !target.isDead && worldObj.loadedEntityList.contains(target))
			return true;
		if(target != null)
			setTarget(null);

		double range = 12;
		List entities = worldObj.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(posX - range, posY - range, posZ - range, posX + range, posY + range, posZ + range), Predicates.instanceOf(EntityPlayer.class));
		entities.add(worldObj.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(posX - range, posY - range, posZ - range, posX + range, posY + range, posZ + range), Predicates.instanceOf(IMob.class)));
		while(entities.size() > 0) {
			Entity e = (Entity) entities.get(worldObj.rand.nextInt(entities.size()));
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
}
