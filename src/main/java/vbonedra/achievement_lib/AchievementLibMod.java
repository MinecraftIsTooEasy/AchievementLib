package vbonedra.achievement_lib;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import vbonedra.achievement_lib.event.AchievementLibEvent;
import net.fabricmc.api.ModInitializer;

import net.xiaoyu233.fml.ModResourceManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AchievementLibMod implements ModInitializer {
    public static final String MOD_ID = "achievement_lib";
    
    public static final Logger ACHIEVEMENT_LIB_MOD_LOGGER = LogManager.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ModResourceManager.addResourcePackDomain(MOD_ID);
        AchievementLibEvent.register();
        registerKeybinding();
    }


    @Environment(EnvType.CLIENT)
    private void registerKeybinding() {
        AchievementLibKeyHandler.register();
    }
}
