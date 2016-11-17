package me.itstheholyblack.testmodpleaseignore;

import me.itstheholyblack.testmodpleaseignore.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Reference.MODID, version = Reference.VERSION, name = Reference.MODNAME)
public class mod {
	@Instance
    public static mod instance = new mod();

	@SidedProxy(clientSide="me.itstheholyblack.testmodpleaseignore.proxy.ClientProxy", serverSide="me.itstheholyblack.testmodpleaseignore.proxy.ServerProxy")
	public static CommonProxy proxy;
	// this.proxy.stage(e) calls the method stage for the client or server proxy depending on what we're running on
    @EventHandler
    public void preInit(FMLPreInitializationEvent e) {
      System.out.println("Entering preInit");
      proxy.preInit(e);
    }

    @EventHandler
    public void init(FMLInitializationEvent e) {
      System.out.println("Entering init");
      proxy.init(e);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent e) {
      System.out.println("Entering postInit");
      proxy.postInit(e);
    }
}
