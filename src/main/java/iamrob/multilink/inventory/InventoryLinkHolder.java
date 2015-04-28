package iamrob.multilink.inventory;

import iamrob.multilink.init.ModItems;
import iamrob.multilink.item.ItemLinkHolder;
import iamrob.multilink.reference.Names;
import iamrob.multilink.util.INBTTaggable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class InventoryLinkHolder implements IInventory, INBTTaggable
{

    public ItemStack parentItemStack;
    public static int inventorySize = 6;
    private ItemStack[] inventory;

    public InventoryLinkHolder(ItemStack stack)
    {
        parentItemStack = stack;
        inventory = new ItemStack[inventorySize];

        readFromNBT(stack.getTagCompound());
    }

    public void onGuiSaved(EntityPlayer player)
    {
        parentItemStack = player.getHeldItem();

        if (parentItemStack != null && parentItemStack.getItem() instanceof ItemLinkHolder) {
            save();
        }
    }

    public void save()
    {
        NBTTagCompound nbtTagCompound = parentItemStack.getTagCompound();

        if (nbtTagCompound == null) {
            nbtTagCompound = new NBTTagCompound();
        }

        writeToNBT(nbtTagCompound);
        parentItemStack.setTagCompound(nbtTagCompound);
    }

    @Override
    public int getSizeInventory()
    {
        return this.inventorySize;
    }

    @Override
    public ItemStack getStackInSlot(int slot)
    {
        return inventory[slot];
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount)
    {
        ItemStack stack = getStackInSlot(slot);

        if (stack != null) {
            if (stack.stackSize <= amount) {
                setInventorySlotContents(slot, null);
            } else {
                stack = stack.splitStack(amount);
                if (stack.stackSize == 0) {
                    setInventorySlotContents(slot, null);
                }
            }
        }
        return stack;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot)
    {
        if (inventory[slot] != null) {
            ItemStack stack = getStackInSlot(slot);
            setInventorySlotContents(slot, null);
            return stack;
        } else {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack)
    {
        inventory[slot] = stack;
    }

    @Override
    public String getInventoryName()
    {
        return Names.Items.LINK_HOLDER;
    }

    @Override
    public boolean hasCustomInventoryName()
    {
        return false;
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 1;
    }

    @Override
    public void markDirty()
    {

    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player)
    {
        if (player.getHeldItem() == null)
            return false;
        else
            return player.getHeldItem().getItem() == ModItems.linkHolder;
    }

    @Override
    public void openInventory()
    {

    }

    @Override
    public void closeInventory()
    {

    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack)
    {
        return stack.getItem() == com.xcompwiz.mystcraft.data.ModItems.linkbook;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound)
    {
        if (nbtTagCompound != null && nbtTagCompound.hasKey(Names.NBT.ITEMS)) {
            NBTTagList tagList = nbtTagCompound.getTagList(Names.NBT.ITEMS, 10);
            inventory = new ItemStack[this.getSizeInventory()];
            for (int i = 0; i < tagList.tagCount(); ++i) {
                NBTTagCompound tagCompound = tagList.getCompoundTagAt(i);
                byte slotIndex = tagCompound.getByte("Slot");
                if (slotIndex >= 0 && slotIndex < inventory.length) {
                    inventory[slotIndex] = ItemStack.loadItemStackFromNBT(tagCompound);
                }
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound)
    {
        NBTTagList tagList = new NBTTagList();
        for (int currentIndex = 0; currentIndex < inventory.length; ++currentIndex) {
            if (inventory[currentIndex] != null) {
                NBTTagCompound tagCompound = new NBTTagCompound();
                tagCompound.setByte("Slot", (byte) currentIndex);
                inventory[currentIndex].writeToNBT(tagCompound);
                tagList.appendTag(tagCompound);
            }
        }
        nbtTagCompound.setTag(Names.NBT.ITEMS, tagList);
    }

    @Override
    public String getTagLabel()
    {
        return "InventoryLinkHolder";
    }
}
