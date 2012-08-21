package net.minecraft.src;

import java.util.*;

public class ODActions extends OldDaysModule{
    public ODActions(mod_OldDays c){
        super(c, 0, "Actions");
        new OldDaysPropertyBool(this, 1, false, true,  "PunchTNT");
        new OldDaysPropertyBool(this, 2, false, false, "ExtinguishTNT");
        new OldDaysPropertyBool(this, 3, false, false, "SmeltOnFire");
        new OldDaysPropertyInt(this,  4, 2,     1,     "Fire", 2).setUseNames();
        new OldDaysPropertyBool(this, 5, false, true,  "PunchSheep");
        new OldDaysPropertyBool(this, 6, false, false, "OldTools");
        new OldDaysPropertyBool(this, 7, true,  true,  "ShroomSpreading");
        new OldDaysPropertyBool(this, 8, false, true,  "SolidTNT");
        new OldDaysPropertyBool(this, 9, false, true,  "BigFences");
        new OldDaysPropertyBool(this, 10,false, false, "LessLavaFlow");
        new OldDaysPropertyBool(this, 11,false, false, "FogKey");
        new OldDaysPropertyBool(this, 12,false, false, "LogRotation");
        new OldDaysPropertyBool(this, 13,false, true,  "OldCrops");
        replaceBlocks();
        registerKey(keyFog = new KeyBinding("Toggle Fog", 33));
    }

    public static int FogKeyd = 0;

    public void callback (int i){
        switch(i){
            case 1: set(net.minecraft.src.BlockTNT2.class, "punchToActivate", PunchTNT); break;
            case 2: set(net.minecraft.src.EntityTNTPrimed2.class, "extinguish", ExtinguishTNT); break;
            case 3: set(net.minecraft.src.EntityItem.class, "smeltOnFire", SmeltOnFire); break;
            case 4: set(net.minecraft.src.BlockFire.class, "oldFire", Fire<2);
                    set(net.minecraft.src.BlockFire.class, "infiniteBurn", Fire<1); break;
            case 5: set(net.minecraft.src.EntitySheep.class, "punchToShear", PunchSheep); break;
            case 6: setToolDurability(OldTools); break;
            case 7: set(net.minecraft.src.BlockMushroom.class, "spreading", ShroomSpreading); break;
            case 8: setSolidTNT(SolidTNT); break;
            case 9: set(net.minecraft.src.BlockFence2.class, "bigfences", BigFences); break;
            case 10:set(net.minecraft.src.BlockFlowing.class, "lessNetherLavaFlow", LessLavaFlow); break;
            case 12:set(net.minecraft.src.BlockLog2.class, "rotate", LogRotation); break;
            case 13:set(net.minecraft.src.BlockFarmlandOld.class, "oldbreaking", OldCrops); break;
        }
    }

    public void catchKeyEvent(KeyBinding keybinding){
        if (keybinding==keyFog && minecraft.currentScreen==null && FogKey){
            boolean flag = org.lwjgl.input.Keyboard.isKeyDown(42) | org.lwjgl.input.Keyboard.isKeyDown(54);
            minecraft.gameSettings.setOptionValue(EnumOptions.RENDER_DISTANCE, flag ? -1 : 1);
        }
    }

    public static boolean SmeltOnFire;
    public static boolean PunchTNT = true;
    public static boolean ExtinguishTNT;
    public static int Fire = 1;
    public static boolean PunchSheep = true;
    public static boolean OldTools;
    public static boolean ShroomSpreading = true;
    public static boolean SolidTNT = true;
    public static boolean BigFences = true;
    public static boolean LessLavaFlow;
    public static boolean FogKey;
    public static boolean LogRotation;
    public static boolean OldCrops = true;
    public KeyBinding keyFog;

    private void setSolidTNT(boolean b){
        mod_OldDays.setField(net.minecraft.src.Material.class, Material.tnt, 33, !b);
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
            Block.blocksList[Block.tilledField.blockID] = null;
            BlockFarmlandOld customTilledField = (BlockFarmlandOld)(new BlockFarmlandOld(60));
            customTilledField.setHardness(0.6F);
            customTilledField.setStepSound(Block.soundGravelFootstep);
            customTilledField.setBlockName("farmland");
            customTilledField.setRequiresSelfNotify();
            Block.blocksList[Block.tilledField.blockID] = customTilledField;
            Block.blocksList[Block.wood.blockID] = null;
            BlockLog2 customWood = (BlockLog2)(new BlockLog2(17));
            customWood.setHardness(2.0F);
            customWood.setStepSound(Block.soundWoodFootstep);
            customWood.setBlockName("log");
            customWood.setRequiresSelfNotify();
            Block.blocksList[Block.wood.blockID] = customWood;
        }catch (Exception exception){
            System.out.println(exception);
        }
    }

    private void setToolDurability(boolean b){
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
        mod_OldDays.setField(net.minecraft.src.Item.class, Item.pickaxeWood, 149, wood);
        mod_OldDays.setField(net.minecraft.src.Item.class, Item.axeWood, 149, wood);
        mod_OldDays.setField(net.minecraft.src.Item.class, Item.shovelWood, 149, wood);
        mod_OldDays.setField(net.minecraft.src.Item.class, Item.swordWood, 149, wood);
        mod_OldDays.setField(net.minecraft.src.Item.class, Item.hoeWood, 149, wood);
        mod_OldDays.setField(net.minecraft.src.Item.class, Item.pickaxeStone, 149, stone);
        mod_OldDays.setField(net.minecraft.src.Item.class, Item.axeStone, 149, stone);
        mod_OldDays.setField(net.minecraft.src.Item.class, Item.shovelStone, 149, stone);
        mod_OldDays.setField(net.minecraft.src.Item.class, Item.swordStone, 149, stone);
        mod_OldDays.setField(net.minecraft.src.Item.class, Item.hoeStone, 149, stone);
        mod_OldDays.setField(net.minecraft.src.Item.class, Item.pickaxeSteel, 149, iron);
        mod_OldDays.setField(net.minecraft.src.Item.class, Item.axeSteel, 149, iron);
        mod_OldDays.setField(net.minecraft.src.Item.class, Item.shovelSteel, 149, iron);
        mod_OldDays.setField(net.minecraft.src.Item.class, Item.swordSteel, 149, iron);
        mod_OldDays.setField(net.minecraft.src.Item.class, Item.hoeSteel, 149, iron);
        mod_OldDays.setField(net.minecraft.src.Item.class, Item.pickaxeGold, 149, gold);
        mod_OldDays.setField(net.minecraft.src.Item.class, Item.axeGold, 149, gold);
        mod_OldDays.setField(net.minecraft.src.Item.class, Item.shovelGold, 149, gold);
        mod_OldDays.setField(net.minecraft.src.Item.class, Item.swordGold, 149, gold);
        mod_OldDays.setField(net.minecraft.src.Item.class, Item.hoeGold, 149, gold);
        mod_OldDays.setField(net.minecraft.src.Item.class, Item.pickaxeDiamond, 149, diamond);
        mod_OldDays.setField(net.minecraft.src.Item.class, Item.axeDiamond, 149, diamond);
        mod_OldDays.setField(net.minecraft.src.Item.class, Item.shovelDiamond, 149, diamond);
        mod_OldDays.setField(net.minecraft.src.Item.class, Item.swordDiamond, 149, diamond);
        mod_OldDays.setField(net.minecraft.src.Item.class, Item.hoeDiamond, 149, diamond);
    }
}