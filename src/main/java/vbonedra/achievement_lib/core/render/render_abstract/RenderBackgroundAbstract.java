package vbonedra.achievement_lib.core.render.render_abstract;

import java.util.Random;

import net.minecraft.Icon;
import net.minecraft.Minecraft;
import net.minecraft.TextureMap;

import org.lwjgl.opengl.GL11;

import vbonedra.achievement_lib.core.render.RenderPane;
import vbonedra.achievement_lib.util.GuiMath;
import vbonedra.achievement_lib.util.GuiPainter;

import static vbonedra.achievement_lib.util.GuiConstants.*;

/** Template for the map background: draws the tiles, subclasses choose which tile goes where. */
public abstract class RenderBackgroundAbstract {

    private final Random random = new Random();

    // maxDepth is how far down (map pixels) the deepest achievement is, tiles get darker until there
    public void draw(Minecraft mc, GuiPainter painter, RenderPane pane, int scrollX, int scrollY, int originX, int originY, int maxDepth) {
        int drawLeft = pane.getBackgroundLeft();
        int drawTop = pane.getBackgroundTop();

        // world position of the background's top left corner
        int worldLeft = scrollX - (originX - drawLeft) + TILE_WORLD_OFFSET;
        int worldTop = scrollY - (originY - drawTop) + TILE_WORLD_OFFSET;

        int firstTileColumn = worldLeft >> 4;
        int firstTileRow = worldTop >> 4;
        int pixelOffsetX = worldLeft & (TILE_SIZE - 1);
        int pixelOffsetY = worldTop & (TILE_SIZE - 1);

        int depthRows = Math.max(1, (maxDepth + TILE_WORLD_OFFSET) >> 4);

        for (int row = 0; row * TILE_SIZE - pixelOffsetY < pane.getBackgroundHeight(); row++) {
            int worldRow = firstTileRow + row;

            float brightness = getBrightness(worldRow, depthRows);
            GL11.glColor4f(brightness, brightness, brightness, 1.0F);

            for (int column = 0; column * TILE_SIZE - pixelOffsetX < pane.getBackgroundWidth(); column++) {
                random.setSeed(1234 + firstTileColumn + column);
                random.nextInt();

                Icon icon = pickIcon(random, worldRow);

                mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
                painter.drawTexturedModelRectFromIcon(
                        drawLeft + column * TILE_SIZE - pixelOffsetX,
                        drawTop + row * TILE_SIZE - pixelOffsetY,
                        icon, TILE_SIZE, TILE_SIZE);
            }
        }
    }

    // the random is already seeded for this tile column
    protected abstract Icon pickIcon(Random random, int worldRow);

    // top row is brightest, depthRows and everything below it is darkest
    protected float getBrightness(int worldRow, int depthRows) {
        float depth = GuiMath.clamp01(worldRow / (float) depthRows);
        return BACKGROUND_BRIGHTNESS_TOP - (BACKGROUND_BRIGHTNESS_TOP - BACKGROUND_BRIGHTNESS_BOTTOM) * depth;
    }
}
