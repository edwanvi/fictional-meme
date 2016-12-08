package me.itstheholyblack.testmodpleaseignore.proxy;

import me.itstheholyblack.testmodpleaseignore.blocks.ModBlocks;
import me.itstheholyblack.testmodpleaseignore.crafting.ShapedRecipes;
import me.itstheholyblack.testmodpleaseignore.crafting.ShapelessRecipes;
import me.itstheholyblack.testmodpleaseignore.crafting.Smelting;
import me.itstheholyblack.testmodpleaseignore.crafting.brewing.AddBrewing;
import me.itstheholyblack.testmodpleaseignore.effect.ModEffects;
import me.itstheholyblack.testmodpleaseignore.entity.ModEntities;
import me.itstheholyblack.testmodpleaseignore.items.ModItems;
import me.itstheholyblack.testmodpleaseignore.network.PacketHandler;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {
	public void preInit(FMLPreInitializationEvent e) {
		System.out.println("Registering packet handles...");
		PacketHandler.init();
		System.out.println("Creating items...");
		ModItems.createItems();
		System.out.println("Creating blocks...");
		ModBlocks.createBlocks();
		FMLLog.info("", "Creating potion effects...");
		ModEffects.initEffects();
		FMLLog.info("", "Creating potion items...");
		ModEffects.initItems();
		FMLLog.info("Creating mobs", "");
		ModEntities.init();
	}
    public void init(FMLInitializationEvent e) {
    	System.out.println("initializing recipies");
    	ShapelessRecipes.initRecipes();
    	Smelting.initRecipies();
    	// Forge borked brewing
    	AddBrewing.initRecipes();
    	ShapedRecipes.initRecipes();
    }

    public void postInit(FMLPostInitializationEvent e) {

    }
}
