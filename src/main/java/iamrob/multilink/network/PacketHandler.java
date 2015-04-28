package iamrob.multilink.network;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import iamrob.multilink.network.message.MessageActivateBook;
import iamrob.multilink.reference.ModInfo;

public class PacketHandler
{

    public static final SimpleNetworkWrapper instance = NetworkRegistry.INSTANCE.newSimpleChannel(ModInfo.ID);

    public static void init()
    {
        instance.registerMessage(MessageActivateBook.class, MessageActivateBook.class, 0, Side.SERVER);
    }
}
