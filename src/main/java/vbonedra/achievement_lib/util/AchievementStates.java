package vbonedra.achievement_lib.util;

import net.minecraft.Achievement;
import net.minecraft.Minecraft;
import net.minecraft.PlayerStatsHelper;
import net.minecraft.StatFileWriter;

import static net.minecraft.GuiAchievements.view_mode;

/** Tells which achievements are unlocked or can be unlocked, in the current view mode. */
public record AchievementStates(Minecraft mc, StatFileWriter statFileWriter) {

    /**
     * If {@code achievement} unlocked.
     * Depends on {@code viewMode}.
     */
    public boolean hasAchievementUnlocked(Achievement achievement) {
        return switch (view_mode) {
            case GuiConstants.VIEW_MODE_PLAYER -> PlayerStatsHelper.hasAchievementUnlocked(achievement);
            case GuiConstants.VIEW_MODE_THIS_WORLD -> mc.theWorld.getWorldInfo().hasAchievementUnlocked(achievement);
            default -> statFileWriter.hasAchievementUnlocked(achievement);
        };
    }

    /**
     * If {@code achievement} connected to root.
     */
    public boolean canUnlockAchievement(Achievement achievement) {
        return haveAllParentAchievementsBeenUnlocked(achievement, true);
    }

    /**
     * If {@code achievement} has at least 1 parent connected to root.
     * {@code includeSecondParents} - if checks for second parent too.
     */
    public boolean haveAllParentAchievementsBeenUnlocked(Achievement achievement, boolean includeSecondParents) {
        if (includeSecondParents && isSecondParentPathUnlocked(achievement)) {
            return true;
        }

        while (achievement.parentAchievement != null) {
            achievement = achievement.parentAchievement;

            if (!hasAchievementUnlocked(achievement)) {
                return false;
            }
            if (includeSecondParents && isSecondParentPathUnlocked(achievement)) {
                return true;
            }
        }

        return true;
    }

    /**
     * If {@code achievement} has second parent connected to root.
     */
    private boolean isSecondParentPathUnlocked(Achievement achievement) {
        return achievement.hasSecondParent()
                && hasAchievementUnlocked(achievement.getSecondParent())
                && haveAllParentAchievementsBeenUnlocked(achievement.getSecondParent(), true);
    }
}
