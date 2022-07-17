package net.modularmods.modularguns.common.modular;

import com.google.gson.stream.JsonReader;
import net.modularmods.modularguns.client.configs.GunRenderConfig;
import net.modularmods.modularguns.common.modular.pack.ContentPack;
import net.modularmods.modularguns.common.modular.pack.ContentPackManager;
import net.modularmods.modularguns.common.modular.type.BaseType;
import net.modularmods.modularguns.common.types.GunType;
import net.modularmods.modularguns.utils.GSONUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;

public class RenderConfigUtils {

    /**
     * Gets a specific render config
     */
    public static <T> T getRenderConfig(BaseType baseType, Class<T> typeClass) {
        try {
            File contentPackDir = baseType.contentPack.directory;
            if (contentPackDir.exists() && contentPackDir.isDirectory()) {
                File renderConfig = new File(contentPackDir, "/" + baseType.getAssetDir() + "/render");
                File typeRender = new File(renderConfig, baseType.internalName + ".render.json");
                JsonReader jsonReader = new JsonReader(new FileReader(typeRender));

                return GSONUtils.fromJson(ContentPackManager.gson, jsonReader, typeClass);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Generate a default render config x
     *
     * @param type
     * @param contentPack
     */
    public static void makeRenderConfig(BaseType type, ContentPack contentPack) {
        final File dir = new File(contentPack.directory, "/" + type.getAssetDir() + "/render");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        final File renderFile = new File(dir, type.internalName + ".render.json");
        if (!renderFile.exists()) {
            try {
                FileWriter fileWriter = new FileWriter(renderFile, true);
                if (type instanceof GunType) {
                    GunRenderConfig renderConfig = new GunRenderConfig();
                    renderConfig.modelFileName = type.internalName.replaceAll(type.contentPack + ".", "");
                    renderConfig.modelFileName = renderConfig.modelFileName + ".gltf";
                    ContentPackManager.gson.toJson(renderConfig, fileWriter);

                    fileWriter.flush();
                    fileWriter.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
