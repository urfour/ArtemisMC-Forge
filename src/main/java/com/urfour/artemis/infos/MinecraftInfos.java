package com.urfour.artemis.infos;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.options.OptionsScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.advancements.AdvancementsScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.client.KeyMapping;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

public class MinecraftInfos {
    private static final Logger LOG = LogManager.getLogger("artemis-infos");
    private final PlayerInfos Player = new PlayerInfos();
    private final WorldInfos World = new WorldInfos();
    private final GUIInfos Gui = new GUIInfos();

    public void update() {
        Player.getInfos();
        World.getInfos();
        Gui.getInfos();
    }

    private static class PlayerInfos {
        private boolean InGame;
        private float Health;
        private float MaxHealth;
        private float Absorption;
        private boolean IsDead;
        private int ArmorPoints;
        private int ExperienceLevel;
        private float Experience;
        private int FoodLevel;
        private float SaturationLevel;
        private boolean IsSneaking;
        private boolean IsRidingHorse;
        private boolean IsBurning;
        private boolean IsInWater;
        private final HashMap<String, Boolean> PlayerEffects = new HashMap<>();
        private static final HashMap<String, String> EFFECT_TO_KEY;
        private final HashMap<String, String> Armor = new HashMap<>();
        private String LeftHandItem;
        private String RightHandItem;
        private int CurrentHotbarSlot;

        static {
            EFFECT_TO_KEY = new HashMap<>();
            EFFECT_TO_KEY.put("speed", "moveSpeed");
            EFFECT_TO_KEY.put("slowness", "moveSlowdown");
            EFFECT_TO_KEY.put("haste", "haste");
            EFFECT_TO_KEY.put("mining_fatigue", "miningFatigue");
            EFFECT_TO_KEY.put("strength", "strength");
            EFFECT_TO_KEY.put("instant_health", "instantHealth");
            EFFECT_TO_KEY.put("instant_damage", "instantDamage");
            EFFECT_TO_KEY.put("jump_boost", "jumpBoost");
            EFFECT_TO_KEY.put("nausea", "confusion");
            EFFECT_TO_KEY.put("regeneration", "regeneration");
            EFFECT_TO_KEY.put("resistance", "resistance");
            EFFECT_TO_KEY.put("fire_resistance", "fireResistance");
            EFFECT_TO_KEY.put("water_breathing", "waterBreathing");
            EFFECT_TO_KEY.put("invisibility", "invisibility");
            EFFECT_TO_KEY.put("blindness", "blindness");
            EFFECT_TO_KEY.put("night_vision", "nightVision");
            EFFECT_TO_KEY.put("hunger", "hunger");
            EFFECT_TO_KEY.put("weakness", "weakness");
            EFFECT_TO_KEY.put("poison", "poison");
            EFFECT_TO_KEY.put("wither", "wither");
            EFFECT_TO_KEY.put("health_boost", "healthBoost");
            EFFECT_TO_KEY.put("absorption", "absorption");
            EFFECT_TO_KEY.put("saturation", "saturation");
            EFFECT_TO_KEY.put("glowing", "glowing");
            EFFECT_TO_KEY.put("levitation", "levitation");
            EFFECT_TO_KEY.put("luck", "luck");
            EFFECT_TO_KEY.put("unluck", "badLuck");
            EFFECT_TO_KEY.put("slow_falling", "slowFalling");
            EFFECT_TO_KEY.put("conduit_power", "conduitPower");
            EFFECT_TO_KEY.put("dolphins_grace", "dolphinsGrace");
            EFFECT_TO_KEY.put("bad_omen", "bad_omen");
            EFFECT_TO_KEY.put("hero_of_the_village", "villageHero");
        }

        private void getInfos() {
            try {
                LocalPlayer player = Minecraft.getInstance().player;
                assert player != null;
                Health = player.getHealth();
                MaxHealth = player.getMaxHealth();
                Absorption = player.getAbsorptionAmount();
                IsDead = !player.isAlive();
                ArmorPoints = player.getArmorValue();
                ExperienceLevel = player.experienceLevel;
                Experience = player.experienceProgress;
                FoodLevel = player.getFoodData().getFoodLevel();
                SaturationLevel = player.getFoodData().getSaturationLevel();
                IsSneaking = player.isCrouching();
                IsRidingHorse = player.getVehicle() instanceof AbstractHorse;
                IsBurning = player.isOnFire();
                IsInWater = player.isInWater();

                PlayerEffects.clear();
                for (MobEffectInstance effectInstance : player.getActiveEffects()) {
                    Holder<MobEffect> effectHolder = effectInstance.getEffect();
                    String registryPath = effectHolder.unwrapKey()
                            .map(key -> key.location().getPath())
                            .orElse(null);
                    
                    if (registryPath != null) {
                        String artemisKey = EFFECT_TO_KEY.get(registryPath);
                        if (artemisKey != null) {
                            PlayerEffects.put(artemisKey, true);
                        }
                    }
                }

                Armor.clear();
                ItemStack helmetStack = player.getInventory().armor.get(3);
                ItemStack chestStack = player.getInventory().armor.get(2);
                ItemStack leggingsStack = player.getInventory().armor.get(1);
                ItemStack bootsStack = player.getInventory().armor.get(0);

                String helmet = helmetStack.isEmpty() ? "Air" : helmetStack.getHoverName().getString();
                String chestplate = chestStack.isEmpty() ? "Air" : chestStack.getHoverName().getString();
                String leggings = leggingsStack.isEmpty() ? "Air" : leggingsStack.getHoverName().getString();
                String boots = bootsStack.isEmpty() ? "Air" : bootsStack.getHoverName().getString();

                if (!helmet.equals("Air")) {
                    Armor.put(helmet, "helmet");
                }
                if (!chestplate.equals("Air")) {
                    Armor.put(chestplate, "chestplate");
                }
                if (!leggings.equals("Air")) {
                    Armor.put(leggings, "leggings");
                }
                if (!boots.equals("Air")) {
                    Armor.put(boots, "boots");
                }
                Armor.put("helmet", helmet);
                Armor.put("chestplate", chestplate);
                Armor.put("leggings", leggings);
                Armor.put("boots", boots);

                ItemStack mainHandStack = player.getItemInHand(InteractionHand.MAIN_HAND);
                ItemStack offHandStack = player.getItemInHand(InteractionHand.OFF_HAND);

                LeftHandItem = mainHandStack.isEmpty() ? "Air" : mainHandStack.getHoverName().getString();
                RightHandItem = offHandStack.isEmpty() ? "Air" : offHandStack.getHoverName().getString();

                CurrentHotbarSlot = player.getInventory().selected;
                InGame = true;
            } catch (Exception ex) {
                InGame = false;
            }
        }
    }

    private static class WorldInfos {
        private long WorldTime;
        private boolean IsDayTime;
        private boolean IsRaining;
        private float RainStrength;
        private String Dimension;
        private String Biome;

        private void getInfos() {
            try {
                ClientLevel world = Minecraft.getInstance().level;
                LocalPlayer player = Minecraft.getInstance().player;
                assert world != null && player != null;
                WorldTime = world.getDayTime();
                IsDayTime = world.isDay();
                IsRaining = world.isRaining();
                RainStrength = world.getRainLevel(1.0F);
                Dimension = world.dimension().location().toString();

                BlockPos pos = player.blockPosition();
                Holder<net.minecraft.world.level.biome.Biome> biomeHolder = world.getBiome(pos);
                Biome = biomeHolder.unwrapKey().map(key -> key.location().getPath()).orElse("unknown");
            } catch (Exception ex) {
                // Ignore
            }
        }
    }

    private static class GUIInfos {
        private static class KeyCode {
            public String code;
            public String context;

            public KeyCode(String code, String context) {
                this.code = code;
                this.context = context;
            }
        }

        private boolean OptionsGuiOpen;
        private boolean InventoryGuiOpen;
        private boolean ChatGuiOpen;
        private boolean PauseGuiOpen;
        private boolean DebugGuiOpen;
        private boolean F3GuiOpen;
        private boolean AdvancementsGuiOpen;
        private boolean RecipeGuiOpen;
        private KeyCode[] Keys;

        private void getInfos() {
            try {
                Minecraft client = Minecraft.getInstance();
                OptionsGuiOpen = client.screen instanceof OptionsScreen;
                InventoryGuiOpen = client.screen instanceof InventoryScreen;
                ChatGuiOpen = client.screen instanceof ChatScreen;
                PauseGuiOpen = client.screen == null;
                DebugGuiOpen = client.getDebugOverlay().showDebugScreen();
                F3GuiOpen = client.getDebugOverlay().showDebugScreen();
                AdvancementsGuiOpen = client.screen instanceof AdvancementsScreen;
                RecipeGuiOpen = client.screen instanceof RecipeUpdateListener listener && listener.getRecipeBookComponent().isVisible();

                if (client.options.keyMappings != null) {
                    Keys = new KeyCode[client.options.keyMappings.length];
                    for (int i = 0; i < client.options.keyMappings.length; i++) {
                        KeyMapping mapping = client.options.keyMappings[i];
                        Keys[i] = new KeyCode(mapping.getKey().getValue() + "", mapping.getName());
                    }
                }
            } catch (Exception ex) {
                // Ignore
            }
        }
    }
}