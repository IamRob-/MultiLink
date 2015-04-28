package iamrob.multilink.inventory;

import com.xcompwiz.mystcraft.api.linking.ILinkInfo;
import com.xcompwiz.mystcraft.item.ItemLinking;
import com.xcompwiz.mystcraft.linking.LinkListenerManager;
import cpw.mods.fml.common.FMLCommonHandler;
import iamrob.multilink.item.ItemLinkHolder;
import iamrob.multilink.network.PacketHandler;
import iamrob.multilink.network.message.MessageCanLink;
import iamrob.multilink.reference.Textures;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ContainerLinkHolder extends Container
{
    private final Textures.LinkHolder texture = Textures.LinkHolder.instance;

    private final EntityPlayer player;
    public final InventoryLinkHolder inventory;

    public List<Byte> linksPermitted;

    public ContainerLinkHolder(EntityPlayer player, InventoryLinkHolder linkHolder, boolean slots)
    {
        this.player = player;
        this.inventory = linkHolder;

        if (slots) {
            //player slots
            for (int x = 0; x < 9; x++) {
                addSlotToContainer(new Slot(player.inventory, x, texture.playerInvX + texture.playerInvPixels * x, texture.playerInvHotbarY));
            }
            for (int y = 0; y < 3; y++) {
                for (int x = 0; x < 9; x++) {
                    addSlotToContainer(new Slot(player.inventory, x + y * 9 + 9, texture.playerInvX + texture.playerInvPixels * x, texture.playerInvY + y * texture.playerInvPixels));
                }
            }
            //LinkHolder slots
            for (int x = 0; x < 2; x++) {
                for (int y = 0; y < 3; y++) {
                    addSlotToContainer(new SlotLinkBook(this, linkHolder, player, y + x * 3, texture.holderInvX + texture.holderPixelsX * x, texture.holderInvY + texture.holderPixelsY * y));
                }
            }
        }
    }

    @Override
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();
        checkLinksPermitted();
    }

    private void checkLinksPermitted()
    {
        if (!player.worldObj.isRemote) {
            if (linksPermitted == null) {
                linksPermitted = new ArrayList<Byte>();
                ItemStack[] items = inventory.getItems();
                for (byte i = 0; i < inventory.getSizeInventory(); i++) {
                    ItemStack stack = items[i];
                    if (stack != null) {
                        ILinkInfo info = ((ItemLinking) stack.getItem()).getLinkInfo(stack);
                        if (LinkListenerManager.isLinkPermitted(player.worldObj, player, info)) {

                            linksPermitted.add(i);
                        }
                    }
                }
                PacketHandler.INSTANCE.sendTo(new MessageCanLink(linksPermitted), (EntityPlayerMP) player);
            }
        }
    }

    public boolean canLink(int slot)
    {
        if (linksPermitted == null) {
            return false;
        } else {
            return linksPermitted.contains((byte) slot);
        }
    }

    @Override
    public void onContainerClosed(EntityPlayer entityPlayer)
    {
        super.onContainerClosed(entityPlayer);

        if (!entityPlayer.worldObj.isRemote) {
            saveInventory(entityPlayer);
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer entityPlayer, int slotIndex)
    {
        ItemStack newItemStack = null;
        Slot slot = (Slot) inventorySlots.get(slotIndex);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemStack = slot.getStack();
            newItemStack = itemStack.copy();

            if (slotIndex >= 36) {
                if (!this.mergeItemStack(itemStack, 0, 36, false)) {
                    return null;
                }
            } else if (itemStack.getItem() != com.xcompwiz.mystcraft.data.ModItems.linkbook
                    || itemStack.getItem() instanceof ItemLinkHolder
                    || !mergeItemStack(itemStack, 36, 36 + inventory.getSizeInventory(), false)) {
                return null;
            }

            if (itemStack.stackSize == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }
        }

        return newItemStack;
    }

    public void saveInventory(EntityPlayer player)
    {
        inventory.onGuiSaved(player);
    }

    @Override
    public boolean canInteractWith(EntityPlayer player)
    {
        return inventory.isUseableByPlayer(player);
    }

    public InventoryLinkHolder getInventoryLinkHolder()
    {
        return inventory;
    }

    public class SlotLinkBook extends Slot
    {

        private final EntityPlayer entityPlayer;
        private ContainerLinkHolder containerLinkHolder;

        public SlotLinkBook(ContainerLinkHolder container, IInventory inv, EntityPlayer player, int id, int x, int y)
        {
            super(inv, id, x, y);
            this.entityPlayer = player;
            this.containerLinkHolder = container;
        }

        @Override
        public boolean isItemValid(ItemStack stack)
        {
            return stack.getItem() == com.xcompwiz.mystcraft.data.ModItems.linkbook;
        }

        @Override
        public void onSlotChanged()
        {
            super.onSlotChanged();

            if (FMLCommonHandler.instance().getEffectiveSide().isServer()) {
                containerLinkHolder.saveInventory(entityPlayer);
            }
        }
    }
}
