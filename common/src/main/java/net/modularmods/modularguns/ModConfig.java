package net.modularmods.modularguns;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;

public class ModConfig {

    public transient static ModConfig INSTANCE;

    public String version = "0.0.1f";

    public ModConfig(File configFile) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            if (configFile.exists()) {
                JsonReader jsonReader = new JsonReader(new FileReader(configFile));
                INSTANCE = gson.fromJson(jsonReader, ModConfig.class);
            } else {
                try (Writer writer = new FileWriter(configFile)) {
                    gson.toJson(this, writer);
                }
                INSTANCE = this;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
