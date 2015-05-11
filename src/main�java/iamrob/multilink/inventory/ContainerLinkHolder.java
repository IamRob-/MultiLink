package iamrob.multilink.inventory;

import com.xcompwiz.mystcraft.api.linking.ILinkInfo;
import com.xcompwiz.mystcraft.item.ItemLinking;
import com.xcompwiz.mystcraft.linking.LinkListenerManager;
import cpw.mods.fml.common.FMLCommonHandler;
import iamrob.multilink.item.ItemLinkHolder;
import iamrob.multilink.network.PacketHandler;
import iamrob.multilink.network.message.MessageCanLink;
import iamrob.multilink.network.message.MessageSkyColour;
import iamrob.multilink.reference.Textures;
import iamrob.multilink.util.LogHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

import java.util.ArrayList;
import java.util.List;

public class ContainerLinkHolder extends Container
{
    private final Textures.LinkHolder texture = Textures.LinkHolder.instance;

    private final EntityPlayer player;
    public final InventoryLinkHolder inventory;

    public List<Byte> linksPermitted;
    public Vec3[] skyColours;

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
        checkSkyColours();
    }

    private void checkLinksPermitted()
    {
        if (!player.worldObj.isRemote && linksPermitted == null) {
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

    private void checkSkyColours()
    {
        if (!player.worldObj.isRemote && skyColours == null) {
            skyColours = new Vec3[inventory.getSizeInventory()];
            ItemStack[] items = inventory.getItems();
            for (int i = 0; i < inventory.getSizeInventory(); i++) {
                ItemStack stack = items[i];
                if (stack != null) {
                    ILinkInfo info = ((ItemLinking) stack.getItem()).getLinkInfo(stack);
                    int dimId = info.getDimensionUID();
                    Vec3 col;

                    switch (dimId) {
                        case -1:
                            col = Vec3.createVectorHelper(0.20000000298023224, 0.029999999329447746, 0.029999999329447746);
                            break;
                        case 0:
                            col = Vec3.createVectorHelper(0.45490196347236633, 0.6705882549285889, 1.0);
                            break;
                        case 1:
                            col = Vec3.createVectorHelper(0.5, 0.4000000059604645, 0.699999988079071);
                            break;
                        default:
                            col = null;
                    }

                    if (col == null) {
                        WorldServer world = DimensionManager.getWorld(dimId);
                        boolean unload = false;
                        if (world == null) {
                            DimensionManager.initDimension(dimId);
                            world = DimensionManager.getWorld(dimId);
                            unload = true;
                        }
                        col = world.getSkyColor(player, 0F);

                        if (col.xCoord == 0.0 && col.yCoord == 0.0 && col.zCoord == 0.0) {
                            col = DimensionManager.getProvider(dimId).getFogColor(0F, 0F);
                        }
                        if (unload) {
                            DimensionManager.unloadWorld(dimId);
                        }
                    }
                    skyColours[i] = col;
                    LogHelper.info(dimId + ": " + col);
                }
            }
            PacketHandler.INSTANCE.sendTo(new MessageSkyColour(skyColours), (EntityPlayerMP) player);
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

    public Vec3 getSkyColour(int slot)
    {
        if (skyColours == null)
            return null;
        else
            return skyColours[slot];
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
