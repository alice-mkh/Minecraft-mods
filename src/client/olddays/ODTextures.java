package net.minecraft.src;

import java.util.*;

public class ODTextures extends OldDaysModule{
    public ODTextures(mod_OldDays c){
        super(c, 7, "Textures");
        new OldDaysPropertyInt(this,  1, 1,     "Cobblestone", 2).setUseNames();
        new OldDaysPropertyBool(this, 2, true,  "MossStone");
        new OldDaysPropertyBool(this, 3, true,  "Stone");
        new OldDaysPropertyInt(this,  4, 0,     "Brick", 2).setUseNames();
        new OldDaysPropertyBool(this, 5, true,  "Sand");
        new OldDaysPropertyInt(this,  6, 0,     "Gravel", 2).setUseNames();
        new OldDaysPropertyBool(this, 7, true,  "Dirt");
        new OldDaysPropertyBool(this, 8, true,  "Grass");
        new OldDaysPropertyInt(this,  9, 1,     "Planks", 2).setUseNames();
        new OldDaysPropertyInt(this,  10,1,     "Sapling", 2).setUseNames();
        new OldDaysPropertyInt(this,  11,1,     "Wool", 2).setUseNames();
        new OldDaysPropertyBool(this, 12,true,  "Glowstone");
        new OldDaysPropertyInt(this,  13,0,     "OreBlocks", 2).setUseNames();
        new OldDaysPropertyBool(this, 14,true,  "Spawner");
        new OldDaysPropertyBool(this, 15,true,  "Furnace");
        new OldDaysPropertyBool(this, 16,true,  "Dispenser");
        new OldDaysPropertyBool(this, 17,false, "Web");
        new OldDaysPropertyBool(this, 18,true,  "Porkchop");
        new OldDaysPropertyBool(this, 19,false, "Axes");
        new OldDaysPropertyBool(this, 20,false, "Coal");
        new OldDaysPropertyBool(this, 21,false, "Flint");
        new OldDaysPropertyBool(this, 22,false, "FlintSteel");
        new OldDaysPropertyBool(this, 23,false, "Feather");
        new OldDaysPropertyBool(this, 24,false, "Pigs");
        new OldDaysPropertyBool(this, 25,false, "Slimes");
        new OldDaysPropertyBool(this, 26,false, "Steve");
        new OldDaysPropertyBool(this, 27,true,  "Explosion");
        new OldDaysPropertyBool(this, 28,false, "Moon");
        new OldDaysPropertyBool(this, 29,true,  "ArmorShape");
        new OldDaysPropertyBool(this, 30,true,  "Cocoa");
        new OldDaysPropertyBool(this, 31,true,  "Netherrack");
        new OldDaysPropertyBool(this, 32,true,  "LeatherArmor");
        new OldDaysPropertyBool(this, 33,true,  "Food");
        new OldDaysPropertyBool(this, 34,true,  "Procedural");
        for (int i = 1; i <= properties.size(); i++){
            if (i != 15 && (i < 24 || i == 30 || i == 31 || i == 33)){
                getPropertyById(i).setFallback("olddays/textures.png");
            }
        }
        getPropertyById(24).setFallback("olddays/pig.png");
        getPropertyById(25).setFallback("olddays/slime.png");
        getPropertyById(26).setFallback("olddays/char.png");
        getPropertyById(27).setFallback("olddays/explosion.png");
        getPropertyById(28).setFallback("olddays/moon_phases.png");
        getPropertyById(32).setFallback("olddays/textures.png", "olddays/cloth_1.png", "olddays/cloth_2.png");
        replaceBlocks();
    }

    @Override
    public void callback (int i){
        boolean fallback = !hasTextures("olddays/textures.png");
        if (i >= 1 && i <= 23 || i >= 29 && i <= 33){
            refreshIconReplacements();
        }
        switch(i){
            case 13:setOreBlocks(); break;
            case 24:setTextureHook("/mob/pig.png", "/olddays/pig.png", !Pigs && !fallback); break;
            case 25:setTextureHook("/mob/slime.png", "/olddays/slime.png", Slimes && !fallback); break;
            case 26:setTextureHook("/mob/char.png", "/olddays/char.png", Steve && !fallback); break;
            case 27:setTextureHook("/misc/explosion.png", "/olddays/explosion.png", Explosion); break;
            case 28:setTextureHook("/environment/moon_phases.png", "/olddays/moon_phases.png", !Moon && !fallback); break;
            case 34:refreshTextureFXes(true); break;
        }
    }

    public static int Cobblestone = 1;
    public static boolean MossStone = true;
    public static boolean Stone = true;
    public static int Brick = 0;
    public static boolean Sand = true;
    public static int Gravel = 0;
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
    public static boolean ArmorShape = true;
    public static boolean Cocoa = true;
    public static boolean Netherrack = true;
    public static boolean LeatherArmor = true;
    public static boolean Food = true;
    public static boolean Procedural = false;

    private void replaceBlocks(){
        try{
            Block.blocksList[Block.blockSteel.blockID] = null;
            BlockOreStorageOld customsteel = (BlockOreStorageOld)(new BlockOreStorageOld(Block.blockSteel.blockID, "iron"));
            customsteel.setHardness(5F);
            customsteel.setResistance(10F);
            customsteel.setStepSound(Block.soundMetalFootstep);
            customsteel.getIndirectPowerOutput("blockIron");
            Block.blocksList[Block.blockSteel.blockID] = customsteel;
            mod_OldDays.setField(Block.class, null, 60, customsteel);//Block: blockSteel
            Block.blocksList[Block.blockGold.blockID] = null;
            BlockOreStorageOld customgold = (BlockOreStorageOld)(new BlockOreStorageOld(Block.blockGold.blockID, "gold"));
            customgold.setHardness(3F);
            customgold.setResistance(10F);
            customgold.setStepSound(Block.soundMetalFootstep);
            customgold.getIndirectPowerOutput("blockGold");
            Block.blocksList[Block.blockGold.blockID] = customgold;
            mod_OldDays.setField(Block.class, null, 59, customgold);//Block: blockGold
            Block.blocksList[Block.blockDiamond.blockID] = null;
            BlockOreStorageOld customdiamond = (BlockOreStorageOld)(new BlockOreStorageOld(Block.blockDiamond.blockID, "diamond"));
            customdiamond.setHardness(5F);
            customdiamond.setResistance(10F);
            customdiamond.setStepSound(Block.soundMetalFootstep);
            customdiamond.getIndirectPowerOutput("blockDiamond");
            Block.blocksList[Block.blockDiamond.blockID] = customdiamond;
            mod_OldDays.setField(Block.class, null, 75, customdiamond);//Block: blockDiamond
        }catch (Exception ex){
            System.out.println(ex);
        }
    }

    @Override
    public void refreshTextures(){
        refreshTextureFXes(false);
        refreshIconReplacements();
    }

    private void refreshTextureFXes(boolean refreshBlocks){
        if (core.getField(BlockFluid.class, Block.waterStill, 0) == null){
            return;
        }
        if (!Procedural){
            core.texman.removeTextureFXes();
            return;
        }
        TextureMap blocks = (TextureMap)(core.getField(RenderEngine.class, core.getMinecraft().renderEngine, 8));
        TextureMap items = (TextureMap)(core.getField(RenderEngine.class, core.getMinecraft().renderEngine, 9));

        if (blocks == null || items == null){
            return;
        }

        if (refreshBlocks){
            core.getMinecraft().renderEngine.refreshTextures();
        }

        Icon origWater = BlockFluid.func_94424_b("water");
        Icon origWaterFlow = BlockFluid.func_94424_b("water_flow");
        Icon origLava = BlockFluid.func_94424_b("lava");
        Icon origLavaFlow = BlockFluid.func_94424_b("lava_flow");
        Icon[] origFire = (Icon[])(core.getField(BlockFire.class, Block.fire, 2));
        Icon origPortal = (Icon)(core.getField(Block.class, Block.portal, 195)); //Block: blockIcon
        Icon origClock = (Icon)(core.getField(Item.class, Item.pocketSundial, 176)); //Item: iconIndex
        Icon origCompass = (Icon)(core.getField(Item.class, Item.compass, 176));

        Icon water = core.texman.registerCustomIcon(blocks, "water", new TextureWaterFX(), origWater);
        Icon waterFlowing = core.texman.registerCustomIcon(blocks, "water_flow", new TextureWaterFlowFX(), origWaterFlow);
        Icon lava = core.texman.registerCustomIcon(blocks, "lava", new TextureLavaFX(), origLava);
        Icon lavaFlowing = core.texman.registerCustomIcon(blocks, "lava_flow", new TextureLavaFlowFX(), origLavaFlow);
        Icon fire0 = core.texman.registerCustomIcon(blocks, "fire_0", new TextureFlamesFX(0), origFire[0]);
        Icon fire1 = core.texman.registerCustomIcon(blocks, "fire_1", new TextureFlamesFX(1), origFire[1]);
        Icon portal = core.texman.registerCustomIcon(blocks, "portal", new TexturePortalFX(), origPortal);
        Icon clock = core.texman.registerCustomIcon(blocks, "clock", new TextureWatchFX(), origClock);
        Icon compass = core.texman.registerCustomIcon(blocks, "compass", new TextureCompassFX(), origCompass);

        core.setField(BlockFluid.class, Block.waterStill, 0, new Icon[]{water, waterFlowing});
        core.setField(BlockFluid.class, Block.waterMoving, 0, new Icon[]{water, waterFlowing});
        core.setField(BlockFluid.class, Block.lavaStill, 0, new Icon[]{lava, lavaFlowing});
        core.setField(BlockFluid.class, Block.lavaMoving, 0, new Icon[]{lava, lavaFlowing});
        core.setField(BlockFire.class, Block.fire, 2, new Icon[]{fire0, fire1});
        core.setField(Block.class, Block.portal, 195, portal);
        core.setField(Item.class, Item.pocketSundial, 176, clock);
        core.setField(Item.class, Item.compass, 176, compass);
        mod_OldDays.texman.updateTextureFXes();
    }

    private void refreshIconReplacements(){
        TextureMap blocks = (TextureMap)(core.getField(RenderEngine.class, core.getMinecraft().renderEngine, 8));
        Icon i = blocks.registerIcon("olddays_cobblestone");
    }

    private void setOreBlocks(){
        set(BlockOreStorageOld.class, "oldtextures", OreBlocks<1 && hasTextures("olddays/textures.png"));
        reload();
    }
}