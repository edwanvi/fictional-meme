package me.itstheholyblack.testmodpleaseignore;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Reference.MODID, version = Reference.VERSION, name = Reference.MODNAME)
public class mod {
	@Instance
    public static mod instance = new mod();

    @EventHandler
    public void preInit(FMLPreInitializationEvent e) {
      System.out.println("Entering preInit");
    }

    @EventHandler
    public void init(FMLInitializationEvent e) {
      System.out.println("Entering init");
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent e) {
      System.out.println("Entering postInit");
    }
}
