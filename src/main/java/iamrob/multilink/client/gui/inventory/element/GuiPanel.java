package iamrob.multilink.client.gui.inventory.element;

import com.xcompwiz.mystcraft.api.linking.ILinkInfo;
import com.xcompwiz.mystcraft.item.ItemLinkbook;
import com.xcompwiz.mystcraft.item.ItemLinking;
import com.xcompwiz.mystcraft.world.WorldProviderMyst;
import com.xcompwiz.mystcraft.world.agedata.AgeData;
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

public class GuiPanel
{
    private ItemStack link;
    private final Textures.LinkHolder texture = Textures.LinkHolder.instance;

    private int x;
    private int y;
    private int w;
    private int h;
    private Vec3 col;

    private int id;

    boolean canLink = false;

    public GuiPanel(GuiLinkHolder gui, ItemStack stack, int id)
    {
        this.link = stack;
        this.id = id;

        x = (texture.guiPanelStartX + (id < 3 ? 0 : 1) * texture.guiPanelPixelsX);
        y = (texture.guiPanelStartY + (id < 3 ? id : id - 3) * texture.guiPanelPixelsY);
        w = texture.guiPanelWidth;
        h = texture.guiPanelHeight;

        ItemLinking item = (ItemLinking) stack.getItem();
        int dimId = item.getLinkInfo(stack).getDimensionUID();
        col = null;
        refreshColours(gui);
    }

    private void refreshColours(GuiLinkHolder gui)
    {
        Vec3[] skyColours = gui.container.skyColours;

        if (skyColours != null && (col == null
                || (col.xCoord == 0.0 && col.yCoord == 0.0 && col.zCoord == 0.0))) {
            col = skyColours[id];
        }
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
        ILinkInfo info = item.getLinkInfo(link);
        int dimID = info.getDimensionUID();
        String title = item.getTitle(link);
        WorldProvider world = WorldProvider.getProviderForDimension(dimID);
        String dim;
        if (world instanceof WorldProviderMyst) {
            dim = AgeData.getAge(dimID, true).getAgeName();
        } else {
            dim = world.getDimensionName();
        }
        String str = title.equals(dim) ? title : title + "\n" + EnumChatFormatting.GRAY + dim;
        drawString(gui, x, y, str);
    }

    public void draw(GuiLinkHolder gui)
    {
        ItemStack[] inventory = gui.container.getInventoryLinkHolder().getItems();

        ItemStack stack = inventory[id];
        if (stack != null) {
            GL11.glColor4f(1F, 1F, 1F, 1F);
            gui.drawTexturedModalRect(gui.getLeft() + x, gui.getTop() + y, texture.guiPanelSourceX, texture.guiPanelSourceY, w, h);

            canLink = gui.container.canLink(id);

            refreshColours(gui);
            if (col == null) {
                col = Vec3.createVectorHelper(0F, 0F, 0F);
            }

            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glDepthMask(false);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            GL11.glColor4d(col.xCoord, col.yCoord, col.zCoord, canLink ? 1 : 0.5);

            GL11.glDisable(GL11.GL_ALPHA_TEST);

            gui.drawTexturedModalRect(gui.getLeft() + x, gui.getTop() + y, texture.guiPanelSourceX, texture.guiPanelInnerSourceY, w, h);

            GL11.glColor4f(1F, 1F, 1F, 1F);
        }
    }

    public void activate()
    {
        PacketHandler.INSTANCE.sendToServer(new MessageActivateBook((byte) id));
    }

}
