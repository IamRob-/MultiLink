package iamrob.multilink.proxy;

import cpw.mods.fml.client.registry.ClientRegistry;
import iamrob.multilink.client.render.item.ItemRenderTileEntity;
import iamrob.multilink.client.render.tileentity.TileRenderBookUnbinder;
import iamrob.multilink.creativetab.CreativeTabMultiLink;
import iamrob.multilink.init.ModBlocks;
import iamrob.multilink.init.ModItems;
import iamrob.multilink.tileentity.TileBookUnbinder;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.MinecraftForgeClient;

public class ClientProxy extends CommonProxy
{

    @Override
    public void createCreativeTabs()
    {
        CreativeTabMultiLink creativeTab = new CreativeTabMultiLink();

        creativeTab.registerItemStack(new ItemStack(ModItems.linkHolder, 1, 0));
        creativeTab.registerItemStack(new ItemStack(ModItems.linkPage, 1, 0));
        creativeTab.registerItemStack(new ItemStack(ModBlocks.bookUnbinder, 1, 0));
    }

    @Override
    public void initRendering()
    {
        TileEntitySpecialRenderer render = new TileRenderBookUnbinder();
        ClientRegistry.bindTileEntitySpecialRenderer(TileBookUnbinder.class, render);
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.bookUnbinder), new ItemRenderTileEntity(render, new TileBookUnbinder()));

    }


}
