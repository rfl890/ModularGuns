package net.modularmods.modularguns.common.modular.pack;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import net.minecraft.client.Minecraft;
import net.modularmods.modularguns.ModularGuns;
import net.modularmods.modularguns.common.modular.RenderConfigUtils;
import net.modularmods.modularguns.common.modular.type.BaseType;
import net.modularmods.modularguns.common.modular.type.ContentTypes;
import net.modularmods.modularguns.common.modular.type.TypeEntry;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * The Content Pack Manager of ModularWarfare
 */
public class ContentPackManager {

    /**
     * Utils
     */
    protected static Pattern zipJar = Pattern.compile("(.+).(zip|jar)$");
    public static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /**
     * List containing all the ContentPack objects loaded by ModularWarfare
     */
    private List<ContentPack> contentPacks = new ArrayList<>();

    /**
     * ArrayList of all BaseType instance loaded by ModularWarfare
     */
    public ArrayList<BaseType> baseTypes = new ArrayList<BaseType>();

    /**
     * Function called at the init of the mod, creating one item per ContentTypes
     * And loads all the content-packs in /ModularWarfare/*
     *
     * @param MOD_DIR
     */
    public void init(File MOD_DIR) {
        // Register all the  types
        ContentTypes.registerTypes(this);
        baseTypes.clear();

        // Loop trough ModularMods directory to find all the .json
        for (File file : MOD_DIR.listFiles()) {
            if (file.isDirectory() || zipJar.matcher(file.getName()).matches()) {
                try {
                    loadContentPack(file);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * Calling special functions for loading each baseTypes items
         */
        for (BaseType baseType : baseTypes) {
            baseType.initValues();
            baseType.reloadModel();
        }
    }

    /**
     * Load a specific content-pack inside /ModularWarfare/
     *
     * @param file the uri of the content-pack to load
     */
    public void loadContentPack(File file) {
        if (file.isDirectory() || zipJar.matcher(file.getName()).matches()) {
            ContentPack pack = new ContentPack(file);
            if (!contentPacks.contains(pack)) {
                createPackInfo(pack);

                for (TypeEntry type : ContentTypes.values) {
                    File subFolder = new File(file, "/" + type.name + "/");
                    if (subFolder.exists()) {
                        for (File typeFile : subFolder.listFiles()) {
                            if (typeFile.isFile() && !typeFile.getName().contains("render.json") && !typeFile.getName().equalsIgnoreCase(".DS_Store")) {
                                try {
                                    JsonReader jsonReader = new JsonReader(new FileReader(typeFile));
                                    BaseType parsedType = gson.fromJson(jsonReader, type.typeClass);
                                    parsedType.id = type.id;
                                    parsedType.contentPack = pack;
                                    baseTypes.add(parsedType);
                                    RenderConfigUtils.makeRenderConfig(parsedType, pack);
                                    ModularGuns.getLogger().info("Loaded type " + parsedType.internalName);
                                } catch (Exception exception) {
                                    exception.printStackTrace();
                                }
                            }
                        }
                    }
                }
                contentPacks.add(pack);
            } else {
                ModularGuns.getLogger().info("Content-pack already loaded !");
            }
        }
    }

    /**
     * Function to return all ContentPack objects loaded by ModularWarfare
     *
     * @return List of loaded Content-Packs
     */
    public List<ContentPack> getContentPacks() {
        return contentPacks;
    }

    /**
     * Get the a BaseType from the registered internalName casted as typeClass
     * Example: Get the GunType object from a gun with the internalName "mypack.akm"
     *
     * @param typeClass    The type of the item you want to cast
     * @param internalName The internalName of the item
     * @param <T>          The type of the item you want to cast
     * @return The BaseType casted as GunType/ArmorType... of the item found
     */
    public <T> T getType(Class<T> typeClass, String internalName) {
        for (BaseType type : baseTypes) {
            if (type.internalName.equalsIgnoreCase(internalName)) {
                return typeClass.cast(type);
            }
        }
        return typeClass.cast(new BaseType());
    }

    /**
     * Create info.json file inside the content-pack file
     * Used to check the content-pack author, version ...
     *
     * @param contentPack The content-pack to add the info.json
     */
    public void createPackInfo(ContentPack contentPack) {
        final File info = new File(contentPack.directory, "info.json");
        if (!info.exists()) {
            try {
                FileWriter fileWriter = new FileWriter(info, true);
                PackInfo packInfo = new PackInfo();
                packInfo.name = contentPack.name;
                ContentPackManager.gson.toJson(packInfo, fileWriter);

                fileWriter.flush();
                fileWriter.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
