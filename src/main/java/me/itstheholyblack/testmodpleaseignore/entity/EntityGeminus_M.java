package me.itstheholyblack.testmodpleaseignore.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nonnull;

import me.itstheholyblack.testmodpleaseignore.core.PlayerDetection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class EntityGeminus_M extends EntityLiving {
	private static final float MAX_HP = 320F;
	// list of players who attacked the geminus pairing
	private final List<UUID> playersWhoAttacked = new ArrayList<>();
	// number of players
	private static final DataParameter<Integer> PLAYER_COUNT = EntityDataManager.createKey(EntityGeminus_M.class, DataSerializers.VARINT);
	public EntityGeminus_M(World worldIn) {
		super(worldIn);
		// bout player sized
		setSize(0.6F, 1.8F);
		// duh.
		isImmuneToFire = true;
	}
	@Override
	protected void entityInit() {
		dataManager.register(PLAYER_COUNT, 0);
	}
	/**
	 * Called when the entity is attacked. Causes blindness and adds the attacker to a hit list.
	 * @author Vazkii, minimal edits by Edwan Vi
	 */
	@Override
	public boolean attackEntityFrom(@Nonnull DamageSource source, float par2) {
		Entity e = source.getEntity();

		if (e instanceof EntityPlayer && PlayerDetection.isTruePlayer(e)) {
			EntityPlayer player = (EntityPlayer) e;

			if(!playersWhoAttacked.contains(player.getUniqueID()))
				playersWhoAttacked.add(player.getUniqueID());

			player.isOnLadder();
			player.isInWater();
			player.isPotionActive(MobEffects.BLINDNESS);
			player.isRiding();

			int cap = 25;
			return super.attackEntityFrom(source, Math.min(cap, par2));
		}
		return false;
	}
	@Override
	/**
	 * Called when the mob's health reaches 0.
	 * @author Edwan Vi (based on Vazkii)
	 */
	public void onDeath(@Nonnull DamageSource source) {
		super.onDeath(source);
		EntityLivingBase entitylivingbase = getAttackingEntity();
		for (int i=1; i<playersWhoAttacked.size(); i++) {
			UUID u = playersWhoAttacked.get(i);
			EntityPlayer e = worldObj.getPlayerEntityByUUID(u);
			// I said you wouldn't survive
			e.attackEntityFrom(DamageSource.magic, Float.MAX_VALUE);
		}
		// "explode"
		playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 20F, (1F + (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.2F) * 0.7F);
		worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, posX, posY, posZ, 1D, 0D, 0D);
	}
	@Override
	public void onLivingUpdate() {
		// count of players
		int playerCount = getPlayerCount();
		// get off meh
		if(!getPassengers().isEmpty())
			dismountRidingEntity();
		if(isDead) {
			return;
		}
		if (ticksExisted % 3 < 2) {
			for(int i = 0; i < playerCount; i++)
				spawnMissile();
		}
	}
	/**
	 * Spawns a missile attack.
	 * @author Vazkii
	 */
	private void spawnMissile() {
		EntityMissile missile = new EntityMissile(this);
		// set missile position to ours, give or take some random values
		missile.setPosition(posX + (Math.random() - 0.5 * 0.1), posY + 2.4 + (Math.random() - 0.5 * 0.1), posZ + (Math.random() - 0.5 * 0.1));
		if(missile.getTarget()) {
			// add the missile to the world
			worldObj.spawnEntityInWorld(missile);
		}
	}
	public int getPlayerCount() {
		return dataManager.get(PLAYER_COUNT);
	}
	@Override
	public boolean isNonBoss() {
		return false;
	}
	// ===BEGIN ENTITYENDERMAN CODE===
	private boolean teleportRandomly() {
		double d0 = posX + (rand.nextDouble() - 0.5D) * 64.0D;
		double d1 = posY + (rand.nextInt(64) - 32);
		double d2 = posZ + (rand.nextDouble() - 0.5D) * 64.0D;
		return teleportTo(d0, d1, d2);
	}
    private boolean teleportTo(double x, double y, double z) {
        net.minecraftforge.event.entity.living.EnderTeleportEvent event = new net.minecraftforge.event.entity.living.EnderTeleportEvent(this, x, y, z, 0);
        if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event)) return false;
        boolean flag = this.attemptTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ());

        if (flag) {
            this.worldObj.playSound((EntityPlayer)null, this.prevPosX, this.prevPosY, this.prevPosZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, this.getSoundCategory(), 1.0F, 1.0F);
            this.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 1.0F);
        }
        return flag;
    }
    // ===END ENTITYENDERMAN CODE===
}
