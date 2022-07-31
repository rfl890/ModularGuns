package net.modularmods.modularguns.client;

import net.modularmods.modularguns.client.renderer.animation.AnimationController;
import net.modularmods.modularguns.client.renderer.animation.GunStateMachine;

public class GunRenderManager {

    public static GunRenderManager instance;

    private AnimationController controller;
    private GunStateMachine stateMachine;

    public GunRenderManager() {
        instance = this;
        controller = new AnimationController();
        stateMachine = new GunStateMachine();
    }

    public static GunRenderManager getInstance() {
        return instance;
    }

    public AnimationController getController() {
        return controller;
    }

    public GunStateMachine getStateMachine() {
        return stateMachine;
    }
}
