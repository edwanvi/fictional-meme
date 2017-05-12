package me.itstheholyblack.testmodpleaseignore.proxy;

import me.itstheholyblack.testmodpleaseignore.EventHandles;
import me.itstheholyblack.testmodpleaseignore.blocks.ModBlocks;
import me.itstheholyblack.testmodpleaseignore.core.ManaCalc;
import me.itstheholyblack.testmodpleaseignore.core.PlayerDataMan;
import me.itstheholyblack.testmodpleaseignore.crafting.ShapedRecipes;
import me.itstheholyblack.testmodpleaseignore.crafting.ShapelessRecipes;
import me.itstheholyblack.testmodpleaseignore.crafting.Smelting;
import me.itstheholyblack.testmodpleaseignore.crafting.brewing.AddBrewing;
import me.itstheholyblack.testmodpleaseignore.effect.ModEffects;
import me.itstheholyblack.testmodpleaseignore.entity.ModEntities;
import me.itstheholyblack.testmodpleaseignore.items.ModItems;
import me.itstheholyblack.testmodpleaseignore.items.casters.CasterTicker;
import me.itstheholyblack.testmodpleaseignore.network.PacketHandler;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Level;

public class CommonProxy {
	public void preInit(FMLPreInitializationEvent e) {
		FMLLog.log(Level.INFO, "Registering TMPI event handlers...");
		MinecraftForge.EVENT_BUS.register(new EventHandles());
		MinecraftForge.EVENT_BUS.register(new PlayerDataMan());
		MinecraftForge.EVENT_BUS.register(new ManaCalc());
		MinecraftForge.EVENT_BUS.register(new CasterTicker());
		FMLLog.log(Level.INFO, "Registering TMPI packets...");
		PacketHandler.init();
		FMLLog.log(Level.INFO, "Creating TMPI items...");
		ModItems.createItems();
		FMLLog.log(Level.INFO, "Creating TMPI blocks...");
		ModBlocks.createBlocks();
		FMLLog.log(Level.INFO, "Creating TMPI potion effects...");
		ModEffects.initEffects();
		FMLLog.log(Level.INFO, "Creating TMPI potion items...");
		ModEffects.initItems();
		FMLLog.log(Level.INFO, "Creating TMPI entities...");
		ModEntities.init();
	}

	public void init(FMLInitializationEvent e) {
		FMLLog.log(Level.INFO, "Creating TMPI recipies...");
		ShapelessRecipes.initRecipes();
		Smelting.initRecipies();
		// Forge borked brewing
		AddBrewing.initRecipes();
		ShapedRecipes.initRecipes();
	}

	public void postInit(FMLPostInitializationEvent e) {

	}

	// localization method
	public String localize(String unlocalized, Object... args) {
		return I18n.translateToLocalFormatted(unlocalized, args);
	}
}
