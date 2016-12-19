package me.itstheholyblack.testmodpleaseignore.entity.render;

import me.itstheholyblack.testmodpleaseignore.entity.EntityGeminus_M;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderGeminus_M extends RenderLiving<EntityGeminus_M> {
	public static final Factory FACTORY = new Factory();
	private ResourceLocation mobTexture = new ResourceLocation("testmodpleaseignore:textures/entity/geminus_m.png");

	public RenderGeminus_M(RenderManager rendermanagerIn) {
		super(rendermanagerIn, new ModelPlayer(0.5F, false), 0.5F);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityGeminus_M entity) {
		return mobTexture;
	}

	public static class Factory implements IRenderFactory<EntityGeminus_M> {
		@Override
		public Render<? super EntityGeminus_M> createRenderFor(RenderManager manager) {
			return new RenderGeminus_M(manager);
		}
	}
}
