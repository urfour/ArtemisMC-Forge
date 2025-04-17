package com.urfour.artemis;

import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {

    // preInit "Run before anything else. Read your config, create blocks, items, etc, and register them with the
    // GameRegistry." (Remove if not needed)
    public void preInit(FMLPreInitializationEvent event) {

        Artemis.LOG.info(Config.greeting);
    }

    public void postInit(FMLPostInitializationEvent event) {
        // This method is intentionally left blank.
    }

}