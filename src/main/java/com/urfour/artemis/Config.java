package com.urfour.artemis;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class Config {

    public static String greeting = "Hello! *boom*";

    public static void synchronizeConfiguration(File configFile) {
        Configuration configuration = new Configuration(configFile);

        greeting = configuration.getString("greeting", Configuration.CATEGORY_GENERAL, greeting, "AAAAAAAAH COMMENT JE DIS BONJOUR");

        if (configuration.hasChanged()) {
            configuration.save();
        }
    }
}
