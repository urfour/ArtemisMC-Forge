package com.urfour.artemis.server;

import com.google.gson.Gson;
import com.urfour.artemis.infos.MinecraftInfos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;

public class Server implements Runnable {
    private static String getArtemisFolder() {
        if (System.getProperty("os.name").contains("Windows")) {
            return System.getenv("ProgramData");
        } else {
            return System.getProperty("user.home") + "/.local/share";
        }
    }

    private final MinecraftInfos infos = new MinecraftInfos();
    private static final Logger LOGGER = LogManager.getLogger("artemis-server");
    private static final String WEB_SERVER_FILE = getArtemisFolder() + "/Artemis/webserver.txt";
    private String IP;
    private final Gson gson = new Gson();
    private HttpClient httpClient;

    public Server() {
        try {
            IP = Files.readAllLines(Paths.get(WEB_SERVER_FILE)).get(0);
            if (!IP.endsWith("/")) {
                IP = IP + "/";
            }
            LOGGER.info("Using IP " + IP + " to send in-game information.");
            this.httpClient = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofMillis(500))
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        try {
            infos.update();
            String json = gson.toJson(infos);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(IP + "plugins/25dacd2d-9275-4d94-bc12-8761dedf0f1d/Minecraft"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .timeout(Duration.ofMillis(500))
                    .build();

            httpClient.sendAsync(request, HttpResponse.BodyHandlers.discarding());
        } catch (Exception ex) {
            LOGGER.error("Error sending game state to Artemis: ", ex);
        }
    }
}