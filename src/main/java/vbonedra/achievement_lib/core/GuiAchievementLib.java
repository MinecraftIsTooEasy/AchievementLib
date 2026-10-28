package vbonedra.achievement_lib.core;

import net.minecraft.*;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import vbonedra.achievement_lib.core.tab.TabAbstract;
import vbonedra.achievement_lib.core.tab.TabRegistry;
import vbonedra.achievement_lib.core.render.RenderBorder;
import vbonedra.achievement_lib.core.render.RenderPane;
import vbonedra.achievement_lib.core.render.RenderTabBar;
import vbonedra.achievement_lib.core.render.RenderTooltip;
import vbonedra.achievement_lib.util.AchievementStates;
import vbonedra.achievement_lib.util.GuiDraw;
import vbonedra.achievement_lib.util.GuiPainter;

import static net.minecraft.GuiAchievements.view_mode;
import static vbonedra.achievement_lib.util.GuiConstants.*;

/**
 * The achievement screen: pane, border, tab bar, buttons. The open tab draws the map.
 */
public class GuiAchievementLib extends GuiScreen {

    private GuiButton button_cycle_view_mode;

    private final GuiPainter painter = new Painter();
    private final RenderPane pane = new RenderPane();
    private final AchievementStates states;
    private final RenderBorder border;
    private final RenderTooltip tooltip;
    private final RenderTabBar tabBar;

    public GuiAchievementLib(StatFileWriter statFileWriter) {
        Minecraft minecraft = Minecraft.getMinecraft();

        states = new AchievementStates(minecraft, statFileWriter);
        border = new RenderBorder(minecraft);
        tooltip = new RenderTooltip(minecraft, painter, states);
        tabBar = new RenderTabBar(minecraft, painter);

    }

    public void setTab(String name) {
        // before the screen is opened initGui prepares the tab
        if (TabRegistry.setCurrent(name) && width > 0) {
            prepareTab(TabRegistry.getCurrent());
        }
    }

    private void prepareTab(TabAbstract tab) {
        tab.updateLayout();
        if (!tab.getScroll().isCentered()) {
            centerMapOn(tab);
        }
    }

    // the tab bar is only useful with more than one tab
    private boolean showTabBar() {
        return TabRegistry.TABS.size() > 1;
    }

    private void centerMapOn(TabAbstract tab) {
        AchievementExtended root = tab.getRootAchievement();
        double iconX = root == null ? 0 : root.x;
        double iconY = root == null ? 0 : root.y;

        tab.getScroll().centerOn(
                iconX + ICON_CENTER_OFFSET - (double) pane.getMapViewWidth() / 2,
                iconY + ICON_CENTER_OFFSET - (double) pane.getMapViewHeight() / 2);
    }

    // scroll limits: icons can reach the middle of the map, going up stops at the first background row (the sand)
    private void updateScrollLimits(TabAbstract tab) {
        tab.getScroll().setLimits(
                tab.getMinX() + ICON_CENTER_OFFSET - pane.getMapViewWidth() / 2,
                tab.getMaxX() + ICON_CENTER_OFFSET - pane.getMapViewWidth() / 2,
                pane.getMapTop() - pane.getBackgroundTop() - TILE_WORLD_OFFSET,
                tab.getMaxY() + ICON_CENTER_OFFSET - pane.getMapViewHeight() / 2);
    }

    // setup and input
    @Override public void initGui() {
        TabRegistry.initializeAllTabs(false); // we don't want to force layout recalculations on every gui initialization, so we don't clear initialized tabs

        TabAbstract tab = TabRegistry.getCurrent();

        pane.setTabAreaHeight(showTabBar() ? TAB_AREA_HEIGHT : 0);
        pane.update(width, height);
        prepareTab(tab);

        int buttonY = pane.getPaneTop() + pane.getPaneHeight() - BORDER_BOTTOM + (BORDER_BOTTOM - BUTTON_HEIGHT) / 2;

        buttonList.clear();
        int buttonX = width - BUTTON_WIDTH - BUTTON_GAP_BORDER;
        buttonList.add(new GuiSmallButton(1, buttonX, buttonY, BUTTON_WIDTH, BUTTON_HEIGHT, I18n.getString("gui.done")));
        buttonX = buttonX - BUTTON_WIDTH - BUTTON_GAP;
        buttonList.add(button_cycle_view_mode = new GuiSmallButton(2, buttonX, buttonY, BUTTON_WIDTH, BUTTON_HEIGHT, ""));
        updateViewModeButtonState(tab);
    }

    // the view mode belongs to the tab, the button is only shown if other players have achievements
    private void updateViewModeButtonState(TabAbstract tab) {
        if (Minecraft.theMinecraft.thePlayer.haveAchievementsBeenUnlockedByOtherPlayers()) {
            button_cycle_view_mode.drawButton = true;
        } else {
            button_cycle_view_mode.drawButton = false;
            view_mode = VIEW_MODE_PLAYER;
        }

        String labelKey;
        if (view_mode == VIEW_MODE_PLAYER) {
            labelKey = "gui.stats.thisWorld";
        } else if (view_mode == VIEW_MODE_THIS_WORLD && Minecraft.inDevMode()) {
            labelKey = "gui.stats.allWorlds";
        } else {
            labelKey = "gui.stats.player";
        }
        button_cycle_view_mode.displayString = I18n.getString(labelKey);
    }

    @Override protected void actionPerformed(GuiButton button) {
        TabAbstract tab = TabRegistry.getCurrent();

        if (button.id == 1) {
            closeScreen();
        } else if (button.id == 2) {
            int lastMode = Minecraft.inDevMode() ? VIEW_MODE_ALL_WORLDS : VIEW_MODE_THIS_WORLD;
            int nextMode = view_mode + 1;
            view_mode = nextMode > lastMode ? VIEW_MODE_PLAYER : nextMode;
            updateViewModeButtonState(tab);
        }

        super.actionPerformed(button);
    }

    @Override protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0 && showTabBar()) {
            int arrow = tabBar.getArrowAt(mouseX, mouseY);
            if (arrow != 0) {
                tabBar.scrollTabs(arrow);
                return;
            }

            String tabName = tabBar.getTabAt(mouseX, mouseY);
            if (tabName != null) {
                setTab(tabName);
                return;
            }
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override protected void keyTyped(char typedChar, int keyCode) {
        if (keyCode == mc.gameSettings.keyBindInventory.keyCode) {
            closeScreen();
        } else {
            super.keyTyped(typedChar, keyCode);
        }
    }

    private void closeScreen() {
        mc.displayGuiScreen(null);
        mc.setIngameFocus();
    }

    @Override public boolean doesGuiPauseGame() {
        return true;
    }

    // update
    @Override public void updateScreen() {
        TabRegistry.getCurrent().getScroll().tick();
    }

    private void handleMapDragging(TabAbstract tab, int mouseX, int mouseY) {
        boolean insideMap = mouseX >= pane.getMapLeft() && mouseX < pane.getMapLeft() + pane.getMapViewWidth()
                && mouseY >= pane.getMapTop() && mouseY < pane.getMapTop() + pane.getMapViewHeight();

        tab.getScroll().handleDragging(Mouse.isButtonDown(0), insideMap, mouseX, mouseY);
    }

    // drawing: screen
    @Override public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        TabAbstract tab = TabRegistry.getCurrent();

        updateViewModeButtonState(tab);
        updateScrollLimits(tab);
        handleMapDragging(tab, mouseX, mouseY);

        if (showTabBar()) {
            tabBar.update(pane, TabRegistry.TABS, TabRegistry.currentName);
        }

        drawDefaultBackground();
        drawAchievementMap(tab, mouseX, mouseY, partialTicks);

        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        drawTitle(tab);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

    protected void drawTitle(TabAbstract tab) {
        int titleX = pane.getPaneLeft() + BORDER_LEFT + TITLE_OFFSET_X;
        int titleY = pane.getPaneTop() + (BORDER_TOP - fontRenderer.FONT_HEIGHT) / 2;
        fontRenderer.drawString(I18n.getString(tab.getTitle()), titleX, titleY, COLOR_PANE_TITLE);
    }

    protected void drawAchievementMap(TabAbstract tab, int mouseX, int mouseY, float partialTicks) {
        int scrollX = tab.getScroll().getScrollX(partialTicks);
        int scrollY = tab.getScroll().getScrollY(partialTicks);

        int originX = pane.getMapLeft();
        int originY = pane.getMapTop();

        zLevel = 0.0F;
        GL11.glDepthFunc(GL11.GL_GEQUAL);
        GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, 0.0F, -200.0F);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);

        // background, lines and icons all reach halfway under the border
        GuiDraw.enableScissor(mc, width, height, pane.getBackgroundLeft(), pane.getBackgroundTop(), pane.getBackgroundWidth(), pane.getBackgroundHeight());
        AchievementExtended hovered = tab.draw(mc, painter, pane, states, scrollX, scrollY, originX, originY, mouseX, mouseY);
        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        border.draw(pane.getPaneLeft(), pane.getPaneTop(), pane.getPaneWidth(), pane.getPaneHeight(), zLevel);
        if (showTabBar()) {
            tabBar.draw(zLevel);
        }
        GL11.glPopMatrix();

        zLevel = 0.0F;
        GL11.glDepthFunc(GL11.GL_LEQUAL);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);

        super.drawScreen(mouseX, mouseY, partialTicks);

        if (hovered != null) {
            tooltip.draw(hovered.achievement, mouseX, mouseY);
        }
        if (showTabBar()) {
            tabBar.drawTooltip(mouseX, mouseY);
        }

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_LIGHTING);
        RenderHelper.disableStandardItemLighting();
    }

    // gives the util classes access to the drawing methods of Gui they can't reach themselves
    private class Painter implements GuiPainter {

        @Override public void drawRect(int left, int top, int right, int bottom, int color) {
            Gui.drawRect(left, top, right, bottom, color);
        }

        @Override public void drawHorizontalLine(int x1, int x2, int y, int color) {
            GuiAchievementLib.this.drawHorizontalLine(x1, x2, y, color);
        }

        @Override public void drawVerticalLine(int x, int y1, int y2, int color) {
            GuiAchievementLib.this.drawVerticalLine(x, y1, y2, color);
        }

        @Override public void drawGradientRect(int left, int top, int right, int bottom, int colorTop, int colorBottom) {
            GuiAchievementLib.this.drawGradientRect(left, top, right, bottom, colorTop, colorBottom);
        }

        @Override public void drawTexturedModalRect(int x, int y, int u, int v, int width, int height) {
            GuiAchievementLib.this.drawTexturedModalRect(x, y, u, v, width, height);
        }

        @Override public void drawTexturedModelRectFromIcon(int x, int y, Icon icon, int width, int height) {
            GuiAchievementLib.this.drawTexturedModelRectFromIcon(x, y, icon, width, height);
        }
    }


    public AchievementStates getStates() {
        return states;
    }
}
