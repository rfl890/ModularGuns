package net.modularmods.modularguns.client;

import net.modularmods.modularguns.client.handlers.SmoothSwingTicker;
import net.modularmods.modularguns.common.CommonHandler;

public class ClientHandler implements CommonHandler {

    private GunRenderManager gunRenderManager;

    @Override
    public void init() {
        //Smooth Swing Ticker Runnable
        SmoothSwingTicker smoothSwingTicker = new SmoothSwingTicker();
        Thread smoothTickThread = new Thread(smoothSwingTicker, "SmoothSwingThread");
        smoothTickThread.start();

        gunRenderManager = new GunRenderManager();
    }

    public GunRenderManager getGunRenderManager() {
        return gunRenderManager;
    }
}
