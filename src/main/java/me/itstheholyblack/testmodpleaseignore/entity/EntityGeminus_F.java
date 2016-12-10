package me.itstheholyblack.testmodpleaseignore.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import me.itstheholyblack.testmodpleaseignore.core.PlayerDetection;
import me.itstheholyblack.testmodpleaseignore.core.Randomizer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemSword;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;

public class EntityGeminus_F extends EntityLiving {
	private static final float MAX_HP = 320F;
	// list of players who attacked the geminus pairing
	// set to null since sister inherits this from brother
	private List<UUID> playersWhoAttacked = new ArrayList<>();
	// player count
	private static final String TAG_PLAYER_COUNT = "playerCount";
	private static final DataParameter<Integer> PLAYER_COUNT = EntityDataManager.createKey(EntityGeminus_F.class, DataSerializers.VARINT);
	// cooldown on missiles/shulkers
	private static final int COOLDOWN = 10;
	private static final DataParameter<Integer> SPAWN_COOLDOWN = EntityDataManager.createKey(EntityGeminus_F.class, DataSerializers.VARINT);
	private static final DataParameter<Boolean> SPAWNING = EntityDataManager.createKey(EntityGeminus_F.class, DataSerializers.BOOLEAN);
	// boss bar
	private final BossInfoServer bossInfo = (BossInfoServer)(new BossInfoServer(this.getDisplayName(), BossInfo.Color.PINK, BossInfo.Overlay.NOTCHED_20));
	private static final DataParameter<BlockPos> HOME = EntityDataManager.createKey(EntityGeminus_F.class, DataSerializers.BLOCK_POS);
	// brother geminus, may be null
	public EntityGeminus_M brother;
	// closest player, may be null
	private EntityPlayer closestPlayer;
	private static final double TELEPORT_RANGE_DOUBLE = 64.0D;
	private static final int TELEPORT_RANGE_INT = (int) TELEPORT_RANGE_DOUBLE;
	public static final PotionEffect blindness = new PotionEffect(MobEffects.BLINDNESS, 600);
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
	protected void initEntityAI() {
		this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, TELEPORT_RANGE_INT));
		this.applyEntityAI();
	}
	protected void applyEntityAI() {
		// no-op
	}
	@Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(MAX_HP);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1);
	}
	@Override
	public void onLivingUpdate() {
		setCooldown(getCooldown()-1);
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
			this.kill();
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
			spawning = true;
		}
		if (!spawning && getCooldown() < 1) {
			spawning = !(Randomizer.getRandomBoolean(this.getHealth() / this.getMaxHealth()));
		}
		if (spawning) {
			for(int i = 0; i < playersWhoAttacked.size(); i++)
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
		if (this.getDistance(homePos.getX(), homePos.getY(), homePos.getZ()) > TELEPORT_RANGE_INT + 10 && !homePos.equals(BlockPos.ORIGIN)) {
			this.setPositionAndUpdate(homePos.getX(), homePos.getY(), homePos.getZ());
		}
		super.updateAITasks();
	}
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
				if (brother != null) {
					brother.setPlayerCount(brother.getPlayerCount() + 1);
				}
			}
			player.isOnLadder();
			player.isInWater();
			// blindness because fuck you
			if (player.getHeldItemMainhand().getItem() instanceof ItemSword) {
				player.addPotionEffect(blindness);
			}
			player.isRiding();

			int cap = 25;
			return super.attackEntityFrom(source, Math.min(cap, par2));
		}
		return false;
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
	// setters and getters below this line *only*
	public void setHome(BlockPos pos) {
		dataManager.set(HOME, pos);
	}
	public BlockPos getHome() {
		return dataManager.get(HOME);
	}
	/**Gets the current value of missile cooldown.*/
	public int getCooldown() {
		return dataManager.get(SPAWN_COOLDOWN);
	}
	/**Sets the current value of missile cooldown.*/
	public void setCooldown(int value) {
		dataManager.set(SPAWN_COOLDOWN, value);
	}
	// setters and getters above this line *only*
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
    /**
     * Returns false if this Entity is a boss, true otherwise.
     */
    public boolean isNonBoss() {
        return false;
    }
    // +++END ENTITYWITHER CODE+++
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
 		System.out.println("Teleporting to " + d0 + " " + + d1 + " " + d2);
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
 	private boolean teleportTo(double x, double y, double z) {
         net.minecraftforge.event.entity.living.EnderTeleportEvent event = new net.minecraftforge.event.entity.living.EnderTeleportEvent(this, x, y, z, 0);
         if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event)) return false;
         boolean flag = this.attemptTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ());

         if (flag) {
             this.world.playSound((EntityPlayer)null, this.prevPosX, this.prevPosY, this.prevPosZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, this.getSoundCategory(), 1.0F, 1.0F);
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
