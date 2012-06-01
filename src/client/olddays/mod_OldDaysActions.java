package net.minecraft.src;

import java.util.*;

public class mod_OldDaysActions extends mod_OldDays{
    public void load(){
        registerModule(this, 0);
        addProperty(1, "Punch TNT",             false, true,  "PunchTNT",        "");
        addProperty(2, "Extinguish TNT",        false, false, "ExtinguishTNT",   "");
        addProperty(3, "Smelt items on fire",   false, false, "SmeltOnFire",     "");
        addProperty(4, "Fire",                  3,     2,     "Fire",            "", new String[]{"Alpha", "Beta 1.5", "Beta 1.6"});
        addProperty(5, "Punch sheep",           false, true,  "PunchSheep",      "");
        addProperty(6, "Old tool durability",   false, false, "OldTools",        "");
        addProperty(7, "Mushroom spreading",    true,  true,  "ShroomSpreading", "");
        addProperty(8, "Solid TNT",             false, true,  "SolidTNT",        "");
        addProperty(9, "Big fences",            false, true,  "BigFences",       "");
        addProperty(10,"Less Nether lava flow", false, false, "LessLavaFlow",    "");
        loadModuleProperties();
        replaceBlocks();
    }

    public void callback (int i){
        switch(i){
            case 1: setBool(net.minecraft.src.BlockTNT2.class, "punchToActivate", PunchTNT); break;
            case 2: setBool(net.minecraft.src.EntityTNTPrimed.class, "extinguish", ExtinguishTNT); break;
            case 3: setBool(net.minecraft.src.EntityItem.class, "smeltOnFire", SmeltOnFire); break;
            case 4: setBool(net.minecraft.src.BlockFire.class, "oldFire", Fire<3);
                    setBool(net.minecraft.src.BlockFire.class, "infiniteBurn", Fire<2); break;
            case 5: setBool(net.minecraft.src.EntitySheep.class, "punchToShear", PunchSheep); break;
            case 6: setToolDurability(OldTools); break;
            case 7: setBool(net.minecraft.src.BlockMushroom.class, "spreading", ShroomSpreading); break;
            case 8: setSolidTNT(SolidTNT); break;
            case 9: setBool(net.minecraft.src.BlockFence2.class, "bigfences", BigFences); break;
            case 10:setBool(net.minecraft.src.BlockFlowing.class, "lessNetherLavaFlow", LessLavaFlow); break;
        }
    }

    public static boolean SmeltOnFire;
    public static boolean PunchTNT = true;
    public static boolean ExtinguishTNT;
    public static int Fire = 2;
    public static boolean PunchSheep = true;
    public static boolean OldTools;
    public static boolean ShroomSpreading = true;
    public static boolean SolidTNT = true;
    public static boolean BigFences = true;
    public static boolean LessLavaFlow;
//Old blocks
//Unflammable fences and stairs

    private void setSolidTNT(boolean b){
        try{
            ModLoader.setPrivateValue(net.minecraft.src.Material.class, Material.tnt, 33, !b);
        }catch(Exception ex){
            System.out.println(ex);
        }
    }

    private void replaceBlocks(){
        try{
            Block.blocksList[Block.tnt.blockID] = null;
            BlockTNT2 customtnt = (BlockTNT2)(new BlockTNT2(46, 8));
            customtnt.setHardness(0.0F);
            customtnt.setStepSound(Block.soundGrassFootstep);
            customtnt.setBlockName("tnt");
            Block.blocksList[Block.tnt.blockID] = customtnt;
            Block.blocksList[Block.fence.blockID] = null;
            BlockFence2 customfence = (BlockFence2)(new BlockFence2(85, 4));
            customfence.setHardness(2.0F);
            customfence.setResistance(5F);
            customfence.setStepSound(Block.soundWoodFootstep);
            customfence.setBlockName("fence");
            Block.blocksList[Block.fence.blockID] = customfence;
        }catch (Exception exception){
            System.out.println(exception);
        }
    }

    private void setToolDurability(boolean b){
        try{
            int wood = 59;
            int stone = 131;
            int iron = 250;
            int gold = 32;
            int diamond = 1561;
            if (b){
                wood = 31;
                stone = 63;
                iron = 127;
                gold = 15;
                diamond = 255;
            }
            ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.pickaxeWood, 145, wood);
            ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.axeWood, 145, wood);
            ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.shovelWood, 145, wood);
            ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.swordWood, 145, wood);
            ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.hoeWood, 145, wood);
            ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.pickaxeStone, 145, stone);
            ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.axeStone, 145, stone);
            ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.shovelStone, 145, stone);
            ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.swordStone, 145, stone);
            ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.hoeStone, 145, stone);
            ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.pickaxeSteel, 145, iron);
            ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.axeSteel, 145, iron);
            ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.shovelSteel, 145, iron);
            ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.swordSteel, 145, iron);
            ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.hoeSteel, 145, iron);
            ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.pickaxeGold, 145, gold);
            ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.axeGold, 145, gold);
            ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.shovelGold, 145, gold);
            ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.swordGold, 145, gold);
            ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.hoeGold, 145, gold);
            ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.pickaxeDiamond, 145, diamond);
            ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.axeDiamond, 145, diamond);
            ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.shovelDiamond, 145, diamond);
            ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.swordDiamond, 145, diamond);
            ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.hoeDiamond, 145, diamond);
        }catch(Exception ex){
            System.out.println(ex);
        }
    }
}