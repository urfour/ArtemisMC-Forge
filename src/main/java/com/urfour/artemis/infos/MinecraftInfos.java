package com.urfour.artemis.infos;

import com.mojang.realmsclient.dto.PlayerInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiKeyBindingList;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

public class MinecraftInfos {
    private static final Logger LOG = LogManager.getLogger("artemis-infos");
    private PlayerInfos Player = new PlayerInfos();
    private WorldInfos World = new WorldInfos();
    private GUIInfos Gui = new GUIInfos();
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
        private HashMap<String, Boolean> PlayerEffects = new HashMap<>();
        private static final HashMap<String, Potion> TARGET_EFFECTS;
        private HashMap<String, String> Armor = new HashMap<>();
        private String LeftHandItem;
        private String RightHandItem;
        private int CurrentHotbarSlot;

        static {
            TARGET_EFFECTS = new HashMap<>();
            TARGET_EFFECTS.put("moveSpeed", MobEffects.SPEED);
            TARGET_EFFECTS.put("moveSlowdown", MobEffects.SLOWNESS);
            TARGET_EFFECTS.put("haste", MobEffects.HASTE);
            TARGET_EFFECTS.put("miningFatigue", MobEffects.MINING_FATIGUE);
            TARGET_EFFECTS.put("strength", MobEffects.STRENGTH);
            TARGET_EFFECTS.put("instantHealth", MobEffects.INSTANT_HEALTH);
            TARGET_EFFECTS.put("instantDamage", MobEffects.INSTANT_DAMAGE);
            TARGET_EFFECTS.put("jumpBoost", MobEffects.JUMP_BOOST);
            TARGET_EFFECTS.put("confusion", MobEffects.NAUSEA);
            TARGET_EFFECTS.put("regeneration", MobEffects.REGENERATION);
            TARGET_EFFECTS.put("resistance", MobEffects.RESISTANCE);
            TARGET_EFFECTS.put("fireResistance", MobEffects.FIRE_RESISTANCE);
            TARGET_EFFECTS.put("waterBreathing", MobEffects.WATER_BREATHING);
            TARGET_EFFECTS.put("invisibility", MobEffects.INVISIBILITY);
            TARGET_EFFECTS.put("blindness", MobEffects.BLINDNESS);
            TARGET_EFFECTS.put("nightVision", MobEffects.NIGHT_VISION);
            TARGET_EFFECTS.put("hunger", MobEffects.HUNGER);
            TARGET_EFFECTS.put("weakness", MobEffects.WEAKNESS);
            TARGET_EFFECTS.put("poison", MobEffects.POISON);
            TARGET_EFFECTS.put("wither", MobEffects.WITHER);
            TARGET_EFFECTS.put("healthBoost", MobEffects.HEALTH_BOOST);
            TARGET_EFFECTS.put("absorption", MobEffects.ABSORPTION);
            TARGET_EFFECTS.put("saturation", MobEffects.SATURATION);
            TARGET_EFFECTS.put("glowing", MobEffects.GLOWING);
            TARGET_EFFECTS.put("levitation", MobEffects.LEVITATION);
            TARGET_EFFECTS.put("luck", MobEffects.LUCK);
            TARGET_EFFECTS.put("badLuck", MobEffects.UNLUCK);
            TARGET_EFFECTS.put("slowFalling", null);
            TARGET_EFFECTS.put("conduitPower", null);
            TARGET_EFFECTS.put("dolphinsGrace", null);
            TARGET_EFFECTS.put("bad_omen", null);
            TARGET_EFFECTS.put("villageHero", null);
        }
        private void getInfos() {
            try {
                EntityPlayerSP player = Minecraft.getMinecraft().player;
                assert player != null;
                Health = player.getHealth();
                MaxHealth = player.getMaxHealth();
                Absorption = player.getAbsorptionAmount();
                IsDead = player.isDead;
                ArmorPoints = player.getTotalArmorValue();
                ExperienceLevel = player.experienceLevel;
                Experience = player.experience;
                FoodLevel = player.getFoodStats().getFoodLevel();
                SaturationLevel = player.getFoodStats().getSaturationLevel();
                IsSneaking = player.isSneaking();
                IsRidingHorse = player.isRidingHorse();
                IsBurning = player.isBurning();
                IsInWater = player.isInWater();
                for (PotionEffect effect : player.getActivePotionEffects()) {
                    if (TARGET_EFFECTS.containsKey(effect.getEffectName())) {
                        PlayerEffects.put(effect.getEffectName(), true);
                    }
                }
                String helmet = player.inventory.armorInventory.get(0).getDisplayName();
                String chestplate = player.inventory.armorInventory.get(1).getDisplayName();
                String leggings = player.inventory.armorInventory.get(2).getDisplayName();
                String boots = player.inventory.armorInventory.get(3).getDisplayName();
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
                String leftHandItem = player.getHeldItem(EnumHand.MAIN_HAND).getDisplayName();
                String rightHandItem = player.getHeldItem(EnumHand.OFF_HAND).getDisplayName();
                if (!leftHandItem.equals("Air")) {
                    LeftHandItem = leftHandItem;
                }
                if (!rightHandItem.equals("Air")) {
                    RightHandItem = rightHandItem;
                }
                CurrentHotbarSlot = player.inventory.currentItem;
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
                WorldClient world = Minecraft.getMinecraft().world;
                WorldTime = world.getWorldTime();
                IsDayTime = world.isDaytime();
                IsRaining = world.isRaining();
                RainStrength = world.rainingStrength;
                Dimension = world.provider.getDimensionType().getName();
                Biome = world.getBiome(Minecraft.getMinecraft().player.getPosition()).getBiomeName();
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
        private boolean OptionsGuiOpen;
        private boolean InventoryGuiOpen;
        private boolean ChatGuiOpen;
        private boolean KeybindsGuiOpen;
        private boolean PauseGuiOpen;
        private boolean DebugGuiOpen;
        private boolean F3GuiOpen;
        private boolean AdvancementsGuiOpen;
        private boolean RecipeGuiOpen;
        private KeyCode[] Keys;

        private void getInfos() {
            try {
                Minecraft client = Minecraft.getMinecraft();
                OptionsGuiOpen = client.currentScreen instanceof GuiOptions;
                InventoryGuiOpen = client.currentScreen instanceof GuiInventory;
                ChatGuiOpen = client.currentScreen instanceof GuiChat;
                PauseGuiOpen = client.currentScreen == null;
                DebugGuiOpen = client.gameSettings.showDebugInfo;
                F3GuiOpen = client.gameSettings.showDebugInfo;
                AdvancementsGuiOpen = client.currentScreen != null && client.currentScreen.getClass().getName().equals("net.minecraft.client.gui.advancements.GuiScreenAdvancements");
                RecipeGuiOpen = client.currentScreen != null && client.currentScreen.getClass().getName().equals("net.minecraft.client.gui.recipebook.GuiRecipeBook");
                Keys = new KeyCode[client.gameSettings.keyBindings.length];
                for (int i = 0; i < client.gameSettings.keyBindings.length; i++) {
                    Keys[i] = new KeyCode(client.gameSettings.keyBindings[i].getKeyCode() + "", client.gameSettings.keyBindings[i].getKeyDescription());
                }
            } catch (Exception ex) {

            }
        }
    }
}