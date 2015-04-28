package iamrob.multilink.inventory;

import iamrob.multilink.init.ModItems;
import iamrob.multilink.reference.Names;
import iamrob.multilink.reference.Textures;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerLinkHolder extends Container implements IInventory
{

    Textures.LinkHolder texture = Textures.LinkHolder.instance;

    public static int inventorySize = 6;
    private ItemStack[] items;

    public ContainerLinkHolder(InventoryPlayer player, boolean slots)
    {

        if (slots) {
            for (int x = 0; x < 9; x++) {
                addSlotToContainer(new Slot(player, x, texture.playerInvX + texture.playerInvPixels * x, texture.playerInvHotbarY));
            }
            for (int y = 0; y < 3; y++) {
                for (int x = 0; x < 9; x++) {
                    addSlotToContainer(new Slot(player, x + y * 9 + 9, texture.playerInvX + texture.playerInvPixels * x, texture.playerInvY + y * texture.playerInvPixels));
                }
            }
            for (int x = 0; x < 2; x++) {
                for (int y = 0; y < 3; y++) {
                    addSlotToContainer(new SlotLinkBook(this, y + x * 3, texture.holderInvX + texture.holderPixelsX * x, texture.holderInvY + texture.holderPixelsY * y));
                }
            }
        }
    }

    @Override
    public int getSizeInventory()
    {
        return this.inventorySize;
    }

    @Override
    public ItemStack getStackInSlot(int slot)
    {
        return items[slot];
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
                markDirty();
            }
        }
        return stack;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot)
    {
        ItemStack stack = getStackInSlot(slot);
        setInventorySlotContents(slot, null);
        return stack;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack)
    {
        items[slot] = stack;
    }

    @Override
    public String getInventoryName()
    {
        return Names.Items.LINK_HOLDER;
    }

    @Override
    public boolean hasCustomInventoryName()
    {
        return true;
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
    public boolean canInteractWith(EntityPlayer player)
    {
        return isUseableByPlayer(player);
    }

    public class SlotLinkBook extends Slot
    {

        public SlotLinkBook(IInventory inv, int id, int x, int y)
        {
            super(inv, id, x, y);
        }

        @Override
        public boolean isItemValid(ItemStack stack)
        {
            return stack.getItem() == com.xcompwiz.mystcraft.data.ModItems.linkbook;
        }
    }
}
