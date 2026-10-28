package vbonedra.achievement_lib.util;

import net.minecraft.ResourceLocation;

import static vbonedra.achievement_lib.AchievementLibMod.MOD_ID;

/** Locations of the textures used by the achievement gui. */
public final class GuiTextures {

    // texture constants
    public static final ResourceLocation ACHIEVEMENT = new ResourceLocation("textures/gui/achievement/achievement_background.png"); // vanilla texture, holds the icon frames
    public static final ResourceLocation BORDER_UP = new ResourceLocation(MOD_ID, "textures/gui/achievement/frame/border_up.png");
    public static final ResourceLocation BORDER_DOWN = new ResourceLocation(MOD_ID, "textures/gui/achievement/frame/border_down.png");
    public static final ResourceLocation BORDER_LEFT = new ResourceLocation(MOD_ID, "textures/gui/achievement/frame/border_left.png");
    public static final ResourceLocation BORDER_RIGHT = new ResourceLocation(MOD_ID, "textures/gui/achievement/frame/border_right.png");
    public static final ResourceLocation CORNER_UP_LEFT = new ResourceLocation(MOD_ID, "textures/gui/achievement/frame/corner_up_left.png");
    public static final ResourceLocation CORNER_UP_RIGHT = new ResourceLocation(MOD_ID, "textures/gui/achievement/frame/corner_up_right.png");
    public static final ResourceLocation CORNER_DOWN_LEFT = new ResourceLocation(MOD_ID, "textures/gui/achievement/frame/corner_down_left.png");
    public static final ResourceLocation CORNER_DOWN_RIGHT = new ResourceLocation(MOD_ID, "textures/gui/achievement/frame/corner_down_right.png");
    public static final ResourceLocation TAB_SELECTED = new ResourceLocation(MOD_ID, "textures/gui/achievement/tab/tab_selected.png");
    public static final ResourceLocation TAB_UNSELECTED = new ResourceLocation(MOD_ID, "textures/gui/achievement/tab/tab_unselected.png");
    public static final ResourceLocation TAB_ARROW_LEFT = new ResourceLocation(MOD_ID, "textures/gui/achievement/tab/tab_arrow_left.png");
    public static final ResourceLocation TAB_ARROW_RIGHT = new ResourceLocation(MOD_ID, "textures/gui/achievement/tab/tab_arrow_right.png");

}
