package iamrob.multilink.client.gui.inventory.element;

import com.xcompwiz.mystcraft.item.ItemLinkbook;
import com.xcompwiz.mystcraft.item.ItemLinking;
import iamrob.multilink.client.gui.inventory.GuiLinkHolder;
import iamrob.multilink.network.PacketHandler;
import iamrob.multilink.network.message.MessageActivateBook;
import iamrob.multilink.reference.Textures;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldProvider;
import org.lwjgl.opengl.GL11;

import java.util.Arrays;
import java.util.List;

public class GuiPanel
{
    private ItemStack link;
    private final Textures.LinkHolder texture = Textures.LinkHolder.instance;

    private int x;
    private int y;
    private int w;
    private int h;
    private double xCol;
    private double yCol;
    private double zCol;

    private int id;

    public GuiPanel(ItemStack stack, int id)
    {
        this.link = stack;
        this.id = id;

        x = (texture.guiPanelStartX + (id < 3 ? 0 : 1) * texture.guiPanelPixelsX);
        y = (texture.guiPanelStartY + (id < 3 ? id : id - 3) * texture.guiPanelPixelsY);
        w = texture.guiPanelWidth;
        h = texture.guiPanelHeight;

        ItemLinking item = (ItemLinking) stack.getItem();
        int dimId = item.getLinkInfo(stack).getDimensionUID();
        Vec3 col;

        switch (dimId) {
            case 1:
                col = Vec3.createVectorHelper(0.5F, 0.4F, 0.7F);
                break;
            case -1:
                col = Vec3.createVectorHelper(0.8F, 0.2F, 0.2F);
                break;
            case 0:
                col = Vec3.createVectorHelper(0.3, 0.6F, 1);
                break;
            default:
                col = Vec3.createVectorHelper(0.3, 0.6F, 1);
                break;
        }
        xCol = col.xCoord;
        yCol = col.yCoord;
        zCol = col.zCoord;
    }

    public boolean inRectangle(GuiLinkHolder gui, int mouseX, int mouseY)
    {
        mouseX -= gui.getLeft();
        mouseY -= gui.getTop();
        return x <= mouseX && mouseX <= x + w && y <= mouseY && mouseY <= y + h;
    }

    public void drawString(GuiLinkHolder gui, int mouseX, int mouseY, String str)
    {
        if (inRectangle(gui, mouseX, mouseY)) {
            gui.drawHoverString(Arrays.asList(str.split("\n")), mouseX
                    - (gui.getLeft() + (gui.getXSize() / 2)), mouseY
                    - gui.getTop());
        }
    }

    public void drawLinkInfo(GuiLinkHolder gui, int x, int y)
    {
        if (!inRectangle(gui, x, y)) {
            return;
        }
        if (link == null || !(link.getItem() instanceof ItemLinkbook))
            return;
        ItemLinkbook item = (ItemLinkbook) link.getItem();
        int dimID = item.getLinkInfo(link).getDimensionUID();
        String title = item.getTitle(link);
        String dim = WorldProvider.getProviderForDimension(dimID).getDimensionName();
        String str = title.equals(dim) ? title : title + "\n"
                + EnumChatFormatting.GRAY + dim;
        drawString(gui, x, y, str);
    }

    public void draw(GuiLinkHolder gui)
    {
        List<ItemStack> inventory = gui.container.getInventory();

        ItemStack stack = inventory.get(id);
        if (stack != null) {
            GL11.glColor4f(1F, 1F, 1F, 1F);
            gui.drawTexturedModalRect(gui.getLeft() + x, gui.getTop() + y, texture.guiPanelSourceX, texture.guiPanelSourceY, w, h);

            GL11.glColor3d(xCol, yCol, zCol);

            gui.drawTexturedModalRect(gui.getLeft() + x, gui.getTop() + y, texture.guiPanelSourceX, texture.guiPanelInnerSourceY, w, h);

            GL11.glColor4f(1F, 1F, 1F, 1F);
        }
    }

    public void activate()
    {
        PacketHandler.INSTANCE.sendToServer(new MessageActivateBook((byte) id));
    }

}
