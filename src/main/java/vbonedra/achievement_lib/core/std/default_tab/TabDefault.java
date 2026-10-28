package vbonedra.achievement_lib.core.std.default_tab;

import net.minecraft.Achievement;
import net.minecraft.AchievementList;
import net.minecraft.Block;

import vbonedra.achievement_lib.core.AchievementExtended;
import vbonedra.achievement_lib.core.tab.TabAbstract;
import vbonedra.achievement_lib.core.std.layout.legacy.RenderLayoutLegacy;

/** The vanilla achievement page: every achievement of AchievementList at its vanilla position, with vanilla lines and background. */
public class TabDefault extends TabAbstract {

    public static final String NAME = "default";

    public TabDefault() {
        super(NAME, "gui.tab.default.title", Block.grass.getIcon(2, 0), new RenderBackgroundDefault(), new RenderLayoutLegacy());

        for (Object entry : AchievementList.achievementList) {
            Achievement achievement = (Achievement) entry;
            addAchievement(new AchievementExtended(achievement, achievement.displayColumn, achievement.displayRow));
        }

    }

}
