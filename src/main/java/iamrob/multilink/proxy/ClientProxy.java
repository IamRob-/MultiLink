package iamrob.multilink.proxy;

import iamrob.multilink.creativetab.CreativeTabMultiLink;
import iamrob.multilink.init.ModItems;
import net.minecraft.item.ItemStack;

public class ClientProxy extends CommonProxy {

    @Override
    public void createCreativeTabs() {
        CreativeTabMultiLink creativeTab = new CreativeTabMultiLink();

        creativeTab.registerItemStack(new ItemStack(ModItems.linkHolder,1,0));
    }
}
