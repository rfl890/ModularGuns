package net.modularmods.modularguns.client.renderer.animation;

import net.modularmods.modularguns.client.configs.GunRenderConfig;
import net.modularmods.modularguns.client.mechanics.GunMotion;
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

    public void update(float partialTick) {
        if (reloading) {
            /** RELOAD **/
            float reloadSpeed = config.animations.get(AnimationType.RELOAD).speed * partialTick;
            float val = (AnimationController.ADS == 0F) ? AnimationController.RELOAD + reloadSpeed : 0;
            AnimationController.RELOAD = Math.max(0, Math.min(1, val));
            if (AnimationController.RELOAD >= 1F) {
                reloading = false;
                AnimationController.RELOAD = 0F;
            }
        }

        /**
         * Process gun balancing X left and right
         */
        GunMotion.processGunBalancing(partialTick);
    }

}
