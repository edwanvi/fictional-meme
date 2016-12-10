package me.itstheholyblack.testmodpleaseignore.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.annotation.Nonnull;

import me.itstheholyblack.testmodpleaseignore.core.PlayerDetection;
import me.itstheholyblack.testmodpleaseignore.core.Randomizer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityGeminus_M extends EntityLiving {
	private static final float MAX_HP = 320F;
	// list of players who attacked the geminus pairing
	public final List<UUID> playersWhoAttacked = new ArrayList<>();
	// number of players
	private static final String TAG_PLAYER_COUNT = "playerCount";
	private static final DataParameter<Integer> PLAYER_COUNT = EntityDataManager.createKey(EntityGeminus_M.class, DataSerializers.VARINT);
	// cooldown on missiles/shulkers
	private static final int COOLDOWN = 10;
	private static final DataParameter<Integer> SPAWN_COOLDOWN = EntityDataManager.createKey(EntityGeminus_M.class, DataSerializers.VARINT);
	private static final DataParameter<Boolean> SPAWNING = EntityDataManager.createKey(EntityGeminus_M.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Integer> SHULKER_COOLDOWN = EntityDataManager.createKey(EntityGeminus_M.class, DataSerializers.VARINT);
	private static final double TELEPORT_RANGE_DOUBLE = 64.0D;
	private static final int TELEPORT_RANGE_INT = (int) TELEPORT_RANGE_DOUBLE;
	// list of shulkers so we don't spawn a billion of them
	public List<EntityShulkerMinion> shulkerList = new ArrayList<>();
	// rand gen
	private static Random rand_gen = new Random();
	// boss bar
	private final BossInfoServer bossInfo = (BossInfoServer)(new BossInfoServer(this.getDisplayName(), BossInfo.Color.BLUE, BossInfo.Overlay.NOTCHED_20));
	// closest player
	private EntityPlayer closestPlayer;
	// home point
	private static final DataParameter<BlockPos> HOME = EntityDataManager.createKey(EntityGeminus_M.class, DataSerializers.BLOCK_POS);
	// sister
	private EntityGeminus_F sister;
	private static final DataParameter<Boolean> hellosis = EntityDataManager.createKey(EntityGeminus_M.class, DataSerializers.BOOLEAN);
	public static final PotionEffect blindness = new PotionEffect(MobEffects.BLINDNESS, 3600);
	public EntityGeminus_M(World worldIn) {
		super(worldIn);
		// bout player sized
		setSize(0.6F, 1.8F);
		// duh.
		isImmuneToFire = true;
		experienceValue = 825;
	}
	@Override
	protected void entityInit() {
		super.entityInit();
		dataManager.register(PLAYER_COUNT, 0);
		dataManager.register(SPAWN_COOLDOWN, COOLDOWN);
		dataManager.register(SPAWNING, false);
		dataManager.register(SHULKER_COOLDOWN, COOLDOWN);
		dataManager.register(HOME, BlockPos.ORIGIN);
		dataManager.register(hellosis, false);
		this.sister = new EntityGeminus_F(this.world, this);
	}
	@Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(MAX_HP);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1);
	}
	/**
	 * Called when the entity is attacked. Causes blindness and adds the attacker to a hit list.
	 * @author Vazkii
	 * @author Edwan Vi
	 */
	@Override
	public boolean attackEntityFrom(@Nonnull DamageSource source, float par2) {
		if (source == DamageSource.OUT_OF_WORLD) {
			// no-op
			return super.attackEntityFrom(source, par2);
		}
		Entity e = source.getEntity();
		this.teleportRandomly();
		if (e instanceof EntityPlayer && PlayerDetection.isTruePlayer(e)) {
			EntityPlayer player = (EntityPlayer) e;

			if(!playersWhoAttacked.contains(player.getUniqueID())) {
				playersWhoAttacked.add(player.getUniqueID());
				setPlayerCount(getPlayerCount() + 1);
			}
			player.isOnLadder();
			player.isInWater();
			player.addPotionEffect(blindness);
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
		if (source == DamageSource.OUT_OF_WORLD) {
			// no-op
			return;
		}
		EntityLivingBase entitylivingbase = getAttackingEntity();
		for (int i=0; i < playersWhoAttacked.size(); i++) {
			UUID u = playersWhoAttacked.get(i);
			EntityPlayer e = world.getPlayerEntityByUUID(u);
			// I said you wouldn't survive
			System.out.println("Killing player " + e.getName());
			e.attackEntityFrom(DamageSource.OUT_OF_WORLD, Float.MAX_VALUE);
		}
		for (int i = 0; i < shulkerList.size(); i++) {
			EntityShulkerMinion e = shulkerList.get(i);
			e.attackEntityFrom(DamageSource.OUT_OF_WORLD, e.getHealth());
		}
		// "explode"
		playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 20F, (1F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F) * 0.7F);
		world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, posX, posY, posZ, 1D, 0D, 0D);
	}
	protected void initEntityAI() {
		this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, TELEPORT_RANGE_INT));
		this.applyEntityAI();
	}
	protected void applyEntityAI() {
		
	}
	@Override
	public void onLivingUpdate() {
		if (this.getHome() == null || this.getHome().equals(BlockPos.ORIGIN)) {
			// i very much doubt it's at 0 0 0
			this.setHome(this.getPosition());
		}
		// this'll work
		if (!this.getSpawnedSister() && !this.world.isRemote) {
			sister.setPosition(posX+1, posY, posZ-1);
			world.spawnEntity(sister);
			this.setSpawnedSister(true);
		}
		this.limbSwingAmount = 0.0F;
		boolean spawning = dataManager.get(SPAWNING);
		this.closestPlayer = this.world.getClosestPlayerToEntity(this, TELEPORT_RANGE_DOUBLE);
		float PERCENT_HP = this.getHealth() / this.getMaxHealth();
		if (this.closestPlayer != null && this.closestPlayer.isSpectator()) {
            this.closestPlayer = null;
        }
		if (this.closestPlayer!= null && this.closestPlayer.isPotionActive(MobEffects.BLINDNESS)) {
			this.teleportToEntity(closestPlayer);
		}
		// count of players
		int playerCount = getPlayerCount();
		setCooldown(getCooldown()-1);
		if (PERCENT_HP < 50 && getShulkerCooldown() > 0) {
			setShulkerCooldown(getShulkerCooldown()-1);
		}
		// get off meh
		if(!getPassengers().isEmpty())
			dismountRidingEntity();
		if(isDead) {
			return;
		}
		if (!spawning && getCooldown() < 1) {
			spawning = !(Randomizer.getRandomBoolean(this.getHealth() / this.getMaxHealth()));
		}
		// shulker spawning code
		if (getShulkerCooldown() < 1) {
			boolean shouldSpawnShulker = !(Randomizer.getRandomBoolean(PERCENT_HP / 10)) && shulkerList.size() < 5;
			boolean mobGriefing = this.world.getGameRules().getBoolean("mobGriefing");
			BlockPos myPosition = this.getPosition();
			double d0 = posX + (rand.nextDouble() - 0.5D) * (TELEPORT_RANGE_DOUBLE/2);
			double d1 = posY + (rand.nextInt(TELEPORT_RANGE_INT/2));
			double d2 = posZ + (rand.nextDouble() - 0.5D) * (TELEPORT_RANGE_DOUBLE/2);
			if (shouldSpawnShulker) {
				BlockPos pos = new BlockPos(d0, d1, d2);
				if (mobGriefing) {
					EntityShulkerMinion e = this.shulkerReplace(pos);
					if (e != null)
						shulkerList.add(e);
					setShulkerCooldown(COOLDOWN);
				} else {
					if (world.getBlockState(pos).getBlock() != Blocks.AIR) {
						while (world.getBlockState(pos).getBlock() != Blocks.AIR) {
							pos = pos.add(0, 1, 0);
						}
						// we have an air block now
						EntityShulkerMinion e = this.shulkerReplace(pos);
						if (e != null)
							shulkerList.add(e);
						setShulkerCooldown(COOLDOWN * 10);
					}
				}
			}
		}
		if (spawning) {
			for(int i = 0; i < playerCount; i++)
				spawnMissile();
			dataManager.set(SPAWNING, false);
			setCooldown(COOLDOWN);
		}
		this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
		super.onLivingUpdate();
	}
	protected void updateAITasks() {
		if (this.closestPlayer != null && this.closestPlayer.getDistanceSqToEntity(this) < 2.0D) {
			this.teleportRandomly();
		}
		BlockPos homePos = getHome();
		// teleport home
		if (this.getDistance(homePos.getX(), homePos.getY(), homePos.getZ()) > 64 && !homePos.equals(BlockPos.ORIGIN)) {
			this.setPositionAndUpdate(homePos.getX(), homePos.getY(), homePos.getZ());
		}
		super.updateAITasks();
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
			world.spawnEntity(missile);
		}
	}
	// setters and getters
	/**Get the number of players fighting this thing*/
	public int getPlayerCount() {
		return dataManager.get(PLAYER_COUNT);
	}
	/**Sets the number of players fighting this thing*/
	public void setPlayerCount(int count) {
		dataManager.set(PLAYER_COUNT, count);
	}
	/**Gets the current value of missile cooldown.*/
	public int getCooldown() {
		return dataManager.get(SPAWN_COOLDOWN);
	}
	/**Sets the current value of missile cooldown.*/
	public void setCooldown(int value) {
		dataManager.set(SPAWN_COOLDOWN, value);
	}
	/**Gets the current value of shulker cooldown.*/
	public int getShulkerCooldown() {
		return dataManager.get(SHULKER_COOLDOWN);
	}
	/**Sets the current value of shulker cooldown.*/
	public void setShulkerCooldown(int value) {
		dataManager.set(SHULKER_COOLDOWN, value);
	}
	public void setHome(BlockPos pos) {
		dataManager.set(HOME, pos);
	}
	public BlockPos getHome() {
		return dataManager.get(HOME);
	}
	public Boolean getSpawnedSister() {
		return dataManager.get(hellosis);
	}
	public void setSpawnedSister(Boolean value) {
		dataManager.set(hellosis, value);
	}
	@Override
	public boolean isNonBoss() {
		return false;
	}
	// ===BEGIN ENTITYENDERMAN CODE===
	/**
	 * Teleport to a random position within 64 blocks.
	 * @return A boolean indicating whether or not we succeeded.
	 * @author Notch
	 */
	private boolean teleportRandomly() {
		double d0 = posX + (rand.nextDouble() - 0.5D) * TELEPORT_RANGE_DOUBLE;
		double d1 = posY + (rand.nextInt(TELEPORT_RANGE_INT) - 32);
		double d2 = posZ + (rand.nextDouble() - 0.5D) * TELEPORT_RANGE_DOUBLE;
		return teleportTo(d0, d1, d2);
	}
	/**
     * Teleport to another entity
     * @author Notch
     */
    protected boolean teleportToEntity(Entity p_70816_1_)
    {
        Vec3d vec3d = new Vec3d(this.posX - p_70816_1_.posX, this.getEntityBoundingBox().minY + (double)(this.height / 2.0F) - p_70816_1_.posY + (double)p_70816_1_.getEyeHeight(), this.posZ - p_70816_1_.posZ);
        vec3d = vec3d.normalize();
        double d0 = 16.0D;
        double d1 = this.posX + (this.rand.nextDouble() - 0.5D) * 8.0D - vec3d.xCoord * 16.0D;
        double d2 = this.posY + (double)(this.rand.nextInt(16) - 8) - vec3d.yCoord * 16.0D;
        double d3 = this.posZ + (this.rand.nextDouble() - 0.5D) * 8.0D - vec3d.zCoord * 16.0D;
        return this.teleportTo(d1, d2, d3);
    }
	/**
	 * Teleport to a given position.
	 * @param x The x coordinate of our destination.
	 * @param y The y coordinate of our destination.
	 * @param z The z coordinate of our destination.
	 * @return A boolean indicating whether or not we succeeded.
	 * @author Notch
	 */
	private boolean teleportTo(double x, double y, double z)
    {
        net.minecraftforge.event.entity.living.EnderTeleportEvent event = new net.minecraftforge.event.entity.living.EnderTeleportEvent(this, x, y, z, 0);
        if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event)) return false;
        boolean flag = this.attemptTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ());

        if (flag)
        {
            this.world.playSound((EntityPlayer)null, this.prevPosX, this.prevPosY, this.prevPosZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, this.getSoundCategory(), 1.0F, 1.0F);
            this.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 1.0F);
        }

        return flag;
    }
    // ===END ENTITYENDERMAN CODE===
    // +++BEGIN ENTITYWITHER CODE+++
    /**
     * Add the given player to the list of players tracking this entity. For instance, a player may track a boss in
     * order to view its associated boss bar.
     */
    public void addTrackingPlayer(EntityPlayerMP player) {
        super.addTrackingPlayer(player);
        this.bossInfo.addPlayer(player);
    }

    /**
     * Removes the given player from the list of players tracking this entity. See {@link Entity#addTrackingPlayer} for
     * more information on tracking.
     */
    public void removeTrackingPlayer(EntityPlayerMP player) {
        super.removeTrackingPlayer(player);
        this.bossInfo.removePlayer(player);
    }
    // +++END ENTITYWITHER CODE+++
    /**
     * Replace a given block with a shulker because f*ck you. THIS CODE DOESN'T GIVE A DAMN ABOUT THE {@code mobGriefing} GAME RULE. DON'T. BE. A. TWIT.
     * Fails if the given block pos is bedrock.
     * @author Edwan Vi
     * @param pos Where to place the shulker.
     * @return The spawned shulker (or {@code null} if we couldn't spawn it for some reason.)
     */
    // @SideOnly(Side.SERVER)
    private EntityShulkerMinion shulkerReplace(BlockPos pos) {
    	System.out.println("Spawning Shulker Minion");
    	EntityShulkerMinion shulk = new EntityShulkerMinion(this.world, this);
    	shulk.setPosition(pos.getX(), pos.getY(), pos.getZ());
    	if (world.getBlockState(pos).getBlock() != Blocks.BEDROCK) {
    		if (!world.isRemote) {
    			this.world.setBlockToAir(pos);
    			world.spawnEntity(shulk);
    		}
    		return shulk;
    	} else {
    		FMLLog.warning("Could not place shulker at given position.", "Could not place shulker at given position.");
    		return null;
    	}
    }
    @Override
    public void writeEntityToNBT(NBTTagCompound par1nbtTagCompound) {
    	super.writeEntityToNBT(par1nbtTagCompound);
    	par1nbtTagCompound.setInteger(TAG_PLAYER_COUNT, getPlayerCount());
    	par1nbtTagCompound.setBoolean("hellosis", this.getSpawnedSister());
    }
    @Override
    public void readEntityFromNBT(NBTTagCompound par1nbtTagCompound){
    	super.readEntityFromNBT(par1nbtTagCompound);
    	if(par1nbtTagCompound.hasKey(TAG_PLAYER_COUNT))
			setPlayerCount(par1nbtTagCompound.getInteger(TAG_PLAYER_COUNT));
    	else setPlayerCount(1);
    	if (par1nbtTagCompound.hasKey("hellosis"))
    		setSpawnedSister(par1nbtTagCompound.getBoolean("hellosis"));
    	else setSpawnedSister(false);
    }
    @Override
 	public boolean canDespawn() {
		return false;	
 	}
}
