package vbonedra.achievement_lib.core.std.default_tab;

import net.minecraft.Icon;
import vbonedra.achievement_lib.core.AchievementExtended;
import vbonedra.achievement_lib.core.render.render_abstract.RenderBackgroundAbstract;
import vbonedra.achievement_lib.core.render.render_abstract.RenderLayoutAbstract;
import vbonedra.achievement_lib.core.tab.TabAbstract;

/**
 * This class is same as {@code TabAbstract.class}, but {@code addAchievement()} method also excludes achievement from {@code TabDefault.class}
 */
public class TabStandard extends TabAbstract {

    public TabStandard(String name, String title, Icon icon, RenderBackgroundAbstract renderBackground, RenderLayoutAbstract renderLayout) {
        super(name, title, icon, renderBackground, renderLayout);
    }

    @Override
    public void addAchievement(AchievementExtended achievementExtended) {
        super.addAchievement(achievementExtended);
        TabDefault.excludeAchievement(achievementExtended.achievement);
    }
}
