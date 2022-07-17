package net.modularmods.modularguns.excepts.fabric;

import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.modularmods.modularguns.common.items.ItemGun;
import net.modularmods.modularguns.fabric.ModularGunsFabric;

public class ExpectRegisterItemRendererImpl {

    public static void registerItemRenderer() {
        BuiltinItemRendererRegistry.INSTANCE.register(ItemGun.instance, ModularGunsFabric.gunRenderer);
    }
}
