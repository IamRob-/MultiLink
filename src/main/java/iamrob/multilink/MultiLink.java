package iamrob.multilink;

import static cpw.mods.fml.common.Mod.EventHandler;
import static cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import iamrob.multilink.handler.GuiHandler;
import iamrob.multilink.init.ModItems;
import iamrob.multilink.network.PacketHandler;
import iamrob.multilink.proxy.IProxy;
import iamrob.multilink.reference.ModInfo;
import iamrob.multilink.reference.Reference;
import iamrob.multilink.util.LogHelper;

@Mod(modid = ModInfo.ID, name = ModInfo.NAME, version = ModInfo.VERSION, dependencies = "required-after:Mystcraft")
public class MultiLink
{

    /*
    TODO
    - pick linkbooks into linkholder
     */

    @Instance(ModInfo.ID)
    public static MultiLink instance;

    @SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
    public static IProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        PacketHandler.init();

        ModItems.init();

        LogHelper.info("Pre Initialization Complete!");
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        GuiHandler.register();

        LogHelper.info("Initialization Complete!");
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        proxy.createCreativeTabs();

        LogHelper.info("Post Initialization Complete!");
    }

}
