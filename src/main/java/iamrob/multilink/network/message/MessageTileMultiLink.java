package iamrob.multilink.network.message;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import iamrob.multilink.tileentity.TileMultiLink;
import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;

public class MessageTileMultiLink implements IMessage
{
    public int x, y, z;
    public byte orientation, state;

    public MessageTileMultiLink()
    {

    }

    public MessageTileMultiLink(TileMultiLink tile)
    {
        this.x = tile.xCoord;
        this.y = tile.yCoord;
        this.z = tile.zCoord;
        this.orientation = (byte) tile.getOrientation().ordinal();
        this.state = (byte) tile.getState();
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
        this.orientation = buf.readByte();
        this.state = buf.readByte();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
        buf.writeByte(orientation);
        buf.writeByte(state);
    }

    public static class Handler implements IMessageHandler<MessageTileMultiLink, IMessage>
    {

        @Override
        public IMessage onMessage(MessageTileMultiLink message, MessageContext ctx)
        {
            TileEntity tileEntity = FMLClientHandler.instance().getClient().theWorld.getTileEntity(message.x, message.y, message.z);

            if (tileEntity instanceof TileMultiLink) {
                TileMultiLink tile = (TileMultiLink) tileEntity;

                tile.setOrientation(message.orientation);
                tile.setState(message.state);
            }

            return null;
        }
    }
}
