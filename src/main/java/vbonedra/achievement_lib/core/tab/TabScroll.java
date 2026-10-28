package vbonedra.achievement_lib.core.tab;

import net.minecraft.MathHelper;

import vbonedra.achievement_lib.util.GuiMath;

/** Scroll position of a tab: eases towards its target and follows the mouse while dragging. */
public class TabScroll {

    // position now, last tick (for interpolation) and target
    private double x;
    private double y;
    private double prevX;
    private double prevY;
    private double targetX;
    private double targetY;

    // limits, set by the screen every frame
    private int minX;
    private int maxX;
    private int minY;
    private int maxY;

    // mouse position at the previous frame while dragging
    private int lastMouseX;
    private int lastMouseY;
    private boolean dragging;
    private boolean centered;

    public void setLimits(int minX, int maxX, int minY, int maxY) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
    }

    // false until the tab was shown once and centered on its first achievement
    public boolean isCentered() {
        return centered;
    }

    public void centerOn(double x, double y) {
        prevX = this.x = targetX = x;
        prevY = this.y = targetY = y;
        centered = true;
    }

    public void tick() {
        prevX = x;
        prevY = y;

        double dx = targetX - x;
        double dy = targetY - y;

        if (dx * dx + dy * dy < 4.0) {
            x += dx;
            y += dy;
        } else {
            x += dx * 0.85;
            y += dy * 0.85;
        }
    }

    public void handleDragging(boolean mouseDown, boolean insideMap, int mouseX, int mouseY) {
        if (!mouseDown) {
            dragging = false;
            return;
        }

        if (insideMap) {
            if (!dragging) {
                dragging = true;
            } else {
                x -= mouseX - lastMouseX;
                y -= mouseY - lastMouseY;
                targetX = prevX = x;
                targetY = prevY = y;
            }
            lastMouseX = mouseX;
            lastMouseY = mouseY;
        }

        targetX = GuiMath.clamp(targetX, minX, maxX);
        targetY = GuiMath.clamp(targetY, minY, maxY);
    }

    // position for drawing, between the last two ticks
    public int getScrollX(float partialTicks) {
        return GuiMath.clamp(MathHelper.floor_double(prevX + (x - prevX) * partialTicks), minX, maxX);
    }

    public int getScrollY(float partialTicks) {
        return GuiMath.clamp(MathHelper.floor_double(prevY + (y - prevY) * partialTicks), minY, maxY);
    }
}
