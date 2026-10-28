package vbonedra.achievement_lib.core.render;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.FontRenderer;
import net.minecraft.I18n;
import net.minecraft.Icon;
import net.minecraft.Minecraft;
import net.minecraft.ResourceLocation;
import net.minecraft.TextureMap;

import org.lwjgl.opengl.GL11;

import vbonedra.achievement_lib.core.tab.TabAbstract;
import vbonedra.achievement_lib.util.GuiDraw;
import vbonedra.achievement_lib.util.GuiPainter;
import vbonedra.achievement_lib.util.GuiTextures;

import static vbonedra.achievement_lib.util.GuiConstants.*;

/**
 * The tab row above the pane: draws the tabs and scroll arrows, finds the tab or arrow under the mouse.
 */
public class RenderTabBar {

    // tab icons are taken from this texture atlas
    private static final ResourceLocation ICON_ATLAS = TextureMap.locationBlocksTexture;

    private final Minecraft mc;
    private final GuiPainter painter;

    private final List<String> names = new ArrayList<>();
    private final List<TabAbstract> tabs = new ArrayList<>();
    private int selectedIndex = -1;
    private int lastSelectedIndex = -1;

    // layout of the row, recalculated in update
    private int paneTop;
    private int tabsLeft;
    private int arrowLeftX;
    private int arrowRightX;
    private int firstVisible; // index of the first tab that is shown
    private int visibleCount;
    private boolean arrowsVisible;

    public RenderTabBar(Minecraft mc, GuiPainter painter) {
        this.mc = mc;
        this.painter = painter;
    }

    // layout
    public void update(RenderPane pane, Map<String, TabAbstract> tabMap, String selectedName) {
        names.clear();
        tabs.clear();
        names.addAll(tabMap.keySet());
        tabs.addAll(tabMap.values());
        selectedIndex = names.indexOf(selectedName);
        paneTop = pane.getPaneTop();

        int count = names.size();
        int stripLeft = pane.getPaneLeft() + TAB_STRIP_MARGIN;
        int stripWidth = pane.getPaneWidth() - TAB_STRIP_MARGIN * 2;

        // arrows are needed if the tabs don't fit
        arrowsVisible = count > getCapacity(stripWidth);

        int tabsWidth = stripWidth;
        tabsLeft = stripLeft;
        if (arrowsVisible) {
            int arrowSpace = ARROW_WIDTH + ARROW_GAP;
            tabsLeft = stripLeft + arrowSpace;
            tabsWidth = stripWidth - arrowSpace * 2;
            arrowLeftX = stripLeft;
            arrowRightX = stripLeft + stripWidth - ARROW_WIDTH;
        }

        visibleCount = Math.min(count, getCapacity(tabsWidth));

        // another tab was selected, scroll so it is shown
        if (selectedIndex != lastSelectedIndex) {
            lastSelectedIndex = selectedIndex;
            if (selectedIndex >= 0) {
                if (selectedIndex < firstVisible) {
                    firstVisible = selectedIndex;
                } else if (selectedIndex >= firstVisible + visibleCount) {
                    firstVisible = selectedIndex - visibleCount + 1;
                }
            }
        }

        firstVisible = Math.max(0, Math.min(firstVisible, count - visibleCount));
    }

    private int getCapacity(int width) {
        return Math.max(1, (width + TAB_GAP) / (TAB_WIDTH + TAB_GAP));
    }

    public void scrollTabs(int direction) {
        firstVisible = Math.max(0, Math.min(firstVisible + direction, names.size() - visibleCount));
    }

    // clicks
    // returns the name of the tab at this position, or null
    public String getTabAt(int mouseX, int mouseY) {
        for (int i = firstVisible; i < firstVisible + visibleCount; i++) {
            int x = getTabX(i);
            int top = getTabTop(i == selectedIndex);

            if (mouseX >= x && mouseX < x + TAB_WIDTH && mouseY >= top && mouseY < paneTop) {
                return names.get(i);
            }
        }
        return null;
    }

    // returns -1 for the left arrow, 1 for the right arrow and 0 for neither
    public int getArrowAt(int mouseX, int mouseY) {
        if (!arrowsVisible || mouseY < paneTop - ARROW_HEIGHT || mouseY >= paneTop) {
            return 0;
        }
        if (mouseX >= arrowLeftX && mouseX < arrowLeftX + ARROW_WIDTH) {
            return -1;
        }
        if (mouseX >= arrowRightX && mouseX < arrowRightX + ARROW_WIDTH) {
            return 1;
        }
        return 0;
    }

    // drawing
    public void draw(float zLevel) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        // the selected tab is drawn last, so it is on top of its neighbours
        for (int i = firstVisible; i < firstVisible + visibleCount; i++) {
            if (i != selectedIndex) {
                drawTab(i, false, zLevel);
            }
        }
        if (selectedIndex >= firstVisible && selectedIndex < firstVisible + visibleCount) {
            drawTab(selectedIndex, true, zLevel);
        }

        if (arrowsVisible) {
            drawArrow(GuiTextures.TAB_ARROW_LEFT, arrowLeftX, firstVisible > 0, zLevel);
            drawArrow(GuiTextures.TAB_ARROW_RIGHT, arrowRightX, firstVisible + visibleCount < names.size(), zLevel);
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private void drawTab(int index, boolean selected, float zLevel) {
        int x = getTabX(index);
        int top = getTabTop(selected);
        int height = selected ? TAB_SELECTED_TEXTURE_HEIGHT : TAB_HEIGHT;

        mc.getTextureManager().bindTexture(selected ? GuiTextures.TAB_SELECTED : GuiTextures.TAB_UNSELECTED);
        GuiDraw.drawExactTexture(x, top, TAB_WIDTH, height, 0, 0, TAB_WIDTH, height, TAB_WIDTH, height, zLevel);

        Icon icon = tabs.get(index).getIcon();
        if (icon != null) {
            // centred in the part of the tab that is above the pane
            int visibleHeight = paneTop - top;
            mc.getTextureManager().bindTexture(ICON_ATLAS);
            painter.drawTexturedModelRectFromIcon(
                    x + (TAB_WIDTH - TAB_ICON_SIZE) / 2,
                    top + (visibleHeight - TAB_ICON_SIZE) / 2,
                    icon, TAB_ICON_SIZE, TAB_ICON_SIZE);
        }
    }

    // arrows that can't scroll further are dimmed
    private void drawArrow(ResourceLocation texture, int x, boolean enabled, float zLevel) {
        float shade = enabled ? 1.0F : 0.5F;
        GL11.glColor4f(shade, shade, shade, 1.0F);

        mc.getTextureManager().bindTexture(texture);
        GuiDraw.drawExactTexture(x, paneTop - ARROW_HEIGHT, ARROW_WIDTH, ARROW_HEIGHT,
                0, 0, ARROW_WIDTH, ARROW_HEIGHT, ARROW_WIDTH, ARROW_HEIGHT, zLevel);
    }

    // \tooltip with the title of the tab under the mouse
    public void drawTooltip(int mouseX, int mouseY) {
        String name = getTabAt(mouseX, mouseY);
        if (name == null) {
            return;
        }

        FontRenderer font = mc.fontRenderer;
        String text = I18n.getString(tabs.get(names.indexOf(name)).getTitle());
        int width = font.getStringWidth(text);
        int x = mouseX + 12;
        int y = mouseY - 4;

        painter.drawGradientRect(x - 3, y - 3, x + width + 3, y + 8 + 3, COLOR_TOOLTIP_BACKGROUND, COLOR_TOOLTIP_BACKGROUND);
        font.drawStringWithShadow(text, x, y, COLOR_TITLE_UNLOCKED);
    }

    // positions
    private int getTabX(int index) {
        return tabsLeft + (index - firstVisible) * (TAB_WIDTH + TAB_GAP);
    }

    private int getTabTop(boolean selected) {
        return paneTop - TAB_HEIGHT - (selected ? TAB_RISE : 0);
    }
}
