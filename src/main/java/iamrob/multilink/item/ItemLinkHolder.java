package iamrob.multilink.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import iamrob.multilink.MultiLink;
import iamrob.multilink.inventory.ContainerLinkHolder;
import iamrob.multilink.reference.Names;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class ItemLinkHolder extends ItemMultiLink
{

    public ItemLinkHolder()
    {
        super();
        this.setUnlocalizedName(Names.Items.LINK_HOLDER);

        setMaxStackSize(1);
        setMaxDamage(32);
    }

    @SideOnly(Side.CLIENT)
    private IIcon emptyIcon;

    @SideOnly(Side.CLIENT)
    private IIcon halfIcon;

    @SideOnly(Side.CLIENT)
    private IIcon fullIcon;

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register)
    {
        emptyIcon = register.registerIcon(getIconName() + "_empty");
        halfIcon = register.registerIcon(getIconName() + "_half");
        fullIcon = register.registerIcon(getIconName() + "_full");

        itemIcon = fullIcon;
    }

    @Override
    public IIcon getIcon(ItemStack stack, int pass)
    {
        int size = linkBookCount(stack).size();
        switch (size) {
            case 0:
                return emptyIcon;
            case 6:
                return fullIcon;
            default:
                return halfIcon;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses()
    {
        return true;
    }

    public ItemStack[] getHolderItems(ItemStack stack)
    {
        if (stack.getTagCompound() == null) {
            return new ItemStack[ContainerLinkHolder.inventorySize];
        }

        ItemStack[] items = new ItemStack[ContainerLinkHolder.inventorySize];

        NBTTagList list = (NBTTagList) stack.getTagCompound().getTag("link_holder_items");

        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound link = (NBTTagCompound) list.getCompoundTagAt(i);
            int index = link.getInteger("index");
            items[index] = ItemStack.loadItemStackFromNBT(link);
        }

        return items;
    }

    public List<Integer> linkBookCount(ItemStack book)
    {
        List<Integer> list = new ArrayList<Integer>();
        int i = 0;
        for (ItemStack stack : getHolderItems(book)) {
            if (stack != null) {
                list.add(i);
            }
            i++;
        }
        return list;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer player)
    {
        if (player.isSneaking()) {
            if (!world.isRemote) {
                player.openGui(MultiLink.instance, 0, player.worldObj, (int) player.posX, (int) player.posY, (int) player.posZ);
            }
        } else {
            if (!world.isRemote) {
                player.openGui(MultiLink.instance, 1, player.worldObj, (int) player.posX, (int) player.posY, (int) player.posZ);
            }
        }
        return itemstack;
    }

}
