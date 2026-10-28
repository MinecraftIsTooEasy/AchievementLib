package vbonedra.achievement_lib.util;

import net.minecraft.Icon;

/** Drawing methods of the vanilla Gui class that other packages can't reach, implemented by the screen. */
public interface GuiPainter {

    void drawRect(int left, int top, int right, int bottom, int color);

    void drawHorizontalLine(int x1, int x2, int y, int color);

    void drawVerticalLine(int x, int y1, int y2, int color);

    void drawGradientRect(int left, int top, int right, int bottom, int colorTop, int colorBottom);

    void drawTexturedModalRect(int x, int y, int u, int v, int width, int height);

    void drawTexturedModelRectFromIcon(int x, int y, Icon icon, int width, int height);
}
