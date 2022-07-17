package net.modularmods.modularguns.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.modularmods.modularguns.ModularGuns;

@Mod(ModularGuns.MOD_ID)
public class ModularGunsForge {
    public ModularGunsForge() {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(ModularGuns.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        ModularGuns.init();
    }
}
