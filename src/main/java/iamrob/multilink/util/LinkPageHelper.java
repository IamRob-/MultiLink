package iamrob.multilink.util;

import com.xcompwiz.mystcraft.linking.LinkOptions;
import iamrob.multilink.init.ModItems;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class LinkPageHelper
{
    public static ItemStack getLinkPageFromBook(ItemStack linkbook)
    {
        ItemStack linkPage = new ItemStack(ModItems.linkPage);

        linkPage.setTagCompound((NBTTagCompound) linkbook.getTagCompound().copy());
        LinkOptions.setFlag(linkPage.stackTagCompound, "Following", true);
        LinkOptions.setFlag(linkPage.stackTagCompound, "Intra Linking", true);

        return linkPage;
    }


}
