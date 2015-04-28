package iamrob.multilink.network.message;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import iamrob.multilink.inventory.ContainerLinkHolder;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Container;

import java.util.ArrayList;
import java.util.List;

public class MessageCanLink implements IMessage
{
    public List<Byte> links;

    public MessageCanLink()
    {

    }

    public MessageCanLink(List<Byte> links)
    {
        this.links = links;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        List<Byte> list = new ArrayList<Byte>();
        int size = buf.readByte();
        for (int i = 0; i < size; i++) {
            list.add(buf.readByte());
        }
        links = list;
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeByte(links.size());
        for (Byte i : links) {
            buf.writeByte(i);
        }
    }

    public static class Handler implements IMessageHandler<MessageCanLink, IMessage>
    {
        @Override
        public IMessage onMessage(MessageCanLink message, MessageContext ctx)
        {
            Container container = Minecraft.getMinecraft().thePlayer.openContainer;
            if (container == null || !(container instanceof ContainerLinkHolder)) {
                return null;
            }

            ((ContainerLinkHolder) container).linksPermitted = message.links;
            return null;
        }
    }
}
