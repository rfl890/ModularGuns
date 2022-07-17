package net.modularmods.modularguns.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.modularmods.modularguns.ModularGuns;
import net.modularmods.modularguns.client.events.TickEventsClient;
import net.modularmods.modularguns.fabric.client.renderer.GunRendererFabric;

public class ModularGunsFabric implements ModInitializer {

    public ModularGuns modularGuns;

    public static GunRendererFabric gunRenderer = new GunRendererFabric();

    @Override
    public void onInitialize() {
        modularGuns = new ModularGuns();
        modularGuns.init();

        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            TickEventsClient.clientTickStart();
        });
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            TickEventsClient.clientTickEnd();
        });

    }
}
