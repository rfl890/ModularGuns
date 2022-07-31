package net.modularmods.modularguns.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.modularmods.modularguns.client.events.TickEventsClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(Minecraft.class)
public class ClientMixin {
    
    @Inject(at = @At("HEAD"), method = "tick")
    private void onStartTick(CallbackInfo info) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            TickEventsClient.clientTickStart();
        }
    }

    @Inject(at = @At("RETURN"), method = "tick")
    private void onEndTick(CallbackInfo info) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            TickEventsClient.clientTickEnd();
        }
    }
}