package net.minecraft.src;

import java.util.*;

public class ODTextures extends OldDaysModule{
    public ODTextures(mod_OldDays c){
        super(c, 7, "Textures");
        addProperty(1, "Cobblestone",         1,     "Cobblestone", "", new String[]{"0.013a_03", "Alpha", "Beta 1.7"});
        addProperty(2, "Old mossy stone",     true,  "MossStone",   "");
        addProperty(3, "Old stone",           true,  "Stone",       "");
        addProperty(4, "Bricks",              0,     "Brick",       "", new String[]{"Classic", "Alpha", "Beta 1.8"});
        addProperty(5, "Old sand",            true,  "Sand",        "");
        addProperty(6, "Old gravel",          true,  "Gravel",      "");
        addProperty(7, "Old dirt",            true,  "Dirt",        "");
        addProperty(8, "Old grass",           true,  "Grass",       "");
        addProperty(9, "Planks",              1,     "Planks",      "", new String[]{"0.013a_03", "Beta", "1.0"});
        addProperty(10,"Sapling",             1,     "Sapling",     "", new String[]{"Classic", "Beta", "1.2"});
        addProperty(11,"Cloth",               1,     "Wool",        "", new String[]{"Classic", "Beta", "1.2"});
        addProperty(12,"Old glowstone",       true,  "Glowstone",   "");
        addProperty(13,"Ore blocks",          0,     "OreBlocks",   "", new String[]{"Classic", "Beta", "1.0"});
        addProperty(14,"Old mob spawner",     true,  "Spawner",     "");
        addProperty(15,"Stone furnace top",   true,  "Furnace",     "");
        addProperty(16,"Old dispenser",       true,  "Dispenser",   "");
        addProperty(17,"Old cobweb",          false, "Web",         "");
        addProperty(18,"Old porkchop",        true,  "Porkchop",    "");
        addProperty(19,"Old axes",            false, "Axes",        "");
        addProperty(20,"Old coal",            false, "Coal",        "");
        addProperty(21,"Old flint",           false, "Flint",       "");
        addProperty(22,"Old flint and steel", false, "FlintSteel",  "");
        addProperty(23,"Old feather",         false, "Feather",     "");
        addProperty(24,"Pig snouts",          false, "Pigs",        "");
        addProperty(25,"Old slimes",          false, "Slimes",      "");
        addProperty(26,"Steve with beard",    false, "Steve",       "");
        addProperty(27,"Old explosion",       true,  "Explosion",   "");
        addProperty(28,"Moon phases",         false, "Moon",        "");
        for (int i = 1; i < properties.size(); i++){
            getPropertyById(i).allowedInFallback = (i != 15);
        }
        replaceBlocks();
    }

    public void callback (int i){
        switch(i){
            case 1: setTextureHook("/terrain.png", 16, "/olddays/textures.png", Cobblestone<1 ? 0 : 1, Cobblestone<2); break;
            case 2: setTextureHook("/terrain.png", 36, "/olddays/textures.png", 2, MossStone); break;
            case 3: setStone(); break;
            case 4: setTextureHook("/terrain.png", 7, "/olddays/textures.png", Brick<1 ? 6 : 7, Brick<2); break;
            case 5: setTextureHook("/terrain.png", 18, "/olddays/textures.png", 11, Sand); break;
            case 6: setTextureHook("/terrain.png", 19, "/olddays/textures.png", 12, Gravel); break;
            case 7: setTextureHook("/terrain.png", 2, "/olddays/textures.png", 14, Dirt);
                    setTextureHook("/terrain.png", 3, "/olddays/textures.png", 15, Dirt); break;
            case 8: setTextureHook("/terrain.png", 0, "/olddays/textures.png", 13, Grass); break;
            case 9: setTextureHook("/terrain.png", 4, "/olddays/textures.png", Planks<1 ? 4 : 5, Planks<2); break;
            case 10:setTextureHook("/terrain.png", 15, "/olddays/textures.png", Sapling<1 ? 9 : 10, Sapling<2); break;
            case 11:setCloth(); break;
            case 12:setTextureHook("/terrain.png", 105, "/olddays/textures.png", 17, Glowstone); break;
            case 13:setOreBlocks(); break;
            case 14:setTextureHook("/terrain.png", 65, "/olddays/textures.png", 16, Spawner); break;
            case 15:setTextureHook("/terrain.png", 62, Stone ? "/olddays/textures.png" : "/terrain.png", Stone ? 3 : 1, Furnace); break;
            case 16:setTextureHook("/terrain.png", 46, "/olddays/textures.png", 48, Dispenser); break;
            case 17:setTextureHook("/terrain.png", 11, "/olddays/textures.png", 8, Web); break;
            case 18:setTextureHook("/gui/items.png", 88, "/olddays/textures.png", 62, Porkchop); break;
            case 19:setTextureHook("/gui/items.png", 112, "/olddays/textures.png", 56, Axes);
                    setTextureHook("/gui/items.png", 113, "/olddays/textures.png", 57, Axes);
                    setTextureHook("/gui/items.png", 114, "/olddays/textures.png", 58, Axes);
                    setTextureHook("/gui/items.png", 115, "/olddays/textures.png", 59, Axes);
                    setTextureHook("/gui/items.png", 116, "/olddays/textures.png", 60, Axes); break;
            case 20:setTextureHook("/gui/items.png", 7, "/olddays/textures.png", 52, Coal); break;
            case 21:setTextureHook("/gui/items.png", 6, "/olddays/textures.png", 53, Flint); break;
            case 22:setTextureHook("/gui/items.png", 5, "/olddays/textures.png", 54, FlintSteel); break;
            case 23:setTextureHook("/gui/items.png", 24, "/olddays/textures.png", 55, Feather); break;
            case 24:setTextureHook("/mob/pig.png", "/olddays/pig.png", !Pigs); break;
            case 25:setTextureHook("/mob/slime.png", "/olddays/slime.png", Slimes); break;
            case 26:setTextureHook("/mob/char.png", "/olddays/char.png", Steve); break;
            case 27:setTextureHook("/misc/explosion.png", "/olddays/explosion.png", Explosion); break;
            case 28:setTextureHook("/terrain/moon_phases.png", "/olddays/moon_phases.png", !Moon); break;
        }
    }

    public static int Cobblestone = 1;
    public static boolean MossStone = true;
    public static boolean Stone = true;
    public static int Brick = 0;
    public static boolean Sand = true;
    public static boolean Gravel = true;
    public static boolean Dirt = true;
    public static boolean Grass = true;
    public static int Planks = 1;
    public static int Sapling = 1;
    public static int Wool = 1;
    public static boolean Glowstone = true;
    public static int OreBlocks = 0;
    public static boolean Spawner = true;
    public static boolean Furnace = true;
    public static boolean Dispenser = true;
    public static boolean Web;
    public static boolean Porkchop = true;
    public static boolean Axes;
    public static boolean Coal;
    public static boolean Flint;
    public static boolean FlintSteel;
    public static boolean Feather;
    public static boolean Pigs;
    public static boolean Slimes;
    public static boolean Steve;
    public static boolean Explosion = true;
    public static boolean Moon;

    private void replaceBlocks(){
        try{
            Block.blocksList[Block.blockSteel.blockID] = null;
            BlockOreStorageOld customsteel = (BlockOreStorageOld)(new BlockOreStorageOld(Block.blockSteel.blockID, 22));
            customsteel.setHardness(5F);
            customsteel.setResistance(10F);
            customsteel.setStepSound(Block.soundMetalFootstep);
            customsteel.setBlockName("blockIron");
            customsteel.sidetex = ModLoader.addOverride("/terrain.png", "/olddays/oreblocks/ironside.png");
            customsteel.bottomtex = ModLoader.addOverride("/terrain.png", "/olddays/oreblocks/ironbottom.png");
            Block.blocksList[Block.blockSteel.blockID] = customsteel;
            Block.blocksList[Block.blockGold.blockID] = null;
            BlockOreStorageOld customgold = (BlockOreStorageOld)(new BlockOreStorageOld(Block.blockGold.blockID, 23));
            customgold.setHardness(3F);
            customgold.setResistance(10F);
            customgold.setStepSound(Block.soundMetalFootstep);
            customgold.setBlockName("blockGold");
            customgold.sidetex = ModLoader.addOverride("/terrain.png", "/olddays/oreblocks/goldside.png");
            customgold.bottomtex = ModLoader.addOverride("/terrain.png", "/olddays/oreblocks/goldbottom.png");
            Block.blocksList[Block.blockGold.blockID] = customgold;
            Block.blocksList[Block.blockDiamond .blockID] = null;
            BlockOreStorageOld customdiamond = (BlockOreStorageOld)(new BlockOreStorageOld(Block.blockDiamond.blockID, 24));
            customdiamond.setHardness(5F);
            customdiamond.setResistance(10F);
            customdiamond.setStepSound(Block.soundMetalFootstep);
            customdiamond.setBlockName("blockDiamond");
            customdiamond.sidetex = ModLoader.addOverride("/terrain.png", "/olddays/oreblocks/diamondside.png");
            customdiamond.bottomtex = ModLoader.addOverride("/terrain.png", "/olddays/oreblocks/diamondbottom.png");
            Block.blocksList[Block.blockDiamond.blockID] = customdiamond;
        }catch (Exception ex){
            System.out.println(ex);
        }
    }

    private void setStone(){
        setTextureHook("/terrain.png", 1, "/olddays/textures.png", 3, Stone);
        if (Furnace){
            setTextureHook("/terrain.png", 62, Stone ? "/olddays/textures.png" : "/terrain.png", Stone ? 3 : 1, Furnace);
        }
    }

    private void setOreBlocks(){
        setBool(net.minecraft.src.BlockOreStorageOld.class, "oldtextures", OreBlocks<1);
        setTextureHook("/terrain.png", 22, "/olddays/textures.png", 49, OreBlocks<2);
        setTextureHook("/terrain.png", 23, "/olddays/textures.png", 50, OreBlocks<2);
        setTextureHook("/terrain.png", 24, "/olddays/textures.png", 51, OreBlocks<2);
        if (OreBlocks<3){
            reload();
        }
    }

    private void setCloth(){
        int[] orig =    new int[]{64, 113, 114, 129, 130, 145, 146, 161, 162, 177, 178, 193, 194, 209, 210, 225};
        int[] beta =    new int[]{47, 18,  19,  20,  21,  22,  23,  24,  25,  26,  27,  28,  29,  30,  31,  61};
        int[] classic = new int[]{47, 45,  45,  32,  44,  36,  35,  24,  34,  40,  39,  41,  43,  38,  33,  46};
        for (int i = 0; i < 16; i++){
            setTextureHook("/terrain.png", orig[i], "/olddays/textures.png", Wool<1 ? classic[i] : beta[i], Wool<2);
        }
    }
}