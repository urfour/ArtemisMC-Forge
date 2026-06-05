package com.urfour.artemis;

import com.urfour.artemis.server.Server;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Mod(Artemis.MODID)
public class Artemis {
    public static final String MODID = "artemis";
    public static final Logger LOG = LogManager.getLogger(MODID);

    public Artemis(IEventBus modEventBus) {
        // Register the clientSetup method for client-side mod setup
        modEventBus.addListener(this::clientSetup);
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        LOG.info("Initializing ArtemisMC Game State Integration...");
        Server server = new Server();
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(server, 0, 100, TimeUnit.MILLISECONDS);
    }
}