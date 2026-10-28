package vbonedra.achievement_lib.util;

/** The directions a modern tree can grow in. */
public final class TreeDirection {

    public static final int RIGHT = 0;  // root on the left, tree grows to the right
    public static final int LEFT = 1;   // root on the right, tree grows to the left
    public static final int DOWN = 2;   // root at the top, tree grows downwards
    public static final int UP = 3;     // root at the bottom, tree grows upwards

    private TreeDirection() {
    }

    public static boolean isHorizontal(int direction) {
        return direction == RIGHT || direction == LEFT;
    }

    // -1 if the tree grows towards smaller coordinates
    public static int getSign(int direction) {
        return direction == LEFT || direction == UP ? -1 : 1;
    }
}
