package iamrob.multilink.network;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import iamrob.multilink.network.message.MessageActivateBook;
import iamrob.multilink.network.message.MessageCanLink;
import iamrob.multilink.reference.ModInfo;

public class PacketHandler
{

    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(ModInfo.ID);

    public static void init()
    {
        INSTANCE.registerMessage(MessageActivateBook.Handler.class, MessageActivateBook.class, 0, Side.SERVER);
        INSTANCE.registerMessage(MessageCanLink.Handler.class, MessageCanLink.class, 1, Side.CLIENT);
    }
}
