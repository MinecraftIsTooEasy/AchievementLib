package vbonedra.achievement_lib.core.render;

import net.minecraft.Minecraft;

import vbonedra.achievement_lib.util.GuiTextures;

import static vbonedra.achievement_lib.util.GuiConstants.*;
import static vbonedra.achievement_lib.util.GuiDraw.drawExactTexture;

/**
 * Draws dynamic border from 4 corner textures and 4 repeating edge textures.
 */
public record RenderBorder(Minecraft mc) {

    public void draw(int x, int y, int width, int height, float zLevel) {
        int edgeWidth = width - BORDER_LEFT - BORDER_RIGHT;
        int edgeHeight = height - BORDER_TOP - BORDER_BOTTOM;

        // repeatable edges (1px wide/tall textures stretched along the edge)
        if (edgeWidth > 0) {
            mc.getTextureManager().bindTexture(GuiTextures.BORDER_UP);
            drawExactTexture(x + BORDER_LEFT, y, edgeWidth, BORDER_TOP, 0, 0, edgeWidth, BORDER_TOP, 1, BORDER_TOP, zLevel);
            mc.getTextureManager().bindTexture(GuiTextures.BORDER_DOWN);
            drawExactTexture(x + BORDER_LEFT, y + height - BORDER_BOTTOM, edgeWidth, BORDER_BOTTOM, 0, 0, edgeWidth, BORDER_BOTTOM, 1, BORDER_BOTTOM, zLevel);
        }
        if (edgeHeight > 0) {
            mc.getTextureManager().bindTexture(GuiTextures.BORDER_LEFT);
            drawExactTexture(x, y + BORDER_TOP, BORDER_LEFT, edgeHeight, 0, 0, BORDER_LEFT, edgeHeight, BORDER_LEFT, 1, zLevel);
            mc.getTextureManager().bindTexture(GuiTextures.BORDER_RIGHT);
            drawExactTexture(x + width - BORDER_RIGHT, y + BORDER_TOP, BORDER_RIGHT, edgeHeight, 0, 0, BORDER_RIGHT, edgeHeight, BORDER_RIGHT, 1, zLevel);
        }

        // corners
        mc.getTextureManager().bindTexture(GuiTextures.CORNER_UP_LEFT);
        drawExactTexture(x, y, BORDER_LEFT, BORDER_TOP, 0, 0, BORDER_LEFT, BORDER_TOP, BORDER_LEFT, BORDER_TOP, zLevel);
        mc.getTextureManager().bindTexture(GuiTextures.CORNER_UP_RIGHT);
        drawExactTexture(x + width - BORDER_RIGHT, y, BORDER_RIGHT, BORDER_TOP, 0, 0, BORDER_RIGHT, BORDER_TOP, BORDER_RIGHT, BORDER_TOP, zLevel);
        mc.getTextureManager().bindTexture(GuiTextures.CORNER_DOWN_LEFT);
        drawExactTexture(x, y + height - BORDER_BOTTOM, BORDER_LEFT, BORDER_BOTTOM, 0, 0, BORDER_LEFT, BORDER_BOTTOM, BORDER_LEFT, BORDER_BOTTOM, zLevel);
        mc.getTextureManager().bindTexture(GuiTextures.CORNER_DOWN_RIGHT);
        drawExactTexture(x + width - BORDER_RIGHT, y + height - BORDER_BOTTOM, BORDER_RIGHT, BORDER_BOTTOM, 0, 0, BORDER_RIGHT, BORDER_BOTTOM, BORDER_RIGHT, BORDER_BOTTOM, zLevel);
    }
}
