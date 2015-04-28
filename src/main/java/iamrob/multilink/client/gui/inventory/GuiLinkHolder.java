package iamrob.multilink.client.gui.inventory;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import iamrob.multilink.client.gui.inventory.element.GuiPanel;
import iamrob.multilink.inventory.ContainerLinkHolder;
import iamrob.multilink.inventory.InventoryLinkHolder;
import iamrob.multilink.reference.Textures;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiLinkHolder extends GuiContainer
{
    public ContainerLinkHolder container;
    private final GuiPanel[] panels;

    private static Textures.LinkHolder textures = Textures.LinkHolder.instance;

    public GuiLinkHolder(EntityPlayer player, InventoryLinkHolder linkHolder)
    {
        super(new ContainerLinkHolder(player, linkHolder, false));
        this.container = (ContainerLinkHolder) this.inventorySlots;

        xSize = textures.guiSizeX;
        ySize = textures.guiSizeY;

        ItemStack[] inventory = container.getInventoryLinkHolder().getItems();

        panels = new GuiPanel[6];
        for (int i = 0; i < 6; i++) {
            ItemStack stack = inventory[i];
            if (stack != null) {
                panels[i] = new GuiPanel(this, stack, i);
            }
        }
    }

    private static final ResourceLocation texture = new ResourceLocation(Textures.textureLoc, textures.uiLoc);
    private static final ResourceLocation textureLeft = new ResourceLocation(Textures.textureLoc, textures.uiLeftLoc);


    @Override
    protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_)
    {
        GL11.glColor4f(1F, 1F, 1F, 1F);

        mc.getTextureManager().bindTexture(textureLeft);
        drawTexturedModalRect(guiLeft - (xSize / 2), getTop(), 0, 0, xSize, ySize);

        mc.getTextureManager().bindTexture(texture);
        drawTexturedModalRect(guiLeft + (xSize / 2), getTop(), 0, 0, xSize, ySize);

        for (GuiPanel panel : panels) {
            if (panel != null) {
                panel.draw(this);
            }
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int x, int y)
    {
        for (GuiPanel panel : panels) {
            if (panel != null) {
                panel.drawLinkInfo(this, x, y);
            }
        }
    }

    public void drawHoverString(List<String> list, int x, int y)
    {
        drawHoveringText(list, x, y, fontRendererObj);
    }

    @Override
    protected void mouseClicked(int x, int y, int button)
    {
        super.mouseClicked(x, y, button);
        if (button != 0) return;

        for (GuiPanel panel : panels) {
            if (panel == null)
                continue;
            if (panel.inRectangle(this, x, y)) {
                panel.activate();
            }
        }
    }

    public int getLeft()
    {
        return guiLeft - (xSize / 2);
    }

    public int getTop()
    {
        return guiTop;
    }

    public int getXSize()
    {
        return xSize;
    }
}
