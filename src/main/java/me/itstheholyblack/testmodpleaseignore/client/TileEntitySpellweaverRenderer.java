package me.itstheholyblack.testmodpleaseignore.client;

import me.itstheholyblack.testmodpleaseignore.blocks.tile_entities.TileEntitySpellweaver;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBook;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.items.ItemStackHandler;

// @SideOnly(Side.CLIENT)
public class TileEntitySpellweaverRenderer extends TileEntitySpecialRenderer<TileEntitySpellweaver> {
	/** The texture for the book above the enchantment table. */
	private static final ResourceLocation TEXTURE_BOOK = new ResourceLocation(
			"textures/entity/enchanting_table_book.png");
	private final ModelBook modelBook = new ModelBook();
	private final RenderItem itemRenderer;
	private final RenderManager renderManager;
	private final Minecraft mc = Minecraft.getMinecraft();
	private static final ResourceLocation MAP_BACKGROUND_TEXTURES = new ResourceLocation(
			"textures/map/map_background.png");

	public TileEntitySpellweaverRenderer() {
		super();
		this.itemRenderer = Minecraft.getMinecraft().getRenderItem();
		this.renderManager = mc.getRenderManager();
	}

	@Override
	public void renderTileEntityAt(TileEntitySpellweaver te, double x, double y, double z, float partialTicks,
			int destroyStage) {
		this.renderItem(te, x, y, z);
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) x + 0.5F, (float) y + 0.6F, (float) z + 0.5F);
		float f = te.tickCount + partialTicks;
		GlStateManager.translate(0.0F, 0.1F + MathHelper.sin(f * 0.1F) * 0.01F, 0.0F);
		float f1;

		for (f1 = te.bookRotation - te.bookRotationPrev; f1 >= (float) Math.PI; f1 -= ((float) Math.PI * 2F)) {
			;
		}

		while (f1 < -(float) Math.PI) {
			f1 += ((float) Math.PI * 2F);
		}

		float f2 = te.bookRotationPrev + f1 * partialTicks;
		GlStateManager.rotate(-f2 * (180F / (float) Math.PI), 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(80.0F, 0.0F, 0.0F, 1.0F);
		this.bindTexture(TEXTURE_BOOK);
		float f3 = te.pageFlipPrev + (te.pageFlip - te.pageFlipPrev) * partialTicks + 0.25F;
		float f4 = te.pageFlipPrev + (te.pageFlip - te.pageFlipPrev) * partialTicks + 0.75F;
		f3 = (f3 - MathHelper.fastFloor(f3)) * 1.6F - 0.3F;
		f4 = (f4 - MathHelper.fastFloor(f4)) * 1.6F - 0.3F;

		if (f3 < 0.0F) {
			f3 = 0.0F;
		}

		if (f4 < 0.0F) {
			f4 = 0.0F;
		}

		if (f3 > 1.0F) {
			f3 = 1.0F;
		}

		if (f4 > 1.0F) {
			f4 = 1.0F;
		}

		float f5 = te.bookSpreadPrev + (te.bookSpread - te.bookSpreadPrev) * partialTicks;
		GlStateManager.enableCull();
		this.modelBook.render((Entity) null, f, f3, f4, f5, 0.0F, 0.0625F);
		GlStateManager.popMatrix();
	}

	private void renderItem(TileEntitySpellweaver te, double x, double y, double z) {
		ItemStackHandler itemHandler = te.getInv();
		// System.out.println(itemHandler.getStackInSlot(0));
		ItemStack itemstack = itemHandler.getStackInSlot(0);

		if (!itemstack.isEmpty()) {
			System.out.println("ItemStack not empty");
			EntityItem entityitem = new EntityItem(te.getWorld(), 0.0D, 0.0D, 0.0D, itemstack);
			Item item = entityitem.getEntityItem().getItem();
			entityitem.getEntityItem().setCount(1);
			entityitem.hoverStart = 0.0F;
			GlStateManager.pushMatrix();
			GlStateManager.translate((float) x + 0.5F, (float) y + 0.6F, (float) z + 0.5F);
			GlStateManager.disableLighting();
			int i = (int) te.bookRotation;

			if (item instanceof net.minecraft.item.ItemMap) {
				i = i % 4 * 2;
			}

			GlStateManager.rotate(i * 360.0F / 8.0F, 0.0F, 0.0F, 1.0F);

			GlStateManager.scale(0.5F, 0.5F, 0.5F);
			GlStateManager.pushAttrib();
			RenderHelper.enableStandardItemLighting();
			System.out.println("Calling renderItem");
			this.itemRenderer.renderItem(entityitem.getEntityItem(), ItemCameraTransforms.TransformType.FIXED);
			RenderHelper.disableStandardItemLighting();
			GlStateManager.popAttrib();

			GlStateManager.enableLighting();
			GlStateManager.popMatrix();
		}
	}
}
