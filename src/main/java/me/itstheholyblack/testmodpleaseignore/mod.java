package me.itstheholyblack.testmodpleaseignore;

import me.itstheholyblack.testmodpleaseignore.proxy.CommonProxy;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Level;

@Mod(modid = Reference.MODID, version = Reference.VERSION, name = Reference.MODNAME, acceptedMinecraftVersions = Reference.ACCPETED_MINECRAFT_VERSION, dependencies = Reference.DEPENDENCIES)
public class mod {
    @Instance
    public static mod instance = new mod();

    @SidedProxy(clientSide = "me.itstheholyblack.testmodpleaseignore.proxy.ClientProxy", serverSide = "me.itstheholyblack.testmodpleaseignore.proxy.ServerProxy")
    public static CommonProxy proxy;

    // proxy.stage(e) calls the method stage for the client or server proxy
    // depending on what we're running on
    @EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        FMLLog.log(Level.INFO, "Entering pre init");
        proxy.preInit(e);
    }

    @EventHandler
    public void init(FMLInitializationEvent e) {
        FMLLog.log(Level.INFO, "Entering init");
        proxy.init(e);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        FMLLog.log(Level.INFO, "Entering post init");
        proxy.postInit(e);
    }
}
