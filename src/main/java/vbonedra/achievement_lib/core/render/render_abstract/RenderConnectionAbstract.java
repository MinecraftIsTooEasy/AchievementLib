package vbonedra.achievement_lib.core.render.render_abstract;

import vbonedra.achievement_lib.core.AchievementExtended;
import vbonedra.achievement_lib.util.AchievementStates;
import vbonedra.achievement_lib.util.GuiPainter;

public abstract class RenderConnectionAbstract {

    /**
     * Draws line between child and parent
     */
    public abstract void drawConnection(
            GuiPainter guiPainter,
            AchievementStates achievementStates,
            int pass,
            AchievementExtended child,
            AchievementExtended parent,
            boolean is_second_parent,
            int offsetX,
            int offsetY
    );

}
