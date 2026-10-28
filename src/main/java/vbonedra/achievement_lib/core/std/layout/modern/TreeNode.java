package vbonedra.achievement_lib.core.std.layout.modern;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import vbonedra.achievement_lib.core.AchievementExtended;

import static vbonedra.achievement_lib.util.GuiConstants.*;

/** Node of the tree layout: places an achievement and its branch as close together as the branches allow. */
public final class TreeNode {

    private final AchievementExtended achievement;
    private final List<TreeNode> children = new ArrayList<>();
    private final int column;
    private float row;
    private int weight;             // number of branch ends below this achievement
    private float relativeRow;      // row relative to the parent
    private float[] contourTop;     // per depth below this node: topmost row, relative to this node
    private float[] contourBottom;  // per depth below this node: lowest row, relative to this node

    public TreeNode(AchievementExtended achievement, int column, Map<AchievementExtended, List<AchievementExtended>> childrenOf) {
        this.achievement = achievement;
        this.column = column;

        List<AchievementExtended> childAchievements = childrenOf.get(achievement);
        if (childAchievements != null) {
            for (AchievementExtended child : childAchievements) {
                children.add(new TreeNode(child, column + 1, childrenOf));
            }
        }
    }

    public void layout() {
        computeWeight();
        if (TREE_HEAVIEST_IN_MIDDLE) {
            arrangeChildren();
        }

        pack();
        assignRows(0.0F);
        addRowOffset(-findMinRow());
    }

    // writes col (tree level) and row of the whole branch, returns the lowest row
    public float collect(float rowOffset) {
        achievement.col = column;
        achievement.row = row + rowOffset;

        float maxRow = achievement.row;
        for (TreeNode child : children) {
            maxRow = Math.max(maxRow, child.collect(rowOffset));
        }
        return maxRow;
    }

    private int computeWeight() {
        if (children.isEmpty()) {
            weight = 1;
            return weight;
        }

        weight = 0;
        for (TreeNode child : children) {
            weight += child.computeWeight();
        }
        return weight;
    }

    private void arrangeChildren() {
        List<TreeNode> sorted = new ArrayList<TreeNode>(children);
        sorted.sort((first, second) -> Integer.compare(second.weight, first.weight));

        // biggest branch in the centre, next ones alternately above and below it
        children.clear();
        for (int i = 0; i < sorted.size(); i++) {
            if (i % 2 == 0) {
                children.add(sorted.get(i));
            } else {
                children.add(0, sorted.get(i));
            }
        }

        for (TreeNode child : children) {
            child.arrangeChildren();
        }
    }

    // places every child as close to its previous siblings as the outlines of their branches allow
    private void pack() {
        for (TreeNode child : children) {
            child.pack();
        }

        if (children.isEmpty()) {
            contourTop = new float[]{0.0F};
            contourBottom = new float[]{0.0F};
            return;
        }

        int depth = 0;
        for (TreeNode child : children) {
            depth = Math.max(depth, child.contourTop.length);
        }

        float[] usedBottom = new float[depth];
        Arrays.fill(usedBottom, Float.NEGATIVE_INFINITY);

        for (int i = 0; i < children.size(); i++) {
            TreeNode child = children.get(i);

            float position = 0.0F;
            if (i > 0) {
                position = Float.NEGATIVE_INFINITY;
                for (int d = 0; d < child.contourTop.length; d++) {
                    if (usedBottom[d] == Float.NEGATIVE_INFINITY) {
                        continue;
                    }
                    float distance = TREE_MIN_ROW_DISTANCE + (d > 0 ? TREE_BRANCH_GAP : 0.0F);
                    position = Math.max(position, usedBottom[d] + distance - child.contourTop[d]);
                }
            }

            child.relativeRow = position;
            for (int d = 0; d < child.contourBottom.length; d++) {
                usedBottom[d] = Math.max(usedBottom[d], position + child.contourBottom[d]);
            }
        }

        // parent stands between its first and last child
        float middle = (children.get(0).relativeRow + children.get(children.size() - 1).relativeRow) / 2.0F;
        for (TreeNode child : children) {
            child.relativeRow -= middle;
        }

        contourTop = new float[depth + 1];
        contourBottom = new float[depth + 1];
        for (int d = 0; d < depth; d++) {
            float top = Float.POSITIVE_INFINITY;
            float bottom = Float.NEGATIVE_INFINITY;
            for (TreeNode child : children) {
                if (d < child.contourTop.length) {
                    top = Math.min(top, child.relativeRow + child.contourTop[d]);
                    bottom = Math.max(bottom, child.relativeRow + child.contourBottom[d]);
                }
            }
            contourTop[d + 1] = top;
            contourBottom[d + 1] = bottom;
        }
    }

    private void assignRows(float parentRow) {
        row = parentRow;
        for (TreeNode child : children) {
            child.assignRows(parentRow + child.relativeRow);
        }
    }

    private float findMinRow() {
        float minRow = row;
        for (TreeNode child : children) {
            minRow = Math.min(minRow, child.findMinRow());
        }
        return minRow;
    }

    private void addRowOffset(float offset) {
        row += offset;
        for (TreeNode child : children) {
            child.addRowOffset(offset);
        }
    }
}
