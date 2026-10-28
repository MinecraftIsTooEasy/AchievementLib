package vbonedra.achievement_lib.core.std.layout.modern;

import java.util.List;

import net.minecraft.Achievement;

import vbonedra.achievement_lib.core.AchievementExtended;
import vbonedra.achievement_lib.core.render.render_abstract.RenderConnectionAbstract;
import vbonedra.achievement_lib.util.AchievementStates;
import vbonedra.achievement_lib.util.GuiMath;
import vbonedra.achievement_lib.util.GuiPainter;
import vbonedra.achievement_lib.util.TreeDirection;

import static vbonedra.achievement_lib.util.GuiConstants.*;

/** Modern lines: outlined, they leave the parent, bend between the tree levels and end at the child. */
public class RenderConnectionModern extends RenderConnectionAbstract {

    private final int direction; // direction the tree grows in, see TreeDirection
    public RenderConnectionModern(int direction) {
        this.direction = direction;
    }

    @Override
    public void draw(GuiPainter painter, AchievementStates states, List<AchievementExtended> achievements, int offsetX, int offsetY) {
        // pass 0: outlines, pass 1: locked and unlockable lines, pass 2: unlocked lines on top
        for (int pass = 0; pass < 3; pass++) {
            for (AchievementExtended extended : achievements) {
                drawConnection(painter, states, pass, extended, extended.parent, false, offsetX, offsetY);
                drawConnection(painter, states, pass, extended, extended.secondParent, true, offsetX, offsetY);
            }
        }
    }

    private void drawConnection(GuiPainter painter, AchievementStates states, int pass, AchievementExtended child, AchievementExtended parent,
                                boolean secondary, int offsetX, int offsetY) {
        if (parent == null) {
            return;
        }

        Achievement childAchievement = child.achievement;
        Achievement parentAchievement = parent.achievement;

        boolean unlocked = states.hasAchievementUnlocked(childAchievement);
        boolean unlockable = states.canUnlockAchievement(childAchievement);

        boolean parentsUnlocked = secondary
                ? states.hasAchievementUnlocked(parentAchievement) && states.haveAllParentAchievementsBeenUnlocked(parentAchievement, true)
                : states.haveAllParentAchievementsBeenUnlocked(childAchievement, false);
        if (!parentsUnlocked) {
            if (!states.hasAchievementUnlocked(parentAchievement)) {
                unlocked = false;
            }
            unlockable = false;
        }

        if (pass == 1 && unlocked || pass == 2 && !unlocked) {
            return;
        }

        int startX = parent.x + ICON_CENTER_OFFSET + offsetX;
        int startY = parent.y + ICON_CENTER_OFFSET + offsetY;
        int endX = child.x + ICON_CENTER_OFFSET + offsetX;
        int endY = child.y + ICON_CENTER_OFFSET + offsetY;

        int bend = (TreeDirection.isHorizontal(direction) ? startX : startY) + TreeDirection.getSign(direction) * TREE_BEND_OFFSET;

        if (pass == 0) {
            drawPath(painter, startX, startY, endX, endY, bend, COLOR_TREE_LINE_OUTLINE, TREE_OUTLINE_HALF_WIDTH);
        } else {
            drawPath(painter, startX, startY, endX, endY, bend, getColor(unlocked, unlockable), 0);
        }
    }

    // parent -> bend, across to the child's line, bend -> child
    private void drawPath(GuiPainter painter, int startX, int startY, int endX, int endY, int bend, int color, int halfWidth) {
        if (TreeDirection.isHorizontal(direction)) {
            drawLine(painter, startX, startY, bend, startY, color, halfWidth);
            drawLine(painter, bend, startY, bend, endY, color, halfWidth);
            drawLine(painter, bend, endY, endX, endY, color, halfWidth);
        } else {
            drawLine(painter, startX, startY, startX, bend, color, halfWidth);
            drawLine(painter, startX, bend, endX, bend, color, halfWidth);
            drawLine(painter, endX, bend, endX, endY, color, halfWidth);
        }
    }

    private void drawLine(GuiPainter painter, int x1, int y1, int x2, int y2, int color, int halfWidth) {
        painter.drawRect(Math.min(x1, x2) - halfWidth, Math.min(y1, y2) - halfWidth,
                Math.max(x1, x2) + 1 + halfWidth, Math.max(y1, y2) + 1 + halfWidth, color);
    }

    private int getColor(boolean unlocked, boolean unlockable) {
        if (unlocked) {
            return COLOR_TREE_LINE_UNLOCKED;
        }
        if (unlockable) {
            return GuiMath.isPulseHigh() ? COLOR_LINE_UNLOCKABLE_BRIGHT : COLOR_LINE_UNLOCKABLE_DIM;
        }
        return COLOR_TREE_LINE_LOCKED;
    }
}
