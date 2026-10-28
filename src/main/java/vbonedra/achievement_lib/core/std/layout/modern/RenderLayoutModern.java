package vbonedra.achievement_lib.core.std.layout.modern;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vbonedra.achievement_lib.core.AchievementExtended;
import vbonedra.achievement_lib.core.render.render_abstract.RenderConnectionAbstract;
import vbonedra.achievement_lib.core.render.render_abstract.RenderLayoutAbstract;
import vbonedra.achievement_lib.util.TreeDirection;

import static vbonedra.achievement_lib.util.GuiConstants.*;

/** Modern placement: the achievements are arranged as a tree, col and row are set by the layout. */
public class RenderLayoutModern extends RenderLayoutAbstract {

    private static final int DIRECTION = TreeDirection.RIGHT;                                           // direction the tree grows in
    private static final RenderConnectionAbstract CONNECTION = new RenderConnectionModern(DIRECTION);   // change this to draw other lines

    public RenderLayoutModern() {
        super(CONNECTION);
    }

    @Override
    protected void place(List<AchievementExtended> achievements) {
        // achievements without a parent in this tab are roots, the others hang below their parent
        Map<AchievementExtended, List<AchievementExtended>> childrenOf = new HashMap<>();
        List<AchievementExtended> roots = new ArrayList<>();
        for (AchievementExtended extended : achievements) {
            if (extended.parent == null) {
                roots.add(extended);
            } else {
                childrenOf.computeIfAbsent(extended.parent, key -> new ArrayList<>()).add(extended);
            }
        }

        // every root gets its own tree, trees are stacked below each other
        float rowOffset = 0.0F;
        for (AchievementExtended root : roots) {
            TreeNode node = new TreeNode(root, 0, childrenOf);
            node.layout();
            float lastRow = node.collect(rowOffset);
            rowOffset = lastRow + 1.0F + TREE_ROOT_GAP;
        }

        toPixels(achievements);
    }

    // col is the tree level, row the place inside the level, the direction decides which one is x and which is y
    private void toPixels(List<AchievementExtended> achievements) {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;

        for (AchievementExtended extended : achievements) {
            if (TreeDirection.isHorizontal(DIRECTION)) {
                extended.x = (int) (TreeDirection.getSign(DIRECTION) * extended.col * TREE_LEVEL_SPACING);
                extended.y = (int) Math.floor(extended.row * TREE_SIBLING_SPACING_Y);
            } else {
                extended.x = (int) Math.floor(extended.row * TREE_SIBLING_SPACING_X);
                extended.y = (int) (TreeDirection.getSign(DIRECTION) * extended.col * TREE_LEVEL_SPACING);
            }
            minX = Math.min(minX, extended.x);
            minY = Math.min(minY, extended.y);
        }

        // the top left achievement is at 0, 0
        for (AchievementExtended extended : achievements) {
            extended.x -= minX;
            extended.y -= minY;
        }
    }
}
