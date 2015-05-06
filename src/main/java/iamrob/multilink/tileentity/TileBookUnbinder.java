package iamrob.multilink.tileentity;

import com.xcompwiz.mystcraft.data.ModItems;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import iamrob.multilink.reference.Names;
import iamrob.multilink.util.LinkPageHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.MathHelper;

public class TileBookUnbinder extends TileMultiLink implements ISidedInventory
{

    public static final int INVENTORY_SIZE = 3;
    public static final int INPUT_INDEX = 0;
    public static final int OUTPUT_PAGE_INDEX = 1;
    public static final int OUTPUT_COVER_INDEX = 2;

    public int deviceCookTime;              // How much longer the Calcinator will cook
    public int itemCookTime;                // How long the current item has been "cooking"
    public final int itemCookLength;              // Total time it takes an item to cook

    private ItemStack[] inventory;

    public TileBookUnbinder()
    {
        inventory = new ItemStack[INVENTORY_SIZE];
        this.itemCookLength = 50;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int p_94128_1_)
    {
        return new int[]{INPUT_INDEX, OUTPUT_COVER_INDEX, OUTPUT_PAGE_INDEX};
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack stack, int side)
    {
        return slot == INPUT_INDEX;
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack stack, int side)
    {
        return slot != INPUT_INDEX;
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound)
    {
        super.writeToNBT(nbtTagCompound);

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
        nbtTagCompound.setInteger("deviceCookTime", deviceCookTime);
        nbtTagCompound.setInteger("itemCookTime", itemCookTime);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound)
    {
        super.readFromNBT(nbtTagCompound);

        if (nbtTagCompound.hasKey(Names.NBT.ITEMS)) {
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

        deviceCookTime = nbtTagCompound.getInteger("deviceCookTime");
        itemCookTime = nbtTagCompound.getInteger("itemCookTime");
    }

    @Override
    public int getSizeInventory()
    {
        return inventory.length;
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
        ItemStack itemStack = getStackInSlot(slot);
        if (itemStack != null) {
            setInventorySlotContents(slot, null);
        }
        return itemStack;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack)
    {
        inventory[slot] = stack;
        updateState();
    }

    public void updateState()
    {
        if (this.state == 0) {
            if (getStackInSlot(INPUT_INDEX) != null) {
                this.state = 1;
                this.worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, this.getBlockType(), 1, this.state);
                this.worldObj.notifyBlockChange(this.xCoord, this.yCoord, this.zCoord, this.getBlockType());
            }
        } else if (this.state == 1) {
            if (getStackInSlot(INPUT_INDEX) == null) {
                this.state = 0;
                this.worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, this.getBlockType(), 1, this.state);
                this.worldObj.notifyBlockChange(this.xCoord, this.yCoord, this.zCoord, this.getBlockType());
            }
        }
    }

    @Override
    public String getInventoryName()
    {
        return Names.Blocks.BOOK_UNBINDER;
    }

    @Override
    public boolean hasCustomInventoryName()
    {
        return false;
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player)
    {
        return player.getDistanceSq(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) <= 64;
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
        if (slot == INPUT_INDEX)
            return stack.getItem() == ModItems.linkbook;
        else
            return false;
    }

    @SideOnly(Side.CLIENT)
    public int getCookProgressScaled(int scale)
    {
        return this.itemCookTime * scale / itemCookLength;
    }

    public float r = 0.175F;
    float i = 0F;
    int clientCookTime = 0;

    @Override
    public void updateEntity()
    {
        boolean isBurning = this.deviceCookTime > 0;
        boolean sendUpdate = false;

        if (this.deviceCookTime > 0) {
            this.deviceCookTime--;
        }

        if (!this.worldObj.isRemote) {
            if (this.deviceCookTime == 0 && this.canUnbind()) {
                this.deviceCookTime = this.itemCookLength;
                sendUpdate = true;
            }

            if (this.deviceCookTime > 0 && this.canUnbind()) {
                this.itemCookTime++;

                if (this.itemCookTime == itemCookLength) {
                    this.itemCookTime = 0;
                    this.unbindItem();
                    sendUpdate = true;
                }
            } else {
                this.itemCookTime = 0;
            }

            if (isBurning != this.deviceCookTime > 0) {
                sendUpdate = true;
            }
        }

        if (sendUpdate) {
            this.markDirty();
            this.state = this.deviceCookTime > 0 ? (byte) 2 : (byte) 0;
            this.worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, this.getBlockType(), 1, this.state);
            this.worldObj.notifyBlockChange(this.xCoord, this.yCoord, this.zCoord, this.getBlockType());
        }

        if (worldObj.isRemote) {
            if (this.state == 2) {
                getNewClientCookTime();
                i = ((float) clientCookTime / (itemCookLength * 2F + 20)) * ((float) Math.PI * 2);
                r = MathHelper.sin(i - 0.303F) / 4F + 0.25F;
            } else {
                if (r < 0.175F) {
                    getNewClientCookTime();
                    i = ((float) clientCookTime / (itemCookLength * 2F + 20)) * ((float) Math.PI * 2);
                    r = MathHelper.sin(i - 0.303F) / 4F + 0.25F;
                } else {
                    r = 0.175F;
                    clientCookTime = 0;
                }
            }
        }
    }

    public void getNewClientCookTime()
    {
        if (i > (1.874F + 0.5F) && i < (5.015F - 0.5F))
            clientCookTime += 4;
        else if ((i > (1.874F - 0.5F) && i < (1.874F + 0.5F))
                || (i > (5.015F - 0.5F) && i < (5.015F + 0.5F)))
            clientCookTime += 2;
        else
            clientCookTime++;
    }

    public boolean canUnbind()
    {
        if (getStackInSlot(INPUT_INDEX) == null) {
            return false;
        } else {
            if (getStackInSlot(OUTPUT_PAGE_INDEX) == null && (getStackInSlot(OUTPUT_COVER_INDEX) == null || getStackInSlot(OUTPUT_COVER_INDEX).stackSize < getStackInSlot(OUTPUT_COVER_INDEX).getMaxStackSize())) {
                return true;
            }
            return false;
        }
    }

    public void unbindItem()
    {
        if (this.canUnbind()) {
            ItemStack linkPage = LinkPageHelper.getLinkPageFromBook(this.getStackInSlot(INPUT_INDEX));
            outputPage(linkPage);
            outputLeather(new ItemStack(Items.leather, 1));

            setInventorySlotContents(INPUT_INDEX, null);
        }
    }

    public void outputPage(ItemStack page)
    {
        if (getStackInSlot(OUTPUT_PAGE_INDEX) == null)
            setInventorySlotContents(OUTPUT_PAGE_INDEX, page);
    }

    public void outputLeather(ItemStack stack)
    {
        int maxStackSize = Math.min(getInventoryStackLimit(), stack.getMaxStackSize());

        if (getStackInSlot(OUTPUT_COVER_INDEX) == null) {
            setInventorySlotContents(OUTPUT_COVER_INDEX, stack);
            return;
        }

        if (getStackInSlot(OUTPUT_COVER_INDEX).isItemEqual(stack) && getStackInSlot(OUTPUT_COVER_INDEX).stackSize < maxStackSize) {
            getStackInSlot(OUTPUT_COVER_INDEX).stackSize += stack.stackSize;
            return;
        }

    }

    @Override
    public boolean receiveClientEvent(int id, int data)
    {
        if (id == 1) {
            this.state = (byte) data;
            this.worldObj.func_147451_t(this.xCoord, this.yCoord, this.zCoord);
            return true;
        }
        return super.receiveClientEvent(id, data);
    }
}
