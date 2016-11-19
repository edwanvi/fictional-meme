package me.itstheholyblack.testmodpleaseignore.proxy;

import me.itstheholyblack.testmodpleaseignore.blocks.ModBlocks;
import me.itstheholyblack.testmodpleaseignore.crafting.ShaplessRecipies;
import me.itstheholyblack.testmodpleaseignore.items.ModItems;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {
	public void preInit(FMLPreInitializationEvent e) {
		System.out.println("Creating items...");
		ModItems.createItems();
		System.out.println("Creating blocks...");
		ModBlocks.createBlocks();
    }

    public void init(FMLInitializationEvent e) {
    	System.out.println("initializing recipies");
    	ShaplessRecipies.initRecipies();
    }

    public void postInit(FMLPostInitializationEvent e) {

    }
}
