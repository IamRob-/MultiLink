package iamrob.multilink.network.message;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

public class MessageActivateBook implements IMessage, IMessageHandler<MessageActivateBook, IMessage>
{

    public MessageActivateBook()
    {

    }

    @Override
    public void fromBytes(ByteBuf buf)
    {

    }

    @Override
    public void toBytes(ByteBuf buf)
    {

    }

    @Override
    public IMessage onMessage(MessageActivateBook message, MessageContext ctx)
    {
        return null;
    }
}
