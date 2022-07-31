package net.modularmods.modularguns.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.modularmods.modularguns.ModularGuns;

import static net.modularmods.modularguns.ModularGuns.MOD_ID;

@Mod(MOD_ID)
public class ModularGunsForge {

    public ModularGuns modularGuns;

    public ModularGunsForge() {
        modularGuns = new ModularGuns();
        modularGuns.init();
        EventBuses.registerModEventBus(MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        modularGuns.postInit();
    }
}
