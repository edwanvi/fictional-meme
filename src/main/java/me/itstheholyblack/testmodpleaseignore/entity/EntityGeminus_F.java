package me.itstheholyblack.testmodpleaseignore.entity;

import net.minecraft.entity.EntityLiving;
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
}
