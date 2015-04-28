package iamrob.multilink.item;

import com.xcompwiz.mystcraft.api.linking.ILinkInfo;
import com.xcompwiz.mystcraft.item.ItemLinkbook;
import com.xcompwiz.mystcraft.item.ItemLinking;
import com.xcompwiz.mystcraft.linking.LinkController;
import com.xcompwiz.mystcraft.linking.LinkListenerManager;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import iamrob.multilink.MultiLink;
import iamrob.multilink.inventory.InventoryLinkHolder;
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
        int size = getLinkBookCount(stack);
        switch (size) {
            case 0:
                return emptyIcon;
            case 6:
                return fullIcon;
            default:
                return halfIcon;
        }
    }

    public int getLinkBookCount(ItemStack holder)
    {
        ItemStack[] inventory = getInventory(holder);

        List<Integer> slots = new ArrayList<Integer>();
        int i = 0;
        for (ItemStack stack : inventory) {
            if (stack != null) {
                slots.add(i);
            }
            i++;
        }
        return slots.size();
    }

    public ItemStack[] getInventory(ItemStack stack)
    {
        NBTTagCompound nbtTagCompound = stack.getTagCompound();
        ItemStack[] inventory = new ItemStack[InventoryLinkHolder.inventorySize];

        if (nbtTagCompound != null && nbtTagCompound.hasKey(Names.NBT.ITEMS)) {
            NBTTagList tagList = nbtTagCompound.getTagList(Names.NBT.ITEMS, 10);
            for (int i = 0; i < tagList.tagCount(); ++i) {
                NBTTagCompound tagCompound = tagList.getCompoundTagAt(i);
                byte slotIndex = tagCompound.getByte("Slot");
                if (slotIndex >= 0 && slotIndex < inventory.length) {
                    inventory[slotIndex] = ItemStack.loadItemStackFromNBT(tagCompound);
                }
            }
        }
        return inventory;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses()
    {
        return true;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer player)
    {
        if (!world.isRemote) {
            if (player.isSneaking()) {
                player.openGui(MultiLink.instance, 0, player.worldObj, (int) player.posX, (int) player.posY, (int) player.posZ);
            } else {
                player.openGui(MultiLink.instance, 1, player.worldObj, (int) player.posX, (int) player.posY, (int) player.posZ);
            }
        }
        return itemstack;
    }

    public void linkActivate(int id, World world, EntityPlayer player)
    {
        if (world.isRemote) {
            return;
        }

        ItemStack bookStack = player.getHeldItem();
        ItemStack[] inventory = getInventory(bookStack);

        if (inventory[id] == null || !(inventory[id].getItem() instanceof ItemLinkbook)) {
            return;
        }
        ItemStack linkStack = inventory[id];
        ItemLinking item = (ItemLinking) linkStack.getItem();

        if (linkStack.getTagCompound() == null) {
            return;
        }

        ILinkInfo info = item.getLinkInfo(linkStack);
        if (LinkListenerManager.isLinkPermitted(world, player, info)) {
            int damage = bookStack.getItemDamage();
            if (damage == bookStack.getMaxDamage()) {
                for (ItemStack stack : inventory) {
                    if (stack == null)
                        continue;
                    ItemLinking link = (ItemLinking) stack.getItem();
                    world.spawnEntityInWorld(link.createEntity(world, player, stack));
                }
                player.setCurrentItemOrArmor(0, null);
                return;
            }
            LinkController.travelEntity(world, player, info);
            bookStack.setItemDamage(bookStack.getItemDamage() + 1);
        }
    }

}
