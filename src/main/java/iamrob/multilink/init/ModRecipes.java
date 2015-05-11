package iamrob.multilink.init;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class ModRecipes
{
    public static void init()
    {
        GameRegistry.addShapedRecipe(new ItemStack(ModItems.linkHolder), " f ", "s  ", " f ", 'f', new ItemStack(com.xcompwiz.mystcraft.data.ModItems.folder), 's', new ItemStack(Items.string));
        GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.bookUnbinder), " b ", "sss", "ppp", 'b', new ItemStack(Items.iron_sword), 's', new ItemStack(Blocks.stone), 'p', new ItemStack(Blocks.planks));
    }
}
