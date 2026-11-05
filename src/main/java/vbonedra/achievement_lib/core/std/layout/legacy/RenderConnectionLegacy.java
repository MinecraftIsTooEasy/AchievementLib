package vbonedra.achievement_lib.core.std.layout.legacy;

import net.minecraft.Achievement;
import vbonedra.achievement_lib.core.AchievementExtended;
import vbonedra.achievement_lib.core.render.render_abstract.RenderConnectionAbstract;
import vbonedra.achievement_lib.util.AchievementStates;
import vbonedra.achievement_lib.util.GuiMath;
import vbonedra.achievement_lib.util.GuiPainter;

import static vbonedra.achievement_lib.util.GuiConstants.*;

public class RenderConnectionLegacy extends RenderConnectionAbstract {

    public int colorLineUnlocked = COLOR_LINE_UNLOCKED;
    public int colorLineUnlockableBright = COLOR_LINE_UNLOCKABLE_BRIGHT;
    public int colorLineUnlockableDim = COLOR_LINE_UNLOCKABLE_DIM;
    public int colorLineLocked = COLOR_LINE_LOCKED;
    public int outlineWidth = 0;

    public void drawConnection(GuiPainter guiPainter, AchievementStates achievementStates,
                               int pass,
                               AchievementExtended child, AchievementExtended parent,
                               boolean secondary,
                               int offsetX, int offsetY
    ) {
        if (parent == null) {
            return;
        }

        Achievement childAchievement = child.achievement;
        Achievement parentAchievement = parent.achievement;

        boolean unlocked = achievementStates.hasAchievementUnlocked(childAchievement);
        boolean unlockable = achievementStates.canUnlockAchievement(childAchievement);

        boolean parentsUnlocked = secondary
                ? achievementStates.hasAchievementUnlocked(parentAchievement) && achievementStates.haveAllParentAchievementsBeenUnlocked(parentAchievement, true)
                : achievementStates.haveAllParentAchievementsBeenUnlocked(childAchievement, false);
        if (!parentsUnlocked) {
            if (!achievementStates.hasAchievementUnlocked(parentAchievement)) {
                unlocked = false;
            }
            unlockable = false;
        }

        if (pass == 1 && unlocked || pass == 2 && !unlocked) {
            return;
        }

        if (pass == 0) {
            drawLine(guiPainter, child, parent, offsetX, offsetY, COLOR_LINE_OUTLINE, outlineWidth);
        } else {
            drawLine(guiPainter, child, parent, offsetX, offsetY, getColor(unlocked, unlockable), 0);
        }
    }

    // flipped achievements start at the parent instead of the child
    public void drawLine(GuiPainter guiPainter, AchievementExtended child, AchievementExtended parent, int offsetX, int offsetY, int color, int outlineWidth) {
        AchievementExtended start = child.achievement.isFlipped() ? parent : child;
        AchievementExtended end = child.achievement.isFlipped() ? child : parent;

        int startX = start.x + ICON_CENTER_OFFSET + offsetX;
        int startY = start.y + ICON_CENTER_OFFSET + offsetY;
        int endX = end.x + ICON_CENTER_OFFSET + offsetX;
        int endY = end.y + ICON_CENTER_OFFSET + offsetY;

        drawSimpleLine(guiPainter, startX, startY, endX, startY, color, outlineWidth);
        drawSimpleLine(guiPainter, endX, startY, endX, endY, color, outlineWidth);
    }

    public void drawSimpleLine(GuiPainter guiPainter, int x1, int y1, int x2, int y2, int color, int outlineWidth) {
        guiPainter.drawRect(Math.min(x1, x2) - outlineWidth, Math.min(y1, y2) - outlineWidth,
                Math.max(x1, x2) + 1 + outlineWidth, Math.max(y1, y2) + 1 + outlineWidth, color);
    }

    public int getColor(boolean unlocked, boolean unlockable) {
        if (unlocked) {
            return colorLineUnlocked;
        }
        if (unlockable) {
            return GuiMath.isPulseHigh() ? colorLineUnlockableBright : colorLineUnlockableDim;
        }
        return colorLineLocked;
    }

}
