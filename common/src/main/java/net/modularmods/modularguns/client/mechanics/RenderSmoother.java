package net.modularmods.modularguns.client.mechanics;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.modularmods.modularguns.client.renderer.RenderParameters;

import java.lang.reflect.Field;

public class RenderSmoother {
    public static Field f;

    /**
     * This function process the actual game render partialTicks
     * to a smoothed 60 FPS partialTicks.
     * This allow to have a smooth animation that is computed
     * at a 60 FPS reference
     * @param partialTicks
     * @return
     */
    public static float smooth(float partialTicks){
        float renderTick = partialTicks;
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            /**
             * Smooth Render Tick to 60 FPS
             */
            RenderParameters.partialTicks = renderTick;

            int fps = 0;
            try {
                f = Minecraft.class.getDeclaredField("fps");
                f.setAccessible(true);
                fps = (int) f.get(Minecraft.getInstance());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
            renderTick *= 60d / (double) fps;
        }
        return renderTick;
    }
}
