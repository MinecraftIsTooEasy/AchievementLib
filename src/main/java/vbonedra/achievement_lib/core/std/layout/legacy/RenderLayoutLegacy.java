package vbonedra.achievement_lib.core.std.layout.legacy;

import java.util.List;

import vbonedra.achievement_lib.core.AchievementExtended;
import vbonedra.achievement_lib.core.render.render_abstract.RenderLayoutAbstract;

import static vbonedra.achievement_lib.util.GuiConstants.*;

/** Vanilla placement: every achievement stands on the grid cell given by its col and row. */
public class RenderLayoutLegacy extends RenderLayoutAbstract {

    public RenderLayoutLegacy() {
        renderConnection = new RenderConnectionLegacy();
    }

    @Override
    protected void setAchievementsPositions(List<AchievementExtended> achievements) {
        for (AchievementExtended extended : achievements) {
            extended.x = (int) (extended.col * GRID_SPACING);
            extended.y = (int) (extended.row * GRID_SPACING);
        }
    }

}
