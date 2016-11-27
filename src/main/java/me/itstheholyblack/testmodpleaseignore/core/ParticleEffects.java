package me.itstheholyblack.testmodpleaseignore.core;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class ParticleEffects {
	/**
	 * Spawns some particles server side.
	 * @param worldIn - the world to spawn the particles in.
	 * @param X - X coordinate of particles to spawn
	 * @param Y - Y coordinate of particles to spawn
	 * @param Z - Z coordinate of particles to spawn
	 * @param particleType - The type of particle to spawn.
	 * @param count - The (approximate?) numer of particles to create.*/
	private void particles(World worldIn, double X, double Y, double Z, EnumParticleTypes particleType, int count) {
		if (worldIn instanceof WorldServer) {
			FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(worldIn.provider.getDimension()).spawnParticle(particleType, true, X, Y, Z, count, 0.5, 1, 0.5, 0.005D); 
		}
	}
}
