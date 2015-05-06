package iamrob.multilink.client.gui.inventory;

import iamrob.multilink.inventory.ContainerLinkHolder;
import iamrob.multilink.inventory.InventoryLinkHolder;
import iamrob.multilink.reference.Textures;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

public class GuiContainerLinkHolder extends GuiContainer
{
    private ContainerLinkHolder container;
    private static Textures.LinkHolder textures = Textures.LinkHolder.instance;

    public GuiContainerLinkHolder(EntityPlayer player, InventoryLinkHolder linkHolder)
    {
        super(new ContainerLinkHolder(player, linkHolder, true));
        container = (ContainerLinkHolder) this.inventorySlots;

        xSize = textures.guiContainerSizeX;
        ySize = textures.guiContainerSizeY;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_)
    {
        GL11.glColor4f(1F, 1F, 1F, 1F);

        Minecraft.getMinecraft().getTextureManager().bindTexture(textures.invTexture);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }
}
