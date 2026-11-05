package vbonedra.achievement_lib.core.render.render_abstract;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.Achievement;
import net.minecraft.Minecraft;
import net.minecraft.RenderHelper;
import net.minecraft.RenderItem;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import vbonedra.achievement_lib.core.AchievementExtended;
import vbonedra.achievement_lib.core.render.RenderPane;
import vbonedra.achievement_lib.util.AchievementStates;
import vbonedra.achievement_lib.util.GuiMath;
import vbonedra.achievement_lib.util.GuiPainter;
import vbonedra.achievement_lib.util.GuiTextures;

import static vbonedra.achievement_lib.util.GuiConstants.*;

/** Template for a layout: places the achievements, then draws their connections and icons. */
public abstract class RenderLayoutAbstract {

    public RenderConnectionAbstract renderConnection;

    // placing
    public void layout(List<AchievementExtended> achievements) {
        linkParents(achievements);
        setAchievementsPositions(achievements);
    }

    // sets x and y of every achievement
    public abstract void setAchievementsPositions(List<AchievementExtended> achievements);

    public void linkParents(List<AchievementExtended> achievements) {
        Map<Achievement, AchievementExtended> extendedOf = new HashMap<Achievement, AchievementExtended>();
        for (AchievementExtended extended : achievements) {
            extendedOf.put(extended.achievement, extended);
        }

        for (AchievementExtended extended : achievements) {
            Achievement achievement = extended.achievement;
            extended.parent = extendedOf.get(achievement.parentAchievement);
            extended.secondParent = achievement.hasSecondParent() ? extendedOf.get(achievement.getSecondParent()) : null;
        }
    }

    // drawing, returns the achievement under the mouse or null
    public AchievementExtended draw(Minecraft mc, GuiPainter guiPainter, RenderPane renderPane, AchievementStates achievementStates, List<AchievementExtended> achievements,
                                    int scrollX, int scrollY, int originX, int originY, int mouseX, int mouseY) {
        // lines are drawn without textures, drawing the icons turns them back on
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthFunc(GL11.GL_LEQUAL);
        GL11.glDisable(GL11.GL_TEXTURE_2D);

        drawConnections(guiPainter, achievementStates, achievements, originX - scrollX, originY - scrollY);

        return drawIcons(mc, guiPainter, renderPane, achievementStates, achievements, scrollX, scrollY, originX, originY, mouseX, mouseY);
    }

    public void drawConnections(GuiPainter guiPainter, AchievementStates achievementStates, List<AchievementExtended> achievements, int offsetX, int offsetY) {
        // pass 0: outlines
        // pass 1: locked and unlockable lines
        // pass 2: unlocked lines on top
        for (int pass = 0; pass < 3; pass++) {
            for (AchievementExtended achievementExtended : achievements) {
                renderConnection.drawConnection(guiPainter, achievementStates, pass, achievementExtended, achievementExtended.parent, false, offsetX, offsetY);
                renderConnection.drawConnection(guiPainter, achievementStates, pass, achievementExtended, achievementExtended.secondParent, true, offsetX, offsetY);
                // TODO: 2nd parent connection must be rendered only when child with such parent is in focus aka under mouse
                //  this is toggleable by bool variable; other options like dimming connection are fine to be implemented
            }
        }
    }

    public AchievementExtended drawIcons(
            Minecraft mc, GuiPainter guiPainter, RenderPane renderPane,
            AchievementStates achievementStates, List<AchievementExtended> achievements,
            int scrollX, int scrollY,
            int originX, int originY,
            int mouseX, int mouseY
    ) {
        // TODO: achievements must have "depth" parameter, depth could be checked by this method to decide how achievement would render and if it should render
        //  that would allow hiding far achievements irrelevant for player at the moment

        AchievementExtended hovered = null;
        RenderItem renderItem = new RenderItem();

        RenderHelper.enableGUIStandardItemLighting();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);

        int clipLeft = renderPane.getBackgroundLeft();
        int clipTop = renderPane.getBackgroundTop();
        int clipRight = clipLeft + renderPane.getBackgroundWidth();
        int clipBottom = clipTop + renderPane.getBackgroundHeight();

        boolean mouseInsideView = mouseX >= clipLeft && mouseX < clipRight
                && mouseY >= clipTop && mouseY < clipBottom;

        for (AchievementExtended extended : achievements) {
            int screenX = originX + extended.x - scrollX;
            int screenY = originY + extended.y - scrollY;

            int frameLeft = screenX - FRAME_OVERHANG;
            int frameTop = screenY - FRAME_OVERHANG;
            boolean visible = frameLeft + FRAME_SIZE > clipLeft && frameLeft < clipRight
                    && frameTop + FRAME_SIZE > clipTop && frameTop < clipBottom;
            if (!visible) {
                continue;
            }

            drawIcon(mc, guiPainter, achievementStates, renderItem, extended.achievement, screenX, screenY);

            boolean overIcon = mouseX >= screenX && mouseX <= screenX + ICON_SIZE
                    && mouseY >= screenY && mouseY <= screenY + ICON_SIZE;
            if (mouseInsideView && overIcon) {
                hovered = extended;
            }
        }

        return hovered;
    }

    // frame and item of one achievement, brighter the closer it is to being unlocked
    public void drawIcon(Minecraft mc, GuiPainter guiPainter, AchievementStates achievementStates, RenderItem renderItem, Achievement achievement, int screenX, int screenY) {
        boolean unlocked = achievementStates.hasAchievementUnlocked(achievement);
        boolean unlockable = achievementStates.canUnlockAchievement(achievement);
        boolean locked = !unlocked && !unlockable;

        float brightness;
        if (unlocked) {
            brightness = 1.0F;
        } else if (unlockable) {
            brightness = GuiMath.isPulseHigh() ? 0.8F : 0.6F;
        } else {
            brightness = 0.3F;
        }

        GL11.glColor4f(brightness, brightness, brightness, 1.0F);

        // frame sprite, blending keeps transparent sprite pixels transparent
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        mc.getTextureManager().bindTexture(GuiTextures.ACHIEVEMENT);
        int frameU = achievement.getSpecial() ? FRAME_TEXTURE_U_SPECIAL : FRAME_TEXTURE_U_NORMAL;
        guiPainter.drawTexturedModalRect(screenX - FRAME_OVERHANG, screenY - FRAME_OVERHANG, frameU, FRAME_TEXTURE_V, FRAME_SIZE, FRAME_SIZE);
        GL11.glDisable(GL11.GL_BLEND);

        // locked items are dark silhouettes
        if (locked) {
            GL11.glColor4f(0.1F, 0.1F, 0.1F, 1.0F);
            renderItem.renderWithColor = false;
        }

        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_CULL_FACE);
        renderItem.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.getTextureManager(),
                achievement.theItemStack, screenX + ITEM_INSET, screenY + ITEM_INSET);
        GL11.glDisable(GL11.GL_LIGHTING);

        if (locked) {
            renderItem.renderWithColor = true;
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
