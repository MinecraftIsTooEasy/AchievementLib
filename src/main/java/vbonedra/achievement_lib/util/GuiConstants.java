package vbonedra.achievement_lib.util;

/** Sizes, colors and other fixed values of the achievement gui. */
public final class GuiConstants {

    // view modes
    public static final int VIEW_MODE_PLAYER = 0;
    public static final int VIEW_MODE_THIS_WORLD = 1;
    public static final int VIEW_MODE_ALL_WORLDS = 2;

    // layout constants
    public static final int GRID_SPACING = 24;          // pixels between achievement grid cells
    public static final int ICON_SIZE = 22;             // hover size of an achievement icon
    public static final int ICON_CENTER_OFFSET = 11;    // line endpoints attach to the icon centre
    public static final int FRAME_SIZE = 26;            // size of the icon frame texture
    public static final int FRAME_OVERHANG = 2;         // frame extends this far outside the icon
    public static final int ITEM_INSET = 3;             // item is drawn this far inside the frame
    public static final int TILE_SIZE = 16;             // size of one background tile
    public static final int TILE_WORLD_OFFSET = 288;    // keeps background world cords positive

    // background brightness, tiles get darker with depth
    public static final float BACKGROUND_BRIGHTNESS_TOP = 0.6F;
    public static final float BACKGROUND_BRIGHTNESS_BOTTOM = 0.0F;

    // dynamic pane constants
    public static final int PANE_MARGIN = 20;       // minimum gap between pane and screen edge
    public static final int MIN_PANE_WIDTH = 256;
    public static final int MIN_PANE_HEIGHT = 202;
    public static final int BORDER_TOP = 23;        // height of top border and top corners
    public static final int BORDER_BOTTOM = 38;     // height of bottom border and bottom corners
    public static final int BORDER_LEFT = 22;       // width of left border and left corners
    public static final int BORDER_RIGHT = 22;      // width of right border and right corners
    public static final int TITLE_OFFSET_X = 4;     // title gap from the left border
    public static final int BUTTON_WIDTH = 80;
    public static final int BUTTON_HEIGHT = 20;
    public static final int BUTTON_GAP = 24;        // space between buttons
    public static final int BUTTON_GAP_BORDER = 48; // space between buttons and pane borders

    // tab constants
    public static final int TAB_WIDTH = 28;
    public static final int TAB_HEIGHT = 28;    // height of an unselected tab, the tab sits on top of the pane
    public static final int TAB_RISE = 4;       // the selected tab is this much taller than the others
    public static final int TAB_OVERLAP = 4;    // the selected tab reaches this far into the pane's top border
    public static final int TAB_SELECTED_TEXTURE_HEIGHT = TAB_HEIGHT + TAB_RISE + TAB_OVERLAP;
    public static final int TAB_AREA_HEIGHT = TAB_HEIGHT + TAB_RISE;    // space above the pane reserved for tabs
    public static final int TAB_GAP = 2;            // space between tabs
    public static final int TAB_STRIP_MARGIN = 8;   // space between the pane's side and the first tab or arrow
    public static final int TAB_ICON_SIZE = 16;
    public static final int ARROW_WIDTH = 12;       // tab scroll arrow buttons
    public static final int ARROW_HEIGHT = TAB_HEIGHT;
    public static final int ARROW_GAP = 4;          // space between an arrow button and the tabs

    // modern tree layout constants
    public static final int TREE_LEVEL_SPACING = 34;                    // pixels between tree levels, in the direction the tree grows
    public static final int TREE_SIBLING_SPACING_X = 28;                // pixels between side by side achievements when the tree grows up or down
    public static final int TREE_SIBLING_SPACING_Y = 27;                // pixels between stacked achievements when the tree grows left or right
    public static final int TREE_BEND_OFFSET = TREE_LEVEL_SPACING / 2;  // connector bend distance from the parent's centre
    public static final int TREE_OUTLINE_HALF_WIDTH = 1;                // connector outline sticks out this far on each side
    public static final float TREE_MIN_ROW_DISTANCE = 1.0F;             // minimum rows between neighbouring achievements
    public static final float TREE_BRANCH_GAP = 0.0F;                   // extra rows kept between neighbouring branches, deeper than their first column
    public static final float TREE_ROOT_GAP = 1.0F;                     // empty rows between separate trees
    public static final boolean TREE_HEAVIEST_IN_MIDDLE = true;         // children with the biggest branches are placed in the middle of their siblings

    // icon frame texture (vanilla sprite sheet, 256x256)
    public static final int FRAME_TEXTURE_V = 202;
    public static final int FRAME_TEXTURE_U_NORMAL = 0;
    public static final int FRAME_TEXTURE_U_SPECIAL = 26;

    // misc constants
    public static final int ACHIEVEMENT_STAT_ID_BASE = 5242880;
    public static final int PULSE_PERIOD_MS = 600;
    public static final double PULSE_THRESHOLD = 0.6;

    // color (ARGB) constants
    public static final int COLOR_LINE_LOCKED = 0xFF000000;
    public static final int COLOR_LINE_UNLOCKED = 0xFF707070;
    public static final int COLOR_LINE_UNLOCKABLE_BRIGHT = 0xFF00C000;
    public static final int COLOR_LINE_UNLOCKABLE_DIM = 0xFF008000;
    public static final int COLOR_TREE_LINE_OUTLINE = 0xFF000000;
    public static final int COLOR_TREE_LINE_LOCKED = 0xFF505050;
    public static final int COLOR_TREE_LINE_UNLOCKED = 0xFFFFFFFF;
    public static final int COLOR_TOOLTIP_BACKGROUND = 0xC0000000;
    public static final int COLOR_TOOLTIP_HINT = 0xFF705050;
    public static final int COLOR_TOOLTIP_DESCRIPTION = 0xFFA0A0A0;
    public static final int COLOR_TOOLTIP_VILLAGES = 0xC09060;
    public static final int COLOR_TOOLTIP_TAKEN = 0xFF9090FF;
    public static final int COLOR_TITLE_LOCKED = 0xFF808080;
    public static final int COLOR_TITLE_LOCKED_SPECIAL = 0xFF808040;
    public static final int COLOR_TITLE_UNLOCKED = 0xFFFFFFFF;
    public static final int COLOR_TITLE_UNLOCKED_SPECIAL = 0xFFFFFF80;
    public static final int COLOR_PANE_TITLE = 0x404040;

}
