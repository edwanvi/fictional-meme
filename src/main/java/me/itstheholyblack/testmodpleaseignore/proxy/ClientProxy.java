package me.itstheholyblack.testmodpleaseignore.proxy;

import me.itstheholyblack.testmodpleaseignore.blocks.ModBlocks;
import me.itstheholyblack.testmodpleaseignore.blocks.tile_entities.TileEntitySpellweaver;
import me.itstheholyblack.testmodpleaseignore.client.TileEntitySpellweaverRenderer;
import me.itstheholyblack.testmodpleaseignore.entity.ModEntities;
import me.itstheholyblack.testmodpleaseignore.items.ModItems;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {
	@Override
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);
        System.out.println("Loading models");
        ModItems.initModels();
        ModBlocks.initModels();
        ModEntities.initModels();
        // bind enchant render
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySpellweaver.class, new TileEntitySpellweaverRenderer());
    }

    @Override
    public void init(FMLInitializationEvent e) {
        super.init(e);
    }

    @Override
    public void postInit(FMLPostInitializationEvent e) {
        super.postInit(e);
    }
    // localization method
    @Override
	public String localize(String unlocalized, Object... args) {
		return I18n.format(unlocalized, args);
    }
}
