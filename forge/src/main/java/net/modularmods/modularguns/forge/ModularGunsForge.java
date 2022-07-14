package net.modularmods.modularguns.forge;

import dev.architectury.platform.forge.EventBuses;
import net.modularmods.modularguns.ModularMods;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ModularMods.MOD_ID)
public class ModularGunsForge {
    public ModularGunsForge() {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(ModularMods.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        ModularMods.init();
    }
}
