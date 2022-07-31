package net.modularmods.modularguns.client.renderer.animation;

import net.modularmods.modularguns.client.configs.GunRenderConfig;
import net.modularmods.modularguns.utils.maths.Interpolation;

public class ActionPlayback {

    public AnimationType action;

    public float time;

    public boolean hasPlayed;

    public ActionPlayback() {
    }

    public void updateTime(GunRenderConfig config, float alpha) {
        float startTime = config.animations.get(action).getStartTime();
        float endTime = config.animations.get(action).getEndTime();
        this.time = Interpolation.LINEAR.interpolate(startTime, endTime, alpha);

        if (this.time >= endTime) {
            this.hasPlayed = true;
        } else {
            this.hasPlayed = false;
        }
    }
}
