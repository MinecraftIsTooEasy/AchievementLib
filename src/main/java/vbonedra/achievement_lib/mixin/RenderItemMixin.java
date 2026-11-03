package vbonedra.achievement_lib.mixin;

import net.minecraft.Minecraft;
import net.minecraft.GuiAchievements;
import net.minecraft.RenderItem;
import net.minecraft.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import vbonedra.achievement_lib.core.GuiAchievementLib;

@Mixin(RenderItem.class)
public class RenderItemMixin {

    // enchantments break render in gui, that's a quick fix which removes enchantments in all achievement related gui
    @Redirect(
            method = "renderItemAndEffectIntoGUI(Lnet/minecraft/FontRenderer;Lnet/minecraft/TextureManager;Lnet/minecraft/ItemStack;II)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/ItemStack;hasEffect()Z")
    )
    private boolean renderItemAndEffectIntoGUI_removeEnchantmentGlint(ItemStack itemStack) {
        Minecraft mc = Minecraft.getMinecraft();

        if (mc != null
                && (mc.currentScreen instanceof GuiAchievements
                || mc.currentScreen instanceof GuiAchievementLib
                || mc.currentScreen.getClass().getSimpleName().toLowerCase().contains("achievement")
                || mc.currentScreen.getClass().getSimpleName().toLowerCase().contains("advancement")
        )
        ) {
            return false;
        }

        return itemStack.hasEffect();
    }
}
