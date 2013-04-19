package net.minecraft.src;

import java.util.*;

public class ODActions extends OldDaysModule{
    public ODActions(mod_OldDays c){
        super(c, 0, "Actions");
        new OldDaysPropertyBool(this, 1, true,  false, "PunchTNT");
        new OldDaysPropertyBool(this, 2, false, false, "ExtinguishTNT");
        new OldDaysPropertyBool(this, 3, false, false, "SmeltOnFire");
        new OldDaysPropertyInt(this,  4, 0,     3,     "Fire", 3).setUseNames();
        new OldDaysPropertyBool(this, 5, true,  false, "PunchSheep");
        new OldDaysPropertyInt(this,  6, 2,     2,     "Durability", 2).setUseNames();
        new OldDaysPropertyBool(this, 7, true,  true,  "ShroomSpreading");
        new OldDaysPropertyBool(this, 8, true,  false, "SolidTNT");
        new OldDaysPropertyBool(this, 9, true,  false, "BigFences");
        new OldDaysPropertyBool(this, 10,false, false, "LessLavaFlow");
        new OldDaysPropertyBool(this, 11,true,  false, "FogKey");
        new OldDaysPropertyBool(this, 12,false, true,  "LogRotation");
        new OldDaysPropertyBool(this, 13,true,  false, "OldCrops");
        new OldDaysPropertyBool(this, 14,false, false, "TimeControl");
        new OldDaysPropertyBool(this, 15,false, false, "OldStairs");
        new OldDaysPropertyBool(this, 16,true,  false, "OldBoatBreaking");
        new OldDaysPropertyBool(this, 17,true,  false, "OldHardness");
        new OldDaysPropertyBool(this, 18,false, true,  "Apples");
        new OldDaysPropertyBool(this, 19,true,  false, "OldBoneMeal");
        new OldDaysPropertyBool(this, 20,false, false, "SnowFallOnGlass");
        replaceBlocks();
        replaceTools();
        registerKey(keyFog = new KeyBinding("Toggle Fog", 33));
    }

    @Override
    public void callback (int i){
        switch(i){
            case 1: set(BlockTNT2.class, "punchToActivate", PunchTNT); break;
            case 2: set(EntityTNTPrimed2.class, "extinguish", ExtinguishTNT); break;
            case 3: set(EntityItem.class, "smeltOnFire", SmeltOnFire); break;
            case 4: set(BlockFire.class, "fixedDamage", Fire<3);
                    set(BlockFire.class, "oldFire", Fire<2);
                    set(BlockFire.class, "infiniteBurn", Fire<1); break;
            case 5: set(EntitySheep.class, "punchToShear", PunchSheep); break;
            case 6: setToolDurability(Durability); break;
            case 7: set(BlockMushroom.class, "spreading", ShroomSpreading); break;
            case 8: setSolidTNT(SolidTNT); break;
            case 9: set(BlockFence2.class, "bigfences", BigFences); break;
            case 10:set(BlockFlowing.class, "lessNetherLavaFlow", LessLavaFlow); break;
            case 12:set(BlockLog2.class, "rotate", LogRotation); break;
            case 13:set(BlockFarmlandOld.class, "oldbreaking", OldCrops); break;
            case 14:set(net.minecraft.client.Minecraft.class, "timecontrol", TimeControl); break;
            case 15:set(BlockStairs.class, "oldstairs", OldStairs); break;
            case 16:set(EntityBoat.class, "oldbreaking", OldBoatBreaking); break;
            case 17:set(ItemAxe2.class, "oldhardness", OldHardness);
                    set(ItemPickaxe2.class, "oldhardness", OldHardness);
                    mod_OldDays.setField(ItemTool.class, Item.shovelWood, 0, OldHardness ? oldSpadeBlocks : spadeBlocks);
                    mod_OldDays.setField(ItemTool.class, Item.shovelStone, 0, OldHardness ? oldSpadeBlocks : spadeBlocks);
                    mod_OldDays.setField(ItemTool.class, Item.shovelSteel, 0, OldHardness ? oldSpadeBlocks : spadeBlocks);
                    mod_OldDays.setField(ItemTool.class, Item.shovelGold, 0, OldHardness ? oldSpadeBlocks : spadeBlocks);
                    mod_OldDays.setField(ItemTool.class, Item.shovelDiamond, 0, OldHardness ? oldSpadeBlocks : spadeBlocks);
                    mod_OldDays.setField(Block.class, Block.obsidian, 178, OldHardness ? 10F : 50F); break;
            case 18:set(BlockLeaves.class, "apples", Apples); break;
            case 19:set(ItemDye.class, "oldBoneMeal", OldBoneMeal); break;
            case 20:set(BlockSnow.class, "snowOnGlass", SnowFallOnGlass); break;
        }
    }

    @Override
    public void catchKeyEvent(KeyBinding keybinding){
        if (keybinding==keyFog && minecraft.currentScreen==null && FogKey){
            boolean flag = org.lwjgl.input.Keyboard.isKeyDown(42) || org.lwjgl.input.Keyboard.isKeyDown(54);
            minecraft.gameSettings.setOptionValue(EnumOptions.RENDER_DISTANCE, flag ? -1 : 1);
        }
    }

    public static boolean SmeltOnFire;
    public static boolean PunchTNT = true;
    public static boolean ExtinguishTNT;
    public static int Fire = 0;
    public static boolean PunchSheep = true;
    public static int Durability = 0;
    public static boolean ShroomSpreading = true;
    public static boolean SolidTNT = true;
    public static boolean BigFences = true;
    public static boolean LessLavaFlow;
    public static boolean FogKey;
    public static boolean LogRotation;
    public static boolean OldCrops = true;
    public static boolean TimeControl;
    public static boolean OldStairs;
    public static boolean OldBoatBreaking = true;
    public static boolean OldHardness = true;
    public static boolean Apples;
    public static boolean OldBoneMeal = true;
    public static boolean SnowFallOnGlass;
    public KeyBinding keyFog;

    private static Block[] oldSpadeBlocks = (new Block[]{Block.grass, Block.dirt, Block.sand, Block.gravel, Block.snow, Block.blockSnow, Block.blockClay, Block.tilledField});
    private static Block[] spadeBlocks = ((Block[])mod_OldDays.getField(ItemSpade.class, null, 0));

    private void setSolidTNT(boolean b){
        mod_OldDays.setField(Material.class, Material.tnt, 34, !b);
    }

    private void replaceBlocks(){
        try{
            Block.blocksList[Block.tnt.blockID] = null;
            BlockTNT2 customtnt = (BlockTNT2)(new BlockTNT2(46));
            customtnt.setHardness(0.0F);
            customtnt.setStepSound(Block.soundGrassFootstep);
            customtnt.getIndirectPowerOutput("tnt");
            Block.blocksList[Block.tnt.blockID] = customtnt;
            mod_OldDays.setField(Block.class, null, 64, customtnt);//Block: tnt
            Block.blocksList[Block.fence.blockID] = null;
            BlockFence2 customfence = (BlockFence2)(new BlockFence2(85, "wood", Material.wood));
            customfence.setHardness(2.0F);
            customfence.setResistance(5F);
            customfence.setStepSound(Block.soundWoodFootstep);
            customfence.getIndirectPowerOutput("fence");
            Block.blocksList[Block.fence.blockID] = customfence;
            mod_OldDays.setField(Block.class, null, 103, customfence);//Block: fence
            Block.blocksList[Block.tilledField.blockID] = null;
            BlockFarmlandOld customTilledField = (BlockFarmlandOld)(new BlockFarmlandOld(60));
            customTilledField.setHardness(0.6F);
            customTilledField.setStepSound(Block.soundGravelFootstep);
            customTilledField.getIndirectPowerOutput("farmland");
            Block.blocksList[Block.tilledField.blockID] = customTilledField;
            mod_OldDays.setField(Block.class, null, 78, customTilledField);//Block: tilledField
            Block.blocksList[Block.wood.blockID] = null;
            BlockLog2 customWood = (BlockLog2)(new BlockLog2(17));
            customWood.setHardness(2.0F);
            customWood.setStepSound(Block.soundWoodFootstep);
            customWood.getIndirectPowerOutput("log");
            Block.blocksList[Block.wood.blockID] = customWood;
            mod_OldDays.setField(Block.class, null, 35, customWood);//Block: wood
            Item.itemsList[Block.wood.blockID] = null;
            Item.itemsList[Block.wood.blockID] = (new ItemMultiTextureTile(Block.wood.blockID - 256, Block.wood, BlockLog.woodType)).setUnlocalizedName("log");
        }catch (Exception exception){
            System.out.println(exception);
        }
    }

    private void replaceTools(){
        Item.itemsList[256 + 14] = null;
        ItemPickaxe2 pickaxeWood = new ItemPickaxe2(14, EnumToolMaterial.WOOD);
        pickaxeWood.setUnlocalizedName("pickaxeWood");
        mod_OldDays.setField(Item.class, null, 17, pickaxeWood);//Item: pickaxeWood
        Item.itemsList[256 + 18] = null;
        ItemPickaxe2 pickaxeStone = new ItemPickaxe2(18, EnumToolMaterial.STONE);
        pickaxeStone.setUnlocalizedName("pickaxeStone");
        mod_OldDays.setField(Item.class, null, 21, pickaxeStone);//Item: pickaxeStone
        Item.itemsList[256 + 1] = null;
        ItemPickaxe2 pickaxeSteel = new ItemPickaxe2(1, EnumToolMaterial.IRON);
        pickaxeSteel.setUnlocalizedName("pickaxeIron");
        mod_OldDays.setField(Item.class, null, 4, pickaxeSteel);//Item: pickaxeSteel
        Item.itemsList[256 + 22] = null;
        ItemPickaxe2 pickaxeDiamond = new ItemPickaxe2(22, EnumToolMaterial.EMERALD);
        pickaxeDiamond.setUnlocalizedName("pickaxeDiamond");
        mod_OldDays.setField(Item.class, null, 25, pickaxeDiamond);//Item: pickaxeDiamond
        Item.itemsList[256 + 29] = null;
        ItemPickaxe2 pickaxeGold = new ItemPickaxe2(29, EnumToolMaterial.GOLD);
        pickaxeGold.setUnlocalizedName("pickaxeGold");
        mod_OldDays.setField(Item.class, null, 32, pickaxeGold);//Item: pickaxeGold

        Item.itemsList[256 + 15] = null;
        ItemAxe2 axeWood = new ItemAxe2(15, EnumToolMaterial.WOOD);
        axeWood.setUnlocalizedName("hatchetWood");
        mod_OldDays.setField(Item.class, null, 18, axeWood);//Item: axeWood
        Item.itemsList[256 + 19] = null;
        ItemAxe2 axeStone = new ItemAxe2(19, EnumToolMaterial.STONE);
        axeStone.setUnlocalizedName("hatchetStone");
        mod_OldDays.setField(Item.class, null, 22, axeStone);//Item: axeStone
        Item.itemsList[256 + 2] = null;
        ItemAxe2 axeSteel = new ItemAxe2(2, EnumToolMaterial.IRON);
        axeSteel.setUnlocalizedName("hatchetIron");
        mod_OldDays.setField(Item.class, null, 5, axeSteel);//Item: axeSteel
        Item.itemsList[256 + 23] = null;
        ItemAxe2 axeDiamond = new ItemAxe2(23, EnumToolMaterial.EMERALD);
        axeDiamond.setUnlocalizedName("hatchetDiamond");
        mod_OldDays.setField(Item.class, null, 26, axeDiamond);//Item: axeDiamond
        Item.itemsList[256 + 30] = null;
        ItemAxe2 axeGold = new ItemAxe2(30, EnumToolMaterial.GOLD);
        axeGold.setUnlocalizedName("hatchetGold");
        mod_OldDays.setField(Item.class, null, 33, axeGold);//Item: axeGold
    }

    private void setToolDurability(int i){
        int wood = 59;
        int stone = 131;
        int iron = 250;
        int gold = 32;
        int diamond = 1561;
        if (i < 2){
            wood = 32 << 0;
            stone = 32 << 1;
            iron = 32 << 2;
            gold = 32 << 0;
            diamond = (32 << 3) * (i < 1 ? 1 : 4);
        }
        int maxDamage = 170;
        mod_OldDays.setField(Item.class, Item.pickaxeWood, maxDamage, wood);
        mod_OldDays.setField(Item.class, Item.axeWood, maxDamage, wood);
        mod_OldDays.setField(Item.class, Item.shovelWood, maxDamage, wood);
        mod_OldDays.setField(Item.class, Item.swordWood, maxDamage, wood);
        mod_OldDays.setField(Item.class, Item.hoeWood, maxDamage, wood);
        mod_OldDays.setField(Item.class, Item.pickaxeStone, maxDamage, stone);
        mod_OldDays.setField(Item.class, Item.axeStone, maxDamage, stone);
        mod_OldDays.setField(Item.class, Item.shovelStone, maxDamage, stone);
        mod_OldDays.setField(Item.class, Item.swordStone, maxDamage, stone);
        mod_OldDays.setField(Item.class, Item.hoeStone, maxDamage, stone);
        mod_OldDays.setField(Item.class, Item.pickaxeSteel, maxDamage, iron);
        mod_OldDays.setField(Item.class, Item.axeSteel, maxDamage, iron);
        mod_OldDays.setField(Item.class, Item.shovelSteel, maxDamage, iron);
        mod_OldDays.setField(Item.class, Item.swordSteel, maxDamage, iron);
        mod_OldDays.setField(Item.class, Item.hoeSteel, maxDamage, iron);
        mod_OldDays.setField(Item.class, Item.pickaxeGold, maxDamage, gold);
        mod_OldDays.setField(Item.class, Item.axeGold, maxDamage, gold);
        mod_OldDays.setField(Item.class, Item.shovelGold, maxDamage, gold);
        mod_OldDays.setField(Item.class, Item.swordGold, maxDamage, gold);
        mod_OldDays.setField(Item.class, Item.hoeGold, maxDamage, gold);
        mod_OldDays.setField(Item.class, Item.pickaxeDiamond, maxDamage, diamond);
        mod_OldDays.setField(Item.class, Item.axeDiamond, maxDamage, diamond);
        mod_OldDays.setField(Item.class, Item.shovelDiamond, maxDamage, diamond);
        mod_OldDays.setField(Item.class, Item.swordDiamond, maxDamage, diamond);
        mod_OldDays.setField(Item.class, Item.hoeDiamond, maxDamage, diamond);
    }
}