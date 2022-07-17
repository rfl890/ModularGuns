package net.modularmods.modularguns.client.events;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.modularmods.modularguns.client.renderer.GunRenderer;
import net.modularmods.modularguns.client.renderer.RenderParameters;
import net.modularmods.modularguns.common.items.ItemGun;

import java.lang.reflect.Field;

import static net.modularmods.modularguns.client.renderer.RenderParameters.*;

public class TickEventsClient {

    public static Field f;

    public static Field xRotLast;
    public static Field yRotLast;

    public static void clientTickStart() {
        GUN_ROT_X_LAST = GUN_ROT_X;
        GUN_ROT_Y_LAST = GUN_ROT_Y;
        GUN_ROT_Z_LAST = GUN_ROT_Z;

        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;

        if (player != null) {
            float xRotLast_ = 0, yRotLast_ = 0;

            try {
                xRotLast = LocalPlayer.class.getDeclaredField("xRotLast");
                yRotLast = LocalPlayer.class.getDeclaredField("yRotLast");
                xRotLast.setAccessible(true);
                yRotLast.setAccessible(true);
                xRotLast_ = (float) xRotLast.get(player);
                yRotLast_ = (float) yRotLast.get(player);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }

            if (player.getViewYRot(partialTicks) > yRotLast_) {
                GUN_ROT_X += (player.getViewYRot(partialTicks) - yRotLast_) / 1.5;
            } else if (player.getViewYRot(partialTicks) < yRotLast_) {
                GUN_ROT_X -= (yRotLast_ - player.getViewYRot(partialTicks)) / 1.5;
            }
            if (player.getViewXRot(partialTicks) > xRotLast_) {
                GUN_ROT_Y += (player.getViewXRot(partialTicks) - xRotLast_) / 5;
            } else if (player.getViewXRot(partialTicks) < xRotLast_) {
                GUN_ROT_Y -= (xRotLast_ - player.getViewXRot(partialTicks)) / 5;
            }
        }

        GUN_ROT_X *= .2F;
        GUN_ROT_Y *= .2F;
        GUN_ROT_Z *= .2F;

        if (GUN_ROT_X > 20) {
            GUN_ROT_X = 20;
        } else if (GUN_ROT_X < -20) {
            GUN_ROT_X = -20;
        }

        if (GUN_ROT_Y > 20) {
            GUN_ROT_Y = 20;
        } else if (GUN_ROT_Y < -20) {
            GUN_ROT_Y = -20;
        }
    }

    public static void clientTickEnd() {
        GunRenderer.stateMachine.onTickUpdate();
    }

    public static void renderTickEvent(float partialTicks) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            /**
             * Smooth Render Tick to 60 FPS
             */
            float renderTick = partialTicks;
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

            /**
             * Animation State Machine (Update)
             */
            GunRenderer.stateMachine.onRenderTickUpdate(renderTick);

            /**
             * EnhancedGunRendered Updates
             */
            if (player.getMainHandItem() != null) {
                if (player.getMainHandItem().getItem() instanceof ItemGun) {
                    if (GunRenderer.controller != null) {
                        GunRenderer.controller.onTickRender(renderTick);
                    }
                }
            }
        }
    }
}
