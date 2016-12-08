package me.itstheholyblack.testmodpleaseignore.entity.render;

import me.itstheholyblack.testmodpleaseignore.entity.EntityGeminus_M;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderGeminus_M extends RenderLiving<EntityGeminus_M> {
	public RenderGeminus_M(RenderManager rendermanagerIn, ModelBase modelbaseIn, float shadowsizeIn) {
		super(rendermanagerIn, new ModelPlayer(shadowsizeIn, false), shadowsizeIn);
		// TODO Auto-generated constructor stub
	}
	@Override
	protected ResourceLocation getEntityTexture(EntityGeminus_M entity) {
		// TODO Auto-generated method stub
		return null;
	}
}
