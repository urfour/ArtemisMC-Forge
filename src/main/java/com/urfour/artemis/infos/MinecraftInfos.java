package com.urfour.artemis.infos;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.controls.ControlsScreen;
import net.minecraft.client.gui.screens.controls.KeyBindsScreen;
import net.minecraft.client.gui.screens.OptionsScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.Biome;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MinecraftInfos {
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
        private static final HashMap<String, MobEffect> TARGET_EFFECTS;
        private HashMap<String, String> Armor = new HashMap<>();
        private String LeftHandItem;
        private String RightHandItem;
        private int CurrentHotbarSlot;

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
                IsBurning = player.isOnFire();
                IsInWater = player.isInWater();
                for (Map.Entry<String, MobEffect> effect : TARGET_EFFECTS.entrySet())
                    PlayerEffects.put(effect.getKey(), player.getEffect(effect.getValue()) != null);
                ArrayList<String> armorItems = new ArrayList<>();
                player.getArmorSlots().forEach(item -> armorItems.add(testIfAir(item)));
                RightHandItem = testIfAir(player.getMainHandItem());
                LeftHandItem = testIfAir(player.getOffhandItem());
                CurrentHotbarSlot = player.getInventory().selected;
                if (!armorItems.isEmpty()) {
                    Armor.put("boots", armorItems.get(0));
                    Armor.put("leggings", armorItems.get(1));
                    Armor.put("chestplate", armorItems.get(2));
                    Armor.put("helmet", armorItems.get(3));
                }
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
                assert world != null;
                WorldTime = world.getDayTime();
                IsDayTime = world.isDay();
                RainStrength = world.getRainLevel(1);
                IsRaining = world.isRaining();
                Dimension = world.dimensionType().effectsLocation().toLanguageKey();
                Holder<Biome> biomeHolder = world.getBiome(Minecraft.getInstance().player.blockPosition());
                Biome biome = biomeHolder.value();
                biomeHolder.unwrapKey().ifPresent(key -> Biome = Util.makeDescriptionId("biome", key.location()));
            } catch (Exception ex) {

            }
        }
    }
    private static class GUIInfos {
        private boolean OptionsGuiOpen;
        private boolean ControlsGuiOpen;
        private boolean ChatGuiOpen;
        private boolean KeybindsGuiOpen;

        private void getInfos() {
            try {
                Minecraft client = Minecraft.getInstance();
                ChatGuiOpen = client.screen instanceof ChatScreen;
                OptionsGuiOpen = client.screen instanceof OptionsScreen;
                ControlsGuiOpen = client.screen instanceof ControlsScreen;
                KeybindsGuiOpen = client.screen instanceof KeyBindsScreen;
            } catch (Exception ignore) {

            }
        }
    }
}
