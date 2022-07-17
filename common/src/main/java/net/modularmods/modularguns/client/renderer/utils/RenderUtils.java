package net.modularmods.modularguns.client.renderer.utils;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import de.javagl.jgltf.model.animation.Animation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.modularmods.gltfloader.GLTFLoader;
import net.modularmods.modularguns.client.gltf.AbstractItemGltfModelReceiver;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;

public class RenderUtils {

    public static void renderLightTextureWithVanillaCommands(AbstractItemGltfModelReceiver modelReceiver, float time) {
        GL13.glActiveTexture(GL13.GL_TEXTURE2);
        int currentTexture2 = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, GLTFLoader.getInstance().getLightTexture().getId());

        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        int currentTexture1 = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        int currentTexture0 = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);

        renderWithVanillaCommands(modelReceiver, time);

        GL13.glActiveTexture(GL13.GL_TEXTURE2);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, currentTexture2);
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, currentTexture1);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, currentTexture0);
    }

    public static void renderLightOverlayTextureWithVanillaCommands(AbstractItemGltfModelReceiver modelReceiver, float time) {
        GL13.glActiveTexture(GL13.GL_TEXTURE2);
        int currentTexture2 = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, GLTFLoader.getInstance().getLightTexture().getId());

        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        int currentTexture1 = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);
        Minecraft mc = Minecraft.getInstance();
        mc.gameRenderer.overlayTexture().setupOverlayColor();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, RenderSystem.getShaderTexture(1));
        mc.gameRenderer.overlayTexture().teardownOverlayColor();

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        int currentTexture0 = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);

        renderWithVanillaCommands(modelReceiver, time);

        GL13.glActiveTexture(GL13.GL_TEXTURE2);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, currentTexture2);
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, currentTexture1);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, currentTexture0);
    }

    public static void renderWithVanillaCommands(AbstractItemGltfModelReceiver itemModelReceiver, float time) {
        for (Animation animation : itemModelReceiver.animations) {
            animation.update(time);
        }
        itemModelReceiver.vanillaSkinningCommands.run();

        setupVanillaShader();

        itemModelReceiver.vanillaRenderCommands.forEach((command) -> command.run());
    }

    public static void setupVanillaShader() {
        GLTFLoader.CURRENT_SHADER_INSTANCE = GameRenderer.getRendertypeEntitySolidShader();
        GLTFLoader.CURRENT_PROGRAM = GLTFLoader.CURRENT_SHADER_INSTANCE.getId();
        GL20.glUseProgram(GLTFLoader.CURRENT_PROGRAM);

        GLTFLoader.CURRENT_SHADER_INSTANCE.PROJECTION_MATRIX.set(RenderSystem.getProjectionMatrix());
        GLTFLoader.CURRENT_SHADER_INSTANCE.PROJECTION_MATRIX.upload();

        GLTFLoader.CURRENT_SHADER_INSTANCE.INVERSE_VIEW_ROTATION_MATRIX.set(RenderSystem.getInverseViewRotationMatrix());
        GLTFLoader.CURRENT_SHADER_INSTANCE.INVERSE_VIEW_ROTATION_MATRIX.upload();

        GLTFLoader.CURRENT_SHADER_INSTANCE.FOG_START.set(RenderSystem.getShaderFogStart());
        GLTFLoader.CURRENT_SHADER_INSTANCE.FOG_START.upload();

        GLTFLoader.CURRENT_SHADER_INSTANCE.FOG_END.set(RenderSystem.getShaderFogEnd());
        GLTFLoader.CURRENT_SHADER_INSTANCE.FOG_END.upload();

        GLTFLoader.CURRENT_SHADER_INSTANCE.FOG_COLOR.set(RenderSystem.getShaderFogColor());
        GLTFLoader.CURRENT_SHADER_INSTANCE.FOG_COLOR.upload();

        GLTFLoader.CURRENT_SHADER_INSTANCE.FOG_SHAPE.set(RenderSystem.getShaderFogShape().getIndex());
        GLTFLoader.CURRENT_SHADER_INSTANCE.FOG_SHAPE.upload();

        GLTFLoader.CURRENT_SHADER_INSTANCE.COLOR_MODULATOR.set(1.0F, 1.0F, 1.0F, 1.0F);
        GLTFLoader.CURRENT_SHADER_INSTANCE.COLOR_MODULATOR.upload();

        GL20.glUniform1i(GL20.glGetUniformLocation(GLTFLoader.CURRENT_PROGRAM, "Sampler0"), 0);
        GL20.glUniform1i(GL20.glGetUniformLocation(GLTFLoader.CURRENT_PROGRAM, "Sampler1"), 1);
        GL20.glUniform1i(GL20.glGetUniformLocation(GLTFLoader.CURRENT_PROGRAM, "Sampler2"), 2);

        RenderSystem.setupShaderLights(GLTFLoader.CURRENT_SHADER_INSTANCE);
        GLTFLoader.LIGHT0_DIRECTION = new Vector3f(GLTFLoader.CURRENT_SHADER_INSTANCE.LIGHT0_DIRECTION.getFloatBuffer().get(0), GLTFLoader.CURRENT_SHADER_INSTANCE.LIGHT0_DIRECTION.getFloatBuffer().get(1), GLTFLoader.CURRENT_SHADER_INSTANCE.LIGHT0_DIRECTION.getFloatBuffer().get(2));
        GLTFLoader.LIGHT1_DIRECTION = new Vector3f(GLTFLoader.CURRENT_SHADER_INSTANCE.LIGHT1_DIRECTION.getFloatBuffer().get(0), GLTFLoader.CURRENT_SHADER_INSTANCE.LIGHT1_DIRECTION.getFloatBuffer().get(1), GLTFLoader.CURRENT_SHADER_INSTANCE.LIGHT1_DIRECTION.getFloatBuffer().get(2));
    }

    public static void renderWithShaderModCommands(AbstractItemGltfModelReceiver modelReceiver, float time) {
        GLTFLoader.MODEL_VIEW_MATRIX_INVERSE = GL20.glGetUniformLocation(GLTFLoader.CURRENT_PROGRAM, "modelViewMatrixInverse");
        GLTFLoader.NORMAL_MATRIX = GL20.glGetUniformLocation(GLTFLoader.CURRENT_PROGRAM, "normalMatrix");

        RenderSystem.getProjectionMatrix().store(GLTFLoader.BUF_FLOAT_16);
        GL20.glUniformMatrix4fv(GL20.glGetUniformLocation(GLTFLoader.CURRENT_PROGRAM, "projectionMatrix"), false, GLTFLoader.BUF_FLOAT_16);
        Matrix4f projectionMatrixInverse = RenderSystem.getProjectionMatrix().copy();
        projectionMatrixInverse.invert();
        projectionMatrixInverse.store(GLTFLoader.BUF_FLOAT_16);
        GL20.glUniformMatrix4fv(GL20.glGetUniformLocation(GLTFLoader.CURRENT_PROGRAM, "projectionMatrixInverse"), false, GLTFLoader.BUF_FLOAT_16);

        GL13.glActiveTexture(GL13.GL_TEXTURE3);
        int currentTexture3 = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        int currentTexture1 = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        int currentTexture0 = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);

        Minecraft mc = Minecraft.getInstance();
        for (Animation animation : modelReceiver.animations) {
            animation.update(time);
        }
        modelReceiver.shaderModCommands.forEach((command) -> command.run());

        GL13.glActiveTexture(GL13.GL_TEXTURE3);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, currentTexture3);
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, currentTexture1);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, currentTexture0);
    }
}
