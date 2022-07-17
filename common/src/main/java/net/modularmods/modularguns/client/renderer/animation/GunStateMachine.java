package net.modularmods.modularguns.client.renderer.animation;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.modularmods.modularguns.client.configs.GunRenderConfig;
import net.modularmods.modularguns.client.renderer.RenderParameters;
import net.modularmods.modularguns.common.types.GunType;

public class GunStateMachine {

    /**
     * RELOAD
     */
    public float reloadTime;
    private ReloadType reloadType;
    public boolean reloading = false;

    /**
     * Recoil
     */
    public float gunRecoil = 0F, lastGunRecoil = 0F;
    public float recoilSide = 0F;
    /**
     * Slide
     */
    public float gunSlide = 0F, lastGunSlide = 0F;

    /**
     * Shoot State Machine
     */
    public boolean shooting = false;
    private float shootTime;
    private float shootProgress = 0f;

    public GunRenderConfig config;

    public void triggerShoot(GunRenderConfig config, GunType gunType, int fireTickDelay) {

        lastGunRecoil = gunRecoil = 1F;
        lastGunSlide = gunSlide = 1F;

        shooting = true;
        shootTime = fireTickDelay;
        recoilSide = (float) (-1F + Math.random() * (1F - (-1F)));
    }

    public void triggerReload(GunRenderConfig config, int reloadTime, ReloadType reloadType) {
        this.reloadTime = reloadType != ReloadType.Full ? reloadTime * 0.65f : reloadTime;
        this.reloadType = reloadType;
        this.reloading = true;
        this.config = config;
    }

    public void onTickUpdate() {
        if (shooting) {
            shootProgress += 1F / shootTime;

            if (shootProgress >= 1F) {
                shooting = false;
                shootProgress = 0f;
            }
        }
        // Recoil
        lastGunRecoil = gunRecoil;
        if (gunRecoil > 0)
            gunRecoil *= 0.5F;
    }

    public void onRenderTickUpdate(float partialTick) {
        if (reloading) {
            /** RELOAD **/
            float reloadSpeed = config.animations.get(AnimationType.RELOAD).speed * partialTick;
            float val = (AnimationController.ADS == 0F) ? AnimationController.RELOAD + reloadSpeed : 0;
            AnimationController.RELOAD = Math.max(0, Math.min(1, val));
            if (AnimationController.RELOAD == 1F) {
                reloading = false;
                AnimationController.RELOAD = 0F;
            }
        }

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
