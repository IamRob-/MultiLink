package iamrob.multilink.inventory.slots;

import cpw.mods.fml.common.FMLCommonHandler;
import iamrob.multilink.inventory.ContainerMultiLink;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SlotFiltered extends Slot
{

    private final EntityPlayer entityPlayer;
    private final ContainerMultiLink container;
    private final Item filtered;

    public SlotFiltered(Item filtered, ContainerMultiLink container, IInventory inv, EntityPlayer player, int id, int x, int y)
    {
        super(inv, id, x, y);
        this.entityPlayer = player;
        this.container = container;
        this.filtered = filtered;
    }

    @Override
    public boolean isItemValid(ItemStack stack)
    {
        return stack.getItem() == filtered;
    }

    @Override
    public void onSlotChanged()
    {
        super.onSlotChanged();

        if (FMLCommonHandler.instance().getEffectiveSide().isServer()) {
            container.saveInventory(entityPlayer);
        }
    }
}
