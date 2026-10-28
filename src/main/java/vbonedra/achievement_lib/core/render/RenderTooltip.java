package vbonedra.achievement_lib.core.render;

import net.minecraft.Achievement;
import net.minecraft.AchievementList;
import net.minecraft.FontRenderer;
import net.minecraft.I18n;
import net.minecraft.Minecraft;
import net.minecraft.WorldAchievement;

import vbonedra.achievement_lib.util.AchievementStates;
import vbonedra.achievement_lib.util.GuiPainter;

import static net.minecraft.GuiAchievements.view_mode;
import static vbonedra.achievement_lib.util.GuiConstants.*;

/**
 * Draws the tooltip of the achievement under the mouse.
 */
public record RenderTooltip(Minecraft mc, GuiPainter painter, AchievementStates states) {

    /**
     * Draws tooltip based on {@code achievement} - see {@code drawLockedBody(...)} and {@code drawAvailableBody(...)}
     */
    public void draw(Achievement achievement, int mouseX, int mouseY) {
        boolean unlocked = states.hasAchievementUnlocked(achievement);
        boolean unlockable = states.canUnlockAchievement(achievement);
        boolean locked = !unlocked && !unlockable;

        String name = I18n.getString(achievement.getName());
        if (Minecraft.inDevMode()) {
            name += " (" + (achievement.statId - ACHIEVEMENT_STAT_ID_BASE) + ")";
        }

        int x = mouseX + 12;
        int y = mouseY - 4;

        if (locked) {
            drawLockedBody(achievement, name, x, y);
        } else {
            drawAvailableBody(achievement, name, x, y, unlocked);
        }

        int titleColor;
        if (locked) {
            titleColor = achievement.getSpecial() ? COLOR_TITLE_LOCKED_SPECIAL : COLOR_TITLE_LOCKED;
        } else {
            titleColor = achievement.getSpecial() ? COLOR_TITLE_UNLOCKED_SPECIAL : COLOR_TITLE_UNLOCKED;
        }
        mc.fontRenderer.drawStringWithShadow(name, x, y, titleColor);
    }

    /**
     * Draws tooltip which points at nearest to root locked unlockable parent.
     */
    private void drawLockedBody(Achievement achievement, String name, int x, int y) {
        FontRenderer font = mc.fontRenderer;
        int textWidth = Math.max(font.getStringWidth(name), 120);

        Achievement prerequisite = achievement;
        while (!states.haveAllParentAchievementsBeenUnlocked(prerequisite, true)) {
            prerequisite = prerequisite.parentAchievement;
        }

        String hint = I18n.getStringParams("gui.achievement.see", I18n.getString(prerequisite.getName()));
        int hintHeight = font.splitStringWidth(hint, textWidth);

        painter.drawGradientRect(x - 3, y - 3, x + textWidth + 3, y + hintHeight + 12 + 3,
                COLOR_TOOLTIP_BACKGROUND, COLOR_TOOLTIP_BACKGROUND);
        font.drawSplitString(hint, x, y + 12, textWidth, COLOR_TOOLTIP_HINT);
    }

    /**
     * Draws tooltip which tells how to get achievement, if it's already taken and who had taken it.
     */
    private void drawAvailableBody(Achievement achievement, String name, int x, int y, boolean unlocked) {
        FontRenderer font = mc.fontRenderer;
        String description = achievement.getDescription();
        boolean thisWorldView = view_mode == VIEW_MODE_THIS_WORLD;

        int textWidth = Math.max(font.getStringWidth(name), achievement.getTooltipWidth());
        if (unlocked && thisWorldView) {
            textWidth = Math.max(textWidth, font.getStringWidth(getTakenByText(achievement)));
        }

        boolean showsVillagesNote = (achievement == AchievementList.buildBetterPickaxe);

        int bodyHeight = font.splitStringWidth(description, textWidth);
        if (unlocked) {
            bodyHeight += 12;
        }
        if (showsVillagesNote) {
            bodyHeight += 12;
        }

        painter.drawGradientRect(x - 3, y - 3, x + textWidth + 3, y + bodyHeight + 3 + 12,
                COLOR_TOOLTIP_BACKGROUND, COLOR_TOOLTIP_BACKGROUND);
        font.drawSplitString(description, x, y + 12, textWidth, COLOR_TOOLTIP_DESCRIPTION);

        if (showsVillagesNote) {
            int noteY = y + bodyHeight + 4 - (unlocked ? 12 : 0);
            font.drawStringWithShadow("Unlocks villages", x, noteY, COLOR_TOOLTIP_VILLAGES);
        }

        if (unlocked) {
            String takenText = thisWorldView ? getTakenByText(achievement) : I18n.getString("achievement.taken");
            font.drawStringWithShadow(takenText, x, y + bodyHeight + 4, COLOR_TOOLTIP_TAKEN);
        }
    }

    public String getTakenByText(Achievement achievement) {
        WorldAchievement worldAchievement = mc.theWorld.getWorldInfo().getWorldAchievement(achievement);
        return I18n.getStringParams("gui.achievement.takenBy", worldAchievement.username);
    }
}
