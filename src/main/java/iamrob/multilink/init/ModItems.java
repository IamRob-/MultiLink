package iamrob.multilink.init;

import cpw.mods.fml.common.registry.GameRegistry;
import iamrob.multilink.item.ItemLinkHolder;
import iamrob.multilink.item.ItemLinkPage;
import iamrob.multilink.item.ItemMultiLink;
import iamrob.multilink.reference.ModInfo;
import iamrob.multilink.reference.Names;

@GameRegistry.ObjectHolder(ModInfo.ID)
public class ModItems
{

    public static final ItemMultiLink linkHolder = new ItemLinkHolder();
    public static final ItemMultiLink linkPage = new ItemLinkPage();

    public static void init()
    {
        GameRegistry.registerItem(linkHolder, Names.Items.LINK_HOLDER);
        GameRegistry.registerItem(linkPage, Names.Items.LINK_PAGE);
    }

}
