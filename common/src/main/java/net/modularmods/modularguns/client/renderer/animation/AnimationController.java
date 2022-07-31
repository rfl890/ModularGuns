package net.modularmods.modularguns.client.renderer.animation;

import net.minecraft.client.Minecraft;
import net.modularmods.modularguns.client.GunRenderManager;
import net.modularmods.modularguns.client.configs.GunRenderConfig;

public class AnimationController {

    private ActionPlayback playback;

    public static float DRAW;
    public static float ADS;
    public static float RELOAD;
    public static float SPRINT;
    public static float INSPECT;

    public static int oldCurrentItem;

    public AnimationController() {
        this.playback = new ActionPlayback();
        this.playback.action = AnimationType.DEFAULT;
    }

    public void compute(GunRenderConfig config, float partialTick) {
        GunStateMachine anim = GunRenderManager.getInstance().getStateMachine();
        if (config == null) return;

        /** DRAW **/
        float drawSpeed = config.animations.get(AnimationType.DRAW).speed * partialTick;
        DRAW = Math.max(0, Math.min(1F, DRAW + drawSpeed));

        /** INSPECT **/
        float inspectSpeed = config.animations.get(AnimationType.INSPECT).speed * partialTick;
        float valInspect = (Minecraft.getInstance().player.isShiftKeyDown()) ? INSPECT + inspectSpeed : 0;
        INSPECT = Math.max(0F, Math.min(1F, valInspect));

        /** ADS **/
        boolean aimChargeMisc = anim.reloading;
        float adsSpeed = config.animations.get(AnimationType.AIM_IN).speed * partialTick;
        float val = (Minecraft.getInstance().mouseHandler.isRightPressed() && !aimChargeMisc) ? ADS + adsSpeed : ADS - adsSpeed;
        ADS = Math.max(0, Math.min(1, val));

        /** SPRINT **/
        float sprintSpeed = 0.15f * partialTick;
        float sprintValue = (Minecraft.getInstance().player.isSprinting()) ? SPRINT + sprintSpeed : SPRINT - sprintSpeed;
        if (anim.gunRecoil > 0.1F) {
            sprintValue = SPRINT - sprintSpeed * 3f;
        }

        SPRINT = Math.max(0, Math.min(1, sprintValue));

        if (DRAW > 0F && DRAW < 1F && (oldCurrentItem != Minecraft.getInstance().player.getInventory().selected)) {
            this.playback.action = AnimationType.DRAW;
        } else if (ADS > 0F && Minecraft.getInstance().mouseHandler.isRightPressed()) {
            this.playback.action = AnimationType.AIM_IN;
        } else if (this.playback.action == AnimationType.AIM_IN && this.playback.hasPlayed && !Minecraft.getInstance().mouseHandler.isRightPressed()) {
            this.playback.action = AnimationType.AIM_OUT;
        } else if (RELOAD > 0F) {
            this.playback.action = AnimationType.RELOAD;
        } else if (INSPECT > 0F) {
            this.playback.action = AnimationType.INSPECT;
        } else if (this.playback.hasPlayed) {
            this.playback.action = AnimationType.DEFAULT;
        }

        if (oldCurrentItem != Minecraft.getInstance().player.getInventory().selected) {
            DRAW = 0;
            oldCurrentItem = Minecraft.getInstance().player.getInventory().selected;
        }

        updateTime(config);
    }


    public void updateTime(GunRenderConfig config) {
        switch (this.playback.action) {
            case DEFAULT:
                this.playback.time = config.animations.get(AnimationType.DEFAULT).getStartTime();
                break;
            case DRAW:
                this.playback.updateTime(config, DRAW);
                if (this.playback.hasPlayed) {
                    oldCurrentItem = Minecraft.getInstance().player.getInventory().selected;
                    DRAW = 0F;
                }
                break;
            case AIM_IN:
                this.playback.updateTime(config, ADS);
                break;
            case AIM_OUT:
                this.playback.updateTime(config, 1F - ADS);
                break;
            case RELOAD:
                this.playback.updateTime(config, RELOAD);
                break;
            case INSPECT:
                this.playback.updateTime(config, INSPECT);
                break;

        }
    }

    public float getTime() {
        return playback.time;
    }
}
