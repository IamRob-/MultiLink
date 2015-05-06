package iamrob.multilink.reference;

import net.minecraft.util.ResourceLocation;

public class Textures
{
    public static final String textureLoc = ModInfo.ID;
    public static final String RESOURCE_PREFIX = ModInfo.ID + ":";

    public static class BookUnbinder
    {
        public static BookUnbinder instance = new Textures.BookUnbinder();

        public static final ResourceLocation modelTexture = new ResourceLocation(textureLoc, "textures/models/bookUnbinder.png");
        public static final ResourceLocation guiTexture = new ResourceLocation(textureLoc, "textures/gui/bookUnbinder.png");

        public static final int playerInvX = 8;
        public static final int playerInvY = 99;
        public static final int playerInvHotbarY = 157;
        public static final int playerInvPixels = 18;

        public static final int guiSizeX = 176;
        public static final int guiSizeY = 181;

        public static final int linkSlotX = 80;
        public static final int linkSlotY = 13;
        public static final int outSlotLeftX = 62;
        public static final int outSlotRightX = 98;
        public static final int outSlotY = 61;

        public static final int progressSourceX = 176;
        public static final int progressPlaceX = 79;
        public static final int progressPlaceY = 33;
        public static final int progressWidth = 18;
        public static final int progressHeight = 24;
    }

    public static class LinkHolder
    {

        public static LinkHolder instance = new Textures.LinkHolder();

        public static final ResourceLocation coverTexture = new ResourceLocation(textureLoc, "textures/gui/linkholder_cover.png");
        public static final ResourceLocation coverLTexture = new ResourceLocation(textureLoc, "textures/gui/linkholder_coverL.png");
        public static final ResourceLocation uiTexture = new ResourceLocation(textureLoc, "textures/gui/linkholder_ui.png");
        public static final ResourceLocation uiLeftTexture = new ResourceLocation(textureLoc, "textures/gui/linkholder_uiL.png");
        public static final ResourceLocation invTexture = new ResourceLocation(textureLoc, "textures/gui/linkholder_inv.png");

        public static final int playerInvX = 29;
        public static final int playerInvY = 172;
        public static final int playerInvHotbarY = 230;
        public static final int playerInvPixels = 18;

        public static final int holderInvX = 50;
        public static final int holderInvY = 27;
        public static final int holderPixelsX = 103;
        public static final int holderPixelsY = 39;

        public static final int guiSizeX = 129;
        public static final int guiSizeY = 180;
        public static final int guiContainerSizeX = 219;
        public static final int guiContainerSizeY = 254;

        public static final int guiPanelStartX = 148;
        public static final int guiPanelStartY = 22;
        public static final int guiPanelWidth = 82;
        public static final int guiPanelHeight = 40;
        public static final int guiPanelSourceX = 130;
        public static final int guiPanelSourceY = 0;
        public static final int guiPanelInnerSourceY = 40;

    }
}
