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

    private final RenderConnectionAbstract connection;

    protected RenderLayoutAbstract(RenderConnectionAbstract connection) {
        this.connection = connection;
    }

    // placing
    public void layout(List<AchievementExtended> achievements) {
        linkParents(achievements);
        place(achievements);
    }

    // sets x and y of every achievement
    protected abstract void place(List<AchievementExtended> achievements);

    private void linkParents(List<AchievementExtended> achievements) {
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
    public AchievementExtended draw(Minecraft mc, GuiPainter painter, RenderPane pane, AchievementStates states, List<AchievementExtended> achievements,
                                    int scrollX, int scrollY, int originX, int originY, int mouseX, int mouseY) {
        // lines are drawn without textures, drawing the icons turns them back on
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthFunc(GL11.GL_LEQUAL);
        GL11.glDisable(GL11.GL_TEXTURE_2D);

        connection.draw(painter, states, achievements, originX - scrollX, originY - scrollY);

        return drawIcons(mc, painter, pane, states, achievements, scrollX, scrollY, originX, originY, mouseX, mouseY);
    }

    private AchievementExtended drawIcons(Minecraft mc, GuiPainter painter, RenderPane pane, AchievementStates states, List<AchievementExtended> achievements,
                                          int scrollX, int scrollY, int originX, int originY, int mouseX, int mouseY) {
        AchievementExtended hovered = null;
        RenderItem renderItem = new RenderItem();

        RenderHelper.enableGUIStandardItemLighting();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);

        int clipLeft = pane.getBackgroundLeft();
        int clipTop = pane.getBackgroundTop();
        int clipRight = clipLeft + pane.getBackgroundWidth();
        int clipBottom = clipTop + pane.getBackgroundHeight();

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

            drawIcon(mc, painter, states, renderItem, extended.achievement, screenX, screenY);

            boolean overIcon = mouseX >= screenX && mouseX <= screenX + ICON_SIZE
                    && mouseY >= screenY && mouseY <= screenY + ICON_SIZE;
            if (mouseInsideView && overIcon) {
                hovered = extended;
            }
        }

        return hovered;
    }

    // frame and item of one achievement, brighter the closer it is to being unlocked
    private void drawIcon(Minecraft mc, GuiPainter painter, AchievementStates states, RenderItem renderItem, Achievement achievement, int screenX, int screenY) {
        boolean unlocked = states.hasAchievementUnlocked(achievement);
        boolean unlockable = states.canUnlockAchievement(achievement);
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
        painter.drawTexturedModalRect(screenX - FRAME_OVERHANG, screenY - FRAME_OVERHANG, frameU, FRAME_TEXTURE_V, FRAME_SIZE, FRAME_SIZE);
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
