package net.minecraft.src;

import net.minecraft.server.MinecraftServer;
import java.io.*;

public class mod_OldSurvivalMode extends BaseModMp{
    public mod_OldSurvivalMode(){
        PropertyManager pmanager = new PropertyManager(new File("server.properties"));
        DisableXP = pmanager.getBooleanProperty("disable-xp", true);
        DisableHunger = pmanager.getBooleanProperty("disable-hunger", true);
        DisableFoodStacking = pmanager.getBooleanProperty("disable-food-stacking", true);
        InstantFood = pmanager.getBooleanProperty("instant-food", true);
        InstantBow = pmanager.getBooleanProperty("instant-bow", true);
        AnimalsFlee = pmanager.getBooleanProperty("animals-flee", false);
        OldCombatSystem = pmanager.getBooleanProperty("old-combat", true);
        OldArmor = pmanager.getBooleanProperty("old-armor", true);
        InfiniteBow = pmanager.getBooleanProperty("infinite-bow", true);
        OldDrops = pmanager.getBooleanProperty("old-drops", true);
        PunchSheep = pmanager.getBooleanProperty("punch-sheep", true);
        DisableRareLoot = pmanager.getBooleanProperty("disable-rare-loot", true);
        SetSwordDamage(OldCombatSystem);
        SetSwordDamage(OldArmor);
//         ReplaceMobs();
    }

    public void load(){}

    public String getVersion(){
        return "1.1.0";
    }

    private void SetSwordDamage(boolean b){
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

    private void SetArmorDamage(boolean b){
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
    private static void ReplaceMobs(){
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

    public boolean hasClientSide()
    {
        return false;
    }

    public static boolean AnimalsFlee = false;
    public static boolean DisableXP = true;
    public static boolean DisableHunger = true;
    public static boolean InstantFood = true;
    public static boolean DisableFoodStacking = true;
    public static boolean InstantBow = true;
    public static boolean OldCombatSystem = true;
    public static boolean OldArmor = true;
    public static boolean InfiniteBow = true;
    public static boolean OldDrops = true;
    public static boolean PunchSheep = true;
    public static boolean DisableRareLoot = true;
/*
    public static Item appleRed = (new ItemFood2(4, 4, 0.3F, false)).setIconCoord(10, 0).setItemName("apple");
    public static Item appleGold = (new ItemFood2(66, 4, 1.2F, false)).setAlwaysEdible().setPotionEffect(Potion.regeneration.id, 5, 0, 1.0F).setIconCoord(11, 0).setItemName("appleGold");
    public static Item bread = (new ItemFood2(41, 5, 0.6F, false)).setIconCoord(9, 2).setItemName("bread");
    public static Item bowlSoup = (new ItemFood2(26, 8, false)).setIconCoord(8, 4).setItemName("mushroomStew");
    public static Item porkRaw = (new ItemFood2(63, 3, 0.3F, true)).setIconCoord(7, 5).setItemName("porkchopRaw");
    public static Item porkCooked = (new ItemFood2(64, 8, 0.8F, true)).setIconCoord(8, 5).setItemName("porkchopCooked");
    public static Item fishRaw = (new ItemFood2(93, 2, 0.3F, false)).setIconCoord(9, 5).setItemName("fishRaw");
    public static Item fishCooked = (new ItemFood2(94, 5, 0.6F, false)).setIconCoord(10, 5).setItemName("fishCooked");
    public static Item beefRaw = (new ItemFood2(107, 3, 0.3F, true)).setIconCoord(9, 6).setItemName("beefRaw");
    public static Item beefCooked = (new ItemFood2(108, 8, 0.8F, true)).setIconCoord(10, 6).setItemName("beefCooked");
    public static Item chickenRaw = (new ItemFood2(109, 2, 0.3F, true)).setPotionEffect(Potion.hunger.id, 30, 0, 0.3F).setIconCoord(9, 7).setItemName("chickenRaw");
    public static Item chickenCooked = (new ItemFood2(110, 6, 0.6F, true)).setIconCoord(10, 7).setItemName("chickenCooked");
    public static Item rottenFlesh = (new ItemFood2(111, 4, 0.1F, true)).setPotionEffect(Potion.hunger.id, 30, 0, 0.8F).setIconCoord(11, 5).setItemName("rottenFlesh");
    public static Item cookie = (new ItemFood2(101, 1, 0.1F, false)).setIconCoord(12, 5).setItemName("cookie");
    public static Item melon = (new ItemFood2(104, 2, 0.3F, false)).setIconCoord(13, 6).setItemName("melon");
    public static Item bow = (new ItemBow2(5)).setIconCoord(5, 1).setItemName("bow");
*/
}