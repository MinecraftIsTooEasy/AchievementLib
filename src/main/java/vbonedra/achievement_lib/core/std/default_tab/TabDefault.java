package vbonedra.achievement_lib.core.std.default_tab;

import net.minecraft.Achievement;
import net.minecraft.AchievementList;
import net.minecraft.Block;

import vbonedra.achievement_lib.core.AchievementExtended;
import vbonedra.achievement_lib.core.tab.TabAbstract;
import vbonedra.achievement_lib.core.std.layout.legacy.RenderLayoutLegacy;

import java.util.HashSet;
import java.util.Set;

/**
 * The vanilla achievement page: every achievement of AchievementList at its vanilla position, with vanilla connections and background.
 * WARNING: This tab isn't meant to be extended!
 */
public class TabDefault extends TabAbstract {

    public static final String NAME = "default";

    public static Set<Achievement> achievementsExcluded = new HashSet<>();

    public TabDefault() {
        super(NAME, "gui.tab.default.title", Block.grass.getIcon(2, 0), new RenderBackgroundDefault(), new RenderLayoutLegacy());

        for (Object entry : AchievementList.achievementList) {
            Achievement achievement = (Achievement) entry;
            if (!achievementsExcluded.contains(achievement)) {
                addAchievement(new AchievementExtended(achievement, achievement.displayColumn, achievement.displayRow));
            }
        }

    }

    public static void excludeAchievement(Achievement achievement) {
        achievementsExcluded.add(achievement);
    }

    public static void unexcludeAchievement(Achievement achievement) {
        achievementsExcluded.remove(achievement);
    }

}
