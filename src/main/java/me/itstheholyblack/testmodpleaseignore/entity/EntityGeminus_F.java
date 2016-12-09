package me.itstheholyblack.testmodpleaseignore.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;

public class EntityGeminus_F extends EntityLiving {
	private static final float MAX_HP = 320F;
	// list of players who attacked the geminus pairing
	// set to null since sister inherits this from brother
	private List<UUID> playersWhoAttacked = null;
	// player count
	private static final String TAG_PLAYER_COUNT = "playerCount";
	private static final DataParameter<Integer> PLAYER_COUNT = EntityDataManager.createKey(EntityGeminus_M.class, DataSerializers.VARINT);
	// cooldown on missiles/shulkers
	private static final int COOLDOWN = 10;
	private static final DataParameter<Integer> SPAWN_COOLDOWN = EntityDataManager.createKey(EntityGeminus_M.class, DataSerializers.VARINT);
	private static final DataParameter<Boolean> SPAWNING = EntityDataManager.createKey(EntityGeminus_M.class, DataSerializers.BOOLEAN);
	// boss bar
	private final BossInfoServer bossInfo = (BossInfoServer)(new BossInfoServer(this.getDisplayName(), BossInfo.Color.GREEN, BossInfo.Overlay.NOTCHED_20));
	private static final DataParameter<BlockPos> HOME = EntityDataManager.createKey(EntityGeminus_M.class, DataSerializers.BLOCK_POS);
	public EntityGeminus_M brother;
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
	public void onLivingUpdate() {
		if (this.playersWhoAttacked == null && brother != null) {
			// first tick
			this.playersWhoAttacked = this.brother.playersWhoAttacked;
		} else if (brother != null && !this.playersWhoAttacked.equals(brother.playersWhoAttacked)) {
			this.playersWhoAttacked = brother.playersWhoAttacked;
		} else {
			this.playersWhoAttacked = new ArrayList<>();
		}
		super.onLivingUpdate();
	}
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
}
