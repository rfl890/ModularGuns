package net.modularmods.modularguns;


import com.google.common.base.Suppliers;
import dev.architectury.platform.Platform;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registries;
import net.minecraft.core.Registry;
import net.minecraft.world.item.Item;
import net.modularmods.modularguns.client.handlers.SmoothSwingTicker;
import net.modularmods.modularguns.common.modular.pack.ContentPackManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.function.Supplier;

public class ModularGuns {

    // Mod info
    public static final String MOD_ID = "modularguns";
    public static final String MOD_NAME = "ModularGuns";
    public static final String MOD_VERSION = "0.0.1";

    // Mod registries
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(MOD_ID, Registry.ITEM_REGISTRY);

    // Main instance
    public static ModularGuns instance;

    public static File MOD_DIR;
    public ContentPackManager contentPackManager;

    public static final Logger LOGGER = LoggerFactory.getLogger("modularmods");

    public void init() {
        this.instance = this;
        LOGGER.info(">> Loading ModularGuns");

        /**
         * Load ModularGuns file + config
         */
        MOD_DIR = new File(Platform.getGameFolder().toString(), "ModularGuns");
        if (!MOD_DIR.exists()) {
            MOD_DIR.mkdir();
            LOGGER.info("Created ModularWarfare folder, it's recommended to install content packs.");
            LOGGER.info("As the mod itself doesn't come with any content.");
        }
        new ModConfig(new File(MOD_DIR, "mod_config.json"));

        /**
         * ContentPack Manager Init
         */
        this.contentPackManager = new ContentPackManager();
        this.contentPackManager.init(MOD_DIR);

        //Smooth Swing Ticker Runnable
        SmoothSwingTicker smoothSwingTicker = new SmoothSwingTicker();
        Thread smoothTickThread = new Thread(smoothSwingTicker, "SmoothSwingThread");
        smoothTickThread.start();
    }


    public static Logger getLogger() {
        return LOGGER;
    }

    public ContentPackManager getContentPackManager() {
        return contentPackManager;
    }

    public static ModularGuns getInstance() {
        return instance;
    }
}
