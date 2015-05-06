package iamrob.multilink.item;

import com.xcompwiz.mystcraft.api.linking.ILinkInfo;
import com.xcompwiz.mystcraft.linking.LinkOptions;
import com.xcompwiz.mystcraft.world.WorldProviderMyst;
import com.xcompwiz.mystcraft.world.agedata.AgeData;
import iamrob.multilink.reference.Names;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.WorldProvider;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class ItemLinkPage extends ItemMultiLink
{
    public ItemLinkPage()
    {
        super();
        this.setUnlocalizedName(Names.Items.LINK_PAGE);
        setMaxStackSize(1);
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book)
    {
        return false;
    }

    public String getTitle(ItemStack itemstack)
    {
        if (itemstack.stackTagCompound != null) return LinkOptions.getDisplayName(itemstack.stackTagCompound);
        return "";
    }

    public ILinkInfo getLinkInfo(ItemStack itemstack)
    {
        if (itemstack.stackTagCompound != null) {
            ILinkInfo info = new LinkOptions(itemstack.stackTagCompound);
            return info;
        }
        return null;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer pl, List list, boolean b)
    {
        ILinkInfo info = getLinkInfo(stack);
        if (info == null)
            return;

        String title = info.getDisplayName();
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
            int dimID = info.getDimensionUID();
            WorldProvider world = WorldProvider.getProviderForDimension(dimID);
            String dim;
            if (world instanceof WorldProviderMyst) {
                dim = AgeData.getAge(dimID, true).getAgeName();
            } else {
                dim = world.getDimensionName();
            }
            if (title.equals(dim)) {
                list.add(dim);
            } else {
                list.add(title);
                list.add(EnumChatFormatting.GRAY + dim);
            }
            ChunkCoordinates coords = info.getSpawn();
            list.add(EnumChatFormatting.GRAY + "X: " + coords.posX + " Y: " + coords.posY + " Z: " + coords.posZ);
        } else {
            list.add(title);
//            list.add(EnumChatFormatting.GRAY + "<Shift for link info>");
        }

    }
}
