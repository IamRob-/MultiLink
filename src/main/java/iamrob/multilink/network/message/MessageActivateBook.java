package iamrob.multilink.network.message;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import iamrob.multilink.item.ItemLinkHolder;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class MessageActivateBook implements IMessage
{
    public byte id;

    public MessageActivateBook()
    {

    }

    public MessageActivateBook(byte id)
    {
        this.id = id;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        id = buf.readByte();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeByte(id);
    }

    public static class Handler implements IMessageHandler<MessageActivateBook, IMessage>
    {
        @Override
        public IMessage onMessage(MessageActivateBook message, MessageContext ctx)
        {
            EntityPlayer player = ctx.getServerHandler().playerEntity;
            if (player != null) {
                ItemStack stack = player.getHeldItem();
                if (stack == null || !(stack.getItem() instanceof ItemLinkHolder))
                    return null;

                ItemLinkHolder item = (ItemLinkHolder) stack.getItem();

                item.linkActivate(message.id, player.worldObj, player);
            }
            return null;
        }
    }
}
