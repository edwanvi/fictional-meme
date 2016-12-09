package me.itstheholyblack.testmodpleaseignore.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;

public class EntityGeminus_F extends EntityLiving {
	// boss bar
	private final BossInfoServer bossInfo = (BossInfoServer)(new BossInfoServer(this.getDisplayName(), BossInfo.Color.GREEN, BossInfo.Overlay.NOTCHED_20));
	public EntityGeminus_M brother;
	public EntityGeminus_F(World worldIn) {
		super(worldIn);
		this.brother = null;
	}
	public EntityGeminus_F(World worldIn, EntityGeminus_M bro) {
		this(worldIn);
		this.brother = bro;
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
