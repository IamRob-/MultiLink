package iamrob.multilink.creativetab;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import iamrob.multilink.init.ModItems;
import iamrob.multilink.reference.ModInfo;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CreativeTabMultiLink extends CreativeTabs
{

    private List<ItemStack> forcelist = new ArrayList();

    public CreativeTabMultiLink()
    {
        super(ModInfo.ID.toLowerCase());
    }

    @Override
    public void displayAllReleventItems(List list)
    {
        Iterator iterator = Item.itemRegistry.iterator();

        while (iterator.hasNext()) {
            Item item = (Item) iterator.next();
            if (item != null) {
                for (CreativeTabs tab : item.getCreativeTabs()) {
                    if (tab == this)
                        item.getSubItems(item, this, list);
                }
            }
        }
        for (ItemStack itemstack : this.forcelist) {
            list.add(itemstack);
        }
    }

    public void registerItemStack(ItemStack itemstack)
    {
        this.forcelist.add(itemstack);
    }


    @Override
    @SideOnly(Side.CLIENT)
    public Item getTabIconItem()
    {
        return ModItems.linkHolder;
    }
}
