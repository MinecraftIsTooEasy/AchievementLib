package vbonedra.achievement_lib;

import fi.dy.masa.malilib.config.ConfigManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import vbonedra.achievement_lib.event.AchievementLibEvent;
import net.fabricmc.api.ModInitializer;

import net.xiaoyu233.fml.ModResourceManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vbonedra.achievement_lib.manylib.AchievementLibConfigs;

public class AchievementLibMod implements ModInitializer {
    public static final String MOD_ID = "achievement_lib";
    
    public static final Logger ACHIEVEMENT_LIB_MOD_LOGGER = LogManager.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ModResourceManager.addResourcePackDomain(MOD_ID);
        AchievementLibEvent.register();
        registerKeybinding();

        AchievementLibConfigs.getInstance().load();
        ConfigManager.getInstance().registerConfig(AchievementLibConfigs.getInstance());
    }


    @Environment(EnvType.CLIENT)
    private void registerKeybinding() {
        AchievementLibKeyHandler.register();
    }
}
// commit
/*

*/

// TODO:
//  - item subtypes: there must be helper class that would register achievement with specific item subtype. such class is meant to be used by other mods
//  - implement alternative solution for "enchantment glint render leak" issue (right now its just turning off enchantment glint)
//  - achievement depth and ability to hide achievements too far from player's progression

// notes/plans:
/*
- also theres some room for improvement in achievement lib, like i would like to add new register functions
    for achievements, so it would be possible to register achievements with icons having item subtypes
- also ild like to add functions like "move all achievements starting from this one
    to the left by N positions" (not sure if its a good idea though, cause what if 2 mods do it at the same time hmmm...)
- also, not sure if its a good idea or not, but mouse wheel scrolling changing scale of renderPane sounds cool
- also glowing background icons would be cool, so if i add lava i would be able to make it glow
- and minimal/maximal size of gui
*/
