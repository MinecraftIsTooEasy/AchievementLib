package vbonedra.achievement_lib.core.render;

import static vbonedra.achievement_lib.util.GuiConstants.*;

/**
 * Size and position of the pane, its map area and its background area on the screen.
 */
public class RenderPane {

    private int screenWidth;
    private int screenHeight;
    private int paneWidth = MIN_PANE_WIDTH;
    private int paneHeight = MIN_PANE_HEIGHT;
    private int tabAreaHeight; // space above the pane that is kept free for the tabs

    public void setTabAreaHeight(int tabAreaHeight) {
        this.tabAreaHeight = tabAreaHeight;
    }

    // pane size follows the screen size
    public void update(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        paneWidth = Math.max(MIN_PANE_WIDTH, screenWidth - PANE_MARGIN * 2);
        paneHeight = Math.max(MIN_PANE_HEIGHT, screenHeight - PANE_MARGIN * 2 - tabAreaHeight);
    }

    // pane
    public int getPaneWidth() {
        return paneWidth;
    }

    public int getPaneHeight() {
        return paneHeight;
    }

    public int getPaneLeft() {
        return (screenWidth - paneWidth) / 2;
    }

    public int getPaneTop() {
        return (screenHeight - paneHeight - tabAreaHeight) / 2 + tabAreaHeight;
    }

    // map area, inside the border
    public int getMapLeft() {
        return getPaneLeft() + BORDER_LEFT;
    }

    public int getMapTop() {
        return getPaneTop() + BORDER_TOP;
    }

    public int getMapViewWidth() {
        return paneWidth - BORDER_LEFT - BORDER_RIGHT;
    }

    public int getMapViewHeight() {
        return paneHeight - BORDER_TOP - BORDER_BOTTOM;
    }

    // background area, reaches half way under the border
    public int getBackgroundLeft() {
        return getPaneLeft() + BORDER_LEFT / 2;
    }

    public int getBackgroundTop() {
        return getPaneTop() + BORDER_TOP / 2;
    }

    public int getBackgroundWidth() {
        return paneWidth - BORDER_LEFT / 2 - BORDER_RIGHT / 2;
    }

    public int getBackgroundHeight() {
        return paneHeight - BORDER_TOP / 2 - BORDER_BOTTOM / 2;
    }
}
