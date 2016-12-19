package me.itstheholyblack.testmodpleaseignore.entity.render;

import me.itstheholyblack.testmodpleaseignore.entity.EntityGeminus_F;
import me.itstheholyblack.testmodpleaseignore.entity.EntityGeminus_M;
import me.itstheholyblack.testmodpleaseignore.entity.render.RenderGeminus_M.Factory;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderGeminus_F extends RenderLiving<EntityGeminus_F> {
	public static final Factory FACTORY = new Factory();
	private ResourceLocation mobTexture = new ResourceLocation("testmodpleaseignore:textures/entity/geminus_f.png");

	public RenderGeminus_F(RenderManager rendermanagerIn) {
		super(rendermanagerIn, new ModelPlayer(0.5F, true), 0.5F);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityGeminus_F entity) {
		return mobTexture;
	}

	public static class Factory implements IRenderFactory<EntityGeminus_F> {
		@Override
		public Render<? super EntityGeminus_F> createRenderFor(RenderManager manager) {
			return new RenderGeminus_F(manager);
		}
	}
}
