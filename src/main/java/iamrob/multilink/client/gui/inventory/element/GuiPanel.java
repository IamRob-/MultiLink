package iamrob.multilink.client.gui.inventory.element;

import iamrob.multilink.client.gui.inventory.GuiLinkHolder;
import iamrob.multilink.item.ItemLinkPage;
import iamrob.multilink.network.PacketHandler;
import iamrob.multilink.network.message.MessageActivateBook;
import iamrob.multilink.reference.Textures;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class GuiPanel
{
    private ItemStack linkPage;
    private final Textures.LinkHolder texture = Textures.LinkHolder.instance;

    private int x;
    private int y;
    private int w;
    private int h;
    private Vec3 col;

    private int slot;

    boolean canLink = false;

    public GuiPanel(GuiLinkHolder gui, ItemStack stack, int slot)
    {
        this.linkPage = stack;
        this.slot = slot;

        x = texture.guiPanelStartX;
        y = texture.guiPanelStartY;
        w = texture.guiPanelWidth;
        h = texture.guiPanelHeight;

        ItemLinkPage item = (ItemLinkPage) stack.getItem();
        col = null;
        refreshColours(gui);
    }

    private void refreshColours(GuiLinkHolder gui)
    {
        Vec3[] skyColours = gui.container.skyColours;

        if (skyColours != null && (col == null || (col.xCoord == 1.0 && col.yCoord == 1.0 && col.zCoord == 1.0))) {
            col = skyColours[slot];
        }
    }

    public boolean inRectangle(GuiLinkHolder gui, int mouseX, int mouseY)
    {
        mouseX -= gui.getLeft();
        mouseY -= gui.getTop();
        return x <= mouseX && mouseX <= x + w && y <= mouseY && mouseY <= y + h;
    }

    public void drawString(GuiLinkHolder gui, int mouseX, int mouseY, List<String> list)
    {
        if (inRectangle(gui, mouseX, mouseY)) {
            gui.drawHoverString(list, mouseX - (gui.getLeft() + (gui.getXSize() / 2)), mouseY - gui.getTop());
        }
    }

    public void drawLinkInfo(GuiLinkHolder gui, int x, int y)
    {
        if (!inRectangle(gui, x, y)) {
            return;
        }
        if (linkPage == null || !(linkPage.getItem() instanceof ItemLinkPage))
            return;
        if (gui.container.getCurrentPage() != linkPage || !(gui.getCurrentPageIndex() < gui.container.getPageCount()))
            return;

        ItemLinkPage item = (ItemLinkPage) linkPage.getItem();
        List<String> list = new ArrayList<String>();
        item.addInformation(linkPage, gui.container.player, list, false);

        drawString(gui, x, y, list);
    }

    public void draw(GuiLinkHolder gui)
    {
        ItemStack[] inv = gui.container.inventoryLinkHolder.getItems();

        ItemStack stack = inv[slot];

        if (stack != null) {
            GL11.glColor4f(1F, 1F, 1F, 1F);
            gui.drawTexturedModalRect(gui.getLeft() + x, gui.getTop() + y, texture.guiPanelSourceX, texture.guiPanelSourceY, w, h);

            canLink = gui.container.canLink(slot);

            refreshColours(gui);
            if (col == null) {
                col = Vec3.createVectorHelper(1.0, 1.0, 1.0);
            }

            GL11.glPushMatrix();
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glDepthMask(false);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            GL11.glColor4d(col.xCoord, col.yCoord, col.zCoord, canLink ? 1 : 0.5);

            gui.drawTexturedModalRect(gui.getLeft() + x, gui.getTop() + y, texture.guiPanelSourceX, texture.guiPanelInnerSourceY, w, h);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glPopMatrix();

        }
    }

    public void activate()
    {
        PacketHandler.INSTANCE.sendToServer(new MessageActivateBook((byte) slot));
    }

}
