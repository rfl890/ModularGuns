package net.modularmods.modularguns.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forgespi.language.IConfigurable;
import net.minecraftforge.forgespi.language.IModFileInfo;
import net.minecraftforge.forgespi.language.IModInfo;
import net.minecraftforge.forgespi.locating.IModFile;
import net.minecraftforge.resource.ResourcePackLoader;
import net.modularmods.modularguns.ModularGuns;

import java.util.List;
import java.util.Map;

@Mod(ModularGuns.MOD_ID)
public class ModularGunsForge {
    public ModularGunsForge() {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(ModularGuns.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        ModularGuns.init();
    }
}
