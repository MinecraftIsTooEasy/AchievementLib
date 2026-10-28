package vbonedra.achievement_lib.util;

import net.minecraft.Minecraft;
import net.minecraft.Tessellator;

import org.lwjgl.opengl.GL11;

/** Basic drawing helpers that don't need the screen. */
public final class GuiDraw {

    // cuts everything drawn afterward to this rectangle, in gui coordinates
    public static void enableScissor(Minecraft mc, int screenWidth, int screenHeight, int x, int y, int w, int h) {
        double scaleX = mc.displayWidth / (double) screenWidth;
        double scaleY = mc.displayHeight / (double) screenHeight;

        int scissorX = (int) (x * scaleX);
        int scissorY = (int) ((screenHeight - y - h) * scaleY);
        int scissorW = (int) (w * scaleX);
        int scissorH = (int) (h * scaleY);

        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor(scissorX, scissorY, scissorW, scissorH);
    }

    // draws part of the bound texture, the texture repeats if the part is bigger than the texture
    public static void drawExactTexture(int x, int y, int width, int height, int u, int v, int uWidth, int vHeight, int texW, int texH, float zLevel) {
        float scaleU = 1.0F / (float) texW;
        float scaleV = 1.0F / (float) texH;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x, y + height, zLevel, u * scaleU, (v + vHeight) * scaleV);
        tessellator.addVertexWithUV(x + width, y + height, zLevel, (u + uWidth) * scaleU, (v + vHeight) * scaleV);
        tessellator.addVertexWithUV(x + width, y, zLevel, (u + uWidth) * scaleU, v * scaleV);
        tessellator.addVertexWithUV(x, y, zLevel, u * scaleU, v * scaleV);
        tessellator.draw();
    }
}
