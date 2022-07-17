package net.modularmods.modularguns.common.modular.pack;

import java.io.File;

public class ContentPack {

    /**
     * General variables
     */
    public String name;
    public File directory;

    public ContentPack(File directory) {
        this.directory = directory;
        this.name = directory.getName();
    }

}
