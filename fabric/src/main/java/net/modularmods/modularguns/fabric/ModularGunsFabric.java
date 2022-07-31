package net.modularmods.modularguns.fabric;

import net.fabricmc.api.ModInitializer;
import net.modularmods.modularguns.ModularGuns;

public class ModularGunsFabric implements ModInitializer {

    public ModularGuns modularGuns;

    @Override
    public void onInitialize() {
        modularGuns = new ModularGuns();
        modularGuns.init();
        modularGuns.postInit();
    }
}
