package me.itstheholyblack.testmodpleaseignore.entity;

import javax.annotation.Nonnull;

import net.minecraft.entity.monster.EntityShulker;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

/**
 * Extension of shulkers that can easily be used for the geminus fight.
 * 
 * @author Edwan Vi
 * @author Mojang
 */
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
