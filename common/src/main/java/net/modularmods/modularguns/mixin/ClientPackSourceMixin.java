package net.modularmods.modularguns.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.ClientPackSource;
import net.minecraft.server.packs.repository.Pack;
import net.modularmods.modularguns.ModularGuns;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(ClientPackSource.class)
public class ClientPackSourceMixin {

    @Inject(at = @At("RETURN"), method = "loadPacks")
    public void loadPacks(Consumer<Pack> consumer, Pack.PackConstructor packConstructor, CallbackInfo info) {
        if (ModularGuns.getInstance() != null) {
            ModularGuns.getInstance().getContentPackManager().getContentPacks().forEach(contentPack -> {
                if (!Minecraft.getInstance().getResourcePackRepository().getSelectedPacks().contains(contentPack)) {
                    consumer.accept(contentPack.pack);
                    System.out.println("Loaded content pack: " + contentPack.name);
                }
            });
        }
    }
}
