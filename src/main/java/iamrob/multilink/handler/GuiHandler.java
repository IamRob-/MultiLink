package iamrob.multilink.handler;

import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import iamrob.multilink.MultiLink;
import iamrob.multilink.client.gui.inventory.GuiContainerLinkHolder;
import iamrob.multilink.client.gui.inventory.GuiLinkHolder;
import iamrob.multilink.inventory.ContainerLinkHolder;
import iamrob.multilink.inventory.InventoryLinkHolder;
import iamrob.multilink.item.ItemLinkHolder;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class GuiHandler implements IGuiHandler
{

    public static void register()
    {
        NetworkRegistry.INSTANCE.registerGuiHandler(MultiLink.instance, new GuiHandler());
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {

        switch (ID) {
            case 0:
            case 1:
                ItemStack book = player.getHeldItem();
                if ((book.getItem() == null) || (!(book.getItem() instanceof ItemLinkHolder))) {
                    return null;
                }
                return new ContainerLinkHolder(player, new InventoryLinkHolder(book), ID == 0 ? true : false);
        }

        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {

        switch (ID) {
            case 0:
            case 1:
                ItemStack book = player.getHeldItem();
                if ((book.getItem() == null) || (!(book.getItem() instanceof ItemLinkHolder))) {
                    return null;
                }
                return ID == 0 ? new GuiContainerLinkHolder(player, new InventoryLinkHolder(book)) : new GuiLinkHolder(player, new InventoryLinkHolder(book));
        }

        return null;
    }
}
