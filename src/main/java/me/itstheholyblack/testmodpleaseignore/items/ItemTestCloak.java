package me.itstheholyblack.testmodpleaseignore.items;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import baubles.api.render.IRenderBauble;
import me.itstheholyblack.testmodpleaseignore.Reference;
import me.itstheholyblack.testmodpleaseignore.model.ModelCloak;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemTestCloak extends Item implements IBauble, IRenderBauble {
	
	private static final ResourceLocation texture = new ResourceLocation("testmodpleaseignore:textures/models/idunncloak.png");
	
	@SideOnly(Side.CLIENT)
	private static ModelCloak model;
	
	public ItemTestCloak() {
		super();
		setRegistryName("itemTestCloak");
		setUnlocalizedName(Reference.MODID + ".itemTestCloak");
		setCreativeTab(ModItems.CREATIVETAB);
		setMaxStackSize(1);
		GameRegistry.register(this);
	}
	
	@Override
	public BaubleType getBaubleType(ItemStack itemstack) {
		return BaubleType.BODY;
	}
	
	@SideOnly(Side.CLIENT)
	ResourceLocation getCloakTexture() {
		return texture;
	}
	
	@Override
	public void onPlayerBaubleRender(ItemStack stack, EntityPlayer player, RenderType type, float partialTicks) {
		if(type == RenderType.BODY) {
			Helper.rotateIfSneaking(player);
			boolean armor = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST) != null;
			GlStateManager.translate(0F, armor ? -0.07F : -0.01F, 0F);

			float s = 1F / 16F;
			GlStateManager.scale(s, s, s);
			if(model == null)
				model = new ModelCloak();

			GlStateManager.enableLighting();
			GlStateManager.enableRescaleNormal();

			Minecraft.getMinecraft().renderEngine.bindTexture(getCloakTexture());
			model.render(1F);

			/* int light = 15728880;
			int lightmapX = light % 65536;
			int lightmapY = light / 65536;
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lightmapX, lightmapY);
			Minecraft.getMinecraft().renderEngine.bindTexture(getCloakGlowTexture());
			model.render(1F); */ 
		}
	}
	@SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
}
