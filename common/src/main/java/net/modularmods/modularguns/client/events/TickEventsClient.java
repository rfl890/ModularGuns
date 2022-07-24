package net.modularmods.modularguns.client.events;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.modularmods.modularguns.client.mechanics.GunMotion;
import net.modularmods.modularguns.client.mechanics.RenderSmoother;
import net.modularmods.modularguns.client.renderer.GunRenderer;
import net.modularmods.modularguns.client.renderer.RenderParameters;
import net.modularmods.modularguns.common.items.ItemGun;

import java.lang.reflect.Field;

import static net.modularmods.modularguns.client.renderer.RenderParameters.*;

public class TickEventsClient {

    /**
     * Callback from the event ClientTickStart
     */
    public static void clientTickStart() {
        /**
         * Player's gun movements
         */
        GunMotion.processMotion();
    }

    /**
     * Callback from the event ClientTickEnd
     */
    public static void clientTickEnd() {
        GunRenderer.stateMachine.onTickUpdate();
    }

    /**
     * Callback from the RenderTickEvent (Mixin)
     * @param partialTicks
     */
    public static void renderTickEvent(float partialTicks) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            if (player.getMainHandItem() != null) {
                if (player.getMainHandItem().getItem() instanceof ItemGun) {
                    /**
                     * Smooth the actual render partialTicks to a smoothed
                     * 60 FPS render compute
                     */
                    float renderTick = RenderSmoother.smooth(partialTicks);

                    /**
                     * Animation State Machine (Update)
                     */
                    GunRenderer.stateMachine.update(renderTick);

                    /**
                     * EnhancedGunRendered Updates
                     */
                    if (GunRenderer.controller != null) {
                        GunRenderer.controller.compute(renderTick);
                    }
                }
            }
        }
    }
}
