package iamrob.multilink.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import iamrob.multilink.reference.Names;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemLinkHolder extends ItemMultiLink {

    public ItemLinkHolder() {
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
    public void registerIcons(IIconRegister register) {
        emptyIcon = register.registerIcon(getIconName() + "_empty");
        halfIcon = register.registerIcon(getIconName() + "_half");
        fullIcon = register.registerIcon(getIconName() + "_full");

        itemIcon = fullIcon;
    }

}
