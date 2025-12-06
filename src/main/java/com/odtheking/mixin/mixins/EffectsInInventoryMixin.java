package com.odtheking.mixin.mixins;

import com.odtheking.odin.features.impl.render.RenderOptimizer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.EffectsInInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EffectsInInventory.class)
public class EffectsInInventoryMixin {
    @Inject(method = "renderEffects(Lnet/minecraft/client/gui/GuiGraphics;II)V", at = @At("HEAD"), cancellable = true)
    private void odin$onRenderEffects(GuiGraphics context, int mx, int my, CallbackInfo callbackInfo) {
        if (RenderOptimizer.getShouldHideStatusEffects()) callbackInfo.cancel();
    }
}