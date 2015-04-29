package iamrob.multilink.network.message;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import iamrob.multilink.inventory.ContainerLinkHolder;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Container;
import net.minecraft.util.Vec3;

public class MessageSkyColour implements IMessage
{
    public Vec3[] skyColours;

    public MessageSkyColour()
    {

    }

    public MessageSkyColour(Vec3[] skyColours)
    {
        this.skyColours = skyColours;
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
//        LogHelper.info("to:");
        buf.writeByte(skyColours.length);
        for (byte i = 0; i < skyColours.length; i++) {
            Vec3 vec = skyColours[i];
            if (vec == null)
                continue;
            buf.writeByte(i);
            buf.writeDouble(vec.xCoord);
            buf.writeDouble(vec.yCoord);
            buf.writeDouble(vec.zCoord);
        }
//        LogHelper.logVecArray(skyColours);
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
//        LogHelper.info("from:");
        byte length = buf.readByte();
        skyColours = new Vec3[length];
        while (buf.isReadable()) {
            byte index = buf.readByte();
            double x = buf.readDouble();
            double y = buf.readDouble();
            double z = buf.readDouble();
            skyColours[index] = Vec3.createVectorHelper(x, y, z);
        }
//        LogHelper.logVecArray(skyColours);
    }

    public static class Handler implements IMessageHandler<MessageSkyColour, IMessage>
    {
        @Override
        public IMessage onMessage(MessageSkyColour message, MessageContext ctx)
        {
            Container container = Minecraft.getMinecraft().thePlayer.openContainer;
//            LogHelper.info("Container: " + container);
            if (container == null || !(container instanceof ContainerLinkHolder)) {
                return null;
            }
//            LogHelper.info("onMessage:");
//            LogHelper.logVecArray(message.skyColours);

            ContainerLinkHolder linkHolder = (ContainerLinkHolder) container;
            linkHolder.skyColours = message.skyColours;
//            LogHelper.info("saving...");
            linkHolder.saveSkyColours();
            return null;
        }
    }
}
