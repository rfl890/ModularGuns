package net.modularmods.modularguns.client.configs;

import com.mojang.math.Vector3f;
import net.modularmods.modularguns.client.renderer.animation.AnimationType;

import java.util.HashMap;

public class GunRenderConfig {

    public String modelFileName = "";

    public HashMap<AnimationType, Animation> animations = new HashMap<>();

    public Sprint sprint = new Sprint();
    public Aim aim = new Aim();
    public Extra extra = new Extra();

    public static class Animation {
        public float startTime;
        public float endTime;
        public float speed;

        public float getStartTime() {
            return startTime * 0.041666667597f;
        }

        public float getEndTime() {
            return endTime * 0.041666667597f;
        }
    }

    public static class Sprint {
        public Vector3f sprintRotate = new Vector3f(-20.0F, 30.0F, -0.0F);
        public Vector3f sprintTranslate = new Vector3f(0.5F, -0.10F, -0.65F);
    }

    public static class Aim {

        //Advanced configuration - Allows you to change how the gun is held without effecting the sight alignment
        public Vector3f rotateHipPosition = new Vector3f(0F, 0F, 0F);
        //Advanced configuration - Allows you to change how the gun is held without effecting the sight alignment
        public Vector3f translateHipPosition = new Vector3f(0F, 0F, 0F);
        //Advanced configuration - Allows you to change how the gun is held while aiming
        public Vector3f rotateAimPosition = new Vector3f(0F, 0F, 0F);
        //Advanced configuration - Allows you to change how the gun is held while aiming
        public Vector3f translateAimPosition = new Vector3f(0F, 0F, 0F);
    }

    public static class Extra {
        public Vector3f translateAll = new Vector3f(0F, 0F, 0F);

        /**
         * Adds backwards recoil translations to the gun staticModel when firing
         */
        public float modelRecoilBackwards = 0.15F;
        /**
         * Adds upwards/downwards recoil translations to the gun staticModel when firing
         */
        public float modelRecoilUpwards = 1.0F;
        /**
         * Adds a left-right staticModel shaking motion when firing, default 0.5
         */
        public float modelRecoilShake = 0.5F;
    }
}
