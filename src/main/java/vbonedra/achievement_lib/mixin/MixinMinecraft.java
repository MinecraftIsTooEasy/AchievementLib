package vbonedra.achievement_lib.mixin;

import net.minecraft.GuiAchievements;
import net.minecraft.GuiScreen;
import net.minecraft.Minecraft;
import net.minecraft.StatFileWriter;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import vbonedra.achievement_lib.core.GuiAchievementLib;

/** Replaces the vanilla achievements screen with GuiAchievementLib, wherever the game opens it. */
@Mixin(Minecraft.class)
public class MixinMinecraft {

    @Shadow public StatFileWriter statFileWriter;

    @ModifyVariable(method = "displayGuiScreen", at = @At("HEAD"), argsOnly = true)
    private GuiScreen achievement_lib$replaceAchievementsGui(GuiScreen screen) {
        if (screen instanceof GuiAchievements) {
            return new GuiAchievementLib(this.statFileWriter);
        }
        return screen;
    }
}