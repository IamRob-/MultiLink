package iamrob.multilink.client.render.item;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

public class ItemRenderTileEntity implements IItemRenderer
{
    TileEntitySpecialRenderer render;
    private TileEntity dummytile;

    public ItemRenderTileEntity(TileEntitySpecialRenderer render, TileEntity tile)
    {
        this.render = render;
        this.dummytile = tile;
    }

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type)
    {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
    {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data)
    {
        GL11.glPushMatrix();
        if (type == IItemRenderer.ItemRenderType.ENTITY)
            GL11.glTranslatef(-0.5F, 0.0F, -0.5F);
        else if (type == ItemRenderType.EQUIPPED_FIRST_PERSON) {
            GL11.glTranslatef(1F, 0F, 0F);
            GL11.glRotatef(-90, 0F, 1F, 0F);
        }
        this.render.renderTileEntityAt(this.dummytile, 0.0D, 0.0D, 0.0D, 0.0F);
        GL11.glPopMatrix();
    }
}
