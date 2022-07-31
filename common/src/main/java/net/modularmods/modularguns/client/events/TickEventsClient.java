package net.modularmods.modularguns.client.events;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.modularmods.modularguns.client.GunRenderManager;
import net.modularmods.modularguns.client.configs.GunRenderConfig;
import net.modularmods.modularguns.client.mechanics.GunMotion;
import net.modularmods.modularguns.client.mechanics.RenderSmoother;
import net.modularmods.modularguns.common.items.ItemGun;

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
        GunRenderManager.getInstance().getStateMachine().onTickUpdate();
    }

    /**
     * Callback from the RenderTickEvent (Mixin)
     *
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
                    GunRenderManager.getInstance().getStateMachine().update(renderTick);

                    /**
                     * EnhancedGunRendered Updates
                     */
                    GunRenderConfig config = ItemGun.getGunType(player.getMainHandItem()).config;
                    if (GunRenderManager.getInstance().getController() != null) {
                        GunRenderManager.getInstance().getController().compute(config, renderTick);
                    }
                }
            }
        }
    }
}
