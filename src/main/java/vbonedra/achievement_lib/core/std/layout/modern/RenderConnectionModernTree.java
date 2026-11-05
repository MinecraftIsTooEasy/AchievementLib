package vbonedra.achievement_lib.core.std.layout.modern;

import vbonedra.achievement_lib.core.AchievementExtended;
import vbonedra.achievement_lib.core.std.layout.legacy.RenderConnectionLegacy;
import vbonedra.achievement_lib.util.GuiPainter;
import vbonedra.achievement_lib.util.TreeDirection;

import static vbonedra.achievement_lib.util.GuiConstants.*;

public class RenderConnectionModernTree extends RenderConnectionLegacy {

    public int treeDirection = 0;

    // parent -> bend, across to the child's line, bend -> child
    public void drawLine(GuiPainter guiPainter, AchievementExtended child, AchievementExtended parent, int offsetX, int offsetY, int color, int outlineWidth) {

        int startX = parent.x + ICON_CENTER_OFFSET + offsetX;
        int startY = parent.y + ICON_CENTER_OFFSET + offsetY;
        int endX = child.x + ICON_CENTER_OFFSET + offsetX;
        int endY = child.y + ICON_CENTER_OFFSET + offsetY;

        int bend = (TreeDirection.isHorizontal(treeDirection) ? startX : startY) + TreeDirection.getSign(treeDirection) * TREE_BEND_OFFSET;
        if (TreeDirection.isHorizontal(treeDirection)) {
            drawSimpleLine(guiPainter, startX, startY, bend, startY, color, outlineWidth);
            drawSimpleLine(guiPainter, bend, startY, bend, endY, color, outlineWidth);
            drawSimpleLine(guiPainter, bend, endY, endX, endY, color, outlineWidth);
        } else {
            drawSimpleLine(guiPainter, startX, startY, startX, bend, color, outlineWidth);
            drawSimpleLine(guiPainter, startX, bend, endX, bend, color, outlineWidth);
            drawSimpleLine(guiPainter, endX, bend, endX, endY, color, outlineWidth);
        }

    }

}
