package iamrob.multilink.client.gui.inventory;

import com.xcompwiz.mystcraft.api.linking.ILinkInfo;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import iamrob.multilink.client.gui.inventory.element.GuiPanel;
import iamrob.multilink.inventory.ContainerLinkHolder;
import iamrob.multilink.inventory.InventoryLinkHolder;
import iamrob.multilink.item.ItemLinkPage;
import iamrob.multilink.reference.Textures;
import iamrob.multilink.util.LogHelper;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;
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

        this.container.setCurrentPageIndex(0);

        List<ItemStack> pageList = this.container.getPageList();
        ItemStack[] inventory = this.container.getInventoryLinkHolder().getItems();

        panels = new GuiPanel[container.getPageCount()];
        int slot = 0;
        for (ItemStack stack : inventory) {
            if (stack != null) {
                panels[pageList.indexOf(stack)] = new GuiPanel(this, stack, slot);
            }
            slot++;
        }
    }

    public int getCurrentPageIndex()
    {
        return this.container.getCurrentPageIndex();
    }

    private void pageLeft()
    {
        int currentPage = getCurrentPageIndex() - 1;
        if (currentPage < 0)
            currentPage = 0;
        this.container.setCurrentPageIndex(currentPage);
    }

    private void pageRight()
    {
        int currentPage = getCurrentPageIndex() + 1;
        if (currentPage > this.container.getPageCount())
            currentPage = this.container.getPageCount();
        this.container.setCurrentPageIndex(currentPage);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int mousex, int mousey)
    {
        GL11.glColor4f(1F, 1F, 1F, 1F);

        mc.getTextureManager().bindTexture(textures.coverTexture);
        drawTexturedModalRect(guiLeft + (xSize / 2), getTop(), 0, 0, xSize, ySize);
        mc.getTextureManager().bindTexture(textures.coverLTexture);
        drawTexturedModalRect(guiLeft - (xSize / 2), getTop(), 0, 0, xSize, ySize);

        if (getCurrentPageIndex() > 0) {
            mc.getTextureManager().bindTexture(textures.uiLeftTexture);
            drawTexturedModalRect(guiLeft - (xSize / 2), getTop(), 0, 0, xSize, ySize);
        }

        ItemStack page = this.container.getCurrentPage();
        if (page != null && getCurrentPageIndex() < this.container.getPageCount()) {
            mc.getTextureManager().bindTexture(textures.uiTexture);
            drawTexturedModalRect(guiLeft + (xSize / 2), getTop(), 0, 0, xSize, ySize);
            panels[getCurrentPageIndex()].draw(this);

            ILinkInfo info = ((ItemLinkPage) page.getItem()).getLinkInfo(page);
            if (info != null) {
                String title = info.getDisplayName();
                int l = this.mc.fontRenderer.getStringWidth(title) / 2;
                this.mc.fontRenderer.drawString(title, 380 - l, 94, 0);
            }
        }

        String s = getCurrentPageIndex() + "/" + this.container.getPageCount();
        int i = this.mc.fontRenderer.getStringWidth(s) / 2;
        this.mc.fontRenderer.drawString(EnumChatFormatting.WHITE + s, 320 - i, 74, 0);

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

        LogHelper.info("x: " + x + " y: " + y);

        if (isClickPageLeft(x, y)) {
            pageLeft();
            return;
        } else if (isClickPageRight(x, y)) {
            pageRight();
            return;
        }

        if (getCurrentPageIndex() < this.container.getPageCount()) {
            GuiPanel panel = panels[getCurrentPageIndex()];

            if (panel.inRectangle(this, x, y)) {
                panel.activate();
            }
        }


    }

    @Override
    protected void keyTyped(char c, int i)
    {
        super.keyTyped(c, i);
        if (i == this.mc.gameSettings.keyBindLeft.getKeyCode() || i == Keyboard.KEY_LEFT) {
            pageLeft();
        } else if (i == this.mc.gameSettings.keyBindRight.getKeyCode() || i == Keyboard.KEY_RIGHT) {
            pageRight();
        }
    }

    private boolean isClickPageLeft(int x, int y)
    {
        return 190 <= x && x <= 220 && 230 <= y && y <= 260;
    }

    private boolean isClickPageRight(int x, int y)
    {
        return 400 <= x && x <= 440 && 230 <= y && y <= 260;
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
