package iamrob.multilink.client.render.tileentity;

import com.xcompwiz.mystcraft.data.Assets;
import iamrob.multilink.client.render.model.ModelBookUnbinder;
import iamrob.multilink.reference.Textures;
import iamrob.multilink.tileentity.TileBookUnbinder;
import net.minecraft.client.model.ModelBook;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

public class TileRenderBookUnbinder extends TileEntitySpecialRenderer
{
    private final ModelBookUnbinder modelBookUnbinder = new ModelBookUnbinder();
    private final ModelBook modelBook = new ModelBook();

    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float var)
    {
        if (te instanceof TileBookUnbinder) {
            TileBookUnbinder tile = (TileBookUnbinder) te;
            ForgeDirection direction = null;

            if (tile.getWorldObj() != null) {
                direction = tile.getOrientation();
            }

            this.bindTexture(Textures.BookUnbinder.modelTexture);

            GL11.glPushMatrix();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
            GL11.glScalef(1.0F, -1.0F, -1.0F);

            short angle = 0;

            if (direction != null) {
                if (direction == ForgeDirection.NORTH) {
                    angle = 180;
                } else if (direction == ForgeDirection.SOUTH) {
                    angle = 0;
                } else if (direction == ForgeDirection.WEST) {
                    angle = 90;
                } else if (direction == ForgeDirection.EAST) {
                    angle = -90;
                }
            }
            GL11.glRotatef(angle, 0.0F, 1.0F, 0.0F);

            modelBookUnbinder.renderAll(-tile.r);

            if (tile.getState() == 1 || tile.getState() == 2) {
                bindTexture(Assets.Entities.linkbook);
                GL11.glTranslatef(0F, 0.74F, -0.125F);
                GL11.glRotatef(90, 0F, 1F, 0F);
                GL11.glRotatef(-90, 0F, 0F, 1F);
                GL11.glScalef(0.8F, 0.8F, 0.8F);
                modelBook.render(null, 0.0F, 0.0F, 0.0F, -230F, 0.0F, 0.0625f);
            }

            GL11.glPopMatrix();

        }
    }
}
