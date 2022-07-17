package net.modularmods.modularguns.common.modular.type;


import net.modularmods.modularguns.client.gltf.AbstractItemGltfModelReceiver;
import net.modularmods.modularguns.common.modular.pack.ContentPack;

public class BaseType {

    /**
     * Max stack size
     */
    public Integer maxStackSize;

    /**
     * Model object (.gltf)
     */
    public AbstractItemGltfModelReceiver model;

    /**
     * Weapon staticModel skins/textures
     */

    public String internalName;
    /**
     * Used to generate .lang files automatically
     */
    public String displayName;
    public String iconName;

    public transient int id;
    public transient ContentPack contentPack;

    /**
     * Method for init extra values for the item
     */
    public void initValues() {
    }

    /**
     * Method to return if the baseType has a model
     */
    public boolean hasModel() {
        return model != null;
    }

    /**
     * Method for sub types to use for handling staticModel reloading
     */
    public void reloadModel() {
    }

    /**
     * Returns internal name if not overridden by sub-type
     */
    @Override
    public String toString() {
        return internalName;
    }

    public String getAssetDir() {
        return "";
    }
}
