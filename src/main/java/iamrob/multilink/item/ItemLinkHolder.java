package iamrob.multilink.item;

import com.xcompwiz.mystcraft.api.linking.ILinkInfo;
import com.xcompwiz.mystcraft.linking.LinkController;
import com.xcompwiz.mystcraft.linking.LinkListenerManager;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import iamrob.multilink.MultiLink;
import iamrob.multilink.inventory.InventoryLinkHolder;
import iamrob.multilink.reference.Names;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ItemLinkHolder extends ItemMultiLink
{
    public ItemLinkHolder()
    {
        super();
        setUnlocalizedName(Names.Items.LINK_HOLDER);

        setCreativeTab(CreativeTabs.tabTransport);

        setMaxStackSize(1);
        setMaxDamage(16);
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
        int size = getLinkPageCount(stack);
        switch (size) {
            case 0:
                return emptyIcon;
            case 6:
                return fullIcon;
            default:
                return halfIcon;
        }
    }

    public int getLinkPageCount(ItemStack holder)
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
    public int getMaxItemUseDuration(ItemStack stack)
    {
        return 1;
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

        if (inventory[id] == null || !(inventory[id].getItem() instanceof ItemLinkPage)) {
            return;
        }
        ItemStack linkStack = inventory[id];
        ItemLinkPage item = (ItemLinkPage) linkStack.getItem();

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
                    dropItem(stack, world, player.posX, player.posY, player.posZ);
                }
                player.setCurrentItemOrArmor(0, null);
                return;
            }
            LinkController.travelEntity(world, player, info);
            bookStack.setItemDamage(bookStack.getItemDamage() + 1);
        }
    }

    private void dropItem(ItemStack stack, World world, double x, double y, double z)
    {
        if (stack != null && stack.stackSize > 0) {
            Random rand = new Random();

            float dX = rand.nextFloat() * 0.8F + 0.1F;
            float dY = rand.nextFloat() * 0.8F + 0.1F;
            float dZ = rand.nextFloat() * 0.8F + 0.1F;

            EntityItem entityItem = new EntityItem(world, x + dX, y + dY, z + dZ, stack.copy());

            if (stack.hasTagCompound()) {
                entityItem.getEntityItem().setTagCompound((NBTTagCompound) stack.getTagCompound().copy());
            }

            float factor = 0.05F;
            entityItem.motionX = rand.nextGaussian() * factor;
            entityItem.motionY = rand.nextGaussian() * factor + 0.2F;
            entityItem.motionZ = rand.nextGaussian() * factor;
            world.spawnEntityInWorld(entityItem);
            stack.stackSize = 0;
        }
    }
}
