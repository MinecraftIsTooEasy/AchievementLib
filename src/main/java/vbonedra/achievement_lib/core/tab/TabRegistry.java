package vbonedra.achievement_lib.core.tab;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static vbonedra.achievement_lib.AchievementLibMod.ACHIEVEMENT_LIB_MOD_LOGGER;

/** All registered tabs by name in {@code Map<String, TabAbstract> TABS} (in the order they are shown) and the {@code currentName} of tab that is open at the moment. */
public final class TabRegistry {

    public static final Map<String, TabAbstract> TABS = new LinkedHashMap<>();
    public static final Map<String, TabAbstract> TABS_REGISTERED = new LinkedHashMap<>();
    private static final List<Class<? extends TabAbstract>> TABS_TO_REGISTER = new ArrayList<>();
    public static String currentName;

    /**
     * Adds {@code tabClass} to {@code TABS_RO_REGISTER}, which would initialize all tabs later on.
     * {@code tabClass} should NOT have any arguments in constructor, if you want to add {@code TabWithArgs.class},
     * make another {@code TabWithArgsA.class} with empty constructor and make it initialize {@code TabWithArgs.class}
     * with required arguments. Otherwise, tab would be skipped during initialization.
     */
    public static void register(Class<? extends TabAbstract> tabClass) {
        TABS_TO_REGISTER.add(tabClass);
    }

    /**
     * Initializes all {@code tabClass} from {@code TABS_TO_REGISTER}.
     * If {@code clear_initialized_tabs} is true, it would reinitialize all tabs.
     */
    public static void initializeAllTabs(boolean clear_initialized_tabs) {
        if (clear_initialized_tabs) TABS.clear();
        for (Class<? extends TabAbstract> clazz : TABS_TO_REGISTER) {
            try {
                TabAbstract tab = clazz.getDeclaredConstructor().newInstance();

                TABS_REGISTERED.put(tab.getName(), tab);
                if (tab.isVisible()) TABS.put(tab.getName(), tab);
                if (currentName == null) currentName = tab.getName();

            } catch (Exception e) {
                ACHIEVEMENT_LIB_MOD_LOGGER.error(e);
            }
        }
    }

    /**
     * Sets {@code currentName} to {@code name} if it is in {@code TABS}.
     * @return true if {@code name} in {@code TABS}, otherwise false
     */
    public static boolean setCurrent(String name) {
        if (!TABS.containsKey(name)) {
            return false;
        }
        currentName = name;
        return true;
    }

    /**
     * @return {@code TABS.get(currentName)} if it exists, otherwise 1st tab in {@code TABS}. If {@code TABS.isEmpty()} - crashes game
     */
    public static TabAbstract getCurrent() {
        if (currentName == null) {
            if (TABS.isEmpty()) throw new IllegalStateException("No achievement tab is registered!");
            return TABS.entrySet().iterator().next().getValue();
        }
        return TABS.get(currentName);
    }
}
