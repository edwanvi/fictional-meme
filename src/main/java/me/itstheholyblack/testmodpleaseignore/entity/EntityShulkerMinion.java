package me.itstheholyblack.testmodpleaseignore.entity;

import javax.annotation.Nonnull;

import net.minecraft.entity.monster.EntityShulker;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityShulkerMinion extends EntityShulker {
	private EntityGeminus_M boss;
	public EntityShulkerMinion(World worldIn, EntityGeminus_M bossman) {
		super(worldIn);
		boss = bossman;
	}
	public EntityShulkerMinion(World worldIn) {
		this(worldIn, null);
	}
	// remove from list
	@Override
	public void onDeath(@Nonnull DamageSource source) {
		super.onDeath(source);
		if (boss != null)
			boss.shulkerList.remove(this);
	}
}
