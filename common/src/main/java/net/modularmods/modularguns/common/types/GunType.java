package net.modularmods.modularguns.common.types;

import net.minecraft.resources.ResourceLocation;
import net.modularmods.gltfloader.GLTFLoader;
import net.modularmods.modularguns.ModularGuns;
import net.modularmods.modularguns.client.configs.GunRenderConfig;
import net.modularmods.modularguns.client.gltf.AbstractItemGltfModelReceiver;
import net.modularmods.modularguns.common.modular.RenderConfigUtils;
import net.modularmods.modularguns.common.modular.type.BaseType;

public class GunType extends BaseType {

    public GunRenderConfig config;

    @Override
    public void initValues() {
        if (this.maxStackSize == null)
            this.maxStackSize = 1;
    }

    /**
     * Function to load the models and register them to the MCglTF API
     */
    @Override
    public void reloadModel() {
        ModularGuns.getLogger().info("Loading model for " + this.internalName);
        AbstractItemGltfModelReceiver modelReceiver = new AbstractItemGltfModelReceiver() {
            @Override
            public ResourceLocation getModelLocation() {
                return new ResourceLocation(ModularGuns.MOD_ID, "gltf/" + internalName + ".gltf");
            }
        };
        this.model = modelReceiver;
        GLTFLoader.getInstance().addGltfModelReceiver(this.model);

        this.config = RenderConfigUtils.getRenderConfig(this, GunRenderConfig.class);
        ModularGuns.getLogger().info("Config file: " + this.config.modelFileName);
    }

    @Override
    public String getAssetDir() {
        return "guns";
    }

}
