package vbonedra.achievement_lib;

import moddedmite.rustedironcore.api.event.Handlers;
import moddedmite.rustedironcore.api.event.listener.IKeybindingListener;
import moddedmite.rustedironcore.api.event.listener.ITickListener;
import moddedmite.rustedironcore.keybinding.KeyBindingExtra;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.Minecraft;
import net.minecraft.KeyBinding;
import net.minecraft.EntityPlayer;
import org.lwjgl.input.Keyboard;
import vbonedra.achievement_lib.core.GuiAchievementLib;

import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public class AchievementLibKeyHandler {
    public static KeyBindingExtra achievementGuiKeyBind;
    public static GuiAchievementLib guiAchievementLib;

    public static void register() {
        Handlers.Keybinding.register(new IKeybindingListener() {
            @Override
            public void onKeybindingRegister(Consumer<KeyBinding> registry) {
                achievementGuiKeyBind = new KeyBindingExtra(
                        "key.achievement_lib.open_gui.name",
                        Keyboard.KEY_L,
                        "key.achievement_lib.category.name"
                );
                registry.accept(achievementGuiKeyBind);
            }
        });
        Handlers.Tick.register(new ITickListener() {
            @Override
            public void onClientTick(Minecraft client) {
                onKeyInput();
            }
        });
    }

    public static void onKeyInput() {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.thePlayer;
        if (player == null) return;
        if (achievementGuiKeyBind != null && achievementGuiKeyBind.isPressed()) {
            mc.displayGuiScreen(null);


            guiAchievementLib = new GuiAchievementLib(mc.statFileWriter);
            mc.displayGuiScreen(guiAchievementLib);
        }
    }
}
