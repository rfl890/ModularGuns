package net.modularmods.modularguns;


import dev.architectury.platform.Platform;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.utils.Env;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.modularmods.modularguns.client.ClientHandler;
import net.modularmods.modularguns.common.CommonHandler;
import net.modularmods.modularguns.common.modular.pack.ContentPackManager;
import net.modularmods.modularguns.server.ServerHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class ModularGuns {

    // Mod info
    public static final String MOD_ID = "modularguns";
    public static final String MOD_NAME = "ModularGuns";
    public static final String MOD_VERSION = "0.0.1";

    // Mod registries
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(MOD_ID, Registry.ITEM_REGISTRY);

    // Main instance
    public static ModularGuns instance;
    public static CommonHandler sidedHandler;

    public static CreativeModeTab MAIN_TAB;

    public static File MOD_DIR;
    public ContentPackManager contentPackManager;

    public static final Logger LOGGER = LoggerFactory.getLogger("modularmods");


    public void init() {
        this.instance = this;
        LOGGER.info(">> Loading ModularGuns");

        this.MAIN_TAB = CreativeTabRegistry.create(new ResourceLocation(ModularGuns.MOD_ID, "main_tab"), () -> new ItemStack(Items.IRON_INGOT));

        /** Load Sided Handler **/
        if (Platform.getEnvironment() == Env.CLIENT) {
            sidedHandler = new ClientHandler();
        } else if (Platform.getEnvironment() == Env.SERVER) {
            sidedHandler = new ServerHandler();
        }
        sidedHandler.init();

        /** Load ModularGuns file + config **/
        MOD_DIR = new File(Platform.getGameFolder().toString(), "ModularGuns");
        if (!MOD_DIR.exists()) {
            MOD_DIR.mkdir();
            LOGGER.info("Created ModularWarfare folder, it's recommended to install content packs.");
            LOGGER.info("As the mod itself doesn't come with any content.");
        }
        new ModConfig(new File(MOD_DIR, "mod_config.json"));

        /** ContentPack Manager Init **/
        this.contentPackManager = new ContentPackManager();
        this.contentPackManager.init(MOD_DIR);
    }

    public void postInit(){
        ITEMS.register();
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
