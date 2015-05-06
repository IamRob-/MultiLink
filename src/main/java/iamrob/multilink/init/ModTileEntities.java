package iamrob.multilink.init;

import cpw.mods.fml.common.registry.GameRegistry;
import iamrob.multilink.reference.Names;
import iamrob.multilink.tileentity.TileBookUnbinder;

public class ModTileEntities
{

    public static void init()
    {
        GameRegistry.registerTileEntity(TileBookUnbinder.class, Names.Blocks.BOOK_UNBINDER);
    }

}
