package com.urfour.artemis.infos;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.controls.ControlsScreen;
import net.minecraft.client.gui.screens.controls.KeyBindsScreen;
import net.minecraft.client.gui.screens.OptionsScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MinecraftInfos {
    private PlayerInfos player = new PlayerInfos();
    private WorldInfos world = new WorldInfos();
    private GUIInfos gui = new GUIInfos();

    public void update() {
        player.getInfos();
        world.getInfos();
        gui.getInfos();
    }

    private static class PlayerInfos {
        private boolean inGame;
        private float health;
        private float maxHealth;
        private float absorption;
        private boolean isDead;
        private int armorPoints;
        private int experienceLevel;
        private float experience;
        private int foodLevel;
        private float saturationLevel;
        private boolean isSneaking;
        private boolean isRidingHorse;
        private boolean isBurning;
        private boolean isInWater;
        private HashMap<String, Boolean> playerEffects = new HashMap<>();
        private static final HashMap<String, MobEffect> TARGET_EFFECTS;
        private HashMap<String, String> armor = new HashMap<>();
        private String leftHandItem;
        private String rightHandItem;
        private int currentHotbarSlot;

        static {
            TARGET_EFFECTS = new HashMap<>();
            TARGET_EFFECTS.put("moveSpeed", MobEffect.byId(1));
            TARGET_EFFECTS.put("moveSlowdown", MobEffect.byId(2));
            TARGET_EFFECTS.put("haste", MobEffect.byId(3));
            TARGET_EFFECTS.put("miningFatigue", MobEffect.byId(4));
            TARGET_EFFECTS.put("strength", MobEffect.byId(5));
            TARGET_EFFECTS.put("instantHealth", MobEffect.byId(6));
            TARGET_EFFECTS.put("instantDamage", MobEffect.byId(7));
            TARGET_EFFECTS.put("jumpBoost", MobEffect.byId(8));
            TARGET_EFFECTS.put("confusion", MobEffect.byId(9));
            TARGET_EFFECTS.put("regeneration", MobEffect.byId(10));
            TARGET_EFFECTS.put("resistance", MobEffect.byId(11));
            TARGET_EFFECTS.put("fireResistance", MobEffect.byId(12));
            TARGET_EFFECTS.put("waterBreathing", MobEffect.byId(13));
            TARGET_EFFECTS.put("invisibility", MobEffect.byId(14));
            TARGET_EFFECTS.put("blindness", MobEffect.byId(15));
            TARGET_EFFECTS.put("nightVision", MobEffect.byId(16));
            TARGET_EFFECTS.put("hunger", MobEffect.byId(17));
            TARGET_EFFECTS.put("weakness", MobEffect.byId(18));
            TARGET_EFFECTS.put("poison", MobEffect.byId(19));
            TARGET_EFFECTS.put("wither", MobEffect.byId(20));
            TARGET_EFFECTS.put("healthBoost", MobEffect.byId(21));
            TARGET_EFFECTS.put("absorption", MobEffect.byId(22));
            TARGET_EFFECTS.put("saturation", MobEffect.byId(23));
            TARGET_EFFECTS.put("glowing", MobEffect.byId(24));
            TARGET_EFFECTS.put("levitation", MobEffect.byId(25));
            TARGET_EFFECTS.put("luck", MobEffect.byId(26));
            TARGET_EFFECTS.put("badLuck", MobEffect.byId(27));
            TARGET_EFFECTS.put("slowFalling", MobEffect.byId(28));
            TARGET_EFFECTS.put("conduitPower", MobEffect.byId(29));
            TARGET_EFFECTS.put("dolphinsGrace", MobEffect.byId(30));
            TARGET_EFFECTS.put("bad_omen", MobEffect.byId(31));
            TARGET_EFFECTS.put("villageHero", MobEffect.byId(32));
        }
        private String testIfAir(ItemStack item) {
            if (item.getDescriptionId().equals("block.minecraft.air")) {
                return null;
            }
            else {
                return item.getDescriptionId()
                        .replace("item.", "")
                        .replace("block.", "");
            }
        }
        private void getInfos() {
            try {
                Player player = Minecraft.getInstance().player;
                assert player != null;
                this.health = player.getHealth();
                maxHealth = player.getMaxHealth();
                absorption = player.getAbsorptionAmount();
                isDead = !player.isAlive();
                armorPoints = player.getArmorValue();
                experienceLevel = player.experienceLevel;
                experience = player.experienceProgress;
                foodLevel = player.getFoodData().getFoodLevel();
                saturationLevel = player.getFoodData().getSaturationLevel();
                isSneaking = player.isCrouching();
                isBurning = player.isOnFire();
                isInWater = player.isInWater();
                for (Map.Entry<String, MobEffect> effect : TARGET_EFFECTS.entrySet())
                    playerEffects.put(effect.getKey(), player.getEffect(effect.getValue()) != null);
                ArrayList<String> armorItems = new ArrayList<>();
                player.getArmorSlots().forEach(item -> armorItems.add(testIfAir(item)));
                rightHandItem = testIfAir(player.getMainHandItem());
                leftHandItem = testIfAir(player.getOffhandItem());
                currentHotbarSlot = player.getInventory().selected;
                armor.put("boots", armorItems.get(0));
                armor.put("leggings", armorItems.get(1));
                armor.put("chestplate", armorItems.get(2));
                armor.put("helmet", armorItems.get(3));
                inGame = true;
            } catch (Exception ex) {
                inGame = false;
            }

        }
    }

    private static class WorldInfos {
        private long worldTime;
        private boolean isDayTime;
        private boolean isRaining;
        private float rainStrength;
        private String dimension;

        private void getInfos() {
            try {
                ClientLevel world = Minecraft.getInstance().level;
                assert world != null;
                worldTime = world.getDayTime();
                isDayTime = world.isDay();
                rainStrength = world.getRainLevel(1);
                isRaining = world.isRaining();
                dimension = world.dimensionType().effectsLocation().toLanguageKey();
            } catch (Exception ex) {

            }
        }
    }
    private static class GUIInfos {
        private class KeyCode {
            public String code;
            public String context;

            public KeyCode(String code, String context) {
                this.code = code;
                this.context = context;
            }
        }
        private boolean optionsGuiOpen;
        private boolean controlsGuiOpen;
        private boolean chatGuiOpen;
        private boolean keybindsGuiOpen;
        //private KeyCode[] keys;

        private void getInfos() {
            try {
                Minecraft client = Minecraft.getInstance();
                chatGuiOpen = client.screen instanceof ChatScreen;
                optionsGuiOpen = client.screen instanceof OptionsScreen;
                controlsGuiOpen = client.screen instanceof ControlsScreen;
                keybindsGuiOpen = client.screen instanceof KeyBindsScreen;
                /*keys = null;
                if (keybindsGuiOpen) {
                    KeyBinding[] temp = client.options.;
                    List<KeyCode> tempList = new ArrayList<>();
                    for (KeyBinding key : temp) {
                        System.out.println(key.toString())
                        if (!key.getTranslationKey().contains("unknown") && key.getTranslationKey().contains("keyboard")) {
                            String context = key.getCategory().equals("key.categories.inventory") ? "GUI" : "UNIVERSAL";
                            tempList.add(new KeyCode(key.getTranslationKey(), null, context));
                        }
                    }
                    keys = new KeyCode[tempList.size()];
                    keys = tempList.toArray(keys);
                }*/
            } catch (Exception ignore) {

            }
        }
    }
}
