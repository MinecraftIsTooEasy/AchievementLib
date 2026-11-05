package vbonedra.achievement_lib.util;

import net.minecraft.*;

import java.lang.reflect.Field;

import static vbonedra.achievement_lib.AchievementLibMod.ACHIEVEMENT_LIB_MOD_LOGGER;

public class AchievementRegisterHelper {

    private static Field itemStackField;

    static {
        try {
            itemStackField = Achievement.class.getDeclaredField("theItemStack");
            itemStackField.setAccessible(true);
        } catch (Exception e) {
            ACHIEVEMENT_LIB_MOD_LOGGER.error("Failed to initialize AchievementRegisterHelper reflection:", e);
        }
    }

    public static void replaceAchievementItemStack(Achievement achievement, ItemStack itemStack) {
        if (itemStackField == null) {
            ACHIEVEMENT_LIB_MOD_LOGGER.warn("Failed to replace itemStack of {} with {}[subtype={}]: {}",
                    achievement.getName(),
                    itemStack.getItem().getUnlocalizedName(),
                    itemStack.getItemSubtype(),
                    "itemStackField is null, maybe it wasn't initialized?"
            );
        }
        try {
            itemStackField.set(achievement, itemStack);
        } catch (Exception e) {
            ACHIEVEMENT_LIB_MOD_LOGGER.warn("Failed to replace itemStack of {} with {}[subtype={}]: {}",
                    achievement.getName(),
                    itemStack.getItem().getUnlocalizedName(),
                    itemStack.getItemSubtype(),
                    String.valueOf(e)
            );
        }
    }

}
