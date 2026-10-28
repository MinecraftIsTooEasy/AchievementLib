package vbonedra.achievement_lib.core.render.render_abstract;

import java.util.List;

import vbonedra.achievement_lib.core.AchievementExtended;
import vbonedra.achievement_lib.util.AchievementStates;
import vbonedra.achievement_lib.util.GuiPainter;

/** Template for drawing the lines between achievements and their parents. */
public abstract class RenderConnectionAbstract {

    // offsetX and offsetY move map pixels to screen pixels, lines connect the x and y of the achievements
    public abstract void draw(GuiPainter painter, AchievementStates states, List<AchievementExtended> achievements, int offsetX, int offsetY);
}
