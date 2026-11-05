package vbonedra.achievement_lib.manylib;

import fi.dy.masa.malilib.config.ConfigTab;
import fi.dy.masa.malilib.config.SimpleConfigs;
import fi.dy.masa.malilib.config.options.*;
import net.minecraft.*;

import java.util.ArrayList;
import java.util.List;

public class AchievementLibConfigs extends SimpleConfigs {
    public static final ConfigBoolean EnableVanillaGuiReplacement = new ConfigBoolean("Enable Vanilla Gui Replacement", true, "Replaces Vanilla Gui with AchievementLib Gui when \"Achievements\" buttons is pressed in pause menu." +
            "\nWARNING: vanilla gui isn't meant to be viewed, it would contain achievements at wrong positions!");

    private static final AchievementLibConfigs Instance;
    public static final List<ConfigBase<?>> GeneralOptions;
    public static final List<ConfigBase<?>> BlocksListOptions = new ArrayList<>();
    public static final List<ConfigBase<?>> Total = new ArrayList<>();
    public static final List<ConfigTab> tabs = new ArrayList<>();

    public AchievementLibConfigs(String name, List<ConfigHotkey> hotkeys, List<ConfigBase<?>> values) {
        super(name, hotkeys, values);
    }

    @Override
    public List<ConfigTab> getConfigTabs() { return tabs; }
    public static AchievementLibConfigs getInstance() { return Instance; }

    static {
        GeneralOptions = List.of(
                EnableVanillaGuiReplacement
        );

        Total.addAll(GeneralOptions);
        Total.addAll(BlocksListOptions);

        tabs.add(new ConfigTab("General", GeneralOptions));

        Instance = new AchievementLibConfigs("AchievementLib", null, Total);
    }
}
