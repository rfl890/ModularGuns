package net.modularmods.modularguns.client.renderer;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.modularmods.gltfloader.GLTFLoader;
import net.modularmods.gltfloader.gltf.RenderedGltfModel;
import net.modularmods.modularguns.client.renderer.animation.AnimationController;
import net.modularmods.modularguns.client.renderer.animation.GunStateMachine;
import net.modularmods.modularguns.client.renderer.utils.RenderUtils;
import net.modularmods.modularguns.common.items.ItemGun;
import net.modularmods.modularguns.common.types.GunType;
import net.modularmods.modularguns.utils.maths.Interpolation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;


public class GunRenderer {

    public static AnimationController controller;
    public static GunStateMachine stateMachine;

    public GunRenderer() {
        stateMachine = new GunStateMachine();
    }

    public void render(ItemStack p_108830_, ItemTransforms.TransformType p_108831_, PoseStack p_108832_, MultiBufferSource p_108833_, int p_108834_, int p_108835_) {
        if (p_108830_ != null) {
            if (p_108830_.getItem() instanceof ItemGun) {

                GunType gunType = ItemGun.getGunType(p_108830_);

                if (this.controller == null || this.controller.getConfig() != gunType.config) {
                    this.controller = new AnimationController(gunType.config);
                }

                int currentVAO = GL11.glGetInteger(GL30.GL_VERTEX_ARRAY_BINDING);
                int currentArrayBuffer = GL11.glGetInteger(GL15.GL_ARRAY_BUFFER_BINDING);
                int currentElementArrayBuffer = GL11.glGetInteger(GL15.GL_ELEMENT_ARRAY_BUFFER_BINDING);

                boolean currentCullFace = GL11.glGetBoolean(GL11.GL_CULL_FACE);

                boolean currentDepthTest = GL11.glGetBoolean(GL11.GL_DEPTH_TEST);
                GL11.glEnable(GL11.GL_DEPTH_TEST);

                switch (p_108831_) {
                    case FIRST_PERSON_RIGHT_HAND:
                        LocalPlayer player = Minecraft.getInstance().player;

                        boolean currentBlend = GL11.glGetBoolean(GL11.GL_BLEND);
                        GL11.glEnable(GL11.GL_BLEND);
                        GlStateManager._blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

                        PoseStack poseStack = p_108832_;

                        /**
                         * ACTION GUN MOTION
                         */
                        float gunRotX = RenderParameters.GUN_ROT_X_LAST + (RenderParameters.GUN_ROT_X - RenderParameters.GUN_ROT_X_LAST) * RenderParameters.partialTicks;
                        float gunRotY = RenderParameters.GUN_ROT_Y_LAST + (RenderParameters.GUN_ROT_Y - RenderParameters.GUN_ROT_Y_LAST) * RenderParameters.partialTicks;
                        poseStack.mulPose(Vector3f.YN.rotationDegrees(gunRotX));
                        poseStack.mulPose(Vector3f.ZN.rotationDegrees(gunRotY));

                        /**
                         * INITIAL BLENDER POSITION
                         */
                        poseStack.mulPose(Vector3f.YP.rotationDegrees(90f));
                        poseStack.translate(-1.8f, 1.0f, -0.065f);
                        /**
                         * ACTION BOBBING
                         */
                        float adsModifier = 0.95f - AnimationController.ADS;
                        float f = player.walkDist - player.walkDistO;
                        float f1 = -(player.walkDist + f * RenderParameters.partialTicks);
                        float f2 = Mth.lerp(RenderParameters.partialTicks, player.oBob, player.bob);
                        poseStack.translate(adsModifier * (Mth.sin(f1 * (float) Math.PI) * f2 * 0.5F), adsModifier * (-Math.abs(Mth.cos(f1 * (float) Math.PI) * f2)), 0.0D);
                        poseStack.mulPose(Vector3f.ZP.rotationDegrees(adsModifier * Mth.sin(f1 * (float) Math.PI) * f2 * 3.0F));
                        poseStack.mulPose(Vector3f.XP.rotationDegrees(adsModifier * Math.abs(Mth.cos(f1 * (float) Math.PI - 0.2F) * f2) * 5.0F));

                        /**
                         * ACTION GUN BALANCING X
                         */
                        poseStack.translate((float) (0.1f * RenderParameters.GUN_BALANCING_X * Math.cos(Math.PI * RenderParameters.SMOOTH_SWING / 50)) * (1F - AnimationController.ADS), 0, 0);
                        poseStack.mulPose(Vector3f.XN.rotationDegrees((RenderParameters.GUN_BALANCING_X * 4F) + (float) (RenderParameters.GUN_BALANCING_X * Math.sin(Math.PI * RenderParameters.SMOOTH_SWING / 35))));
                        poseStack.mulPose(Vector3f.XN.rotationDegrees((float) Math.sin(Math.PI * RenderParameters.GUN_BALANCING_X)));
                        poseStack.mulPose(Vector3f.XN.rotationDegrees((RenderParameters.GUN_BALANCING_X) * 0.4F));

                        /**
                         * ACTION GUN BALANCING Y
                         */
                        poseStack.translate(0, adsModifier * Interpolation.SINE_IN.interpolate(0F, (-0.2f * (1F - AnimationController.ADS)), RenderParameters.GUN_BALANCING_Y), 0);
                        poseStack.translate(0, adsModifier * ((float) (0.05f * (Math.sin(RenderParameters.SMOOTH_SWING / 10) * RenderParameters.GUN_BALANCING_Y))), 0);
                        poseStack.mulPose(Vector3f.YP.rotationDegrees(adsModifier * 0.1f * Interpolation.SINE_OUT.interpolate(-RenderParameters.GUN_BALANCING_Y, RenderParameters.GUN_BALANCING_Y, adsModifier * Mth.sin(f2 * (float) Math.PI))));

                        /**
                         * ACTION GUN SWAY
                         */
                        RenderParameters.VAL = (float) (Math.sin(RenderParameters.SMOOTH_SWING / 100) * 8);
                        RenderParameters.VAL2 = (float) (Math.sin(RenderParameters.SMOOTH_SWING / 80) * 8);
                        RenderParameters.VALROT = (float) (Math.sin(RenderParameters.SMOOTH_SWING / 90) * 1.2f);

                        poseStack.translate(0f, ((RenderParameters.VAL / 500) * (0.95f - AnimationController.ADS)), ((RenderParameters.VAL2 / 500 * (0.95f - AnimationController.ADS))));
                        poseStack.mulPose(Vector3f.XP.rotationDegrees(adsModifier * RenderParameters.VALROT));

                        /**
                         * ACTION SPRINT
                         */
                        RenderParameters.VALSPRINT = (float) (Math.cos(RenderParameters.SMOOTH_SWING / 5) * 5);
                        float VALSPRINT2 = (float) (Math.sin(RenderParameters.SMOOTH_SWING / 5) * 5);

                        //OPTIONAL
                        float VALSPRINT3 = (float) (Math.sin(RenderParameters.SMOOTH_SWING / 8) * 6);
                        float VALSPRINT4 = (float) (Math.sin(RenderParameters.SMOOTH_SWING / 9) * 7);
                        float VALSPRINT5 = (float) (Math.sin(RenderParameters.SMOOTH_SWING / 10) * 8);
                        //OPTIONAL

                        poseStack.mulPose(new Vector3f(1F, 1F, -1F).rotationDegrees(adsModifier * RenderParameters.VALSPRINT * AnimationController.SPRINT));
                        poseStack.mulPose(new Vector3f(0F, 0F, 1F).rotationDegrees(adsModifier * 0.2f * VALSPRINT2 * AnimationController.SPRINT));

                        //OPTIONAL
                        poseStack.mulPose(new Vector3f(0F, 1F, 0F).rotationDegrees(adsModifier * 0.3f * VALSPRINT3 * AnimationController.SPRINT));
                        poseStack.mulPose(new Vector3f(0F, 0F, 1F).rotationDegrees(adsModifier * 0.4f * VALSPRINT3 * AnimationController.SPRINT));
                        poseStack.mulPose(new Vector3f(-1F, -1F, 0F).rotationDegrees(adsModifier * 0.5f * VALSPRINT5 * AnimationController.SPRINT));
                        //OPTIONAL

                        Vector3f customHipRotation = new Vector3f(gunType.config.aim.rotateHipPosition.x(), gunType.config.aim.rotateHipPosition.y(), gunType.config.aim.rotateHipPosition.z());
                        Vector3f customHipTranslate = new Vector3f(gunType.config.aim.translateHipPosition.x(), (gunType.config.aim.translateHipPosition.y()), (gunType.config.aim.translateHipPosition.z()));
                        Vector3f customSprintRotation = new Vector3f((gunType.config.sprint.sprintRotate.x() * AnimationController.SPRINT), (gunType.config.sprint.sprintRotate.y() * AnimationController.SPRINT), (gunType.config.sprint.sprintRotate.z() * AnimationController.SPRINT));
                        Vector3f customSprintTranslate = new Vector3f((gunType.config.sprint.sprintTranslate.x() * AnimationController.SPRINT), (gunType.config.sprint.sprintTranslate.y() * AnimationController.SPRINT), (gunType.config.sprint.sprintTranslate.z() * AnimationController.SPRINT));

                        customSprintRotation.mul((1F - AnimationController.ADS));
                        customSprintTranslate.mul((1F - AnimationController.ADS));

                        poseStack.mulPose(Vector3f.XP.rotationDegrees(customHipRotation.x() + customSprintRotation.x()));
                        poseStack.mulPose(Vector3f.YP.rotationDegrees(customHipRotation.y() + customSprintRotation.y()));
                        poseStack.mulPose(Vector3f.ZP.rotationDegrees(customHipRotation.z() + customSprintRotation.z()));
                        poseStack.translate(customHipTranslate.x() + customSprintTranslate.x(), customHipTranslate.y() + customSprintTranslate.y(), customHipTranslate.z() + customSprintTranslate.z());

                        GLTFLoader.CURRENT_POSE = poseStack.last().pose();
                        GLTFLoader.CURRENT_NORMAL = poseStack.last().normal();

                        GL30.glVertexAttribI2i(RenderedGltfModel.vaUV2, p_108834_ & '\uffff', p_108834_ >> 16 & '\uffff');

                        GLTFLoader.CURRENT_PROGRAM = GL11.glGetInteger(GL20.GL_CURRENT_PROGRAM);
                        if (GLTFLoader.CURRENT_PROGRAM == 0) {
                            RenderUtils.renderLightTextureWithVanillaCommands(gunType.model, controller.getTime());
                            GL20.glUseProgram(0);
                        } else {
                            GLTFLoader.MODEL_VIEW_MATRIX = GL20.glGetUniformLocation(GLTFLoader.CURRENT_PROGRAM, "modelViewMatrix");
                            if (GLTFLoader.MODEL_VIEW_MATRIX == -1) {
                                int currentProgram = GLTFLoader.CURRENT_PROGRAM;
                                RenderUtils.renderLightTextureWithVanillaCommands(gunType.model, controller.getTime());
                                GL20.glUseProgram(currentProgram);
                            } else {
                                RenderUtils.renderWithShaderModCommands(gunType.model, controller.getTime());
                            }
                        }

                        GL30.glVertexAttribI2i(RenderedGltfModel.vaUV2, 0, 0);

                        if (!currentBlend) GL11.glDisable(GL11.GL_BLEND);
                        break;
                    default:
                        break;
                }

                if (!currentDepthTest) GL11.glDisable(GL11.GL_DEPTH_TEST);

                if (currentCullFace) GL11.glEnable(GL11.GL_CULL_FACE);
                else GL11.glDisable(GL11.GL_CULL_FACE);

                GL30.glBindVertexArray(currentVAO);
                GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, currentArrayBuffer);
                GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, currentElementArrayBuffer);
            }
        }
    }
}
