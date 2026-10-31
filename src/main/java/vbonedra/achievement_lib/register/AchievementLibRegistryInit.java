package vbonedra.achievement_lib.register;

import huix.glacier.api.entrypoint.IGameRegistry;
import vbonedra.achievement_lib.core.tab.TabRegistry;
import vbonedra.achievement_lib.core.std.default_tab.TabDefault;

public class AchievementLibRegistryInit implements IGameRegistry {

	@Override
	public void onGameRegistry() {
        TabRegistry.register(TabDefault.class);

	}
}
