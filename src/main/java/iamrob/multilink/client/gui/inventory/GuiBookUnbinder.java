package iamrob.multilink.client.gui.inventory;

import iamrob.multilink.inventory.ContainerBookUnbinder;
import iamrob.multilink.reference.Textures;
import iamrob.multilink.tileentity.TileBookUnbinder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

public class GuiBookUnbinder extends GuiContainer
{
    private ContainerBookUnbinder container;
    private TileBookUnbinder tile;
    private static Textures.BookUnbinder textures = Textures.BookUnbinder.instance;

    public GuiBookUnbinder(EntityPlayer player, TileBookUnbinder tile)
    {
        super(new ContainerBookUnbinder(player, tile));
        container = (ContainerBookUnbinder) this.inventorySlots;
        this.tile = tile;

        xSize = textures.guiSizeX;
        ySize = textures.guiSizeY;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_)
    {
        GL11.glColor4f(1F, 1F, 1F, 1F);
        Minecraft.getMinecraft().getTextureManager().bindTexture(textures.guiTexture);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

        int scaleAdjustment = this.tile.getCookProgressScaled(textures.progressHeight);
        drawTexturedModalRect(guiLeft + textures.progressPlaceX, guiTop + textures.progressPlaceY, textures.progressSourceX, 0, textures.progressWidth, scaleAdjustment);

    }
}
