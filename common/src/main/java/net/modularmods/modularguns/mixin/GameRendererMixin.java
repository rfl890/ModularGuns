package net.modularmods.modularguns.mixin;

import net.minecraft.client.renderer.GameRenderer;
import net.modularmods.modularguns.client.events.TickEventsClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Inject(at = @At("HEAD"), method = "render")
    private void render(float f, long l, boolean bl, CallbackInfo info) {
        TickEventsClient.renderTickEvent(f);
    }

}
