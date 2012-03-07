package net.minecraft.src;

import java.io.*;
import net.minecraft.client.Minecraft;

public class mod_OldSurvivalMode extends BaseMod{
    public String getVersion(){
        return "1.2.3";
    }

    public mod_OldSurvivalMode(){
        OldSurvivalModeProperties oldsurvivalmodeproperties = new OldSurvivalModeProperties();
        try{
            File file = new File((new StringBuilder()).append(Minecraft.getMinecraftDir()).append("/config/OldSurvivalMode.properties").toString());
            boolean flag = file.createNewFile();
            if(flag){
                FileOutputStream fileoutputstream = new FileOutputStream(file);
                oldsurvivalmodeproperties.setProperty("AnimalsFleeWhenDamaged", Boolean.toString(false));
                oldsurvivalmodeproperties.setProperty("DisableFoodStacking", Boolean.toString(true));
                oldsurvivalmodeproperties.setProperty("InstantBow", Boolean.toString(true));
                oldsurvivalmodeproperties.setProperty("InstantFood", Boolean.toString(true));
                oldsurvivalmodeproperties.setProperty("DisableHunger", Boolean.toString(true));
                oldsurvivalmodeproperties.setProperty("DisableXP", Boolean.toString(true));
                oldsurvivalmodeproperties.setProperty("OldCombatSystem", Boolean.toString(true));
                oldsurvivalmodeproperties.setProperty("OldArmor", Boolean.toString(true));
                oldsurvivalmodeproperties.setProperty("InfiniteBow", Boolean.toString(true));
                oldsurvivalmodeproperties.setProperty("OldDrops", Boolean.toString(true));
                oldsurvivalmodeproperties.setProperty("AllowDebug", Boolean.toString(true));
                oldsurvivalmodeproperties.setProperty("PunchSheep", Boolean.toString(true));
                oldsurvivalmodeproperties.setProperty("DisableRareLoot", Boolean.toString(true));
                oldsurvivalmodeproperties.store(fileoutputstream, "Survival mode config");

                fileoutputstream.close();
            }
            oldsurvivalmodeproperties.load(new FileInputStream((new StringBuilder()).append(Minecraft.getMinecraftDir()).append("/config/OldSurvivalMode.properties").toString()));
            DisableXP = Boolean.parseBoolean(oldsurvivalmodeproperties.getProperty("DisableXP"));
            DisableHunger = Boolean.parseBoolean(oldsurvivalmodeproperties.getProperty("DisableHunger"));
            InstantFood = Boolean.parseBoolean(oldsurvivalmodeproperties.getProperty("InstantFood"));
            InstantBow = Boolean.parseBoolean(oldsurvivalmodeproperties.getProperty("InstantBow"));
            DisableFoodStacking = Boolean.parseBoolean(oldsurvivalmodeproperties.getProperty("DisableFoodStacking"));
            AnimalsFlee = Boolean.parseBoolean(oldsurvivalmodeproperties.getProperty("AnimalsFleeWhenDamaged"));
            OldCombatSystem = Boolean.parseBoolean(oldsurvivalmodeproperties.getProperty("OldCombatSystem"));
            OldArmor = Boolean.parseBoolean(oldsurvivalmodeproperties.getProperty("OldArmor"));
            InfiniteBow = Boolean.parseBoolean(oldsurvivalmodeproperties.getProperty("InfiniteBow"));
            OldDrops = Boolean.parseBoolean(oldsurvivalmodeproperties.getProperty("OldDrops"));
            AllowDebug = Boolean.parseBoolean(oldsurvivalmodeproperties.getProperty("AllowDebug"));
            PunchSheep = Boolean.parseBoolean(oldsurvivalmodeproperties.getProperty("PunchSheep"));
            DisableRareLoot = Boolean.parseBoolean(oldsurvivalmodeproperties.getProperty("DisableRareLoot"));
        }
        catch(IOException ioexception){
            ioexception.printStackTrace();
        }
        ModLoader.setInGameHook(this, true, true);
        setSwordDamage(OldCombatSystem);
        setArmorDamage(OldArmor);
//         ReplaceMobs();
    }
    
    private void setSwordDamage(boolean b){
        try{
            if (b){
                ModLoader.setPrivateValue(net.minecraft.src.ItemSword.class, Item.swordDiamond, 0, 10);
                ModLoader.setPrivateValue(net.minecraft.src.ItemSword.class, Item.swordSteel, 0, 8);
                ModLoader.setPrivateValue(net.minecraft.src.ItemSword.class, Item.swordStone, 0, 6);
            }else{
                ModLoader.setPrivateValue(net.minecraft.src.ItemSword.class, Item.swordDiamond, 0, 7);
                ModLoader.setPrivateValue(net.minecraft.src.ItemSword.class, Item.swordSteel, 0, 6);
                ModLoader.setPrivateValue(net.minecraft.src.ItemSword.class, Item.swordStone, 0, 5);
            }
            ModLoader.setPrivateValue(net.minecraft.src.ItemSword.class, Item.swordWood, 0, 4);
            ModLoader.setPrivateValue(net.minecraft.src.ItemSword.class, Item.swordGold, 0, 4);
        }catch(Exception exception){
            System.out.println(new StringBuilder().append("WTF? '").append(exception).append("' OMG EXCEPTION LOL"));
        }
    }
    
    private void setArmorDamage(boolean b){
        try{
            if (b){
                ModLoader.setPrivateValue(net.minecraft.src.ItemArmor.class, Item.helmetLeather, 2, 3);
                ModLoader.setPrivateValue(net.minecraft.src.ItemArmor.class, Item.helmetChain, 2, 3);
                ModLoader.setPrivateValue(net.minecraft.src.ItemArmor.class, Item.helmetSteel, 2, 3);
                ModLoader.setPrivateValue(net.minecraft.src.ItemArmor.class, Item.helmetGold, 2, 3);
                ModLoader.setPrivateValue(net.minecraft.src.ItemArmor.class, Item.plateLeather, 2, 8);
                ModLoader.setPrivateValue(net.minecraft.src.ItemArmor.class, Item.plateChain, 2, 8);
                ModLoader.setPrivateValue(net.minecraft.src.ItemArmor.class, Item.plateSteel, 2, 8);
                ModLoader.setPrivateValue(net.minecraft.src.ItemArmor.class, Item.plateGold, 2, 8);
                ModLoader.setPrivateValue(net.minecraft.src.ItemArmor.class, Item.legsLeather, 2, 6);
                ModLoader.setPrivateValue(net.minecraft.src.ItemArmor.class, Item.legsChain, 2, 6);
                ModLoader.setPrivateValue(net.minecraft.src.ItemArmor.class, Item.legsSteel, 2, 6);
                ModLoader.setPrivateValue(net.minecraft.src.ItemArmor.class, Item.legsGold, 2, 6);
                ModLoader.setPrivateValue(net.minecraft.src.ItemArmor.class, Item.bootsLeather, 2, 3);
                ModLoader.setPrivateValue(net.minecraft.src.ItemArmor.class, Item.bootsChain, 2, 3);
                ModLoader.setPrivateValue(net.minecraft.src.ItemArmor.class, Item.bootsSteel, 2, 3);
                ModLoader.setPrivateValue(net.minecraft.src.ItemArmor.class, Item.bootsGold, 2, 3);
                ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.helmetLeather, 145, 33 << 0);
                ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.helmetChain, 145, 33 << 1);
                ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.helmetSteel, 145, 33 << 2);
                ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.helmetGold, 145, 33 << 4);
                ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.helmetDiamond, 145, 33 << 3);
                ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.plateLeather, 145, 48 << 0);
                ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.plateChain, 145, 48 << 1);
                ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.plateSteel, 145, 48 << 2);
                ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.plateGold, 145, 48 << 4);
                ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.plateDiamond, 145, 48 << 3);
                ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.legsLeather, 145, 45 << 0);
                ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.legsChain, 145, 45 << 1);
                ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.legsSteel, 145, 45 << 2);
                ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.legsGold, 145, 45 << 4);
                ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.legsDiamond, 145, 45 << 3);
                ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.bootsLeather, 145, 39 << 0);
                ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.bootsChain, 145, 39 << 1);
                ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.bootsSteel, 145, 39 << 2);
                ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.bootsGold, 145, 39 << 4);
                ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.bootsDiamond, 145, 39 << 3);
            }else{
                ModLoader.setPrivateValue(net.minecraft.src.ItemArmor.class, Item.helmetLeather, 2, 1);
                ModLoader.setPrivateValue(net.minecraft.src.ItemArmor.class, Item.helmetChain, 2, 2);
                ModLoader.setPrivateValue(net.minecraft.src.ItemArmor.class, Item.helmetSteel, 2, 2);
                ModLoader.setPrivateValue(net.minecraft.src.ItemArmor.class, Item.helmetGold, 2, 2);
                ModLoader.setPrivateValue(net.minecraft.src.ItemArmor.class, Item.plateLeather, 2, 3);
                ModLoader.setPrivateValue(net.minecraft.src.ItemArmor.class, Item.plateChain, 2, 5);
                ModLoader.setPrivateValue(net.minecraft.src.ItemArmor.class, Item.plateSteel, 2, 6);
                ModLoader.setPrivateValue(net.minecraft.src.ItemArmor.class, Item.plateGold, 2, 5);
                ModLoader.setPrivateValue(net.minecraft.src.ItemArmor.class, Item.legsLeather, 2, 2);
                ModLoader.setPrivateValue(net.minecraft.src.ItemArmor.class, Item.legsChain, 2, 4);
                ModLoader.setPrivateValue(net.minecraft.src.ItemArmor.class, Item.legsSteel, 2, 5);
                ModLoader.setPrivateValue(net.minecraft.src.ItemArmor.class, Item.legsGold, 2, 3);
                ModLoader.setPrivateValue(net.minecraft.src.ItemArmor.class, Item.bootsLeather, 2, 1);
                ModLoader.setPrivateValue(net.minecraft.src.ItemArmor.class, Item.bootsChain, 2, 1);
                ModLoader.setPrivateValue(net.minecraft.src.ItemArmor.class, Item.bootsSteel, 2, 2);
                ModLoader.setPrivateValue(net.minecraft.src.ItemArmor.class, Item.bootsGold, 2, 1);
                ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.helmetLeather, 145, 55);
                ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.helmetChain, 145, 165);
                ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.helmetSteel, 145, 165);
                ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.helmetGold, 145, 77);
                ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.helmetDiamond, 145, 363);
                ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.plateLeather, 145, 80);
                ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.plateChain, 145, 240);
                ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.plateSteel, 145, 240);
                ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.plateGold, 145, 112);
                ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.plateDiamond, 145, 528);
                ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.legsLeather, 145, 75);
                ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.legsChain, 145, 225);
                ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.legsSteel, 145, 225);
                ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.legsGold, 145, 105);
                ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.legsDiamond, 145, 495);
                ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.bootsLeather, 145, 65);
                ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.bootsChain, 145, 195);
                ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.bootsSteel, 145, 195);
                ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.bootsGold, 145, 91);
                ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.bootsDiamond, 145, 429);
            }
            ModLoader.setPrivateValue(net.minecraft.src.ItemArmor.class, Item.helmetDiamond, 2, 3);
            ModLoader.setPrivateValue(net.minecraft.src.ItemArmor.class, Item.plateDiamond, 2, 8);
            ModLoader.setPrivateValue(net.minecraft.src.ItemArmor.class, Item.legsDiamond, 2, 6);
            ModLoader.setPrivateValue(net.minecraft.src.ItemArmor.class, Item.bootsDiamond, 2, 3);
        }catch(Exception exception){
            System.out.println(new StringBuilder().append("WTF? '").append(exception).append("' OMG EXCEPTION LOL"));
        }
    }
/*
    private static void replaceMobs(){
        try{
            ModLoader.RegisterEntityID(EntityZombie2.class, "Zombie", 54);
            ModLoader.RegisterEntityID(EntityPigZombie2.class, "PigZombie", 57);
            ModLoader.RegisterEntityID(EntityCow2.class, "Cow", 92);
            ModLoader.RegisterEntityID(EntityChicken2.class, "Chicken", 93);
            ModLoader.RegisterEntityID(EntityMooshroom2.class, "MushroomCow", 96);
        }catch (Exception exception){
            System.out.println(new StringBuilder().append("WTF? '").append(exception).append("' OMG EXCEPTION LOL"));
        }
    }
*/
    public boolean onTickInGame(float f, Minecraft minecraft){
        tickDebug(minecraft);
        return true;
    }

    public void tickDebug(Minecraft minecraft){
        if (AllowDebug){
            return;
        }
        minecraft.gameSettings.showDebugInfo = false;
    }

    public void load(){};

    public static boolean DisableXP;
    public static boolean DisableHunger;
    public static boolean InstantFood;
    public static boolean InstantBow;
    public static boolean DisableFoodStacking;
    public static boolean AnimalsFlee;
    public static boolean OldCombatSystem;
    public static boolean OldArmor;
    public static boolean InfiniteBow;
    public static boolean OldDrops;
    public static boolean AllowDebug;
    public static boolean PunchSheep;
    public static boolean DisableRareLoot;
}