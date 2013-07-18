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
        new OldDaysPropertyInt(this,  15,1,     3,     "Stairs", 3).setUseNames();
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
            case 14:set(Minecraft.class, "timecontrol", TimeControl); break;
            case 15:set(BlockStairs.class, "oldstairs", Stairs == 0);
                    set(BlockStairs.class, "upsidedown", Stairs > 1);
                    set(BlockStairs.class, "corner", Stairs > 2); break;
            case 16:set(EntityBoat.class, "oldbreaking", OldBoatBreaking); break;
            case 17:set(ItemAxe2.class, "oldhardness", OldHardness);
                    set(ItemPickaxe2.class, "oldhardness", OldHardness);
                    mod_OldDays.setField(ItemTool.class, Item.shovelWood, 0, OldHardness ? oldSpadeBlocks : spadeBlocks);
                    mod_OldDays.setField(ItemTool.class, Item.shovelStone, 0, OldHardness ? oldSpadeBlocks : spadeBlocks);
                    mod_OldDays.setField(ItemTool.class, Item.shovelIron, 0, OldHardness ? oldSpadeBlocks : spadeBlocks);
                    mod_OldDays.setField(ItemTool.class, Item.shovelGold, 0, OldHardness ? oldSpadeBlocks : spadeBlocks);
                    mod_OldDays.setField(ItemTool.class, Item.shovelDiamond, 0, OldHardness ? oldSpadeBlocks : spadeBlocks);
                    mod_OldDays.setField(Block.class, Block.obsidian, 184, OldHardness ? 10F : 50F); break;
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
    public static int Stairs;
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
            BlockTNT2 customtnt = (BlockTNT2)(new BlockTNT2(Block.tnt.blockID));
            customtnt.setHardness(0.0F);
            customtnt.setStepSound(Block.soundGrassFootstep);
            customtnt.setUnlocalizedName("tnt");
            customtnt.func_111022_d("tnt");
            Block.blocksList[Block.tnt.blockID] = customtnt;
            mod_OldDays.setField(Block.class, null, 65, customtnt);//Block: tnt
            Block.blocksList[Block.fence.blockID] = null;
            BlockFence2 customfence = (BlockFence2)(new BlockFence2(Block.fence.blockID, "planks_oak", Material.wood));
            customfence.setHardness(2.0F);
            customfence.setResistance(5F);
            customfence.setStepSound(Block.soundWoodFootstep);
            customfence.setUnlocalizedName("fence");
            Block.blocksList[Block.fence.blockID] = customfence;
            mod_OldDays.setField(Block.class, null, 104, customfence);//Block: fence
            Block.blocksList[Block.tilledField.blockID] = null;
            BlockFarmlandOld customTilledField = (BlockFarmlandOld)(new BlockFarmlandOld(Block.tilledField.blockID));
            customTilledField.setHardness(0.6F);
            customTilledField.setStepSound(Block.soundGravelFootstep);
            customTilledField.setUnlocalizedName("farmland");
            customTilledField.func_111022_d("farmland");
            Block.blocksList[Block.tilledField.blockID] = customTilledField;
            mod_OldDays.setField(Block.class, null, 79, customTilledField);//Block: tilledField
            Block.blocksList[Block.wood.blockID] = null;
            BlockLog2 customWood = (BlockLog2)(new BlockLog2(Block.wood.blockID));
            customWood.setHardness(2.0F);
            customWood.setStepSound(Block.soundWoodFootstep);
            customWood.setUnlocalizedName("log");
            customWood.func_111022_d("log");
            Block.blocksList[Block.wood.blockID] = customWood;
            mod_OldDays.setField(Block.class, null, 36, customWood);//Block: wood
            Item.itemsList[Block.wood.blockID] = null;
            Item.itemsList[Block.wood.blockID] = (new ItemMultiTextureTile(Block.wood.blockID - 256, Block.wood, BlockLog.woodType)).setUnlocalizedName("log");
        }catch (Exception exception){
            System.out.println(exception);
        }
    }

    private void replaceTools(){
        Item.itemsList[256 + Item.pickaxeWood.itemID] = null;
        ItemPickaxe2 pickaxeWood = new ItemPickaxe2(Item.pickaxeWood.itemID, EnumToolMaterial.WOOD);
        pickaxeWood.setUnlocalizedName("pickaxeWood");
        pickaxeWood.func_111206_d("wood_pickaxe");
        mod_OldDays.setField(Item.class, null, 18, pickaxeWood);//Item: pickaxeWood
        Item.itemsList[256 + Item.pickaxeWood.itemID] = pickaxeWood;
        Item.itemsList[256 + Item.pickaxeStone.itemID] = null;
        ItemPickaxe2 pickaxeStone = new ItemPickaxe2(Item.pickaxeStone.itemID, EnumToolMaterial.STONE);
        pickaxeStone.setUnlocalizedName("pickaxeStone");
        pickaxeStone.func_111206_d("stone_pickaxe");
        mod_OldDays.setField(Item.class, null, 22, pickaxeStone);//Item: pickaxeStone
        Item.itemsList[256 + Item.pickaxeStone.itemID] = pickaxeStone;
        Item.itemsList[256 + Item.pickaxeIron.itemID] = null;
        ItemPickaxe2 pickaxeIron = new ItemPickaxe2(Item.pickaxeIron.itemID, EnumToolMaterial.IRON);
        pickaxeIron.setUnlocalizedName("pickaxeIron");
        pickaxeIron.func_111206_d("iron_pickaxe");
        mod_OldDays.setField(Item.class, null, 5, pickaxeIron);//Item: pickaxeIron
        Item.itemsList[256 + Item.pickaxeIron.itemID] = pickaxeIron;
        Item.itemsList[256 + Item.pickaxeDiamond.itemID] = null;
        ItemPickaxe2 pickaxeDiamond = new ItemPickaxe2(Item.pickaxeDiamond.itemID, EnumToolMaterial.EMERALD);
        pickaxeDiamond.setUnlocalizedName("pickaxeDiamond");
        pickaxeDiamond.func_111206_d("diamond_pickaxe");
        mod_OldDays.setField(Item.class, null, 26, pickaxeDiamond);//Item: pickaxeDiamond
        Item.itemsList[256 + Item.pickaxeDiamond.itemID] = pickaxeDiamond;
        Item.itemsList[256 + Item.pickaxeGold.itemID] = null;
        ItemPickaxe2 pickaxeGold = new ItemPickaxe2(Item.pickaxeGold.itemID, EnumToolMaterial.GOLD);
        pickaxeGold.setUnlocalizedName("pickaxeGold");
        pickaxeGold.func_111206_d("gold_pickaxe");
        mod_OldDays.setField(Item.class, null, 33, pickaxeGold);//Item: pickaxeGold
        Item.itemsList[256 + Item.pickaxeGold.itemID] = pickaxeGold;

        Item.itemsList[256 + Item.axeWood.itemID] = null;
        ItemAxe2 axeWood = new ItemAxe2(Item.axeWood.itemID, EnumToolMaterial.WOOD);
        axeWood.setUnlocalizedName("hatchetWood");
        axeWood.func_111206_d("wood_axe");
        mod_OldDays.setField(Item.class, null, 19, axeWood);//Item: axeWood
        Item.itemsList[256 + Item.axeWood.itemID] = axeWood;
        Item.itemsList[256 + Item.axeStone.itemID] = null;
        ItemAxe2 axeStone = new ItemAxe2(Item.axeStone.itemID, EnumToolMaterial.STONE);
        axeStone.setUnlocalizedName("hatchetStone");
        axeStone.func_111206_d("stone_axe");
        mod_OldDays.setField(Item.class, null, 23, axeStone);//Item: axeStone
        Item.itemsList[256 + Item.axeStone.itemID] = axeStone;
        Item.itemsList[256 + Item.axeIron.itemID] = null;
        ItemAxe2 axeIron = new ItemAxe2(Item.axeIron.itemID, EnumToolMaterial.IRON);
        axeIron.setUnlocalizedName("hatchetIron");
        axeIron.func_111206_d("iron_axe");
        mod_OldDays.setField(Item.class, null, 6, axeIron);//Item: axeIron
        Item.itemsList[256 + Item.axeIron.itemID] = axeIron;
        Item.itemsList[256 + Item.axeDiamond.itemID] = null;
        ItemAxe2 axeDiamond = new ItemAxe2(Item.axeDiamond.itemID, EnumToolMaterial.EMERALD);
        axeDiamond.setUnlocalizedName("hatchetDiamond");
        axeDiamond.func_111206_d("diamond_axe");
        mod_OldDays.setField(Item.class, null, 27, axeDiamond);//Item: axeDiamond
        Item.itemsList[256 + Item.axeDiamond.itemID] = axeDiamond;
        Item.itemsList[256 + Item.axeGold.itemID] = null;
        ItemAxe2 axeGold = new ItemAxe2(Item.axeGold.itemID, EnumToolMaterial.GOLD);
        axeGold.setUnlocalizedName("hatchetGold");
        axeGold.func_111206_d("gold_axe");
        mod_OldDays.setField(Item.class, null, 34, axeGold);//Item: axeGold
        Item.itemsList[256 + Item.axeGold.itemID] = axeGold;
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
        int maxDamage = 176;
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
        mod_OldDays.setField(Item.class, Item.pickaxeIron, maxDamage, iron);
        mod_OldDays.setField(Item.class, Item.axeIron, maxDamage, iron);
        mod_OldDays.setField(Item.class, Item.shovelIron, maxDamage, iron);
        mod_OldDays.setField(Item.class, Item.swordIron, maxDamage, iron);
        mod_OldDays.setField(Item.class, Item.hoeIron, maxDamage, iron);
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