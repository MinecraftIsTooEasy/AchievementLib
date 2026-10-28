package vbonedra.achievement_lib.util;

import net.minecraft.Minecraft;

import static vbonedra.achievement_lib.util.GuiConstants.PULSE_PERIOD_MS;
import static vbonedra.achievement_lib.util.GuiConstants.PULSE_THRESHOLD;

/** Small calculations used all over the gui. */
public final class GuiMath {

    // flashing of achievements that can be unlocked
    public static boolean isPulseHigh() {
        return Math.sin(((double) (Minecraft.getSystemTime() % PULSE_PERIOD_MS) / PULSE_PERIOD_MS) * Math.PI * 2.0) > PULSE_THRESHOLD;
    }

    // keeps the value in [0, 1]
    public static float clamp01(float value) {
        return Math.max(0.0F, Math.min(1.0F, value));
    }

    // keeps the value in [min, max - 1]
    public static int clamp(int value, int min, int max) {
        if (value < min) {
            value = min;
        }
        if (value >= max) {
            value = max - 1;
        }
        return value;
    }

    public static double clamp(double value, int min, int max) {
        if (value < min) {
            value = min;
        }
        if (value >= max) {
            value = max - 1;
        }
        return value;
    }
}
