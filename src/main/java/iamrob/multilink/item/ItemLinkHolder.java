package iamrob.multilink.item;

import com.xcompwiz.mystcraft.api.linking.ILinkInfo;
import com.xcompwiz.mystcraft.block.BlockBookDisplay;
import com.xcompwiz.mystcraft.entity.EntityLinkbook;
import com.xcompwiz.mystcraft.item.ItemLinkbook;
import com.xcompwiz.mystcraft.item.ItemLinking;
import com.xcompwiz.mystcraft.linking.LinkController;
import com.xcompwiz.mystcraft.linking.LinkListenerManager;
import com.xcompwiz.mystcraft.tileentity.TileEntityBookRotateable;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import iamrob.multilink.MultiLink;
import iamrob.multilink.inventory.InventoryLinkHolder;
import iamrob.multilink.reference.Names;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
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
//        NBTTagCompound nbtTagCompound = stack.getTagCompound();
//        ItemStack[] inventory = new ItemStack[InventoryLinkHolder.inventorySize];
//
//        if (nbtTagCompound != null && nbtTagCompound.hasKey(Names.NBT.ITEMS)) {
//            NBTTagList tagList = nbtTagCompound.getTagList(Names.NBT.ITEMS, 10);
//            for (int i = 0; i < tagList.tagCount(); ++i) {
//                NBTTagCompound tagCompound = tagList.getCompoundTagAt(i);
//                byte slotIndex = tagCompound.getByte("Slot");
//                if (slotIndex >= 0 && slotIndex < inventory.length) {
//                    inventory[slotIndex] = ItemStack.loadItemStackFromNBT(tagCompound);
//                }
//            }
//        }
        InventoryLinkHolder inv = new InventoryLinkHolder(stack);
        return inv.getItems();
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

    @Override
    public boolean onItemUse(ItemStack p_77648_1_, EntityPlayer p_77648_2_, World p_77648_3_, int p_77648_4_, int p_77648_5_, int p_77648_6_, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_)
    {
        return false;
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        if (world.isRemote || player.isSneaking())
            return false;

        Block block = world.getBlock(x, y, z);

        if (block instanceof BlockBookDisplay) {
            final TileEntityBookRotateable tile = (TileEntityBookRotateable) world.getTileEntity(x, y, z);
            if (tile == null) {
                return true;
            }
            List<Integer> slotList = getSlotList(stack);

            if (tile.getBook() == null) {
                if (slotList.size() == 0) {
                    return false;
                }
                ItemStack[] items = getInventory(stack);
                int slot = firstBookSlot(slotList);
                ItemStack linkBook = items[slot];
                if (linkBook != null && tile.isItemValidForSlot(0, linkBook)) {
                    tile.setBook(linkBook);
                    InventoryLinkHolder inv = new InventoryLinkHolder(stack);
                    inv.setInventorySlotContents(slot, null);
                    inv.save();
                    return true;
                }
            } else {
                if (slotList.size() == 6) {
                    return false;
                }
                ItemStack linkBook = tile.getBook();
                InventoryLinkHolder inv = new InventoryLinkHolder(stack);
                inv.setInventorySlotContents(firstAvailableSlot(slotList), linkBook);
                inv.save();
                tile.setBook(null);
                return true;
            }
        }
        return true;
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
    {
        if (!(entity instanceof EntityLinkbook)) {
            return false;
        }
        List<Integer> slotList = getSlotList(stack);
        if (slotList.size() == 6) {
            return false;
        }
        EntityLinkbook link = (EntityLinkbook) entity;
        ItemStack linkBook = link.getStackInSlot(0);
        InventoryLinkHolder inv = new InventoryLinkHolder(stack);
        inv.setInventorySlotContents(firstAvailableSlot(slotList), linkBook);
        inv.save();
        link.inventory.setBook(null);
        return true;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack)
    {
        return 1;
    }

    private List<Integer> getSlotList(ItemStack book)
    {
        List<Integer> slots = new ArrayList<Integer>();
        int i = 0;
        for (ItemStack stack : getInventory(book)) {
            if (stack != null) {
                slots.add(i);
            }
            i++;
        }
        return slots;
    }

    private int firstAvailableSlot(List<Integer> slots)
    {
        for (int i = 0; i < 6; i++) {
            if (!slots.contains(i)) {
                return i;
            }
        }
        return 5;
    }

    private int firstBookSlot(List<Integer> slots)
    {
        for (int i = 0; i < 6; i++) {
            if (slots.contains(i)) {
                return i;
            }
        }
        return 0;
    }

    @Override
    public boolean isItemTool(ItemStack p_77616_1_)
    {
        return false;
    }

    @Override
    public int getItemEnchantability()
    {
        return 1;
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
