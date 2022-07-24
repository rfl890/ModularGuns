package net.modularmods.modularguns.client.mechanics;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.modularmods.modularguns.client.renderer.RenderParameters;

import java.lang.reflect.Field;

import static net.modularmods.modularguns.client.renderer.RenderParameters.*;
import static net.modularmods.modularguns.client.renderer.RenderParameters.GUN_ROT_Y;

public class GunMotion {

    public static Field xRotLast;
    public static Field yRotLast;

    public static void processMotion(){
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

    public static void processGunBalancing(float partialTick){
        LocalPlayer player = Minecraft.getInstance().player;
        float balancing_speed_x = 0.08f * partialTick;

        if (player.xxa > 0F) {
            RenderParameters.GUN_BALANCING_X = Math.min(1.0F, RenderParameters.GUN_BALANCING_X + balancing_speed_x);
        } else if (player.xxa < 0F) {
            RenderParameters.GUN_BALANCING_X = Math.max(-1.0F, RenderParameters.GUN_BALANCING_X - balancing_speed_x);
        } else if (player.xxa == 0F && RenderParameters.GUN_BALANCING_X != 0F) {
            if (RenderParameters.GUN_BALANCING_X > 0F) {
                RenderParameters.GUN_BALANCING_X = Math.max(0, RenderParameters.GUN_BALANCING_X - balancing_speed_x);
            } else if (RenderParameters.GUN_BALANCING_X < 0F) {
                RenderParameters.GUN_BALANCING_X = Math.min(0, RenderParameters.GUN_BALANCING_X + balancing_speed_x);
            }
        }

        float balancing_speed_y = 0.08f / 2 * partialTick;
        if (player.zza > 0F) {
            RenderParameters.GUN_BALANCING_Y = Math.min((player.isSprinting() ? 3.0F : 1.0F), RenderParameters.GUN_BALANCING_Y + balancing_speed_y);
        } else if (player.zza < 0F) {
            RenderParameters.GUN_BALANCING_Y = Math.max(-1.0F, RenderParameters.GUN_BALANCING_Y - balancing_speed_y);
        } else if (player.zza == 0F && RenderParameters.GUN_BALANCING_Y != 0F) {
            if (RenderParameters.GUN_BALANCING_Y > 0F) {
                RenderParameters.GUN_BALANCING_Y = Math.max(0, RenderParameters.GUN_BALANCING_Y - balancing_speed_y * 2);
            } else if (RenderParameters.GUN_BALANCING_Y < 0F) {
                RenderParameters.GUN_BALANCING_Y = Math.min(0, RenderParameters.GUN_BALANCING_Y + balancing_speed_y * 2);
            }
        }
    }
}
