package me.itstheholyblack.testmodpleaseignore.entity.render;

import me.itstheholyblack.testmodpleaseignore.entity.EntityExplosiveArrow;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderArrow;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderExplosiveArrow extends RenderArrow<EntityExplosiveArrow> {
    public static final Factory FACTORY = new Factory();
    public static final ResourceLocation RES_ARROW = new ResourceLocation("textures/entity/projectiles/arrow.png");
    public static final ResourceLocation RES_TIPPED_ARROW = new ResourceLocation(
            "textures/entity/projectiles/tipped_arrow.png");

    public RenderExplosiveArrow(RenderManager manager) {
        super(manager);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called
     * unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntityExplosiveArrow entity) {
        return RES_ARROW;
    }

    public static class Factory implements IRenderFactory<EntityExplosiveArrow> {
        @Override
        public Render<? super EntityExplosiveArrow> createRenderFor(RenderManager manager) {
            return new RenderExplosiveArrow(manager);
        }
    }
}