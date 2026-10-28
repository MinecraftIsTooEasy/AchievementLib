package vbonedra.achievement_lib.core;

import net.minecraft.Achievement;

/** An extended achievement for use in tabs without affecting real achievement. */
public class AchievementExtended {

    public final Achievement achievement;

    // place in grid cells: input of the legacy layout, written by the modern layout
    public float col;
    public float row;

    // place on the map in pixels, written by the layout, used for drawing
    public int x;
    public int y;

    // parents inside the same tab, set by the layout, null if the parent is in another tab
    public AchievementExtended parent;
    public AchievementExtended secondParent;

    public AchievementExtended(Achievement achievement) {
        this(achievement, 0, 0);
    }

    public AchievementExtended(Achievement achievement, float col, float row) {
        this.achievement = achievement;
        this.col = col;
        this.row = row;
    }
}
