package me.itstheholyblack.testmodpleaseignore.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nonnull;

import me.itstheholyblack.testmodpleaseignore.blocks.ModBlocks;
import me.itstheholyblack.testmodpleaseignore.core.LibMisc;
import me.itstheholyblack.testmodpleaseignore.core.PlayerDetection;
import me.itstheholyblack.testmodpleaseignore.util.Randomizer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;

public class EntityGeminus_F extends EntityMob implements IMob, IRangedAttackMob {
	private static final float MAX_HP = 320F;
	// list of players who attacked the geminus pairing
	// set to null since sister inherits this from brother
	private List<UUID> playersWhoAttacked = new ArrayList<>();
	// player count
	private static final String TAG_PLAYER_COUNT = "playerCount";
	private static final DataParameter<Integer> PLAYER_COUNT = EntityDataManager.createKey(EntityGeminus_F.class,
			DataSerializers.VARINT);
	// cooldown on missiles/shulkers
	private static final int COOLDOWN = 10;
	private static final DataParameter<Integer> SPAWN_COOLDOWN = EntityDataManager.createKey(EntityGeminus_F.class,
			DataSerializers.VARINT);
	private static final DataParameter<Boolean> SPAWNING = EntityDataManager.createKey(EntityGeminus_F.class,
			DataSerializers.BOOLEAN);
	// boss bar
	private final BossInfoServer bossInfo = (new BossInfoServer(this.getDisplayName(), BossInfo.Color.PINK,
			BossInfo.Overlay.NOTCHED_20));
	private static final DataParameter<BlockPos> HOME = EntityDataManager.createKey(EntityGeminus_F.class,
			DataSerializers.BLOCK_POS);
	// brother geminus, may be null
	public EntityGeminus_M brother;
	// closest player, may be null
	private EntityPlayer closestPlayer;
	private static final double TELEPORT_RANGE_DOUBLE = 32.0D;
	private static final int TELEPORT_RANGE_INT = (int) TELEPORT_RANGE_DOUBLE;
	public static final PotionEffect blindness = new PotionEffect(MobEffects.BLINDNESS, 900);

	public EntityGeminus_F(World worldIn) {
		super(worldIn);
		this.brother = null;
		setSize(0.6F, 1.8F);
		// duh.
		isImmuneToFire = true;
		experienceValue = 825;
	}

	public EntityGeminus_F(World worldIn, EntityGeminus_M bro) {
		this(worldIn);
		this.brother = bro;
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataManager.register(PLAYER_COUNT, 0);
		dataManager.register(SPAWN_COOLDOWN, COOLDOWN);
		dataManager.register(SPAWNING, false);
		dataManager.register(HOME, BlockPos.ORIGIN);
	}

	@Override
	protected void initEntityAI() {
		this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, TELEPORT_RANGE_INT));
		this.tasks.addTask(1, new EntityAIAttackRanged(this, 1.25D, 1, 10.0F));
		this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityLiving.class, 10, false, false, LibMisc.PLAYER_SELECTOR));
		this.applyEntityAI();
	}
	
	protected void applyEntityAI() {
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(MAX_HP);
		this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1);
	}

	@Override
	public void onLivingUpdate() {
		setCooldown(getCooldown() - 1);
		if (this.getHome() == null || this.getHome().equals(BlockPos.ORIGIN)) {
			// i very much doubt it's at 0 0 0
			this.setHome(this.getPosition());
		}
		if (this.playersWhoAttacked.size() == 0 && brother != null) {
			// first tick
			this.playersWhoAttacked = this.brother.playersWhoAttacked;
		} else if (brother != null && !this.playersWhoAttacked.equals(brother.playersWhoAttacked)) {
			this.playersWhoAttacked = brother.playersWhoAttacked;
		} else {
			this.playersWhoAttacked = new ArrayList<>();
		}
		if (this.brother != null && brother.isDead) {
			this.world.createExplosion(null, this.getPosition().getX(), this.getPosition().getY(),
					this.getPosition().getZ(), 5.0F, false);
			this.setDead();
		}
		// stop limbs swing
		this.limbSwingAmount = 0.0F;
		boolean spawning = dataManager.get(SPAWNING);
		this.closestPlayer = this.world.getClosestPlayerToEntity(this, TELEPORT_RANGE_DOUBLE);
		float PERCENT_HP = this.getHealth() / this.getMaxHealth();
		if (this.closestPlayer != null && this.closestPlayer.isSpectator()) {
			this.closestPlayer = null;
		}
		if (this.closestPlayer != null && this.closestPlayer.isPotionActive(MobEffects.BLINDNESS)) {
			this.teleportToEntity(closestPlayer);
			spawning = true;
		}
		if (!spawning && getCooldown() < 1) {
			spawning = !(Randomizer.getRandomBoolean(this.getHealth() / this.getMaxHealth()));
		}
		if (spawning) {
			this.attackEntityWithRangedAttack(this.closestPlayer, 0);
		}
		boolean close;
		try {
			close = this.getDistanceToEntity(closestPlayer) <= 10.0F;
		} catch (NullPointerException e) {
			close = false;
		}
		if (close && this.getHealth() < this.getMaxHealth() / 2 && Randomizer.getRandomBoolean(0.1)) {
			LibMisc.makeSphere(this.getEntityWorld(), this.closestPlayer.getPosition(),
					ModBlocks.m_fumes.getDefaultState(), 5);
		}
		this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
		super.onLivingUpdate();
	}

	@Override
	protected void updateAITasks() {
		if (this.closestPlayer != null && this.closestPlayer.getDistanceSqToEntity(this) < 2.0D) {
			this.teleportRandomly();
		}
		BlockPos homePos = getHome();
		// teleport home
		if (this.getDistance(homePos.getX(), homePos.getY(), homePos.getZ()) > TELEPORT_RANGE_INT + 10
				&& !homePos.equals(BlockPos.ORIGIN)) {
			this.setPositionAndUpdate(homePos.getX(), homePos.getY(), homePos.getZ());
		}
		super.updateAITasks();
	}

	@Override
	public boolean attackEntityFrom(@Nonnull DamageSource source, float par2) {
		if (source == DamageSource.OUT_OF_WORLD) {
			// respect /kill and falling
			return super.attackEntityFrom(source, par2);
		} else {
			Entity e = source.getEntity();
			if (e instanceof EntityPlayer && PlayerDetection.isTruePlayer(e)) {
				EntityPlayer player = (EntityPlayer) e;
				System.out.println("Attacked by " + e.getName());
				int i = 0;
				// Try to teleport 64 times
				while (!this.teleportToEntity(e) && i < 64) {
					i++;
				}
				if (!playersWhoAttacked.contains(player.getUniqueID())) {
					playersWhoAttacked.add(player.getUniqueID());
					dataManager.set(PLAYER_COUNT, dataManager.get(PLAYER_COUNT) + 1);
				}
				if ((!(player.getHeldItemMainhand().getItem() instanceof ItemBow)
						|| player.getHeldItemMainhand() == ItemStack.EMPTY) && !world.isRemote) {
					player.addPotionEffect(blindness);
				}

				int cap = 25;
				return super.attackEntityFrom(source, Math.min(cap, par2));
			} else {
				int i = 0;
				while (!this.teleportRandomly() && i < 64) {
					i++;
				}
			}
			return false;
		}
	}

	/**
	 * Spawns a missile attack.
	 * 
	 * @param target The entity this missle will follow.
	 */
	private void spawnMissile(EntityLivingBase target) {
		EntityMissile missile = new EntityMissile(this, target);
		// set missile position to ours, give or take some random values
		missile.setPosition(posX + (Math.random() - 0.5 * 0.1), posY + 2.4 + (Math.random() - 0.5 * 0.1),
				posZ + (Math.random() - 0.5 * 0.1));
		// add the missile to the world
		world.spawnEntity(missile);
	}

	public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor) {
		this.spawnMissile(target);
	}

	// setters and getters below this line *only*
	public void setHome(BlockPos pos) {
		dataManager.set(HOME, pos);
	}

	public BlockPos getHome() {
		return dataManager.get(HOME);
	}

	/** Gets the current value of missile cooldown. */
	public int getCooldown() {
		return dataManager.get(SPAWN_COOLDOWN);
	}

	/** Sets the current value of missile cooldown. */
	public void setCooldown(int value) {
		dataManager.set(SPAWN_COOLDOWN, value);
	}

	// setters and getters above this line *only*
	// +++BEGIN ENTITYWITHER CODE+++
	/**
	 * Add the given player to the list of players tracking this entity. For
	 * instance, a player may track a boss in order to view its associated boss
	 * bar.
	 */
	@Override
	public void addTrackingPlayer(EntityPlayerMP player) {
		super.addTrackingPlayer(player);
		this.bossInfo.addPlayer(player);
	}

	/**
	 * Removes the given player from the list of players tracking this entity.
	 * See {@link Entity#addTrackingPlayer} for more information on tracking.
	 */
	@Override
	public void removeTrackingPlayer(EntityPlayerMP player) {
		super.removeTrackingPlayer(player);
		this.bossInfo.removePlayer(player);
	}

	/**
	 * Returns false if this Entity is a boss, true otherwise.
	 */
	@Override
	public boolean isNonBoss() {
		return false;
	}

	// +++END ENTITYWITHER CODE+++
	// ===BEGIN ENTITYENDERMAN CODE===
	/**
	 * Teleport to a random position within 64 blocks.
	 * 
	 * @return A boolean indicating whether or not we succeeded.
	 * @author Mojang
	 */
	private boolean teleportRandomly() {
		double d0 = posX + (rand.nextDouble() - 0.5D) * TELEPORT_RANGE_DOUBLE;
		double d1 = posY + (rand.nextInt(TELEPORT_RANGE_INT) - 32);
		double d2 = posZ + (rand.nextDouble() - 0.5D) * TELEPORT_RANGE_DOUBLE;
		return teleportTo(d0, d1, d2);
	}

	/**
	 * Teleport to another entity
	 * 
	 * @author Mojang
	 */
	protected boolean teleportToEntity(Entity p_70816_1_) {
		Vec3d vec3d = new Vec3d(this.posX - p_70816_1_.posX,
				this.getEntityBoundingBox().minY + this.height / 2.0F - p_70816_1_.posY + p_70816_1_.getEyeHeight(),
				this.posZ - p_70816_1_.posZ);
		vec3d = vec3d.normalize();
		double d0 = 16.0D;
		double d1 = this.posX + (this.rand.nextDouble() - 0.5D) * 8.0D - vec3d.xCoord * 16.0D;
		double d2 = this.posY + (this.rand.nextInt(16) - 8) - vec3d.yCoord * 16.0D;
		double d3 = this.posZ + (this.rand.nextDouble() - 0.5D) * 8.0D - vec3d.zCoord * 16.0D;
		return this.teleportTo(d1, d2, d3);
	}

	/**
	 * Teleport to a given position.
	 * 
	 * @param x
	 *            The x coordinate of our destination.
	 * @param y
	 *            The y coordinate of our destination.
	 * @param z
	 *            The z coordinate of our destination.
	 * @return A boolean indicating whether or not we succeeded.
	 * @author Mojang
	 */
	private boolean teleportTo(double x, double y, double z) {
		net.minecraftforge.event.entity.living.EnderTeleportEvent event = new net.minecraftforge.event.entity.living.EnderTeleportEvent(
				this, x, y, z, 0);
		if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event))
			return false;
		boolean flag = this.attemptTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ());

		if (flag) {
			this.world.playSound((EntityPlayer) null, this.prevPosX, this.prevPosY, this.prevPosZ,
					SoundEvents.ENTITY_ENDERMEN_TELEPORT, this.getSoundCategory(), 1.0F, 1.0F);
			this.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 1.0F);
		}
		return flag;
	}

	// ===END ENTITYENDERMAN CODE===
	@Override
	public boolean canDespawn() {
		return false;
	}
}
