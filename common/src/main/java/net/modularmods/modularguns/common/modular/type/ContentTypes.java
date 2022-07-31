package net.modularmods.modularguns.common.modular.type;

import dev.architectury.registry.item.ItemPropertiesRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.modularmods.modularguns.ModularGuns;
import net.modularmods.modularguns.common.items.ItemGun;
import net.modularmods.modularguns.common.modular.pack.ContentPackManager;
import net.modularmods.modularguns.common.types.GunType;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class ContentTypes {

    public static ArrayList<TypeEntry> values = new ArrayList<TypeEntry>();
    private static int typeId = 0;

    public static void registerTypes(ContentPackManager manager) {
        registerType("guns", ItemGun.class, GunType.class, typeId++);
    }

    /**
     * Main function to register new types
     *
     * @param name      Name of the type
     * @param typeClass Class of the type (must extend BaseType)
     * @param typeId    ID number of the type, must be unique
     */
    public static void registerType(String name, Class<? extends Item> itemClass, Class<? extends BaseType> typeClass, int typeId) {
        values.add(new TypeEntry(name, typeClass, typeId));

        try {
            ModularGuns.ITEMS.register(name, () -> {
                try {
                    return itemClass.getDeclaredConstructor().newInstance();
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
                }
                return null;
            });
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
}