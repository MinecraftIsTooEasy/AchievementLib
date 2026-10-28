package vbonedra.achievement_lib.core.std.layout.legacy;

import java.util.List;

import net.minecraft.Achievement;

import vbonedra.achievement_lib.core.AchievementExtended;
import vbonedra.achievement_lib.core.render.render_abstract.RenderConnectionAbstract;
import vbonedra.achievement_lib.util.AchievementStates;
import vbonedra.achievement_lib.util.GuiMath;
import vbonedra.achievement_lib.util.GuiPainter;

import static vbonedra.achievement_lib.util.GuiConstants.*;

/** Vanilla lines: one pixel wide, a horizontal part from the child and a vertical part to the parent. */
public class RenderConnectionLegacy extends RenderConnectionAbstract {

    @Override
    public void draw(GuiPainter painter, AchievementStates states, List<AchievementExtended> achievements, int offsetX, int offsetY) {
        // pass 0: locked and unlockable lines, pass 1: unlocked lines on top
        for (int pass = 0; pass < 2; pass++) {
            boolean unlockedPass = (pass == 1);

            for (AchievementExtended achievementExtended : achievements) {
                Achievement achievement = achievementExtended.achievement;

                if (achievementExtended.parent != null) {
                    boolean unlocked = states.hasAchievementUnlocked(achievement);
                    boolean unlockable = states.canUnlockAchievement(achievement);

                    if (!states.haveAllParentAchievementsBeenUnlocked(achievement, false)) {
                        if (!states.hasAchievementUnlocked(achievementExtended.parent.achievement)) {
                            unlocked = false;
                        }
                        unlockable = false;
                    }

                    if (unlocked != unlockedPass) {
                        continue;
                    }

                    if (achievementExtended.secondParent != null && states.hasAchievementUnlocked(achievementExtended.parent.achievement)) {
                        drawSecondParentConnection(painter, states, achievementExtended, offsetX, offsetY);
                    }

                    drawConnector(painter, achievementExtended, achievementExtended.parent, offsetX, offsetY, getColor(unlocked, unlockable));
                }

                // the first parent is in another tab or not unlocked
                Achievement firstParent = achievement.parentAchievement;
                if (achievementExtended.secondParent != null && (firstParent == null || !states.hasAchievementUnlocked(firstParent))) {
                    drawSecondParentConnection(painter, states, achievementExtended, offsetX, offsetY);
                }
            }
        }
    }

    private void drawSecondParentConnection(GuiPainter painter, AchievementStates states, AchievementExtended child, int offsetX, int offsetY) {
        Achievement secondParent = child.secondParent.achievement;

        for (int pass = 0; pass < 2; pass++) {
            boolean unlockedPass = (pass == 1);

            boolean unlocked = states.hasAchievementUnlocked(child.achievement);
            boolean unlockable = states.canUnlockAchievement(child.achievement);

            if (!states.hasAchievementUnlocked(secondParent) || !states.haveAllParentAchievementsBeenUnlocked(secondParent, true)) {
                if (!states.hasAchievementUnlocked(secondParent)) {
                    unlocked = false;
                }
                unlockable = false;
            }

            if (unlocked != unlockedPass) {
                continue;
            }

            drawConnector(painter, child, child.secondParent, offsetX, offsetY, getColor(unlocked, unlockable));
        }
    }

    // flipped achievements start at the parent instead of the child
    private void drawConnector(GuiPainter painter, AchievementExtended child, AchievementExtended parent, int offsetX, int offsetY, int color) {
        AchievementExtended start = child.achievement.isFlipped() ? parent : child;
        AchievementExtended end = child.achievement.isFlipped() ? child : parent;

        int startX = start.x + ICON_CENTER_OFFSET + offsetX;
        int startY = start.y + ICON_CENTER_OFFSET + offsetY;
        int endX = end.x + ICON_CENTER_OFFSET + offsetX;
        int endY = end.y + ICON_CENTER_OFFSET + offsetY;

        painter.drawHorizontalLine(startX, endX, startY, color);
        painter.drawVerticalLine(endX, startY, endY, color);
    }

    private int getColor(boolean unlocked, boolean unlockable) {
        if (unlocked) {
            return COLOR_LINE_UNLOCKED;
        }
        if (unlockable) {
            return GuiMath.isPulseHigh() ? COLOR_LINE_UNLOCKABLE_BRIGHT : COLOR_LINE_UNLOCKABLE_DIM;
        }
        return COLOR_LINE_LOCKED;
    }
}
