package vbonedra.achievement_lib.core.tab;

import net.minecraft.Icon;
import net.minecraft.Minecraft;
import vbonedra.achievement_lib.core.AchievementExtended;
import vbonedra.achievement_lib.core.render.render_abstract.RenderBackgroundAbstract;
import vbonedra.achievement_lib.core.render.render_abstract.RenderLayoutAbstract;
import vbonedra.achievement_lib.core.render.RenderPane;
import vbonedra.achievement_lib.util.AchievementStates;
import vbonedra.achievement_lib.util.GuiPainter;

import java.util.ArrayList;
import java.util.List;

/**
 * Tab: achievements, background, layout, scroll position, view mode, etc.
 */
public abstract class TabAbstract {
    private final String name;
    private final String title;
    private final Icon icon;
    private final RenderBackgroundAbstract renderBackground;
    private final RenderLayoutAbstract renderLayout;
    private final List<AchievementExtended> achievements = new ArrayList<>();
    private final TabScroll scroll = new TabScroll();

    // bounds of the placed achievements in map pixels
    private boolean layoutDirty = true;
    private int minX;
    private int minY;
    private int maxX;
    private int maxY;

    /**
     * name tells tabs apart in code, title is the text shown in the gui, icon is read when the tab is drawn
     */
    public TabAbstract(
            String name,
            String title,
            Icon icon,
            RenderBackgroundAbstract renderBackground,
            RenderLayoutAbstract renderLayout
    ) {
        this.name = name;
        this.title = title;
        this.icon = icon;
        this.renderBackground = renderBackground;
        this.renderLayout = renderLayout;
    }

    // returns the achievement under the mouse or null
    public AchievementExtended draw(Minecraft mc, GuiPainter painter, RenderPane pane, AchievementStates states,
                                    int scrollX, int scrollY, int originX, int originY, int mouseX, int mouseY) {
        updateLayout();

        this.renderBackground.draw(mc, painter, pane, scrollX, scrollY, originX, originY, getMaxAchievementDepth());
        return this.renderLayout.draw(mc, painter, pane, states, this.achievements, scrollX, scrollY, originX, originY, mouseX, mouseY);
    }

    public void addAchievement(AchievementExtended achievementExtended) {
        if (achievementExtended != null && !this.achievements.contains(achievementExtended)) {
            this.achievements.add(achievementExtended);
            this.layoutDirty = true;
        }
    }

    // call after changing col or row of an achievement
    public void markLayoutDirty() {
        this.layoutDirty = true;
    }

    // places the achievements again if something changed
    public void updateLayout() {
        if (!this.layoutDirty) {
            return;
        }
        this.layoutDirty = false;
        this.renderLayout.layout(this.achievements);

        if (this.achievements.isEmpty()) {
            this.minX = this.minY = this.maxX = this.maxY = 0;
            return;
        }
        this.minX = Integer.MAX_VALUE;
        this.minY = Integer.MAX_VALUE;
        this.maxX = Integer.MIN_VALUE;
        this.maxY = Integer.MIN_VALUE;
        for (AchievementExtended extended : this.achievements) {
            this.minX = Math.min(this.minX, extended.x);
            this.minY = Math.min(this.minY, extended.y);
            this.maxX = Math.max(this.maxX, extended.x);
            this.maxY = Math.max(this.maxY, extended.y);
        }
    }

    // the achievement the map is centered on when the tab is opened first: the first one without a parent in this tab
    public AchievementExtended getRootAchievement() {
        updateLayout();
        for (AchievementExtended extended : this.achievements) {
            if (extended.parent == null) {
                return extended;
            }
        }
        return null;
    }

    // if tab must be shown in tab bar
    public boolean isVisible() {
        return true;
    }


    // how far down and to the right the achievements reach, main use is in renderBackground
    public int getMaxAchievementDepth() {
        updateLayout();
        return this.maxY;
    }
    public int getMaxAchievementWidth() {
        updateLayout();
        return this.maxX;
    }

    // getters
    public String getName() {
        return this.name;
    }
    public String getTitle() {
        return this.title;
    }
    public Icon getIcon() {
        return this.icon;
    }
    public RenderBackgroundAbstract getRenderBackground() {
        return this.renderBackground;
    }
    public RenderLayoutAbstract getRenderLayout() {
        return this.renderLayout;
    }
    public List<AchievementExtended> getAchievements() {
        return this.achievements;
    }
    public TabScroll getScroll() {
        return this.scroll;
    }
    public int getMinX() {
        updateLayout();
        return this.minX;
    }
    public int getMinY() {
        updateLayout();
        return this.minY;
    }
    public int getMaxX() {
        updateLayout();
        return this.maxX;
    }
    public int getMaxY() {
        updateLayout();
        return this.maxY;
    }
}
