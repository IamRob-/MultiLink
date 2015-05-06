package iamrob.multilink.inventory;

import com.xcompwiz.mystcraft.data.ModItems;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import iamrob.multilink.inventory.slots.SlotFiltered;
import iamrob.multilink.reference.Textures;
import iamrob.multilink.tileentity.TileBookUnbinder;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerBookUnbinder extends ContainerMultiLink
{
    private final Textures.BookUnbinder texture = Textures.BookUnbinder.instance;

    public final TileBookUnbinder tile;

    private int lastCookTime;
    private int lastItemCookTime;

    public ContainerBookUnbinder(EntityPlayer player, TileBookUnbinder tile)
    {
        super(player, tile);

        this.tile = tile;

        //player slots
        for (int x = 0; x < 9; x++) {
            addSlotToContainer(new Slot(player.inventory, x, texture.playerInvX + texture.playerInvPixels * x, texture.playerInvHotbarY));
        }
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                addSlotToContainer(new Slot(player.inventory, x + y * 9 + 9, texture.playerInvX + texture.playerInvPixels * x, texture.playerInvY + y * texture.playerInvPixels));
            }
        }

        //Book unbinder slots
        addSlotToContainer(new SlotFiltered(ModItems.linkbook, this, tile, player, tile.INPUT_INDEX, texture.linkSlotX, texture.linkSlotY));
        addSlotToContainer(new SlotBookUnbinder(tile, tile.OUTPUT_PAGE_INDEX, texture.outSlotLeftX, texture.outSlotY));
        addSlotToContainer(new SlotBookUnbinder(tile, tile.OUTPUT_COVER_INDEX, texture.outSlotRightX, texture.outSlotY));

    }

    @Override
    public void saveInventory(EntityPlayer player)
    {
        tile.updateState();
    }

    @Override
    public void addCraftingToCrafters(ICrafting iCrafting)
    {
        super.addCraftingToCrafters(iCrafting);
        iCrafting.sendProgressBarUpdate(this, 0, this.tile.deviceCookTime);
        iCrafting.sendProgressBarUpdate(this, 1, this.tile.itemCookTime);

    }

    @Override
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        for (Object crafter : this.crafters) {
            ICrafting icrafting = (ICrafting) crafter;

            if (this.lastCookTime != this.tile.deviceCookTime) {
                icrafting.sendProgressBarUpdate(this, 0, this.tile.deviceCookTime);
            }

            if (this.lastItemCookTime != this.tile.itemCookTime) {
                icrafting.sendProgressBarUpdate(this, 1, this.tile.itemCookTime);
            }
        }

        this.lastCookTime = this.tile.deviceCookTime;
        this.lastItemCookTime = this.tile.itemCookTime;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void updateProgressBar(int id, int val)
    {
        if (id == 0) {
            this.tile.deviceCookTime = val;
        }
        if (id == 1) {
            this.tile.itemCookTime = val;
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer p_75145_1_)
    {
        return true;
    }

    private class SlotBookUnbinder extends Slot
    {
        public SlotBookUnbinder(IInventory inventory, int slotIndex, int x, int y)
        {
            super(inventory, slotIndex, x, y);
        }

        @Override
        public void onPickupFromSlot(EntityPlayer entityPlayer, ItemStack itemStack)
        {
            super.onPickupFromSlot(entityPlayer, itemStack);
            FMLCommonHandler.instance().firePlayerCraftingEvent(entityPlayer, itemStack, inventory);
        }

        @Override
        public boolean isItemValid(ItemStack itemStack)
        {
            return false;
        }
    }
}
