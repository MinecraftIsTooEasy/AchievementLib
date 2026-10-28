package vbonedra.achievement_lib.register;

import huix.glacier.api.entrypoint.IGameRegistry;
import vbonedra.achievement_lib.core.tab.TabRegistry;
import vbonedra.achievement_lib.core.std.default_tab.TabDefault;
import vbonedra.achievement_lib.core.std.example.TabExample;

public class AchievementLibRegistryInit implements IGameRegistry {

	@Override
	public void onGameRegistry() {
        TabRegistry.register(TabDefault.class);
        TabRegistry.register(TabExample.class);

	}
}
