package net.modularmods.modularguns.client.gltf;

import de.javagl.jgltf.model.GltfAnimations;
import de.javagl.jgltf.model.animation.Animation;
import net.modularmods.gltfloader.gltf.IGltfModelReceiver;
import net.modularmods.gltfloader.gltf.RenderedGltfModel;
import net.modularmods.modularguns.ModularGuns;

import java.util.List;

public abstract class AbstractItemGltfModelReceiver implements IGltfModelReceiver {

    public Runnable vanillaSkinningCommands;

    public List<Runnable> vanillaRenderCommands;

    public List<Runnable> shaderModCommands;

    public List<Animation> animations;

    @Override
    public void onModelLoaded(RenderedGltfModel renderedModel) {
        vanillaSkinningCommands = renderedModel.vanillaSceneSkinningCommands.get(0);
        vanillaRenderCommands = renderedModel.vanillaSceneRenderCommands.get(0);
        shaderModCommands = renderedModel.shaderModSceneCommands.get(0);

        animations = GltfAnimations.createModelAnimations(renderedModel.gltfModel.getAnimationModels());

        renderedModel.gltfModel.getAnimationModels().forEach(anim -> {
            ModularGuns.getLogger().info("Registering animation " + anim.getName());
        });
    }

}
