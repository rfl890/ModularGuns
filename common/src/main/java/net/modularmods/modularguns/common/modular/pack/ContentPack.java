package net.modularmods.modularguns.common.modular.pack;

import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.FilePackResources;
import net.minecraft.server.packs.FolderPackResources;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackCompatibility;
import net.minecraft.server.packs.repository.PackSource;

import java.io.File;
import java.util.Locale;

public class ContentPack {

    /**
     * General variables
     */
    public String name;
    public File directory;
    public Pack pack;
    public boolean isZip;

    public ContentPack(File file) {
        this.directory = file;
        this.name = directory.getName();
        this.pack = loadContentResourcePack(new File(directory.toString()));
        this.isZip = ContentPackManager.zipJar.matcher(file.getName()).matches();
    }

    public Pack loadContentResourcePack(File file) {
        PackResources packResources;
        if (isZip) {
            packResources = new FilePackResources(file);
        } else {
            packResources = new FolderPackResources(file);
        }

        Pack resourcePack = new Pack(file.getName(), true, () -> {
            return packResources;
        }, Component.translatable(file.getName()), Component.translatable(file.getName().toUpperCase(Locale.ROOT)), PackCompatibility.COMPATIBLE, Pack.Position.TOP, true, PackSource.DEFAULT);
        return resourcePack;
    }

}
