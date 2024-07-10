package com.urfour.artemis.infos;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiKeyBindingList;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
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
        private final String RightHandItem = null;
        private int CurrentHotbarSlot;

        static {
            TARGET_EFFECTS = new HashMap<>();
            TARGET_EFFECTS.put("moveSpeed", Potion.moveSpeed);
            TARGET_EFFECTS.put("moveSlowdown", Potion.moveSlowdown);
            TARGET_EFFECTS.put("haste", Potion.digSpeed);
            TARGET_EFFECTS.put("miningFatigue", Potion.digSlowdown);
            TARGET_EFFECTS.put("strength", Potion.damageBoost);
            TARGET_EFFECTS.put("instantHealth", Potion.heal);
            TARGET_EFFECTS.put("instantDamage", Potion.harm);
            TARGET_EFFECTS.put("jumpBoost", Potion.jump);
            TARGET_EFFECTS.put("confusion", Potion.confusion);
            TARGET_EFFECTS.put("regeneration", Potion.regeneration);
            TARGET_EFFECTS.put("resistance", Potion.resistance);
            TARGET_EFFECTS.put("fireResistance", Potion.fireResistance);
            TARGET_EFFECTS.put("waterBreathing", Potion.waterBreathing);
            TARGET_EFFECTS.put("invisibility", Potion.invisibility);
            TARGET_EFFECTS.put("blindness", Potion.blindness);
            TARGET_EFFECTS.put("nightVision", Potion.nightVision);
            TARGET_EFFECTS.put("hunger", Potion.hunger);
            TARGET_EFFECTS.put("weakness", Potion.weakness);
            TARGET_EFFECTS.put("poison", Potion.poison);
            TARGET_EFFECTS.put("wither", Potion.wither);
            TARGET_EFFECTS.put("healthBoost", null);
            TARGET_EFFECTS.put("absorption", null);
            TARGET_EFFECTS.put("saturation", null);
            TARGET_EFFECTS.put("glowing", null);
            TARGET_EFFECTS.put("levitation", null);
            TARGET_EFFECTS.put("luck", null);
            TARGET_EFFECTS.put("badLuck", null);
            TARGET_EFFECTS.put("slowFalling", null);
            TARGET_EFFECTS.put("conduitPower", null);
            TARGET_EFFECTS.put("dolphinsGrace", null);
            TARGET_EFFECTS.put("bad_omen", null);
            TARGET_EFFECTS.put("villageHero", null);
        }
        private void getInfos() {
            try {
                EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
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
                if (player.inventory.armorInventory[0] != null)
                    Armor.put(player.inventory.armorInventory[0].getDisplayName(), "helmet");
                if (player.inventory.armorInventory[1] != null)
                    Armor.put(player.inventory.armorInventory[1].getDisplayName(), "chestplate");
                if (player.inventory.armorInventory[2] != null)
                    Armor.put(player.inventory.armorInventory[2].getDisplayName(), "leggings");
                if (player.inventory.armorInventory[3] != null)
                    Armor.put(player.inventory.armorInventory[3].getDisplayName(), "boots");
                if (player.getHeldItem() != null) {
                    LeftHandItem = player.getHeldItem().getDisplayName();
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

        private void getInfos() {
            try {
                WorldClient world = Minecraft.getMinecraft().theWorld;
                WorldTime = world.getWorldTime();
                IsDayTime = world.isDaytime();
                IsRaining = world.isRaining();
                RainStrength = world.rainingStrength;
                Dimension = world.provider.getDimensionName();
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
