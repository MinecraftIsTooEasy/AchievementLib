package vbonedra.achievement_lib.core.std.example;

import net.minecraft.AchievementList;
import net.minecraft.Block;

import vbonedra.achievement_lib.core.AchievementExtended;
import vbonedra.achievement_lib.core.tab.TabAbstract;
import vbonedra.achievement_lib.core.std.layout.modern.RenderLayoutModern;
import vbonedra.achievement_lib.core.std.default_tab.RenderBackgroundDefault;

import static vbonedra.achievement_lib.AchievementLibKeyHandler.guiAchievementLib;

public class TabExample extends TabAbstract {

    public static final String NAME = "farming";

    public TabExample() {
        super(NAME, "gui.achievementGui.tab.farming", Block.tilledField.getIcon(1, 0), new RenderBackgroundDefault(), new RenderLayoutModern());

        addAchievement(new AchievementExtended(AchievementList.buildHoe));
        addAchievement(new AchievementExtended(AchievementList.flour));
        addAchievement(new AchievementExtended(AchievementList.makeBread));
        addAchievement(new AchievementExtended(AchievementList.bakeCake));
        addAchievement(new AchievementExtended(AchievementList.buildScythe));
        addAchievement(new AchievementExtended(AchievementList.plantDoctor));
        addAchievement(new AchievementExtended(AchievementList.soilEnrichment));
        addAchievement(new AchievementExtended(AchievementList.makeMycelium));
        addAchievement(new AchievementExtended(AchievementList.supersizeMe));
    }

    @Override
    public boolean isVisible() {
        for (AchievementExtended achievementExtended : getAchievements()) {
            if (guiAchievementLib.getStates().canUnlockAchievement(achievementExtended.achievement) ||
                    guiAchievementLib.getStates().hasAchievementUnlocked(achievementExtended.achievement)) return true;
        }
        return false;
    }
}
