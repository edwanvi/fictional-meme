package me.itstheholyblack.testmodpleaseignore.proxy;

import me.itstheholyblack.testmodpleaseignore.blocks.ModBlocks;
import me.itstheholyblack.testmodpleaseignore.crafting.ShapelessRecipes;
import me.itstheholyblack.testmodpleaseignore.crafting.Smelting;
import me.itstheholyblack.testmodpleaseignore.crafting.brewing.AddBrewing;
import me.itstheholyblack.testmodpleaseignore.effect.ModEffects;
import me.itstheholyblack.testmodpleaseignore.items.ModItems;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {
	public void preInit(FMLPreInitializationEvent e) {
		System.out.println("Creating items...");
		ModItems.createItems();
		System.out.println("Creating blocks...");
		ModBlocks.createBlocks();
		FMLLog.info("", "Creating potion effects...");
		ModEffects.initEffects();
		FMLLog.info("", "Creating potion items...");
		ModEffects.initItems();
	}
    public void init(FMLInitializationEvent e) {
    	System.out.println("initializing recipies");
    	ShapelessRecipes.initRecipes();
    	Smelting.initRecipies();
    	// Forge borked brewing
    	AddBrewing.initRecipes();
    }

    public void postInit(FMLPostInitializationEvent e) {

    }
}
