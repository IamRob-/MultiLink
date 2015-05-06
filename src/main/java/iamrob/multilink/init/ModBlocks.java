package iamrob.multilink.init;

import cpw.mods.fml.common.registry.GameRegistry;
import iamrob.multilink.block.BlockBookUnbinder;
import iamrob.multilink.block.BlockMultiLink;
import iamrob.multilink.reference.ModInfo;
import iamrob.multilink.reference.Names;

@GameRegistry.ObjectHolder(ModInfo.ID)
public class ModBlocks
{
    public static final BlockMultiLink bookUnbinder = new BlockBookUnbinder();

    public static void init()
    {
        GameRegistry.registerBlock(bookUnbinder, Names.Blocks.BOOK_UNBINDER);
    }
}
